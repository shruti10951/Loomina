package com.shrujan.loomina.data.remote.dto

import com.google.gson.annotations.SerializedName
import java.util.Date

/**
 * Data Transfer Object (DTO) representing the user details
 * returned from the backend API.
 *
 * This class maps the JSON response from the backend into
 * a Kotlin object using Gson.
 */
data class UserResponse (

    // The unique identifier of the user, mapped from "_id" in JSON
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
