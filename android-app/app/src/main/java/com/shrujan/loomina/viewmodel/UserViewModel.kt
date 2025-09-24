package com.shrujan.loomina.viewmodel

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

    private val _userState = MutableStateFlow<UserResponse?>(null)
    val userState: StateFlow<UserResponse?> = _userState

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        fetchUser()
    }

    private fun fetchUser() {
        viewModelScope.launch {
            when(val result = userRepository.getCurrentUser()){
                is ApiResult.Success -> _userState.value = result.data
                is ApiResult.Error -> _error.value =  result.message
            }
        }
    }
}