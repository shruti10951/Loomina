package com.shrujan.loomina.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.shrujan.loomina.data.local.UserPreferences
import com.shrujan.loomina.data.repository.AuthRepository
import com.shrujan.loomina.data.repository.UserRepository
import com.shrujan.loomina.theme.LoominaTheme
import com.shrujan.loomina.viewmodel.AuthViewModel
import com.shrujan.loomina.viewmodel.HomeViewModel
import com.shrujan.loomina.viewmodel.factory.AuthViewModelFactory
import com.shrujan.loomina.viewmodel.factory.HomeViewModelFactory

@Composable
fun HomeScreen(
    navController: NavController,
    innerPadding: PaddingValues,
    viewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(
            userRepository = UserRepository(LocalContext.current)
        )
    ),
    authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(
            AuthRepository(LocalContext.current),
            UserPreferences(LocalContext.current)
        )
    )
) {
    val userState by viewModel.user.collectAsState()
    val errorState by viewModel.error.collectAsState()

    // Load user when screen starts
    LaunchedEffect(Unit) {
        viewModel.loadUser()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        when {
            userState != null -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Welcome, ${userState!!.username}")
                    Text("Email: ${userState!!.email}")
                    Text("Bio: ${userState!!.bio ?: "No bio"}")

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            authViewModel.logout()
                            navController.navigate("login") {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    ) {
                        Text("Logout")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { navController.navigate("show-threads") }
                    ) {
                        Text("Show Threads")
                    }
                }
            }

            errorState != null -> {
                Text("Error: $errorState")
            }

            else -> {
                CircularProgressIndicator()
            }
        }

    }
}
