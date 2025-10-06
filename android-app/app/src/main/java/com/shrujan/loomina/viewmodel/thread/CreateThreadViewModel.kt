package com.shrujan.loomina.viewmodel

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

    fun createThread(
        threadTitle: String,
        prompt: String,
        coverImage: String?,
        genre: List<String>,
        tags: List<String>
    ) {
        if (_uiState.value.loading) return

        val request = ThreadRequest(threadTitle, prompt, coverImage, genre, tags)

        // Validation
        if (threadTitle.isBlank() || prompt.isBlank()) {
            _uiState.value = CreateThreadUiState(error = "Please fill all required fields.")
            return
        }

        inFlightCreate?.cancel()
        _uiState.value = CreateThreadUiState(loading = true)

        inFlightCreate = viewModelScope.launch {
            when (val result = repo.createThread(
                request.threadTitle,
                request.prompt,
                request.coverImage,
                request.genre,
                request.tags
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
