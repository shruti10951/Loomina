package com.shrujan.loomina.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ThreadResponse(
    @SerializedName("_id") val id: String,

    val threadTitle: String,
    val prompt: String,

    val creationTime: String,

    val user: MinimalUser,      // nested object for user info

    val numberOfLikes: Int,
    val numberOfComments: Int,
    val likedBy: List<String>,

    val likedByCurrentUser: Boolean,

    val coverImage: String?,

    val genre: List<String>,
    val tags: List<String>,

    val reportCount: Int

)
