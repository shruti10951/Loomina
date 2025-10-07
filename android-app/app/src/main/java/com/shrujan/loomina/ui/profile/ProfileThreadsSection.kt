package com.shrujan.loomina.ui.profile

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.shrujan.loomina.data.remote.dto.ThreadResponse
import com.shrujan.loomina.ui.thread.ThreadItem
import com.shrujan.loomina.ui.thread.ThreadItemData

@Composable
fun ProfileThreadsSection(
    threads: List<ThreadResponse>,
    error: String?,
    onThreadClick: (ThreadResponse) -> Unit
) {
    if (error != null) {
        Text(text = error)
    } else {
        threads.forEach { thread ->
            ThreadItem(
                thread = ThreadItemData(
                    title = thread.threadTitle,
                    author = thread.user.username,
                    coverImage = thread.coverImage,
                    likes = thread.numberOfLikes,
                    comments = thread.numberOfComments,
                    prompt = thread.prompt
                ),
                onClick = { onThreadClick(thread) }
            )
        }
    }
}

