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
    private val repo: AuthRepository = AuthRepository()
) : ViewModel() {

    // --- Login ---
    var uiState: MutableState<LoginUiState> = mutableStateOf(LoginUiState())
        private set

    private var inFlightLogin: Job? = null

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

    fun cleanLoginError() {
        if (uiState.value.error != null) {
            uiState.value = uiState.value.copy(error = null)
        }
    }

    // --- Register ---
    var registerUiState: MutableState<RegisterUiState> = mutableStateOf(RegisterUiState())
        private set

    private var inFlightRegister: Job? = null

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

    fun cleanRegisterError() {
        if (registerUiState.value.error != null) {
            registerUiState.value = registerUiState.value.copy(error = null)
        }
    }
}
