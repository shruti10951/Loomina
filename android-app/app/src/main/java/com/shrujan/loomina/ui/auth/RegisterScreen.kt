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
 * Registration screen UI.
 *
 * Provides input fields for username, email, and password,
 * a register button, and a link back to the login screen.
 *
 * @param onLoginClick Callback when "Login" link is clicked.
 * @param onRegisterClick Callback when "Register" button is clicked, passes (email, username, password).
 */
@Composable
fun RegisterScreen(
    onLoginClick: () -> Unit,
    onRegisterClick: (String, String, String) -> Unit
) {
    LoominaTheme {
        // Root container that centers the form
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
                    "Register",
                    fontSize = 28.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                // State variable for username input
                var username by remember { mutableStateOf("") }
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

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

                // Register button → calls parent callback with form values
                Button(
                    onClick = { onRegisterClick(email, username, password) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Register")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Navigation link back to login screen
                TextButton(
                    onClick = onLoginClick
                ) {
                    Text("Already have an account? Login!")
                }
            }
        }
    }
}
