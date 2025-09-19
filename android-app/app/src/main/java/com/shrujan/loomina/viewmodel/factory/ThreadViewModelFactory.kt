package com.shrujan.loomina.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shrujan.loomina.data.repository.ThreadRepository
import com.shrujan.loomina.viewmodel.CreateThreadViewModel
import com.shrujan.loomina.viewmodel.ShowThreadsViewModel

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