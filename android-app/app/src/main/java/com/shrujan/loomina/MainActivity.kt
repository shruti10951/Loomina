package com.shrujan.loomina

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.shrujan.loomina.ui.components.AppScaffold

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
    AppScaffold(navController = navController)
}
