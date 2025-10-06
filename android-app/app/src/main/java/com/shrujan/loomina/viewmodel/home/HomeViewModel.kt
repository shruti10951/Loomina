package com.shrujan.loomina.viewmodel.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shrujan.loomina.data.remote.dto.UserResponse
import com.shrujan.loomina.data.repository.UserRepository
import com.shrujan.loomina.utils.ApiResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * HomeViewModel - Handles user data fetching and exposes it to the UI.
 * Uses Kotlin Coroutines + StateFlow for reactive state management.
 */
class HomeViewModel (
    private val userRepository: UserRepository
) : ViewModel() {

    // Backing property for user details, nullable initially
    private val _user = MutableStateFlow<UserResponse?>(null)
    // Publicly exposed immutable StateFlow so UI can observe changes
    val user: StateFlow<UserResponse?> = _user

    // Backing property for error messages, nullable initially
    private val _error = MutableStateFlow<String?>(null)
    // Publicly exposed immutable StateFlow so UI can observe changes
    val error: StateFlow<String?> = _error

    /**
     * Loads the currently logged-in user's details from backend.
     * The result is collected from UserRepository and stored in StateFlow.
     */
    fun loadUser() {
        viewModelScope.launch {
            when (val result = userRepository.getCurrentUser()) {
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