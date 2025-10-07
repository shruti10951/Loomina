package com.shrujan.loomina.viewmodel.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shrujan.loomina.data.remote.dto.UserResponse
import com.shrujan.loomina.data.repository.UserRepository
import com.shrujan.loomina.utils.ApiResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel (
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    fun loadUser() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true)
            when (val result = userRepository.getCurrentUser()) {
                is ApiResult.Success -> {
                    _uiState.value = HomeUiState(user = result.data)
                }
                is ApiResult.Error -> {
                    _uiState.value = HomeUiState(error = result.message)
                }
            }
        }
    }


}