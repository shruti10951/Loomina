package com.shrujan.loomina.data.remote.dto

data class SparkRequest (
    val sparkText: String,
    val previousSparkId: String? = null,
    val isStart: Boolean = false,
    val isSensitive: Boolean = false,
    val threadId: String
)