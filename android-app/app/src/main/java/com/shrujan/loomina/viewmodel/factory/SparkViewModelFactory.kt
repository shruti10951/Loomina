package com.shrujan.loomina.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shrujan.loomina.data.repository.SparkRepository
import com.shrujan.loomina.viewmodel.SparkViewModel


class SparkViewModelFactory(
    private val repository: SparkRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST") // Suppresses unchecked cast warning since we validate the class type.
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SparkViewModel::class.java) ->
                SparkViewModel(repository) as T

            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}