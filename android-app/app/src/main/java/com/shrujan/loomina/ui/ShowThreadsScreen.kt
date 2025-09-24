package com.shrujan.loomina.ui

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.shrujan.loomina.R
import com.shrujan.loomina.data.repository.ThreadRepository
import com.shrujan.loomina.viewmodel.ShowThreadsViewModel
import com.shrujan.loomina.viewmodel.factory.ThreadViewModelFactory

@Composable
fun ShowThreadsScreen(
    navController: NavController,
    innerPadding: PaddingValues,
    viewModel: ShowThreadsViewModel = viewModel(factory = ThreadViewModelFactory(
        repository = ThreadRepository(LocalContext.current)
    )),
) {
    val uiState = viewModel.uiState.value

    LaunchedEffect(Unit) {
        viewModel.loadUserThreads()
    }

    when {
        uiState.loading -> Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }

        uiState.error != null -> Text(
            text = "Error: ${uiState.error}",
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(16.dp)
        )

        else -> LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(uiState.threads) { thread ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    onClick = { /* Navigate to thread detail */ }
                ) {
                    Column {
                        // 👤 User row
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(thread.user.userProfileImage?.trim())
                                    .crossfade(true)
                                    .placeholder(R.drawable.placeholder)
                                    .error(R.drawable.image_error)
                                    .listener(
                                        onSuccess = { _: ImageRequest, _: SuccessResult ->
                                            Log.d("CoilDebug", "User image loaded")
                                        },
                                        onError = { _: ImageRequest, error: ErrorResult ->
                                            Log.e("CoilDebug", "User image failed", error.throwable)
                                        }
                                    )
                                    .build(),
                                contentDescription = "User profile image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = thread.user.username,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        // 🖼️ Cover image
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(thread.coverImage?.trim())
                                .crossfade(true)
                                .placeholder(R.drawable.placeholder)
                                .error(R.drawable.image_error)
                                .listener(
                                    onSuccess = { _: ImageRequest, _: SuccessResult ->
                                        Log.d("CoilDebug", "Cover image loaded")
                                    },
                                    onError = { _: ImageRequest, error: ErrorResult ->
                                        Log.e("CoilDebug", "Cover image failed", error.throwable)
                                    }
                                )
                                .build(),
                            contentDescription = "Thread cover image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                        )

                        // 📝 Thread content
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = thread.threadTitle,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = thread.prompt,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 2
                            )
                        }

                        // ❤️ Likes & 💬 Comments row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Favorite,
                                    contentDescription = "Likes",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("${thread.numberOfLikes}")
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "Comments",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("${thread.numberOfComments}")
                            }
                        }
                    }
                }
            }
        }
    }
}
