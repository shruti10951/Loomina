package com.shrujan.loomina.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shrujan.loomina.data.repository.ThreadRepository
import com.shrujan.loomina.utils.ApiResult
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import com.shrujan.loomina.data.remote.dto.ThreadResponse

data class ShowThreadsUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val threads: List<ThreadResponse> = emptyList()
)

class ShowThreadsViewModel(
    private val repo: ThreadRepository
) : ViewModel() {

    private val _uiState = mutableStateOf(ShowThreadsUiState())
    val uiState: State<ShowThreadsUiState> = _uiState

    fun loadUserThreads() {
        _uiState.value = ShowThreadsUiState(loading = true)
        viewModelScope.launch {
            when (val result = repo.getMyThreads()) {
                is ApiResult.Success -> {
                    _uiState.value = ShowThreadsUiState(threads = result.data)
                }
                is ApiResult.Error -> {
                    _uiState.value = ShowThreadsUiState(error = result.message)
                }
            }
        }
    }
}
