package com.shrujan.loomina.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.shrujan.loomina.ui.CreateStoryScreen
import com.shrujan.loomina.ui.CreateThreadScreen
import com.shrujan.loomina.ui.ShowThreadsScreen
import com.shrujan.loomina.ui.auth.LoginScreen
import com.shrujan.loomina.ui.auth.RegisterScreen
import com.shrujan.loomina.ui.create.CreateScreen
import com.shrujan.loomina.ui.home.HomeScreen
import com.shrujan.loomina.ui.welcome.SplashScreen
import com.shrujan.loomina.ui.welcome.WelcomeScreen


@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ) {
        // Splash
        composable(Routes.SPLASH) {
            SplashScreen(navController = navController)
        }

        composable(Routes.WELCOME) {
            WelcomeScreen(navController = navController)
        }

        composable(Routes.LOGIN) {
            LoginScreen(navController = navController)
        }

        composable(Routes.REGISTER) {
            RegisterScreen(navController = navController)
        }

        composable(Routes.HOME) {
            HomeScreen(navController = navController)
        }

        composable(Routes.CREATE) {
            CreateScreen(navController = navController)
        }

        composable(Routes.THREAD) {
            CreateThreadScreen(navController = navController)
        }

        composable(Routes.STORY) {
            CreateStoryScreen(navController = navController)
        }

        composable(Routes.SHOW_THREADS) {
            ShowThreadsScreen(navController = navController)
        }
    }
}
