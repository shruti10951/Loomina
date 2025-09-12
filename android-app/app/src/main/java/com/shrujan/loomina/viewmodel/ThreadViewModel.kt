package com.shrujan.loomina.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shrujan.loomina.data.remote.dto.ThreadResponse
import com.shrujan.loomina.data.repository.ThreadRepository
import com.shrujan.loomina.utils.ApiResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * UI state holder for creating a thread.
 */
data class ThreadUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val thread: ThreadResponse? = null
)

/**
 * ThreadViewModel bridges UI (Compose) with ThreadRepository.
 * - Manages createThread logic
 * - Exposes UI state (ThreadUiState)
 */
class ThreadViewModel(
    private val repo: ThreadRepository
) : ViewModel() {

    // UI state for thread creation
    var uiState: MutableState<ThreadUiState> = mutableStateOf(ThreadUiState())
        private set

    // Track running thread creation job
    private var inFlightCreate: Job? = null

    /**
     * Handles thread creation process:
     * - Cancels any existing in-flight request
     * - Updates state to loading
     * - Calls repository
     * - Updates UI state with result (success or error)
     */
    fun createThread(
        threadTitle: String,
        prompt: String,
        coverImage: String?,
        genre: List<String>,
        tags: List<String>
    ) {
        if (uiState.value.loading) return // prevent spamming

        inFlightCreate?.cancel()
        uiState.value = ThreadUiState(loading = true)

        inFlightCreate = viewModelScope.launch {
            when (val result = repo.createThread(threadTitle, prompt, coverImage, genre, tags)) {
                is ApiResult.Success -> {
                    uiState.value = ThreadUiState(
                        loading = false,
                        thread = result.data,
                        error = null
                    )
                }
                is ApiResult.Error -> {
                    uiState.value = ThreadUiState(
                        loading = false,
                        error = result.message
                    )
                }
            }
        }
    }
}
