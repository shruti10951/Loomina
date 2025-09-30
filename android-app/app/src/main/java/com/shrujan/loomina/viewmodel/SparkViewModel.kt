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

data class SparkUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val spark: SparkResponse? = null
)

class SparkViewModel(
    private val repository: SparkRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SparkUiState())
    val uiState: StateFlow<SparkUiState> = _uiState

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
            _uiState.value = SparkUiState(error = "Please provide spark text to post.")
            return
        }

        inFlightCreate?.cancel()
        _uiState.value = SparkUiState(loading = true)

        inFlightCreate = viewModelScope.launch {
            when (val result = repository.createSpark(
                threadId,
                request.sparkText,
                request.previousSparkId,
                request.isStart,
                request.isSensitive
            )) {
                is ApiResult.Success -> {
                    _uiState.value = SparkUiState(
                        loading = false,
                        spark = result.data
                    )
                }

                is ApiResult.Error -> {
                    _uiState.value = SparkUiState(
                        loading = false,
                        error = result.message
                    )
                }
            }
        }
    }

    private val _spark = MutableStateFlow<SparkResponse?>(null)
    val spark: StateFlow<SparkResponse?> =_spark

    private val _error = MutableStateFlow<String?>(null)
    val err0r: StateFlow<String?> = _error


    fun getSparkById(
        sparkId: String
    ) {
        viewModelScope.launch {
            when (val result = repository.getSparkById(sparkId)) {
                is ApiResult.Success -> _spark.value = result.data
                is ApiResult.Error -> _error.value = result.message
            }
        }
    }

    fun resetState() {
        _uiState.value = SparkUiState()
    }
}
