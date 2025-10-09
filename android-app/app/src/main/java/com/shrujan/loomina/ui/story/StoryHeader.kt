package com.shrujan.loomina.ui.story

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.shrujan.loomina.R
import com.shrujan.loomina.data.remote.dto.StoryResponse
import com.shrujan.loomina.ui.components.GenreTagRow
import com.shrujan.loomina.ui.components.StatsRow
import com.shrujan.loomina.ui.components.UserInfoRow

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StoryHeader(story: StoryResponse) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            // Cover Image
            if (!story.coverImage.isNullOrEmpty()) {
                AsyncImage(
                    model = story.coverImage,
                    contentDescription = "Cover Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    placeholder = painterResource(R.drawable.placeholder),
                    error = painterResource(R.drawable.image_error)
                )
            }

            Column(modifier = Modifier.padding(16.dp)) {
                // User info
                UserInfoRow(
                    username = story.user.username,
                    profileImage = story.user.userProfileImage,
                    creationTime = story.creationTime
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Title & synopsis
                Text(
                    text = story.storyTitle,
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = story.storySynopsis,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Stats
                StatsRow(
                    likes = story.numberOfLikes,
                    comments = story.numberOfComments,
                    chapters = story.numberOfChapters
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Genres & tags
                GenreTagRow(story.genre, story.tags)

                Spacer(modifier = Modifier.height(8.dp))

                // Completed status
                if (story.isCompleted) {
                    Text(
                        text = "✅ Completed",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

