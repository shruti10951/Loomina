package com.shrujan.loomina.viewmodel.thread

import com.shrujan.loomina.data.remote.dto.SparkResponse
import com.shrujan.loomina.data.remote.dto.ThreadResponse

data class ThreadUiState (
    val loading: Boolean = false,
    val myThreads: List<ThreadResponse> = emptyList(),
    val currentThread: ThreadResponse? = null,
    val sparks: List<SparkResponse> = emptyList(),
    val error: String? = null
)
