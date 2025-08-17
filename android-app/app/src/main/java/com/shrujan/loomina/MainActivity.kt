package com.shrujan.loomina

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.shrujan.loomina.data.local.UserPreferences
import com.shrujan.loomina.data.repository.AuthRepository
import com.shrujan.loomina.ui.HomeScreen
import com.shrujan.loomina.ui.auth.LoginScreen
import com.shrujan.loomina.ui.auth.RegisterScreen
import com.shrujan.loomina.ui.welcome.SplashScreen
import com.shrujan.loomina.ui.welcome.WelcomeScreen
import com.shrujan.loomina.viewmodel.AuthViewModel
import com.shrujan.loomina.viewmodel.AuthViewModelFactory

/**
 * MainActivity acts as the single-activity entry point for the app.
 * It sets up Compose, initializes navigation, and wires up the AuthViewModel.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable drawing behind system bars
        enableEdgeToEdge()

        // Set Compose content
        setContent {
            LoominaApp()
        }
    }
}

/**
 * Root composable for the app.
 * - Provides navigation between screens
 * - Instantiates AuthViewModel with dependencies
 * - Decides initial screen (Splash → Welcome/Home)
 */
@Composable
fun LoominaApp() {
    val navController = rememberNavController()
    val context = LocalContext.current

    // Initialize repository and preferences
    val userPrefs = UserPreferences(context)
    val repository = AuthRepository()

    // Create AuthViewModel using a factory (manual DI)
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(repository, userPrefs)
    )

    // Observe saved token from datastore
    val savedToken by authViewModel.savedToken.collectAsState(initial = null)

    // App navigation graph
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        // Splash Screen → decides where to go (Home or Welcome)
        composable("splash") {
            SplashScreen(
                onTimeout = {
                    if (savedToken != null) {
                        navController.navigate("home") {
                            popUpTo("splash") { inclusive = true }
                        }
                    } else {
                        navController.navigate("welcome") {
                            popUpTo("splash") { inclusive = true }
                        }
                    }
                }
            )
        }

        // Welcome Screen → entry for new/unauthenticated users
        composable("welcome") {
            WelcomeScreen(
                onGetStartedClick = {
                    navController.navigate("login")
                }
            )
        }

        // Login Screen
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

            // Navigate to Home if login succeeds
            LaunchedEffect(ui.token) {
                if (ui.token != null) {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            }
        }

        // Register Screen
        composable("register") {
            val ui = authViewModel.registerUiState.value

            RegisterScreen(
                onLoginClick = {
                    navController.popBackStack()
                },
                onRegisterClick = { email, username, password ->
                    authViewModel.register(email, username, password)
                }
            )

            // Navigate to Home if register succeeds
            LaunchedEffect(ui.token) {
                if (ui.token != null) {
                    navController.navigate("home") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            }
        }

        // Home Screen (after successful login/register)
        composable("home") {
            HomeScreen(
                onLogoutClick = {
                    authViewModel.logout()
                    navController.navigate("welcome") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }
    }
}
