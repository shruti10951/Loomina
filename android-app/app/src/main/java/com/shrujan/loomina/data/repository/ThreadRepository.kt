package com.shrujan.loomina.data.repository

import android.content.Context
import com.shrujan.loomina.data.remote.api.RetrofitProvider
import com.shrujan.loomina.data.remote.dto.ThreadRequest
import com.shrujan.loomina.data.remote.dto.ThreadResponse
import com.shrujan.loomina.utils.ApiResult
import okio.IOException
import retrofit2.HttpException

/**
 * ThreadRepository handles all thread-related network operations,
 * such as creating a new thread.
 *
 * It communicates with the backend API through RetrofitProvider
 * and wraps responses in ApiResult for consistent error handling.
 */
class ThreadRepository(context: Context) {
    private val apiProvider = RetrofitProvider.getInstance(context)

    suspend fun createThread(
        threadTitle: String,
        prompt: String,
        coverImage: String?,
        genre: List<String>,
        tags: List<String>
    ): ApiResult<ThreadResponse> {
        return try {
            val res = apiProvider.threadApi.createThread(
                ThreadRequest(threadTitle, prompt, coverImage, genre, tags)
            )
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
