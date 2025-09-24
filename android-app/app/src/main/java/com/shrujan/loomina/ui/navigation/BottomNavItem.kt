package com.shrujan.loomina.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem (
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    object Home: BottomNavItem(Routes.HOME, "Home", Icons.Filled.Home)
    object Explore: BottomNavItem(Routes.EXPLORE, "Explore", Icons.Filled.Search)
    object Create: BottomNavItem(Routes.CREATE, "Create", Icons.Filled.Add)
    object Notification: BottomNavItem(Routes.NOTIFICATION, "Notif", Icons.Filled.Notifications)
    object Profile: BottomNavItem(Routes.PROFILE, "Profile", Icons.Filled.Person)
}