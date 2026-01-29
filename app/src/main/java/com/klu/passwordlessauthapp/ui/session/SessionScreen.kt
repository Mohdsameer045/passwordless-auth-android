package com.klu.passwordlessauthapp.ui.session

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SessionScreen(
    email: String,
    sessionStartTime: Long,
    onLogout: () -> Unit
) {
    var elapsedTime by remember { mutableStateOf(0L) }

    LaunchedEffect(sessionStartTime) {
        while (true) {
            elapsedTime = (System.currentTimeMillis() - sessionStartTime) / 1000
            delay(1000)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Session Active ✅",
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Logged in as",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = email,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Session started at",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = formatTime(sessionStartTime),
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Session duration",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = formatDuration(elapsedTime),
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(28.dp))

                Button(
                    onClick = onLogout,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("Logout")
                }
            }
        }
    }
}

private fun formatTime(timestamp: Long): String {
    val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

private fun formatDuration(seconds: Long): String =
    String.format("%02d:%02d", seconds / 60, seconds % 60)
