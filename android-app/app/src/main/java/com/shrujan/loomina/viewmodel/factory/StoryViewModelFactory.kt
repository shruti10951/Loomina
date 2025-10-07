package com.shrujan.loomina.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shrujan.loomina.data.repository.StoryRepository
import com.shrujan.loomina.viewmodel.story.CreateStoryViewModel
import com.shrujan.loomina.viewmodel.story.StoryViewModel

class StoryViewModelFactory(
    private val repository: StoryRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(StoryViewModel::class.java) ->
                StoryViewModel(repository) as T
            modelClass.isAssignableFrom(CreateStoryViewModel::class.java) ->
                CreateStoryViewModel(repository) as T

            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}