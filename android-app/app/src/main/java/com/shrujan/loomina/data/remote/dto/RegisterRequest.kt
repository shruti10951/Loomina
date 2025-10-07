package com.shrujan.loomina.data.remote.dto

/**
 * Request body for registration API.
 *
 * @param email user’s email address
 * @param username chosen username
 * @param password chosen password
 */
data class RegisterRequest(
    val email: String,
    val username: String,
    val password: String
)
