package com.shrujan.loomina.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shrujan.loomina.data.remote.dto.ThreadResponse
import com.shrujan.loomina.data.repository.ThreadRepository
import com.shrujan.loomina.utils.ApiResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ThreadViewModel(
    private val repository: ThreadRepository
) : ViewModel() {

    private val _threads = MutableStateFlow<List<ThreadResponse>>(emptyList())
    val threads: StateFlow<List<ThreadResponse>> = _threads

    private val _thread = MutableStateFlow<ThreadResponse?>(null)
    val thread: StateFlow<ThreadResponse?> = _thread

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    fun getMyThreads() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            when (val result = repository.getMyThreads()) {
                is ApiResult.Success -> _threads.value = result.data
                is ApiResult.Error -> _error.value = result.message
            }
            _loading.value = false
        }
    }

    fun getThreadById(threadId: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            when (val result = repository.getThreadById(threadId)) {
                is ApiResult.Success -> _thread.value = result.data
                is ApiResult.Error -> _error.value = result.message
            }
            _loading.value = false
        }
    }
}
