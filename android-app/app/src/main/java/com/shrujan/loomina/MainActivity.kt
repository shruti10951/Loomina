package com.shrujan.loomina

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.shrujan.loomina.ui.auth.LoginScreen
import com.shrujan.loomina.ui.auth.RegisterScreen
import com.shrujan.loomina.ui.welcome.WelcomeScreen
import com.shrujan.loomina.viewmodel.AuthViewModel




class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LoominaApp()
        }
    }
}

@Composable
fun LoominaApp() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "welcome"
    ) {
        composable("welcome") {
            WelcomeScreen(
                onGetStartedClick = {
                    navController.navigate("login")
                }
            )
        }

        composable("login") {
            val ui = authViewModel.uiState.value

            LoginScreen(
                onLoginClick = { email, password ->
                    authViewModel.login(email, password)
                },
                onRegisterClick = {
                    navController.navigate("register")
                }
            )

            // Optional lightweight feedback (snackbar/dialog later)
            if (ui.loading) {
                // You can add a small CircularProgressIndicator in the screen
                print("loadin")
            }
            if (ui.error != null) {
                // Show a Text(ui.error) in the screen or a snackbar
                print("erorr")
            }
            if (ui.token != null) {
                // Success — navigate to next screen (e.g., home/thread)
                // navController.navigate("thread") { popUpTo("login") { inclusive = true } }
                print("success")
            }
        }

        composable("register") {
            val ui = authViewModel.uiState.value

            RegisterScreen(
                onLoginClick = {
                    navController.popBackStack() // go back to login
                },
                onRegisterClick = { email, username, password ->
                    authViewModel.register(email, username, password)
                }
            )

            // Optional lightweight feedback (snackbar/dialog later)
            if (ui.loading) {
                // You can add a small CircularProgressIndicator in the screen
                print("loadin")
            }
            if (ui.error != null) {
                // Show a Text(ui.error) in the screen or a snackbar
                print("erorr")
            }
            if (ui.token != null) {
                // Success — navigate to next screen (e.g., home/thread)
                // navController.navigate("thread") { popUpTo("login") { inclusive = true } }
                print("success")
            }
        }
    }
}
