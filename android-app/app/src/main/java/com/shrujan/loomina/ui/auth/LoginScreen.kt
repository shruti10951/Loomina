package com.shrujan.loomina.ui.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shrujan.loomina.theme.LoominaTheme

/**
 * Login screen UI.
 *
 * Provides text fields for email and password,
 * a login button, and a link to the registration screen.
 *
 * @param onLoginClick Callback when login button is clicked.
 * @param onRegisterClick Callback when "Register" text button is clicked.
 */
@Composable
fun LoginScreen(
    onLoginClick: (String, String) -> Unit,
    onRegisterClick: () -> Unit
) {
    LoominaTheme {
        // Root container that centers the login form
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Title
                Text(
                    "Login",
                    fontSize = 28.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                // State variable for email input
                var email by remember { mutableStateOf("") }
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // State variable for password input
                var password by remember { mutableStateOf("") }
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Login button → calls parent callback with email & password
                Button(
                    onClick = { onLoginClick(email, password) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Login")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Navigation link to registration screen
                TextButton(
                    onClick = onRegisterClick
                ) {
                    Text("Don't have an account? Register!")
                }
            }
        }
    }
}
