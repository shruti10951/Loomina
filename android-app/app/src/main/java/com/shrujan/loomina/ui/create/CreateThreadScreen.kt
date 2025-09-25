package com.shrujan.loomina.ui.create

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.shrujan.loomina.R
import com.shrujan.loomina.data.repository.ThreadRepository
import com.shrujan.loomina.viewmodel.CreateThreadViewModel
import com.shrujan.loomina.viewmodel.factory.ThreadViewModelFactory

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CreateThreadScreen(
    navController: NavController,
    viewModel: CreateThreadViewModel = viewModel(
        factory = ThreadViewModelFactory(
            repository = ThreadRepository(LocalContext.current)
        )
    )
) {
    val context = LocalContext.current

    var threadTitle by remember { mutableStateOf("") }
    var prompt by remember { mutableStateOf("") }
    var coverImageUrl by remember { mutableStateOf("") }

    val genres = listOf("Fantasy", "Sci-Fi", "Romance", "Horror", "Mystery", "Drama", "Comedy")
    val selectedGenres = remember { mutableStateListOf<String>() }

    val tags = remember { mutableStateListOf<String>() }
    var currentTag by remember { mutableStateOf("") }

    // Observe ViewModel state
    val uiState by viewModel.uiState.collectAsState()

    val isFormValid = threadTitle.trim().isNotEmpty() &&
            prompt.trim().isNotEmpty() &&
            selectedGenres.isNotEmpty() &&
            tags.isNotEmpty()

    // 🔔 Toast for errors (one-time)
    uiState.error?.let { errorMsg ->
        LaunchedEffect(errorMsg) {
            Toast.makeText(context, "Error: $errorMsg", Toast.LENGTH_SHORT).show()
        }
    }

    // 🔔 Toast for success (one-time)
    uiState.thread?.let { createdThread ->
        LaunchedEffect(createdThread) {
            Toast.makeText(context, "Thread created successfully!", Toast.LENGTH_SHORT).show()
            navController.popBackStack() // go back after success
        }
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Thread Title
            OutlinedTextField(
                value = threadTitle,
                onValueChange = { threadTitle = it },
                label = { Text("Thread Title") },
                modifier = Modifier.fillMaxWidth()
            )

            // Prompt
            OutlinedTextField(
                value = prompt,
                onValueChange = { prompt = it },
                label = { Text("Prompt") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 4
            )

            // Cover Image URL
            OutlinedTextField(
                value = coverImageUrl,
                onValueChange = { coverImageUrl = it },
                label = { Text("Cover Image URL") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Uri)
            )

            if (coverImageUrl.isNotBlank()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(coverImageUrl.trim())
                            .crossfade(true)
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.image_error)
                            .listener(
                                onSuccess = { _: ImageRequest, _: SuccessResult ->
                                    Log.d("CoilDebug", "Image loaded successfully")
                                },
                                onError = { _: ImageRequest, error: ErrorResult ->
                                    Log.e("CoilDebug", "Image load failed", error.throwable)
                                }
                            )
                            .build(),
                        contentDescription = "Cover Image Preview",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            // Genre Chips
            Text("Select Genres")
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                genres.forEach { genre ->
                    FilterChip(
                        selected = selectedGenres.contains(genre),
                        onClick = {
                            if (selectedGenres.contains(genre)) {
                                selectedGenres.remove(genre)
                            } else {
                                selectedGenres.add(genre)
                            }
                        },
                        label = { Text(genre) }
                    )
                }
            }

            // Tags Input
            Text("Add Tags")
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = currentTag,
                    onValueChange = { currentTag = it },
                    label = { Text("Enter Tag") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    if (currentTag.isNotBlank() && !tags.contains(currentTag)) {
                        tags.add(currentTag)
                        currentTag = ""
                    }
                }) {
                    Text("Add")
                }
            }

            // Display Tags
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                tags.forEach { tag ->
                    InputChip(
                        selected = false,
                        onClick = { },
                        label = { Text(tag) },
                        trailingIcon = {
                            IconButton(onClick = { tags.remove(tag) }) {
                                Icon(Icons.Default.Close, contentDescription = "Remove tag")
                            }
                        }
                    )
                }
            }

            // Submit Button
            Button(
                onClick = {
                    viewModel.createThread(
                        threadTitle = threadTitle,
                        prompt = prompt,
                        coverImage = coverImageUrl,
                        genre = selectedGenres,
                        tags = tags
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = isFormValid && !uiState.loading
            ) {
                if (uiState.loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Create Thread")
                }
            }
        }
    }
}
