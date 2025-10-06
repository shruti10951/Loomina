package com.shrujan.loomina.viewmodel.story

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shrujan.loomina.data.remote.dto.StoryRequest
import com.shrujan.loomina.data.remote.dto.StoryResponse
import com.shrujan.loomina.data.repository.StoryRepository
import com.shrujan.loomina.utils.ApiResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


data class CreateStoryUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val story: StoryResponse? = null
)

class CreateStoryViewModel (
    private val repository: StoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateStoryUiState())
    val uiState: StateFlow<CreateStoryUiState> = _uiState

    private var inFlightCreate: Job? = null

    fun createStory(
        storyTitle: String,
        storySynopsis: String,
        coverImage: String?,
        genre: List<String>,
        tags: List<String>
    ) {
        if (_uiState.value.loading) return

        val request = StoryRequest(storyTitle, storySynopsis, coverImage, genre, tags).sanitized()

        if(!request.isValid()){
            _uiState.value = CreateStoryUiState(error = "Please fill all required fields correctly.")
            return
        }

        inFlightCreate?.cancel()

        _uiState.value = CreateStoryUiState(loading = true)

        inFlightCreate = viewModelScope.launch {
            when (val result = repository.createStory(
                request.storyTitle,
                request.storySynopsis,
                request.coverImage,
                request.genre,
                request.tags
            )) {
                is ApiResult.Success -> {
                    _uiState.value = CreateStoryUiState(
                        loading = false,
                        story = result.data
                    )
                }
                is ApiResult.Error -> {
                    _uiState.value = CreateStoryUiState(
                        loading = false,
                        error = result.message
                    )
                }
            }
        }
    }

    fun resetState(){
        _uiState.value = CreateStoryUiState()
    }

}