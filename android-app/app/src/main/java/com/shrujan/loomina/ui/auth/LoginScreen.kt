package com.shrujan.loomina.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.shrujan.loomina.data.local.UserPreferences
import com.shrujan.loomina.data.repository.AuthRepository
import com.shrujan.loomina.ui.navigation.Routes
import com.shrujan.loomina.viewmodel.AuthViewModel
import com.shrujan.loomina.viewmodel.factory.AuthViewModelFactory

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(
            AuthRepository(LocalContext.current),
            UserPreferences(LocalContext.current)
        )
    )
) {
    val uiState = viewModel.uiState.value

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Navigate to home when login succeeds
    LaunchedEffect(uiState.token) {
        if (uiState.token != null) {
            // when you are ready to enter the app:
            navController.navigate(Routes.MAIN) {
                // remove splash from back stack
                popUpTo(Routes.SPLASH) { inclusive = true }
                launchSingleTop = true
            }

        }
    }

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
            Text("Login", fontSize = 28.sp)

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.login(email, password) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.loading
            ) {
                Text(if (uiState.loading) "Logging in..." else "Login")
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = { navController.navigate("register") }
            ) {
                Text("Don't have an account? Register!")
            }

            // Show error if login failed
            uiState.error?.let { error ->
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = error, color = MaterialTheme.colorScheme.error)
            }
        }

    }
}

