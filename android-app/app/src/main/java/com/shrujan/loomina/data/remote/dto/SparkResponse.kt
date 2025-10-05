package com.shrujan.loomina.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SparkResponse (
    @SerializedName("_id") val id: String,
    val threadId: String,
    val user: MinimalUser,
    val sparkText: String,
    val creationTime: String,

    val numberOfLikes: Int,
    val numberOfComments: Int,
    val likedBy: List<String>,

    val likedByCurrentUser: Boolean,

    val previousSparkId: String? = null,

    val isStart: Boolean,
    val isReported: Boolean,
    val isDeleted: Boolean,
    val isSensitive: Boolean,
    val isEdited: Boolean,

    val reportCount: Int


)