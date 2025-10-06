package com.shrujan.loomina.viewmodel.social

import com.shrujan.loomina.data.remote.dto.UserResponse

data class UserUiState(
    val loading: Boolean = false,
    val user: UserResponse? = null,
    val error: String? = null
)