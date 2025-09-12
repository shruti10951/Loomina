package com.shrujan.loomina.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.shrujan.loomina.theme.LoominaTheme
import com.shrujan.loomina.viewmodel.AuthViewModel
import com.shrujan.loomina.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel,
    authViewModel: AuthViewModel
) {
    val userState by viewModel.user.collectAsState()
    val errorState by viewModel.error.collectAsState()

    // Load user when screen starts
    LaunchedEffect(Unit) {
        viewModel.loadUser()
    }

    LoominaTheme {
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
                            onClick = { navController.navigate("create") }
                        ) {
                            Text("Create")
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
}
