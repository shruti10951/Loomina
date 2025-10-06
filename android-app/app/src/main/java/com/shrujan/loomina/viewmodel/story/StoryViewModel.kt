package com.shrujan.loomina.viewmodel.story

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shrujan.loomina.data.remote.dto.StoryResponse
import com.shrujan.loomina.data.repository.StoryRepository
import com.shrujan.loomina.utils.ApiResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StoryViewModel (
    private val storyRepository: StoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(StoryUiState())
    val uiState: StateFlow<StoryUiState> = _uiState

    fun getMyStories() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                loading = true,
                error = null
            )

            when (val result = storyRepository.getMyStories()) {
                is ApiResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        myStories = result.data,
                        error = null
                    )
                }
                is ApiResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        error = result.message,
                    )
                }
            }
        }
    }



}