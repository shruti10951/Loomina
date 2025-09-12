package com.shrujan.loomina.data.repository

import android.content.Context
import com.shrujan.loomina.data.remote.api.RetrofitProvider
import com.shrujan.loomina.data.remote.dto.UserResponse
import com.shrujan.loomina.utils.ApiResult
import okio.IOException
import retrofit2.HttpException

/**
 * Repository class responsible for managing user-related API operations.
 * Uses RetrofitProvider (with AuthInterceptor) to automatically attach JWT.
 *
 * @property apiProvider Provides Retrofit API instances for network calls.
 */
class UserRepository(
    context: Context
) {
    private val apiProvider = RetrofitProvider.getInstance(context)

    /**
     * Fetches the current logged-in user's profile from the server.
     *
     * @return ApiResult<UserResponse> - Success if user details are retrieved,
     *         or Error if there’s a network/HTTP/unexpected issue.
     */
    suspend fun getCurrentUser(): ApiResult<UserResponse> {
        return try {
            // AuthInterceptor already adds "Authorization: Bearer <token>"
            val res = apiProvider.userApi.getCurrentUser()
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
