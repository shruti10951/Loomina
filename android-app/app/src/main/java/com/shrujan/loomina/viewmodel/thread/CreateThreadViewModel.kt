package com.shrujan.loomina.viewmodel.thread

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shrujan.loomina.data.remote.dto.ThreadRequest
import com.shrujan.loomina.data.remote.dto.ThreadResponse
import com.shrujan.loomina.data.repository.ThreadRepository
import com.shrujan.loomina.utils.ApiResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class CreateThreadUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val thread: ThreadResponse? = null
)

class CreateThreadViewModel(
    private val repo: ThreadRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateThreadUiState())
    val uiState: StateFlow<CreateThreadUiState> = _uiState

    private var inFlightCreate: Job? = null

    fun createThread(request: ThreadRequest) {
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
            when (val result = repo.createThread(
                sanitizedRequest.threadTitle,
                sanitizedRequest.prompt,
                sanitizedRequest.coverImage,
                sanitizedRequest.genre,
                sanitizedRequest.tags
            )) {
                is ApiResult.Success -> {
                    _uiState.value = CreateThreadUiState(
                        loading = false,
                        thread = result.data
                    )
                }
                is ApiResult.Error -> {
                    _uiState.value = CreateThreadUiState(
                        loading = false,
                        error = result.message
                    )
                }
            }
        }
    }

    fun resetState() {
        _uiState.value = CreateThreadUiState()
    }
}
