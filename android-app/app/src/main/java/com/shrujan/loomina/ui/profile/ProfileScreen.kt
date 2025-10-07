package com.shrujan.loomina.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.shrujan.loomina.data.repository.SparkRepository
import com.shrujan.loomina.data.repository.StoryRepository
import com.shrujan.loomina.data.repository.ThreadRepository
import com.shrujan.loomina.data.repository.UserRepository
import com.shrujan.loomina.ui.navigation.Routes
import com.shrujan.loomina.viewmodel.story.StoryViewModel
import com.shrujan.loomina.viewmodel.thread.ThreadViewModel
import com.shrujan.loomina.viewmodel.social.UserViewModel
import com.shrujan.loomina.viewmodel.factory.StoryViewModelFactory
import com.shrujan.loomina.viewmodel.factory.ThreadViewModelFactory
import com.shrujan.loomina.viewmodel.factory.UserViewModelFactory

@Composable
fun ProfileScreen(
    navController: NavController,
    innerPadding: PaddingValues,
    userViewModel: UserViewModel = viewModel(
        factory = UserViewModelFactory(UserRepository(LocalContext.current))
    ),
    threadViewModel: ThreadViewModel = viewModel(
        factory = ThreadViewModelFactory(
            threadRepository = ThreadRepository(LocalContext.current),
            sparkRepository = SparkRepository(LocalContext.current)
        )
    ),
    storyViewModel: StoryViewModel = viewModel(
        factory = StoryViewModelFactory(StoryRepository(LocalContext.current))
    ),
) {
    val userState by userViewModel.uiState.collectAsState()

    val threadState by threadViewModel.uiState.collectAsState()

    val storyState by storyViewModel.uiState.collectAsState()

    val tabs = listOf("Threads", "Stories")
    val selectedTab = remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        threadViewModel.getMyThreads()
        storyViewModel.getMyStories()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {

            userState.loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            userState.error != null -> {
                Text(
                    text = userState.error ?: "Unknown error",
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            userState.user != null -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        ProfileHeader(
                            user = userState.user!!,
                            threadCount = threadState.myThreads.size,
                            storyCount = storyState.myStories.size
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        ProfileTabs(
                            tabs = tabs,
                            selectedTabIndex = selectedTab.intValue,
                            onTabSelected = { selectedTab.intValue = it }
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    item {
                        if (selectedTab.intValue == 0) {
                            if (threadState.myThreads.isEmpty()) {
                                Text("You haven’t created any threads yet.")
                            } else {
                                ProfileThreadsSection(
                                    threads = threadState.myThreads,
                                    error = threadState.error
                                ) { thread ->
                                    navController.navigate(Routes.showThreadDetails(thread.id))
                                }
                            }
                        } else {
                            if (storyState.myStories.isEmpty()) {
                                Text("You haven’t created any stories yet.")
                            } else {
                                ProfileStoriesSection(
                                    stories = storyState.myStories,
                                    error = storyState.error
                                ) {
                                    // TODO: navigate to story detail
                                }
                            }

                        }
                    }
                }
            }

        }
    }


}
