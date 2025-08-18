package com.shrujan.loomina.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shrujan.loomina.data.repository.UserRepository

/**
 * Factory class responsible for creating instances of HomeViewModel.
 * Since HomeViewModel requires a UserRepository dependency,
 * this factory ensures it is properly injected during creation.
 */
class HomeViewModelFactory(
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {

    /**
     * Creates a new instance of the given ViewModel class.
     *
     * @param modelClass The class of the ViewModel requested.
     * @return An instance of HomeViewModel if the requested class matches.
     * @throws IllegalArgumentException if the class type is unknown.
     */
    @Suppress("UNCHECKED_CAST") // Suppresses unchecked cast warning since we validate the class type.
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Check if the requested ViewModel is HomeViewModel
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            // Return a new instance of HomeViewModel with UserRepository injected
            return HomeViewModel(userRepository) as T
        }
        // If the requested ViewModel type does not match, throw an error
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
