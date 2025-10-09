package com.shrujan.loomina.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.sharp.AccountBox
import androidx.compose.material.icons.sharp.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun StatsRow (
    likes: Int,
    comments: Int,
    chapters: Int?,
    modifier: Modifier = Modifier
) {
    Row (
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        StatItem(
            icon = Icons.Default.Favorite,
            text = "$likes Likes"
        )
        StatItem(
            icon = Icons.Filled.Face,
            text = "$comments Comments"
        )
        chapters?.let {
            StatItem(
                icon = Icons.Sharp.AccountBox,
                text = "$chapters Chapters"
            )
        }
    }
}

@Composable
fun StatItem(icon: ImageVector, text: String) {
    Row (
        verticalAlignment = Alignment.CenterVertically
    ){
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

