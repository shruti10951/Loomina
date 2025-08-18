package com.shrujan.loomina.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shrujan.loomina.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    onLogoutClick: () -> Unit,
    viewModel: HomeViewModel,
    token: String
) {

    val userState by viewModel.user.collectAsState()
    val errorState by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadUser(token)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when {
            userState != null -> {
                Column {
                    Text("Welcome, ${userState!!.username}")
                    Text("Email: ${userState!!.email}")
                    Text("Bio: ${userState!!.bio ?: "No bio"}")

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(onClick = onLogoutClick) {
                        Text("Logout")
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
