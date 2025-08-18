package com.shrujan.loomina.data.remote.dto

import com.google.gson.annotations.SerializedName
import java.util.Date

data class UserResponse (
    @SerializedName("_id") val id: String,
    val username: String,
    val email: String,
    val bio: String? = null,
    val userProfileImage: String? = null,
    val favouriteGenres: List<String> = emptyList(),
    val favouriteTags: List<String> = emptyList(),
    val followers: List<String> = emptyList(),
    val following: List<String> = emptyList(),
    val creationTime: String

)