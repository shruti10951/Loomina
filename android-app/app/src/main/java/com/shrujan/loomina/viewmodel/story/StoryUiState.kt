package com.shrujan.loomina.viewmodel.story

import com.shrujan.loomina.data.remote.dto.StoryResponse

data class StoryUiState (
    val loading: Boolean = false,
    val myStories: List<StoryResponse> = emptyList(),
    val currentStory: StoryResponse? = null,
    val error: String? = null
)