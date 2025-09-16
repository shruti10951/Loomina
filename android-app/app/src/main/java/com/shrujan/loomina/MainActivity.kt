package com.shrujan.loomina

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.shrujan.loomina.data.local.UserPreferences
import com.shrujan.loomina.data.repository.AuthRepository
import com.shrujan.loomina.data.repository.ThreadRepository
import com.shrujan.loomina.data.repository.UserRepository
import com.shrujan.loomina.ui.CreateScreen
import com.shrujan.loomina.ui.CreateStoryScreen
import com.shrujan.loomina.ui.CreateThreadScreen
import com.shrujan.loomina.ui.HomeScreen
import com.shrujan.loomina.ui.ShowThreadsScreen
import com.shrujan.loomina.ui.auth.LoginScreen
import com.shrujan.loomina.ui.auth.RegisterScreen
import com.shrujan.loomina.ui.welcome.SplashScreen
import com.shrujan.loomina.ui.welcome.WelcomeScreen
import com.shrujan.loomina.viewmodel.AuthViewModel
import com.shrujan.loomina.viewmodel.AuthViewModelFactory
import com.shrujan.loomina.viewmodel.HomeViewModel
import com.shrujan.loomina.viewmodel.HomeViewModelFactory
import com.shrujan.loomina.viewmodel.CreateThreadViewModel
import com.shrujan.loomina.viewmodel.ShowThreadsViewModel
import com.shrujan.loomina.viewmodel.ThreadViewModelFactory

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

    // Initialize repository and preferences
    val userPrefs = UserPreferences(context)
    val authRepository = AuthRepository(context)
    val userRepository = UserRepository(context)
    val threadRepository = ThreadRepository(context)

    // ViewModels provided here
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(authRepository, userPrefs)
    )
    val homeViewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(userRepository)
    )
    val createThreadViewModel: CreateThreadViewModel = viewModel (
        factory = ThreadViewModelFactory(threadRepository)
    )
    val showThreadsViewModel: ShowThreadsViewModel = viewModel (
        factory = ThreadViewModelFactory(threadRepository)
    )

    val savedToken by authViewModel.savedToken.collectAsState(initial = null)

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        // Splash Screen
        // here we need to check the saved token before going to home screen
        composable("splash") {
            SplashScreen(
                onTimeout = {
                    if (savedToken != null) {
                        navController.navigate("home") {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        }
                    } else {
                        navController.navigate("welcome") {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }
            )
        }

        // Welcome
        composable("welcome") {
            WelcomeScreen(
                onGetStartedClick = { navController.navigate("login") }
            )
        }

        // Login
        composable("login") {
            LoginScreen(
                navController = navController,
                viewModel = authViewModel
            )
        }

        // Register
        composable("register") {
            RegisterScreen(
                navController = navController,
                viewModel = authViewModel
            )
        }

        // Home
        composable("home") {
            savedToken?.let { token ->
                HomeScreen(
                    navController = navController,
                    viewModel = homeViewModel,
                    authViewModel = authViewModel
                )
            }
        }

        // Create (thread or story selector)
        composable("create") {
            CreateScreen(
                onThreadClick = { navController.navigate("thread") },
                onStoryClick = { navController.navigate("story") }
            )
        }

        // Thread creation
        composable("thread") {
            CreateThreadScreen(
                navController = navController,
                viewModel = createThreadViewModel
            )
        }

        // Story creation
        composable("story") {
            CreateStoryScreen(

            )
        }

        composable("show-threads") {
            ShowThreadsScreen(
                navController = navController,
                viewModel = showThreadsViewModel
            )
        }
    }
}
