package com.shrujan.loomina.data.remote.dto

/**
 * Response body for authentication APIs (login/register).
 *
 * @param access_token JWT access token provided by backend
 * @param token_type type of token (e.g., "Bearer")
 */
data class TokenResponse(
    val access_token: String,
    val token_type: String
)
