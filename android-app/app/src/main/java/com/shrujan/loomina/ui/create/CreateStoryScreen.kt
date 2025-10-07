package com.shrujan.loomina.ui.create

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.shrujan.loomina.data.remote.dto.StoryRequest
import com.shrujan.loomina.data.repository.StoryRepository
import com.shrujan.loomina.viewmodel.story.CreateStoryViewModel
import com.shrujan.loomina.viewmodel.factory.StoryViewModelFactory

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CreateStoryScreen(
    navController: NavController,
    viewModel: CreateStoryViewModel = viewModel(
        factory = StoryViewModelFactory(
            repository = StoryRepository(LocalContext.current)
        )
    )
) {
    val context = LocalContext.current

    var storyTitle by remember { mutableStateOf("") }
    var storySynopsis by remember { mutableStateOf("") }
    var coverImageUrl by remember { mutableStateOf("") }

    val genres = listOf("Fantasy", "Sci-Fi", "Romance", "Horror", "Mystery", "Drama", "Comedy")
    val selectedGenres = remember { mutableStateListOf<String>() }

    val tags = remember { mutableStateListOf<String>() }
    var currentTag by remember {mutableStateOf("")}

    // Observe ViewModel State
    val uiState by viewModel.uiState.collectAsState()

    val isFormValid = storyTitle.trim().isNotEmpty() &&
            storySynopsis.trim().isNotEmpty() &&
            selectedGenres.isNotEmpty() &&
            tags.isNotEmpty()

    // 🔔 Toast for errors
    uiState.error?.let { errorMsg ->
        LaunchedEffect(errorMsg) {
            Toast.makeText(
                context,
                "Error: $errorMsg",
                Toast.LENGTH_SHORT)
                .show()
        }
    }

    // 🔔 Toast for success
    uiState.story?.let { createdStory ->
        LaunchedEffect(createdStory) {
            Toast.makeText(
                context,
                "Story created successfully!",
                Toast.LENGTH_SHORT)
                .show()

            navController.popBackStack() // go back after success
            viewModel.resetState()
            // Reset local form fields if needed
            storyTitle = ""
            storySynopsis = ""
            coverImageUrl = ""
            selectedGenres.clear()
            tags.clear()
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
            // Story Title
            OutlinedTextField(
                value = storyTitle,
                onValueChange = { storyTitle = it },
                label = { Text("Story Title") },
                modifier = Modifier.fillMaxWidth()
            )

            // Synopsis
            OutlinedTextField(
                value = storySynopsis,
                onValueChange = { storySynopsis = it },
                label = { Text("Story Synopsis") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 6
            )

            // Cover Image URL
            OutlinedTextField(
                value = coverImageUrl,
                onValueChange = { coverImageUrl = it },
                label = { Text("Cover Image URL") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Uri)
            )

            if(coverImageUrl.isNotBlank()) {
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
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = currentTag,
                    onValueChange = { currentTag = it },
                    label = { Text("Enter Tag") },
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        if (currentTag.isNotBlank() && !tags.contains(currentTag)) {
                            tags.add(currentTag)
                            currentTag = ""
                        }
                    }
                ) {
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
                            IconButton(onClick = { tags.remove(tag) } ) {
                                Icon(Icons.Default.Close, contentDescription = "Remove Tag")
                            }
                        }
                    )
                }
            }

            // Submit Button
            Button(
                onClick = {
                    viewModel.createStory(
                        StoryRequest(
                            storyTitle = storyTitle,
                            storySynopsis = storySynopsis,
                            coverImage = coverImageUrl,
                            genre = selectedGenres,
                            tags = tags
                        )
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
                    Text("Create Story")
                }
            }
        }
    }


}