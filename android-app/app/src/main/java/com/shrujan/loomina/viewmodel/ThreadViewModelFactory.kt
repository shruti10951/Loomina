package com.shrujan.loomina.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shrujan.loomina.data.repository.ThreadRepository

class ThreadViewModelFactory(
    private val repository: ThreadRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST") // Suppresses unchecked cast warning since we validate the class type.
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(CreateThreadViewModel::class.java) ->
                CreateThreadViewModel(repository) as T
            modelClass.isAssignableFrom(ShowThreadsViewModel::class.java) ->
                ShowThreadsViewModel(repository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
