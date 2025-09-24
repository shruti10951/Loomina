package com.shrujan.loomina.ui.thread

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.shrujan.loomina.R

data class ThreadItemData (
    val title: String,
    val author: String,
    val coverImage: String?,
    val prompt: String,
    val likes: Int,
    val comments: Int
)

@Composable
fun ThreadItem(
    thread: ThreadItemData,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable{ onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(thread.coverImage?.trim())
                    .crossfade(true)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.image_error)
                    .build(),
                contentDescription = "Thread Cover Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(text = thread.title, style = MaterialTheme.typography.titleMedium)

                Spacer(modifier = Modifier.height(4.dp))

                Text(text = "by ${thread.author}", style = MaterialTheme.typography.bodySmall)

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = thread.prompt,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${thread.likes} likes • ${thread.comments} comments",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}