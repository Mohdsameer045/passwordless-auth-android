package com.klu.passwordlessauthapp.analytics

import timber.log.Timber

class AnalyticsLogger {

    fun logOtpGenerated(email: String, otp: String) {
        Timber.d("OTP generated for email: $email | OTP = $otp")
    }


    fun logOtpValidationSuccess(email: String) {
        Timber.d("OTP validation SUCCESS for email: $email")
    }

    fun logOtpValidationFailure(email: String) {
        Timber.d("OTP validation FAILURE for email: $email")
    }

    fun logLogout(email: String) {
        Timber.d("User logged out: $email")
    }
}
