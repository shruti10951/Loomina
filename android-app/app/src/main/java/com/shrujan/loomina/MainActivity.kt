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
    val context = LocalContext.current

    // repo & prefs for ViewModel
    val userPrefs = UserPreferences(context)
    val repository = AuthRepository()

    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(repository, userPrefs)
    )

    val savedToken by authViewModel.savedToken.collectAsState(initial = null)

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        // Splash Screen
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

        // Welcome Screen
        composable("welcome") {
            WelcomeScreen(
                onGetStartedClick = {
                    navController.navigate("login")
                }
            )
        }

        // Login
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

            LaunchedEffect(ui.token) {
                if (ui.token != null) {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            }
        }

        // Register
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

            LaunchedEffect(ui.token) {
                if (ui.token != null) {
                    navController.navigate("home") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            }
        }

        // Home
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
