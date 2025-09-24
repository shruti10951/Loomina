package com.shrujan.loomina.ui.profile

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.shrujan.loomina.R
import com.shrujan.loomina.data.remote.dto.UserResponse

@Composable
fun ProfileHeader(user: UserResponse, threadCount: Int, storyCount: Int) {
    Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(user.userProfileImage?.trim())
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
                .size(100.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(user.username, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text(user.email, style = MaterialTheme.typography.bodyMedium)
        Text(
            user.bio ?: "",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(horizontal = 16.dp),
            maxLines = 2
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        ) {
            ProfileStat(count = user.followers.size.toString(), label = "Followers")
            ProfileStat(count = user.following.size.toString(), label = "Following")
            ProfileStat(count = threadCount.toString(), label = "Threads")
            ProfileStat(count = storyCount.toString(), label = "Stories")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { /* TODO: Edit Profile */ },
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text("Edit Profile")
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