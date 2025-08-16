package com.shrujan.loomina.ui.welcome

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shrujan.loomina.theme.LoominaTheme

@Composable
fun WelcomeScreen(onGetStartedClick: () -> Unit){
    LoominaTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column (horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Welcome To Loomina",
                    fontSize = 28.sp,
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(32.dp))
                Button(onClick = onGetStartedClick) {
                    Text(
                        text = "Get Started"
                    )
                }
            }
        }
    }
}