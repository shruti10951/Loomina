package com.shrujan.loomina.ui.thread

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.shrujan.loomina.R
import com.shrujan.loomina.data.remote.dto.ThreadResponse


@Composable
fun ThreadHeader (
    thread: ThreadResponse
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column (
            modifier = Modifier
                .padding(12.dp)
        ) {
            Text(
                text = thread.threadTitle,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = thread.prompt,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            if(!thread.coverImage.isNullOrEmpty()) {
                AsyncImage(
                    model = thread.coverImage,
                    contentDescription = "Cover Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    placeholder = painterResource(R.drawable.placeholder),
                    error = painterResource(R.drawable.image_error)
                )
            }

        }
    }
}