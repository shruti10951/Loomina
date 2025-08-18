package com.shrujan.loomina.data.repository

import com.shrujan.loomina.data.remote.api.RetrofitProvider
import com.shrujan.loomina.data.remote.dto.UserResponse
import com.shrujan.loomina.utils.ApiResult
import okio.IOException
import retrofit2.HttpException

/**
 * Repository class responsible for managing user-related API operations.
 * Acts as a middle layer between the ViewModel and the API.
 *
 * @property apiProvider Provides Retrofit API instances for network calls.
 */
class UserRepository (
    private val apiProvider: RetrofitProvider = RetrofitProvider
) {

    /**
     * Fetches the current logged-in user's profile from the server.
     *
     * @param token Authentication token (JWT) provided after login.
     * @return ApiResult<UserResponse> - Success if user details are retrieved,
     *         or Error if there’s a network/HTTP/unexpected issue.
     */
    suspend fun getCurrentUser(token: String): ApiResult<UserResponse> {
        return try {
            // Makes an API call to fetch current user using Bearer token for authorization
            val res = apiProvider.userApi.getCurrentUser("Bearer $token")
            ApiResult.Success(res) // Wrap successful response

        } catch (e: HttpException) {
            // Handles HTTP errors (like 401 Unauthorized, 404 Not Found, etc.)
            ApiResult.Error(e.response()?.errorBody()?.string() ?: "HTTP ${e.code()}")

        } catch (e: IOException) {
            // Handles network failures (e.g., no internet connection)
            ApiResult.Error("Network error. Check your connection.")

        } catch (e: Exception) {
            // Handles any unexpected exceptions
            ApiResult.Error("Unexpected error: ${e.message ?: "unknown"}")
        }
    }

}
