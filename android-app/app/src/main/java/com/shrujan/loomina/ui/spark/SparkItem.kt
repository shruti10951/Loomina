package com.shrujan.loomina.ui.spark

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.shrujan.loomina.R
import com.shrujan.loomina.data.remote.dto.SparkResponse

@Composable
fun SparkItem(
    spark: SparkResponse,
    onCommentClick: () -> Unit = {},
    onExtendClick: () -> Unit = {},
    onLikeClick: () -> Unit
) {

    var isLiked by remember { mutableStateOf(spark.likedByCurrentUser) }
    var likesCount by remember { mutableStateOf(spark.numberOfLikes) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(spark.user.userProfileImage?.trim())
                    .crossfade(true)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.image_error)
                    .listener(
                        onSuccess = { _: ImageRequest, _: SuccessResult ->
                            Log.d("CoilDebug", "Profile image loaded successfully")
                        },
                        onError = { _: ImageRequest, error: ErrorResult ->
                            Log.e("CoilDebug", "Profile image failed", error.throwable)
                        }
                    )
                    .build(),
                contentDescription = "Profile Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = spark.user.username,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = spark.sparkText,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Like button
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { onLikeClick() }) {
                            Icon(
                                imageVector = if (spark.likedByCurrentUser)
                                    Icons.Filled.Favorite
                                else
                                    Icons.Filled.FavoriteBorder,
                                contentDescription = "Like",
                                tint = if(spark.likedByCurrentUser)
                                    MaterialTheme.colorScheme.primary
                                else
                                    LocalContentColor.current
                            )
                        }

                        Text(text = "${spark.numberOfLikes}")
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onCommentClick) {
                            Icon(
                                imageVector = Icons.Filled.Face,
                                contentDescription = "Comment"
                            )
                        }
                        Text(text = "${spark.numberOfComments}")
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onExtendClick) {
                            Icon(
                                imageVector = Icons.Filled.Create,
                                contentDescription = "Extend"
                            )
                        }
                    }
                }
            }
        }
    }
}
