package com.shrujan.loomina.data.remote.dto

data class SparkLikeResponse (
    val sparkId: String,
    val action: String,
    val numberOfLikes: Int,
    val likedByCurrentUser: Boolean
)