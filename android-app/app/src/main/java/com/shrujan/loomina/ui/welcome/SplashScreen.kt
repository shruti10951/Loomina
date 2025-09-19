package com.shrujan.loomina.ui.welcome


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.shrujan.loomina.data.local.UserPreferences
import com.shrujan.loomina.data.repository.AuthRepository
import com.shrujan.loomina.ui.navigation.Routes
import com.shrujan.loomina.viewmodel.AuthState
import com.shrujan.loomina.viewmodel.AuthViewModel
import com.shrujan.loomina.viewmodel.factory.AuthViewModelFactory
import kotlinx.coroutines.delay

/**
 * A simple splash screen that displays the app name "Loomina"
 */
@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(
        AuthRepository(LocalContext.current),
        UserPreferences(LocalContext.current)
    ))
) {

    val state by viewModel.authState.collectAsStateWithLifecycle()
    var splashFinished by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(2000)
        splashFinished = true
    }

    when {
        !splashFinished -> { /* show logo */ }
        state is AuthState.Authenticated -> {
            LaunchedEffect(Unit) {
                navController.navigate(Routes.HOME) {
                    popUpTo(0) { inclusive = true }
                }
            }
        }
        state is AuthState.Unauthenticated -> {
            LaunchedEffect(Unit) {
                navController.navigate(Routes.WELCOME) {
                    popUpTo(0) { inclusive = true }
                }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Loomina",
            style = MaterialTheme.typography.headlineLarge
        )
    }
}

