package com.shrujan.loomina.data.repository

import android.content.Context
import com.shrujan.loomina.data.remote.api.RetrofitProvider
import com.shrujan.loomina.data.remote.dto.LoginRequest
import com.shrujan.loomina.data.remote.dto.RegisterRequest
import com.shrujan.loomina.data.remote.dto.TokenResponse
import com.shrujan.loomina.utils.ApiResult
import okio.IOException
import retrofit2.HttpException

/**
 * AuthRepository handles authentication-related
 * network operations such as login and registration.
 *
 * It uses RetrofitProvider to communicate with the backend API
 * and wraps responses in ApiResult for consistent error handling.
 */
class AuthRepository(
    context: Context
) {
    private val apiProvider = RetrofitProvider.getInstance(context)

    /**
     * Attempts to log in the user with given credentials.
     */
    suspend fun login(email: String, password: String): ApiResult<TokenResponse> {
        return try {
            val res = apiProvider.authApi.login(LoginRequest(email, password))
            ApiResult.Success(res)
        } catch (e: HttpException) {
            ApiResult.Error(e.response()?.errorBody()?.string() ?: "HTTP ${e.code()}")
        } catch (e: IOException) {
            ApiResult.Error("Network error. Check your connection.")
        } catch (e: Exception) {
            ApiResult.Error("Unexpected error: ${e.message ?: "unknown"}")
        }
    }

    /**
     * Attempts to register a new user with given details.
     */
    suspend fun register(email: String, username: String, password: String): ApiResult<TokenResponse> {
        return try {
            val res = apiProvider.authApi.register(RegisterRequest(email, username, password))
            ApiResult.Success(res)
        } catch (e: HttpException) {
            ApiResult.Error(e.response()?.errorBody()?.string() ?: "HTTP ${e.code()}")
        } catch (e: IOException) {
            ApiResult.Error("Network error. Check your connection.")
        } catch (e: Exception) {
            ApiResult.Error("Unexpected error: ${e.message ?: "unknown"}")
        }
    }
}
