package com.shrujan.loomina.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shrujan.loomina.data.repository.SparkRepository
import com.shrujan.loomina.data.repository.ThreadRepository
import com.shrujan.loomina.viewmodel.CreateThreadViewModel
import com.shrujan.loomina.viewmodel.ThreadViewModel

class ThreadViewModelFactory(
    private val threadRepository: ThreadRepository,
    private val sparkRepository: SparkRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(CreateThreadViewModel::class.java) ->
                CreateThreadViewModel(threadRepository) as T
            modelClass.isAssignableFrom(ThreadViewModel::class.java) ->
                ThreadViewModel(threadRepository, sparkRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
