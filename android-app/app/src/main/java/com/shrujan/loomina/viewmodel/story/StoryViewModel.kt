package com.shrujan.loomina.viewmodel.story

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shrujan.loomina.data.remote.dto.StoryResponse
import com.shrujan.loomina.data.repository.StoryRepository
import com.shrujan.loomina.utils.ApiResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StoryViewModel (
    private val storyRepository: StoryRepository
) : ViewModel() {

    private val _stories = MutableStateFlow<List<StoryResponse>>(emptyList())
    val stories: StateFlow<List<StoryResponse>> = _stories

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun getMyStories() {
        viewModelScope.launch {
            when (val result = storyRepository.getMyStories()) {
                is ApiResult.Success -> _stories.value = result.data
                is ApiResult.Error -> _error.value = result.message
            }
        }
    }



}