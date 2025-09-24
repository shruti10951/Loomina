package com.shrujan.loomina.ui.profile

import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.request.ErrorResult
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
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
import com.shrujan.loomina.data.repository.UserRepository
import com.shrujan.loomina.ui.story.StoryItem
import com.shrujan.loomina.ui.story.StoryItemData
import com.shrujan.loomina.ui.thread.ThreadItem
import com.shrujan.loomina.ui.thread.ThreadItemData
import com.shrujan.loomina.viewmodel.UserViewModel
import com.shrujan.loomina.viewmodel.factory.UserViewModelFactory
import kotlin.concurrent.thread


@Composable
fun ProfileScreen(
    navController: NavController,
    innerPadding: PaddingValues,
    viewModel: UserViewModel = viewModel(factory = UserViewModelFactory (
        userRepository = UserRepository(LocalContext.current)
    )),
) {
    val user by viewModel.userState.collectAsState()
    val error by viewModel.error.collectAsState()

    // Tabs state
    val tabs = listOf("Threads", "Stories")
    val selectedTab = remember { mutableIntStateOf(0) }
    
    // Dummy data
    val dummyThreads = listOf(
        ThreadItemData(
            title = "A Journey Through Loomina",
            author = "Shruti",
            coverImage = "https://www.allkpop.com/upload/2024/09/content/250959/1727272769-20240925-jeonghan.jpg",
            prompt = "A story about lost memories. A story about lost memories",
            likes = 12,
            comments = 2
        ),
        ThreadItemData(
            title = "Mystery in the AI World",
            author = "Janhavi",
            coverImage = "https://www.billboard.com/wp-content/uploads/2022/08/Mingyu-of-SEVENTEEN-2021-billboard-1548.jpg",
            prompt = "A story about Namita lol. A story about lost memories",
            likes = 22,
            comments = 20
        ),
        ThreadItemData(
            title = "Collaborative Story Example",
            author = "Namita",
            coverImage = null,
            prompt = "A story about l7tgyvbhwst memories",
            likes = 34,
            comments = 12
        )
    )

    val dummyStories = listOf(
        StoryItemData(
            title = "Collaborative Story Example",
            author = "Namita",
            coverImage = null,
            synopsis = "A story about l7tgyvbhwst memories",
            likes = 34,
            comments = 12
        ),
        StoryItemData(
            title = "Mystery in the AI World",
            author = "Janhavi",
            coverImage = "https://www.billboard.com/wp-content/uploads/2022/08/Mingyu-of-SEVENTEEN-2021-billboard-1548.jpg",
            synopsis = "A story about Namita lol. A story about lost memories",
            likes = 22,
            comments = 20
        ),
        StoryItemData(
            title = "A Journey Through Loomina",
            author = "Shruti",
            coverImage = "https://www.allkpop.com/upload/2024/09/content/250959/1727272769-20240925-jeonghan.jpg",
            synopsis = "A story about lost memories. A story about lost memories",
            likes = 12,
            comments = 2
        )
    )



    Box(modifier = Modifier.fillMaxSize()) {
        if(error != null) {
            Text(text = error ?: "Unknown error")
        } else if(user == null) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))

                    // Profile Header
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(user!!.userProfileImage?.trim())
                            .crossfade(true)
                            .placeholder(R.drawable.placeholder)       // your placeholder drawable
                            .error(R.drawable.image_error)             // your error drawable
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

                    // Stats Row
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        ProfileStat(count = user!!.followers.size.toString(), label = "Followers")
                        ProfileStat(count = user!!.following.size.toString(), label = "Following")
                        ProfileStat(count = "0", label = "Threads") // TODO: fetch actual
                        ProfileStat(count = "0", label = "Stories") // TODO: fetch actual
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Actions
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

                // Display posts based on selected tab
                item {
                    val itemsList = if(selectedTab.intValue == 0) dummyThreads else dummyStories

                    Column {
                        if(selectedTab.value == 0) {
                            itemsList.forEach { thread ->
                                ThreadItem(thread = thread as ThreadItemData) {
                                    // TODO: navigate to thread detail
                                }
                            }
                        } else {
                            itemsList.forEach { story ->
                                StoryItem(story = story as StoryItemData) {
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



@Composable
fun ProfileStat(count: String, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(count, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Text(label, style = MaterialTheme.typography.bodySmall)
    }
}