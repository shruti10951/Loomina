package com.shrujan.loomina.ui.navigation

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.shrujan.loomina.ui.create.CreateStoryScreen
import com.shrujan.loomina.ui.create.CreateThreadScreen
import com.shrujan.loomina.ui.auth.LoginScreen
import com.shrujan.loomina.ui.auth.RegisterScreen
import com.shrujan.loomina.ui.create.CreateScreen
import com.shrujan.loomina.ui.explore.ExploreScreen
import com.shrujan.loomina.ui.home.HomeScreen
import com.shrujan.loomina.ui.library.LibraryScreen
import com.shrujan.loomina.ui.profile.ProfileScreen
import com.shrujan.loomina.ui.spark.AddSparksScreen
import com.shrujan.loomina.ui.welcome.SplashScreen
import com.shrujan.loomina.ui.welcome.WelcomeScreen

@Composable
fun AppNavGraph(navController: NavHostController, innerPadding: PaddingValues) {
    NavHost(navController = navController, startDestination = Routes.SPLASH) {

        // top-level onboarding/auth routes
        composable(Routes.SPLASH) { SplashScreen(navController = navController) }
        composable(Routes.WELCOME) { WelcomeScreen(navController = navController) }
        composable(Routes.LOGIN) { LoginScreen(navController = navController) }
        composable(Routes.REGISTER) { RegisterScreen(navController = navController) }

        // MAIN nested graph: contains all bottom-nav screens and related screens
        navigation(startDestination = Routes.HOME, route = Routes.MAIN) {
            composable(Routes.HOME) {
                HomeScreen(navController = navController, innerPadding = innerPadding)
            }
            composable(Routes.EXPLORE) {
                ExploreScreen(navController = navController, innerPadding = innerPadding)
            }
            composable(Routes.CREATE) {
                CreateScreen(navController = navController, innerPadding = innerPadding)
            }
            composable(Routes.LIBRARY) {
                LibraryScreen(navController = navController, innerPadding = innerPadding)
            }
            composable(Routes.PROFILE) {
                ProfileScreen(navController = navController, innerPadding = innerPadding)
            }

            // screens reachable from main (kept inside main so back behavior is consistent)
            composable(Routes.THREAD) { CreateThreadScreen(navController = navController) }
            composable(Routes.STORY) { CreateStoryScreen(navController = navController) }

            composable(
                route = Routes.CREATE_SPARK,
                arguments = listOf(navArgument("threadId") { type = NavType.StringType })
            ) { backStackEntry ->
                val threadId = backStackEntry.arguments?.getString("threadId") ?: ""
                AddSparksScreen(
                    navController = navController,
                    innerPadding = innerPadding,
                    threadId = threadId,
                    onSparkAdded = {
                        Log.d("Spark", "Created")
                    }
                )
            }


            // FOR TESTING ONLY
            composable("test_spark") {
                AddSparksScreen()
            }



        }
    }
}
