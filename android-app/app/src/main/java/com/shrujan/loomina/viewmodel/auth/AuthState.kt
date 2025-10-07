package com.shrujan.loomina.viewmodel.auth

import com.shrujan.loomina.data.remote.dto.TokenResponse

sealed class AuthState {
    object Loading : AuthState()
    data class Authenticated(val token: String) : AuthState()
    object Unauthenticated : AuthState()
}

data class AuthUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val token: TokenResponse? = null
)
