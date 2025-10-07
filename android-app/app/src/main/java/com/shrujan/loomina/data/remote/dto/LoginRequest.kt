package com.shrujan.loomina.data.remote.dto

/**
 * Request body for login API.
 *
 * @param email user’s email address
 * @param password user’s password
 */
data class LoginRequest(
    val email: String,
    val password: String
)
