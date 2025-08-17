package com.shrujan.loomina.data.repository

import com.shrujan.loomina.data.remote.api.RetrofitProvider
import com.shrujan.loomina.data.remote.dto.LoginRequest
import com.shrujan.loomina.data.remote.dto.RegisterRequest
import com.shrujan.loomina.data.remote.dto.TokenResponse
import com.shrujan.loomina.utils.ApiResult
import okio.IOException
import retrofit2.HttpException

/**
 * AuthRepository is responsible for handling authentication-related
 * network operations such as login and registration.
 *
 * It uses RetrofitProvider to communicate with the backend API
 * and wraps responses in ApiResult for consistent error handling.
 */
class AuthRepository(
    private val apiProvider: RetrofitProvider = RetrofitProvider
) {

    /**
     * Attempts to log in the user with given credentials.
     *
     * @param email user's email
     * @param password user's password
     * @return ApiResult containing TokenResponse on success, or error message on failure
     */
    suspend fun login(email: String, password: String): ApiResult<TokenResponse> {
        return try {
            // Make login API request with provided credentials
            val res = apiProvider.authApi.login(LoginRequest(email, password))
            ApiResult.Success(res)  // Wrap successful response
        } catch (e: HttpException) {
            // API responded with an error (e.g., 401 Unauthorized, 409 Conflict)
            ApiResult.Error(e.response()?.errorBody()?.string() ?: "HTTP ${e.code()}")
        } catch (e: IOException) {
            // Network-related error (e.g., no internet, timeout)
            ApiResult.Error("Network error. Check your connection.")
        } catch (e: Exception) {
            // Catch-all for unexpected errors
            ApiResult.Error("Unexpected error: ${e.message ?: "unknown"}")
        }
    }

    /**
     * Attempts to register a new user with given details.
     *
     * @param email user's email
     * @param username chosen username
     * @param password chosen password
     * @return ApiResult containing TokenResponse on success, or error message on failure
     */
    suspend fun register(email: String, username: String, password: String): ApiResult<TokenResponse> {
        return try {
            // Make registration API request with provided details
            val res = apiProvider.authApi.register(RegisterRequest(email, username, password))
            ApiResult.Success(res)  // Wrap successful response
        } catch (e: HttpException) {
            ApiResult.Error(e.response()?.errorBody()?.string() ?: "HTTP ${e.code()}")
        } catch (e: IOException) {
            ApiResult.Error("Network error. Check your connection.")
        } catch (e: Exception) {
            ApiResult.Error("Unexpected error: ${e.message ?: "unknown"}")
        }
    }
}
