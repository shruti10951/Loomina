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
import com.shrujan.loomina.data.repository.StoryRepository
import com.shrujan.loomina.data.repository.ThreadRepository
import com.shrujan.loomina.data.repository.UserRepository
import com.shrujan.loomina.theme.LoominaTheme
import com.shrujan.loomina.viewmodel.StoryViewModel
import com.shrujan.loomina.viewmodel.ThreadViewModel
import com.shrujan.loomina.viewmodel.UserViewModel
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
        factory = ThreadViewModelFactory(ThreadRepository(LocalContext.current))
    ),
    storyViewModel: StoryViewModel = viewModel(
        factory = StoryViewModelFactory(StoryRepository(LocalContext.current))
    ),
) {
    val user by userViewModel.userState.collectAsState()
    val userError by userViewModel.error.collectAsState()

    val myThreads by threadViewModel.threads.collectAsState()
    val threadError by threadViewModel.error.collectAsState()

    val myStories by storyViewModel.stories.collectAsState()
    val storyError by storyViewModel.error.collectAsState()

    val tabs = listOf("Threads", "Stories")
    val selectedTab = remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        threadViewModel.getMyThreads()
        storyViewModel.getMyStories()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            userError != null -> {
                Text(
                    text = userError ?: "Unknown error",
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            user == null -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        ProfileHeader(user = user!!, myThreads.size, myStories.size)

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
                            ProfileThreadsSection(
                                threads = myThreads,
                                error = threadError
                            ) {
                                // TODO: navigate to thread detail
                            }
                        } else {
                            ProfileStoriesSection(
                                stories = myStories,
                                error = storyError
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
