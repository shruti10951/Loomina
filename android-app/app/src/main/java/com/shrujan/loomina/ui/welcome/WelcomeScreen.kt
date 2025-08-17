package com.shrujan.loomina.ui.welcome

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shrujan.loomina.theme.LoominaTheme

/**
 * WelcomeScreen - The first screen shown to users when they open the app.
 *
 * @param onGetStartedClick Callback function triggered when the "Get Started" button is clicked.
 */
@Composable
fun WelcomeScreen(onGetStartedClick: () -> Unit) {
    // Applying the app's custom theme to the whole screen
    LoominaTheme {
        // Root container to fill the screen and center the content
        Box(
            modifier = Modifier
                .fillMaxSize()        // Occupies the full screen
                .padding(16.dp),      // Adds padding from screen edges
            contentAlignment = Alignment.Center
        ) {
            // Vertical arrangement of UI elements (title + button)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                // App title / welcome text
                Text(
                    text = "Welcome To Loomina",
                    fontSize = 28.sp, // Explicit size for prominence
                    style = MaterialTheme.typography.headlineMedium
                )

                // Spacer to add vertical gap between title and button
                Spacer(modifier = Modifier.height(32.dp))

                // Button that starts the app experience
                Button(onClick = onGetStartedClick) {
                    Text(text = "Get Started")
                }
            }
        }
    }
}
