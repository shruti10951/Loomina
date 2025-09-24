package com.shrujan.loomina.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.shrujan.loomina.ui.navigation.AppNavGraph
import com.shrujan.loomina.ui.navigation.Routes

@Composable
fun AppScaffold(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavRoutes = listOf(
        Routes.HOME,
        Routes.EXPLORE,
        Routes.CREATE,
        Routes.LIBRARY,
        Routes.PROFILE
    )

    val showBottomBar = currentRoute in bottomNavRoutes

    // Exit app from any bottom-tab route
    BackHandler(enabled = currentRoute in bottomNavRoutes) {
        (navController.context as? androidx.activity.ComponentActivity)?.finishAffinity()
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) BottomNavBar(navController = navController)
        }
    ) { innerPadding ->
        AppNavGraph(
            navController = navController,
            innerPadding = innerPadding
        )
    }
}
