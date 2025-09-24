package com.shrujan.loomina.ui.profile

import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.request.ErrorResult
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.shrujan.loomina.R
import com.shrujan.loomina.data.repository.StoryRepository
import com.shrujan.loomina.data.repository.ThreadRepository
import com.shrujan.loomina.data.repository.UserRepository
import com.shrujan.loomina.ui.story.StoryItem
import com.shrujan.loomina.ui.story.StoryItemData
import com.shrujan.loomina.ui.thread.ThreadItem
import com.shrujan.loomina.ui.thread.ThreadItemData
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
    userViewModel: UserViewModel = viewModel(factory = UserViewModelFactory(
        userRepository = UserRepository(LocalContext.current)
    )),
    threadViewModel: ThreadViewModel = viewModel(factory = ThreadViewModelFactory(
        repository = ThreadRepository(LocalContext.current),
    )),
    storyViewModel: StoryViewModel = viewModel(factory = StoryViewModelFactory(
        repository = StoryRepository(LocalContext.current),
    )),
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
                Text(text = userError ?: "Unknown error")
            }
            user == null -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    // Profile header
                    item {
                        Spacer(modifier = Modifier.height(16.dp))

                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(user!!.userProfileImage?.trim())
                                .crossfade(true)
                                .placeholder(R.drawable.placeholder)
                                .error(R.drawable.image_error)
                                .listener(
                                    onSuccess = { _: ImageRequest, _: SuccessResult ->
                                        Log.d("CoilDebug", "Profile image loaded successfully")
                                    },
                                    onError = { _: ImageRequest, error: ErrorResult ->
                                        Log.e("CoilDebug", "Profile image failed to load", error.throwable)
                                    }
                                )
                                .build(),
                            contentDescription = "Profile Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(user!!.username, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Text(user!!.email, style = MaterialTheme.typography.bodyMedium)
                        Text(
                            user!!.bio ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(horizontal = 16.dp),
                            maxLines = 2
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {
                            ProfileStat(count = user!!.followers.size.toString(), label = "Followers")
                            ProfileStat(count = user!!.following.size.toString(), label = "Following")
                            ProfileStat(count = myThreads.size.toString(), label = "Threads")
                            ProfileStat(count = myStories.size.toString(), label = "Stories")
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { /* TODO: Edit Profile */ },
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            Text("Edit Profile")
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        TabRow(selectedTabIndex = selectedTab.intValue) {
                            tabs.forEachIndexed { index, title ->
                                Tab(
                                    selected = selectedTab.intValue == index,
                                    onClick = { selectedTab.intValue = index },
                                    text = { Text(title) }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    // Threads / Stories section
                    if (selectedTab.intValue == 0) {
                        when {
                            threadError != null -> {
                                item { Text(text = threadError ?: "Error loading threads") }
                            }
                            else -> {
                                items(myThreads) { thread ->
                                    ThreadItem(
                                        thread = ThreadItemData(
                                            title = thread.threadTitle,
                                            author = thread.user.username,
                                            coverImage = thread.coverImage,
                                            likes = thread.numberOfLikes,
                                            comments = thread.numberOfComments,
                                            prompt = thread.prompt
                                        )
                                    ) {
                                        // TODO: navigate to thread detail
                                    }
                                }
                            }
                        }
                    } else {
                        when {
                            storyError != null -> {
                                item { Text(text = storyError ?: "Error loading stories") }
                            }
                            else -> {
                                items(myStories) { story ->
                                    StoryItem(
                                        story = StoryItemData(
                                            title = story.storyTitle,
                                            author = story.user.username,
                                            coverImage = story.coverImage,
                                            likes = story.numberOfLikes,
                                            comments = story.numberOfComments,
                                            synopsis = story.storySynopsis,
                                        )
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
}

@Composable
fun ProfileStat(count: String, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(count, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Text(label, style = MaterialTheme.typography.bodySmall)
    }
}
