package com.shrujan.loomina.ui.components

import android.util.Log
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.shrujan.loomina.ui.navigation.BottomNavItem
import com.shrujan.loomina.ui.navigation.Routes

@Composable
fun BottomNavBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Explore,
        BottomNavItem.Create,
        BottomNavItem.Notification,
        BottomNavItem.Profile
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                onClick = {
                    // don't navigate if already selected
                    if (currentDestination?.hierarchy?.any { it.route == item.route } == true) return@NavigationBarItem

                    Log.d("BottomNav", "navigate to ${item.route}")

                    navController.navigate(item.route) {
                        // pop up to the MAIN graph root so other tab entries are cleared
                        popUpTo(Routes.MAIN) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                alwaysShowLabel = false // shows label only for selected
            )

//            NavigationBarItem(
//                icon = { Icon(item.icon, contentDescription = item.label) },
//                label = { Text(item.label) },
//                selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
//                onClick = {
//                    // don't navigate if already selected
//                    if (currentDestination?.hierarchy?.any { it.route == item.route } == true) return@NavigationBarItem
//
//                    Log.d("BottomNav", "navigate to ${item.route}")
//
//                    navController.navigate(item.route) {
//                        // pop up to the MAIN graph root so other tab entries are cleared
//                        popUpTo(Routes.MAIN) {
//                            saveState = true
//                        }
//                        launchSingleTop = true
//                        restoreState = true
//                    }
//                }
//            )
        }
    }
}
