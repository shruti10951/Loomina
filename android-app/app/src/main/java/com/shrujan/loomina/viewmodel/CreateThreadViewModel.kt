package com.shrujan.loomina.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shrujan.loomina.data.remote.dto.ThreadRequest
import com.shrujan.loomina.data.remote.dto.ThreadResponse
import com.shrujan.loomina.data.repository.ThreadRepository
import com.shrujan.loomina.utils.ApiResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


data class CreateThreadUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val thread: ThreadResponse? = null
)


class CreateThreadViewModel(
    private val repo: ThreadRepository
) : ViewModel() {

    // UI state for thread creation
    var uiState: MutableState<CreateThreadUiState> = mutableStateOf(CreateThreadUiState())
        private set

    // Track running thread creation job
    private var inFlightCreate: Job? = null


    fun createThread(
        threadTitle: String,
        prompt: String,
        coverImage: String?,
        genre: List<String>,
        tags: List<String>
    ) {
        if (uiState.value.loading) return

        val request = ThreadRequest(threadTitle, prompt, coverImage, genre, tags).sanitized()
        if (!request.isValid()) {
            uiState.value = CreateThreadUiState(
                loading = false,
                error = "Please fill all required fields correctly."
            )
            return
        }

        inFlightCreate?.cancel()
        uiState.value = CreateThreadUiState(loading = true)

        inFlightCreate = viewModelScope.launch {
            when (val result = repo.createThread(
                request.threadTitle,
                request.prompt,
                request.coverImage,
                request.genre,
                request.tags
            )) {
                is ApiResult.Success -> {
                    uiState.value = CreateThreadUiState(
                        loading = false,
                        thread = result.data,
                        error = null
                    )
                }
                is ApiResult.Error -> {
                    uiState.value = CreateThreadUiState(
                        loading = false,
                        error = result.message
                    )
                }
            }
        }
    }

}
