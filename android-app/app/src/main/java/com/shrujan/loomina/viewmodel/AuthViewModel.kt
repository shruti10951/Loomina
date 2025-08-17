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

data class LoginUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val token: TokenResponse? = null
)

data class RegisterUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val token: TokenResponse? = null
)

class AuthViewModel(
    private val repo: AuthRepository,
    private val userPrefs: UserPreferences
) : ViewModel() {

    val savedToken: Flow<String?> = userPrefs.token

    var uiState: MutableState<LoginUiState> = mutableStateOf(LoginUiState())
        private set

    var registerUiState: MutableState<RegisterUiState> = mutableStateOf(RegisterUiState())
        private set

    private var inFlightLogin: Job? = null
    private var inFlightRegister: Job? = null

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

                    result.data.access_token?.let { token ->
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

                    result.data.access_token?.let { token ->
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

    fun logout() {
        viewModelScope.launch {
            userPrefs.clearToken()
        }
    }
}
