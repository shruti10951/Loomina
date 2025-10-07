package com.shrujan.loomina.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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



class AuthViewModel(
    private val repo: AuthRepository,
    private val userPrefs: UserPreferences
) : ViewModel() {

    // Flow that exposes the saved token from DataStore
    val savedToken: Flow<String?> = userPrefs.token

    // New: StateFlow that wraps savedToken into AuthState
    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState

    // UI state for login and register
    var uiState: MutableState<AuthUiState> = mutableStateOf(AuthUiState())
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

    fun login(email: String, password: String) {
        if (uiState.value.loading) return

        inFlightLogin?.cancel()
        uiState.value = AuthUiState(loading = true)

        inFlightLogin = viewModelScope.launch {
            when (val result = repo.login(email, password)) {
                is ApiResult.Success -> {
                    uiState.value = AuthUiState(
                        loading = false,
                        token = result.data,
                        error = null
                    )
                    result.data.access_token.let { token ->
                        userPrefs.saveToken(token)
                    }
                }
                is ApiResult.Error -> {
                    uiState.value = AuthUiState(
                        loading = false,
                        error = result.message
                    )
                }
            }
        }
    }

    fun register(email: String, username: String, password: String) {
        if (uiState.value.loading) return

        inFlightRegister?.cancel()
        uiState.value = AuthUiState(loading = true)

        inFlightRegister = viewModelScope.launch {
            when (val result = repo.register(email, username, password)) {
                is ApiResult.Success -> {
                    uiState.value = AuthUiState(
                        loading = false,
                        token = result.data,
                        error = null
                    )
                    result.data.access_token.let { token ->
                        userPrefs.saveToken(token)
                    }
                }
                is ApiResult.Error -> {
                    uiState.value = AuthUiState(
                        loading = false,
                        error = result.message
                    )
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            userPrefs.clearToken()
        }
    }
}
