package com.shrujan.loomina.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
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
import com.shrujan.loomina.ui.explore.ExploreScreen
import com.shrujan.loomina.ui.home.HomeScreen
import com.shrujan.loomina.ui.notification.NotificationScreen
import com.shrujan.loomina.ui.profile.ProfileScreen
import com.shrujan.loomina.ui.welcome.SplashScreen
import com.shrujan.loomina.ui.welcome.WelcomeScreen

@Composable
fun AppNavGraph(navController: NavHostController, innerPadding: PaddingValues) {
    NavHost(navController = navController, startDestination = Routes.SPLASH) {
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
            HomeScreen(
                navController = navController,
                innerPadding = innerPadding
            )
        }

        composable(Routes.EXPLORE) {
            ExploreScreen(
                navController = navController,
                innerPadding = innerPadding
            )
        }

        composable(Routes.CREATE) {
            CreateScreen(
                navController = navController,
                innerPadding = innerPadding
            )
        }

        composable(Routes.NOTIFICATION) {
            NotificationScreen(
                navController = navController,
                innerPadding = innerPadding
            )
        }

        composable(Routes.PROFILE) {
            ProfileScreen(
                navController = navController,
                innerPadding = innerPadding
            )
        }

        composable(Routes.THREAD) {
            CreateThreadScreen(navController = navController)
        }

        composable(Routes.STORY) {
            CreateStoryScreen(navController = navController)
        }

        composable(Routes.SHOW_THREADS) {
            ShowThreadsScreen(
                navController = navController,
                innerPadding = innerPadding
            )
        }

    }
}
