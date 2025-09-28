package com.shrujan.loomina.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shrujan.loomina.data.remote.dto.SparkRequest
import com.shrujan.loomina.data.remote.dto.SparkResponse
import com.shrujan.loomina.data.repository.SparkRepository
import com.shrujan.loomina.utils.ApiResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class CreateSparkUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val spark: SparkResponse? = null
)

class CreateSparkViewModel(
    private val repository: SparkRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateSparkUiState())
    val uiState: StateFlow<CreateSparkUiState> = _uiState

    private var inFlightCreate: Job? = null

    fun createSpark(
        threadId: String,
        sparkText: String,
        previousSparkId: String? = null,
        isSensitive: Boolean = false
    ) {
        if (_uiState.value.loading) return

        val isStart = previousSparkId == null

        val request = SparkRequest(
            sparkText,
            previousSparkId,
            isStart,
            isSensitive
        )

        // Validation
        if (sparkText.isBlank()) {
            _uiState.value = CreateSparkUiState(error = "Please provide spark text to post.")
            return
        }

        inFlightCreate?.cancel()
        _uiState.value = CreateSparkUiState(loading = true)

        inFlightCreate = viewModelScope.launch {
            when (val result = repository.createSpark(
                threadId,
                request.sparkText,
                request.previousSparkId,
                request.isStart,
                request.isSensitive
            )) {
                is ApiResult.Success -> {
                    _uiState.value = CreateSparkUiState(
                        loading = false,
                        spark = result.data
                    )
                }

                is ApiResult.Error -> {
                    _uiState.value = CreateSparkUiState(
                        loading = false,
                        error = result.message
                    )
                }
            }
        }
    }

    fun resetState() {
        _uiState.value = CreateSparkUiState()
    }
}
