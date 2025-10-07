package com.shrujan.loomina.viewmodel.thread

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shrujan.loomina.data.remote.dto.SparkResponse
import com.shrujan.loomina.data.remote.dto.ThreadResponse
import com.shrujan.loomina.data.repository.SparkRepository
import com.shrujan.loomina.data.repository.ThreadRepository
import com.shrujan.loomina.utils.ApiResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ThreadViewModel(
    private val repository: ThreadRepository,
    private val sparkRepository: SparkRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ThreadUiState())
    val uiState: StateFlow<ThreadUiState> = _uiState

    fun getMyThreads() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                loading = true,
                error = null
            )
            when (val result = repository.getMyThreads()) {
                is ApiResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        myThreads = result.data,
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

    fun getThreadById(threadId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                loading = true,
                error = null
            )
            when (val result = repository.getThreadById(threadId)) {
                is ApiResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        currentThread = result.data,
                        error = null
                    )
                }
                is ApiResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        error = result.message
                    )
                }
            }
        }
    }

    fun fetchOrderedSparks(threadId: String) {
        viewModelScope.launch {
            when (val result = repository.fetchOrderedSparks(threadId)) {
                is ApiResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        sparks = result.data,
                        error = null
                    )
                }
                is ApiResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        error = result.message
                    )
                }
            }
        }
    }

    fun toggleSparkLike(sparkId: String) {
        viewModelScope.launch {
            val sparks = _uiState.value.sparks.toMutableList()
            val index = sparks.indexOfFirst { it.id == sparkId }

            if (index == -1 ) return@launch

            val currentSpark = sparks[index]

            when (val result = sparkRepository.toggleLike(sparkId)) {
                is ApiResult.Success -> {
                    val updatedSpark = currentSpark.copy(
                        numberOfLikes = result.data.numberOfLikes,
                        likedByCurrentUser = result.data.likedByCurrentUser
                    )
                    sparks[index] = updatedSpark
                    _uiState.value = _uiState.value.copy(
                        sparks = sparks,
                        error = null
                    )

                }

                is ApiResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        error = result.message
                    )
                }
            }
        }
    }
}