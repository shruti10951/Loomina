package com.shrujan.loomina.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shrujan.loomina.data.remote.dto.TokenResponse
import com.shrujan.loomina.data.repository.AuthRepository
import com.shrujan.loomina.utils.ApiResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.MutableState
import com.shrujan.loomina.data.local.UserPreferences
import kotlinx.coroutines.flow.Flow

/**
 * UI state holder for login screen.
 * Tracks loading state, errors, and token on success.
 */
data class LoginUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val token: TokenResponse? = null
)

/**
 * UI state holder for register screen.
 * Similar to LoginUiState but separated for clarity.
 */
data class RegisterUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val token: TokenResponse? = null
)

/**
 * AuthViewModel acts as the bridge between UI (Compose) and data layer (Repository + Preferences).
 * - Manages login and registration logic
 * - Exposes UI state (LoginUiState, RegisterUiState)
 * - Persists tokens via UserPreferences
 */
class AuthViewModel(
    private val repo: AuthRepository,
    private val userPrefs: UserPreferences
) : ViewModel() {

    // Flow that exposes the saved token from DataStore
    val savedToken: Flow<String?> = userPrefs.token

    // UI state for login
    var uiState: MutableState<LoginUiState> = mutableStateOf(LoginUiState())
        private set

    // UI state for register
    var registerUiState: MutableState<RegisterUiState> = mutableStateOf(RegisterUiState())
        private set

    // Track running login/register jobs to cancel old requests if needed
    private var inFlightLogin: Job? = null
    private var inFlightRegister: Job? = null

    /**
     * Handles login process:
     * - Cancels any existing login job
     * - Updates state to loading
     * - Calls repository
     * - Updates UI state with result (success or error)
     * - Saves token on success
     */
    fun login(email: String, password: String) {
        if (uiState.value.loading) return  // Prevent duplicate requests

        inFlightLogin?.cancel()
        uiState.value = LoginUiState(loading = true)

        inFlightLogin = viewModelScope.launch {
            when (val result = repo.login(email, password)) {
                is ApiResult.Success -> {
                    uiState.value = LoginUiState(
                        loading = false,
                        token = result.data,
                        error = null
                    )

                    // Save token in DataStore for persistence
                    result.data.access_token.let { token ->
                        userPrefs.saveToken(token)
                    }
                }
                is ApiResult.Error -> {
                    uiState.value = LoginUiState(
                        loading = false,
                        error = result.message
                    )
                }
            }
        }
    }

    /**
     * Handles registration process:
     * - Cancels any existing registration job
     * - Updates state to loading
     * - Calls repository
     * - Updates UI state with result (success or error)
     * - Saves token on success
     */
    fun register(email: String, username: String, password: String) {
        if (registerUiState.value.loading) return  // Prevent duplicate requests

        inFlightRegister?.cancel()
        registerUiState.value = RegisterUiState(loading = true)

        inFlightRegister = viewModelScope.launch {
            when (val result = repo.register(email, username, password)) {
                is ApiResult.Success -> {
                    registerUiState.value = RegisterUiState(
                        loading = false,
                        token = result.data,
                        error = null
                    )

                    // Save token in DataStore for persistence
                    result.data.access_token.let { token ->
                        userPrefs.saveToken(token)
                    }
                }
                is ApiResult.Error -> {
                    registerUiState.value = RegisterUiState(
                        loading = false,
                        error = result.message
                    )
                }
            }
        }
    }

    /**
     * Clears the saved token (logout functionality).
     */
    fun logout() {
        viewModelScope.launch {
            userPrefs.clearToken()
        }
    }
}
