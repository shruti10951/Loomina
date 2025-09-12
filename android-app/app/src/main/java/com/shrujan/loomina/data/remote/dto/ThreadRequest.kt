package com.shrujan.loomina.data.remote.dto

data class ThreadRequest(
    val threadTitle: String,
    val prompt: String,
    val coverImage: String? = null,
    val genre: List<String>,
    val tags: List<String>
)
