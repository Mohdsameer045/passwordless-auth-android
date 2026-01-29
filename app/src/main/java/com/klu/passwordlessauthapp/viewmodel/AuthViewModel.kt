package com.klu.passwordlessauthapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.klu.passwordlessauthapp.analytics.AnalyticsLogger
import com.klu.passwordlessauthapp.data.OtpManager
import com.klu.passwordlessauthapp.data.OtpValidationResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

private const val OTP_EXPIRY_MS = 60_000L
private const val MAX_ATTEMPTS = 3

class AuthViewModel(
    private val otpManager: OtpManager = OtpManager(),
    private val analyticsLogger: AnalyticsLogger = AnalyticsLogger()
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Login)
    val authState: StateFlow<AuthState> = _authState

    private var currentEmail: String? = null
    private var expiryJob: Job? = null

    fun sendOtp(email: String) {
        currentEmail = email
        val otp = otpManager.generateOtp(email)
        analyticsLogger.logOtpGenerated(email, otp)

        startOtpExpiryTimer()

        _authState.value = AuthState.Otp(
            email = email,
            remainingAttempts = MAX_ATTEMPTS,
            expiryTimeMillis = System.currentTimeMillis() + OTP_EXPIRY_MS,
            errorMessage = null
        )
    }

    fun verifyOtp(inputOtp: String) {
        val email = currentEmail ?: return

        when (otpManager.validateOtp(email, inputOtp)) {

            OtpValidationResult.Success -> {
                analyticsLogger.logOtpValidationSuccess(email)
                expiryJob?.cancel()
                _authState.value = AuthState.Session(
                    email = email,
                    sessionStartTime = System.currentTimeMillis()
                )
            }

            OtpValidationResult.Failure -> {
                analyticsLogger.logOtpValidationFailure(email)
                val state = _authState.value as AuthState.Otp
                _authState.value = state.copy(
                    remainingAttempts = state.remainingAttempts - 1,
                    errorMessage = "Invalid OTP. Please try again."
                )
            }

            OtpValidationResult.AttemptsExceeded -> {
                _authState.value = AuthState.Error(
                    "Maximum OTP attempts exceeded. Please request a new OTP."
                )
            }

            OtpValidationResult.Expired -> {
                _authState.value = AuthState.Error(
                    "OTP expired. Please request a new OTP."
                )
            }
        }
    }

    fun resendOtp() {
        currentEmail?.let { sendOtp(it) }
    }

    fun logout() {
        currentEmail?.let { analyticsLogger.logLogout(it) }
        currentEmail = null
        _authState.value = AuthState.Login
    }

    private fun startOtpExpiryTimer() {
        expiryJob?.cancel()
        expiryJob = viewModelScope.launch {
            delay(OTP_EXPIRY_MS)
            if (_authState.value is AuthState.Otp) {
                _authState.value = AuthState.Error(
                    "OTP expired. Please request a new OTP."
                )
            }
        }
    }
}
