package com.shrujan.loomina.ui.story

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.shrujan.loomina.data.repository.StoryRepository
import com.shrujan.loomina.viewmodel.factory.StoryViewModelFactory
import com.shrujan.loomina.viewmodel.story.StoryViewModel

@Composable
fun StoryDetailsScreen (
    navController: NavController,
    innerPadding: PaddingValues,
    storyId: String,
    storyViewModel: StoryViewModel = viewModel (
        factory = StoryViewModelFactory(
            repository = StoryRepository(LocalContext.current)
        )
    )
) {

    val storyState by storyViewModel.uiState.collectAsState()

    LaunchedEffect(storyId) {
        storyViewModel.getStoryById(storyId)
    }

    LazyColumn (
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ){
        item {
            when {
                storyState.loading -> Text("Loading Story...")

                storyState.error != null -> Text(
                    text = storyState.error!!,
                    color = MaterialTheme.colorScheme.error
                )

                storyState.currentStory != null -> StoryHeader(
                    story = storyState.currentStory!!
                )
            }
        }
    }
    
}