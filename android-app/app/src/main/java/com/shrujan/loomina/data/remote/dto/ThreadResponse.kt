package com.shrujan.loomina.data.remote.dto

data class ThreadResponse(
    val id: String,
    val threadTitle: String,
    val prompt: String,
    val creationTime: String,
    val user: MinimalUser,      // nested object for user info
    val numberOfLikes: Int,
    val numberOfComments: Int,
    val likedBy: List<String>,
    val coverImage: String?,
    val genre: List<String>,
    val tags: List<String>,
    val reportCount: Int
)
