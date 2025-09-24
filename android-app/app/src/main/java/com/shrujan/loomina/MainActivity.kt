package com.shrujan.loomina

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.shrujan.loomina.theme.LoominaTheme
import com.shrujan.loomina.ui.components.AppScaffold

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Let Compose handle the system bars and background
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            LoominaApp()
        }
    }
}

@Composable
fun LoominaApp() {
    LoominaTheme {
        // Surface ensures proper background according to light/dark theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = androidx.compose.material3.MaterialTheme.colorScheme.background
        ) {
            val navController = rememberNavController()
            AppScaffold(navController = navController)
        }
    }
}
