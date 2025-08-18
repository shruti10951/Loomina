package com.shrujan.loomina.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shrujan.loomina.data.remote.dto.UserResponse
import com.shrujan.loomina.data.repository.UserRepository
import com.shrujan.loomina.utils.ApiResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel (private val userRepository: UserRepository) : ViewModel() {

    private val _user = MutableStateFlow<UserResponse?>(null)
    val user: StateFlow<UserResponse?> = _user

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadUser(token: String){
        viewModelScope.launch {
            when (val result = userRepository.getCurrentUser(token)) {
                is ApiResult.Success -> {
                    _user.value = result.data
                }
                is ApiResult.Error -> {
                    _error.value = result.message
                }
            }
        }
    }


}