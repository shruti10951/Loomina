package com.shrujan.loomina.data.remote.dto

data class StoryResponse(
    val id: String,
    val storyTitle: String,
    val storySynopsis: String,
    val creationTime: String,
    val user: MinimalUser,      // nested object for user info
    val numberOfLikes: Int,
    val numberOfComments: Int,
    val likedBy: List<String>,
    val coverImage: String?,
    val genre: List<String>,
    val tags: List<String>,
    val reportCount: Int,
    val isCompleted: Boolean
)
