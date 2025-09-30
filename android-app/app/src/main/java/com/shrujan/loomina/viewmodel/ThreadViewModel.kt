package com.shrujan.loomina.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shrujan.loomina.data.remote.dto.SparkResponse
import com.shrujan.loomina.data.remote.dto.ThreadResponse
import com.shrujan.loomina.data.repository.ThreadRepository
import com.shrujan.loomina.utils.ApiResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ThreadViewModel(
    private val repository: ThreadRepository
) : ViewModel() {

    private val _myThreads = MutableStateFlow<List<ThreadResponse>>(emptyList())
    val myThreads: StateFlow<List<ThreadResponse>> = _myThreads

    private val _thread = MutableStateFlow<ThreadResponse?>(null)
    val thread: StateFlow<ThreadResponse?> = _thread

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    // sparks state
    private val _sparks = MutableStateFlow<List<SparkResponse>>(emptyList())
    val sparks: StateFlow<List<SparkResponse>> = _sparks

    fun getMyThreads() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            when (val result = repository.getMyThreads()) {
                is ApiResult.Success -> _myThreads.value = result.data
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

    fun fetchOrderedSparks(threadId: String) {
        viewModelScope.launch {
            when (val result = repository.fetchOrderedSparks(threadId)) {
                is ApiResult.Success -> _sparks.value = result.data
                is ApiResult.Error -> _error.value = result.message
            }
        }
    }
}
