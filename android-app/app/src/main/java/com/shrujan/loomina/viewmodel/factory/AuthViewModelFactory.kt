package com.shrujan.loomina.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shrujan.loomina.data.local.UserPreferences
import com.shrujan.loomina.data.repository.AuthRepository
import com.shrujan.loomina.viewmodel.auth.AuthViewModel

/**
 * Factory class for creating instances of AuthViewModel with
 * required dependencies (AuthRepository and UserPreferences).
 *
 * This is necessary because ViewModels by default can only be
 * created with a no-argument constructor. When dependencies
 * are needed, we use a custom ViewModelProvider.Factory.
 */
class AuthViewModelFactory(
    private val repository: AuthRepository,
    private val preferences: UserPreferences
) : ViewModelProvider.Factory {

    /**
     * Creates an instance of AuthViewModel if the requested
     * model class matches, otherwise throws an exception.
     *
     * @param modelClass the ViewModel class to create
     * @return instance of AuthViewModel
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(repository, preferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}