package com.klu.passwordlessauthapp.ui.otp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.max

@Composable
fun OtpScreen(
    email: String,
    remainingAttempts: Int,
    expiryTimeMillis: Long,
    errorMessage: String?,
    onVerifyOtp: (String) -> Unit,
    onResendOtp: () -> Unit
) {
    var otp by rememberSaveable { mutableStateOf("") }
    var secondsLeft by remember {
        mutableStateOf(calculateRemainingSeconds(expiryTimeMillis))
    }

    // Countdown timer
    LaunchedEffect(expiryTimeMillis) {
        while (secondsLeft > 0) {
            delay(1000)
            secondsLeft = calculateRemainingSeconds(expiryTimeMillis)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {

                Text(
                    text = "Verify OTP",
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Code sent to",
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = otp,
                    onValueChange = { value: String ->
                        if (value.length <= 6) {
                            otp = value
                        }
                    },
                    label = { Text("Enter 6-digit OTP") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Attempts left: $remainingAttempts",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "Expires in: ${formatSeconds(secondsLeft)}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { onVerifyOtp(otp) },
                    enabled = otp.length == 6 && secondsLeft > 0,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text("Verify OTP")
                }

                Spacer(modifier = Modifier.height(12.dp))

                TextButton(
                    onClick = onResendOtp,
                    enabled = secondsLeft.toInt() == 0,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Resend OTP")
                }
            }
        }
    }
}

/* ---------- Helper functions (NO SPACES, NO CONFUSION) ---------- */

private fun calculateRemainingSeconds(expiryTimeMillis: Long): Long {
    return max(0, (expiryTimeMillis - System.currentTimeMillis()) / 1000)
}

private fun formatSeconds(seconds: Long): String {
    val mins = seconds / 60
    val secs = seconds % 60
    return String.format("%02d:%02d", mins, secs)
}
