package com.shrujan.loomina.viewmodel.story

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shrujan.loomina.data.remote.dto.StoryRequest
import com.shrujan.loomina.data.remote.dto.StoryResponse
import com.shrujan.loomina.data.repository.StoryRepository
import com.shrujan.loomina.utils.ApiResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


data class CreateStoryUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val story: StoryResponse? = null
)

class CreateStoryViewModel (
    private val repository: StoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateStoryUiState())
    val uiState: StateFlow<CreateStoryUiState> = _uiState

    private var inFlightCreate: Job? = null

    fun createStory(
        request: StoryRequest
    ) {

        // Sanitize the request
        val sanitizedRequest = request.sanitized()

        // Validate
        if (!sanitizedRequest.isValid()) {
            _uiState.value = _uiState.value.copy(error = "Please fill all required fields correctly.")
            return
        }

        // Prevent duplicate in-flight requests
        if (_uiState.value.loading) return

        inFlightCreate?.cancel()
        _uiState.value = _uiState.value.copy(loading = true, error = null)

        inFlightCreate = viewModelScope.launch {
            when (val result = repository.createStory(sanitizedRequest)) {
                is ApiResult.Success -> {
                    _uiState.value = CreateStoryUiState(
                        loading = false,
                        story = result.data
                    )
                }
                is ApiResult.Error -> {
                    _uiState.value = CreateStoryUiState(
                        loading = false,
                        error = result.message
                    )
                }
            }
        }
    }

    fun resetState(){
        _uiState.value = CreateStoryUiState()
    }

}