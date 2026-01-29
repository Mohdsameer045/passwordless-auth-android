package com.klu.passwordlessauthapp.data

import kotlinx.coroutines.delay
import kotlin.random.Random

private const val OTP_EXPIRY_MS = 60_000L
private const val MAX_ATTEMPTS = 3

data class OtpData(
    val otp: String,
    val createdAt: Long,
    val attempts: Int = 0
)

class OtpManager {

    private val otpStore: MutableMap<String, OtpData> = mutableMapOf()

    fun generateOtp(email: String): String {
        val otp = (100000..999999).random().toString()
        otpStore[email] = OtpData(
            otp = otp,
            createdAt = System.currentTimeMillis(),
            attempts = 0
        )
        return otp
    }

    fun validateOtp(email: String, inputOtp: String): OtpValidationResult {
        val otpData = otpStore[email]
            ?: return OtpValidationResult.Expired

        // Check expiry
        val isExpired = System.currentTimeMillis() - otpData.createdAt > OTP_EXPIRY_MS
        if (isExpired) {
            otpStore.remove(email)
            return OtpValidationResult.Expired
        }

        // Check attempts
        if (otpData.attempts >= MAX_ATTEMPTS) {
            otpStore.remove(email)
            return OtpValidationResult.AttemptsExceeded
        }

        return if (otpData.otp == inputOtp) {
            otpStore.remove(email)
            OtpValidationResult.Success
        } else {
            otpStore[email] = otpData.copy(attempts = otpData.attempts + 1)
            OtpValidationResult.Failure
        }
    }

    fun hasActiveOtp(email: String): Boolean {
        val otpData = otpStore[email] ?: return false
        return System.currentTimeMillis() - otpData.createdAt <= OTP_EXPIRY_MS
    }
}

sealed class OtpValidationResult {
    object Success : OtpValidationResult()
    object Failure : OtpValidationResult()
    object Expired : OtpValidationResult()
    object AttemptsExceeded : OtpValidationResult()
}
