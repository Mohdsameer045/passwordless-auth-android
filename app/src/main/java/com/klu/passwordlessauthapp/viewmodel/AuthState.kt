package com.klu.passwordlessauthapp.viewmodel

sealed class AuthState {

    object Login : AuthState()

    data class Otp(
        val email: String,
        val remainingAttempts: Int,
        val expiryTimeMillis: Long,
        val errorMessage: String? = null
    ) : AuthState()

    data class Session(
        val email: String,
        val sessionStartTime: Long
    ) : AuthState()

    data class Error(
        val message: String
    ) : AuthState()
}
