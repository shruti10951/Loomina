package com.shrujan.loomina.ui.welcome

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay

/**
 * A simple splash screen that displays the app name "Loomina"
 * for 2 seconds before navigating to the next screen.
 *
 * @param onTimeout Callback triggered after the delay to handle navigation.
 */
@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    // Launches a coroutine when this composable enters the composition.
    LaunchedEffect(Unit) {
        delay(2000) // Delay for 2 seconds
        onTimeout() // Trigger navigation after timeout
    }

    // Centered container for splash content
    Box(
        modifier = Modifier.fillMaxSize(), // Take up full screen
        contentAlignment = Alignment.Center // Center align content
    ) {
        // Display the app name with Material theme typography
        Text(
            text = "Loomina",
            style = MaterialTheme.typography.headlineLarge
        )
    }
}
