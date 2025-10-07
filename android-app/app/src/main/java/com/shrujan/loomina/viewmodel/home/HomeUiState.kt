package com.shrujan.loomina.viewmodel.home

import com.shrujan.loomina.data.remote.dto.UserResponse

data class HomeUiState(
    val loading: Boolean = false,
    val user: UserResponse? = null,
    val error: String? = null
)
