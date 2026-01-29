package com.klu.passwordlessauthapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.klu.passwordlessauthapp.ui.login.LoginScreen
import com.klu.passwordlessauthapp.ui.otp.OtpScreen
import com.klu.passwordlessauthapp.ui.session.SessionScreen
import com.klu.passwordlessauthapp.ui.theme.PasswordlessAuthAppTheme
import com.klu.passwordlessauthapp.viewmodel.AuthState
import com.klu.passwordlessauthapp.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PasswordlessAuthAppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AuthApp(authViewModel)
                }
            }
        }
    }
}

@Composable
fun AuthApp(viewModel: AuthViewModel) {
    val state by viewModel.authState.collectAsState()

    when (state) {
        is AuthState.Login ->
            LoginScreen { viewModel.sendOtp(it) }

        is AuthState.Otp -> {
            val s = state as AuthState.Otp
            OtpScreen(
                email = s.email,
                remainingAttempts = s.remainingAttempts,
                expiryTimeMillis = s.expiryTimeMillis,
                errorMessage = s.errorMessage,
                onVerifyOtp = viewModel::verifyOtp,
                onResendOtp = viewModel::resendOtp
            )
        }

        is AuthState.Session -> {
            val s = state as AuthState.Session
            SessionScreen(
                email = s.email,
                sessionStartTime = s.sessionStartTime,
                onLogout = viewModel::logout
            )
        }

        is AuthState.Error -> {
            val s = state as AuthState.Error
            Column(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(s.message, color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = viewModel::logout) {
                    Text("Go Back")
                }
            }
        }
    }
}
