package com.shrujan.loomina.data.remote.dto

data class RegisterRequest(
    val email: String,
    val username: String,
    val password: String
)