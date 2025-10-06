package com.shrujan.loomina.viewmodel.social

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shrujan.loomina.data.remote.dto.UserResponse
import com.shrujan.loomina.data.repository.UserRepository
import com.shrujan.loomina.utils.ApiResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserUiState())
    val uiState: StateFlow<UserUiState> = _uiState

    init {
        fetchUser()
    }

    fun fetchUser() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true)
            when (val result = userRepository.getCurrentUser()) {
                is ApiResult.Success -> {
                    _uiState.value = UserUiState(user = result.data)
                }
                is ApiResult.Error -> {
                    _uiState.value = UserUiState(error = result.message)
                }
            }
        }
    }
}