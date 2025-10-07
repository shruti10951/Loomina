package com.shrujan.loomina.ui.profile

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.shrujan.loomina.data.remote.dto.StoryResponse
import com.shrujan.loomina.ui.story.StoryItem
import com.shrujan.loomina.ui.story.StoryItemData

@Composable
fun ProfileStoriesSection(
    stories: List<StoryResponse>,
    error: String?,
    onStoryClick: (StoryResponse) -> Unit
) {
    if (error != null) {
        Text(text = error)
    } else {
        stories.forEach { story ->
            StoryItem(
                story = StoryItemData(
                    title = story.storyTitle,
                    author = story.user.username,
                    coverImage = story.coverImage,
                    likes = story.numberOfLikes,
                    comments = story.numberOfComments,
                    synopsis = story.storySynopsis
                ),
                onClick = { onStoryClick(story) }
            )
        }
    }
}
