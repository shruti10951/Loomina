package com.shrujan.loomina.data.remote.dto

data class ThreadResponse (
    val id: String,
    val threadTitle: String,
    val prompt: String,
    val coverImage: String?,
    val genre: List<String>,
    val tags: List<String>,
    val createdAt: String,
    val createdBy: String
)