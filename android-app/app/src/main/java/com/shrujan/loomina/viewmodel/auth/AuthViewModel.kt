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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Represents authentication state of the user.
 */
sealed class AuthState {
    object Loading : AuthState()
    data class Authenticated(val token: String) : AuthState()
    object Unauthenticated : AuthState()
}

/**
 * UI state holder for login screen.
 */
data class LoginUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val token: TokenResponse? = null
)

/**
 * UI state holder for register screen.
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
 * - Exposes high-level AuthState for navigation decisions (loading / authenticated / unauthenticated)
 */
class AuthViewModel(
    private val repo: AuthRepository,
    private val userPrefs: UserPreferences
) : ViewModel() {

    // Flow that exposes the saved token from DataStore
    val savedToken: Flow<String?> = userPrefs.token

    // New: StateFlow that wraps savedToken into AuthState
    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState

    // UI state for login
    var uiState: MutableState<LoginUiState> = mutableStateOf(LoginUiState())
        private set

    // UI state for register
    var registerUiState: MutableState<RegisterUiState> = mutableStateOf(RegisterUiState())
        private set

    // Track running login/register jobs
    private var inFlightLogin: Job? = null
    private var inFlightRegister: Job? = null

    init {
        // Observe DataStore token and update authState accordingly
        viewModelScope.launch {
            savedToken.collect { token ->
                _authState.value = if (token != null) {
                    AuthState.Authenticated(token)
                } else {
                    AuthState.Unauthenticated
                }
            }
        }
    }

    /**
     * Handles login process:
     */
    fun login(email: String, password: String) {
        if (uiState.value.loading) return

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
     */
    fun register(email: String, username: String, password: String) {
        if (registerUiState.value.loading) return

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
     * Clears the saved token (logout).
     */
    fun logout() {
        viewModelScope.launch {
            userPrefs.clearToken()
        }
    }
}
