package com.shrujan.loomina.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shrujan.loomina.data.remote.dto.ThreadResponse
import com.shrujan.loomina.data.repository.ThreadRepository
import com.shrujan.loomina.utils.ApiResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ThreadViewModel (
    private val threadRepository: ThreadRepository
) : ViewModel() {
    private val _threads = MutableStateFlow<List<ThreadResponse>>(emptyList())
    val threads: StateFlow<List<ThreadResponse>> = _threads

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun getMyThreads() {
        viewModelScope.launch {
            when (val result = threadRepository.getMyThreads()) {
                is ApiResult.Success -> _threads.value = result.data
                is ApiResult.Error -> _error.value = result.message
            }
        }
    }



}