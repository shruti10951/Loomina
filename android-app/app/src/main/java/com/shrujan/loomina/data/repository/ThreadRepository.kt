package com.shrujan.loomina.data.repository

import android.content.Context
import com.shrujan.loomina.data.remote.api.RetrofitProvider
import com.shrujan.loomina.data.remote.dto.SparkResponse
import com.shrujan.loomina.data.remote.dto.ThreadRequest
import com.shrujan.loomina.data.remote.dto.ThreadResponse
import com.shrujan.loomina.utils.ApiResult
import java.io.IOException
import retrofit2.HttpException


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

    suspend fun getMyThreads(): ApiResult<List<ThreadResponse>> {
        return try {
            val res = apiProvider.threadApi.getMyThreads() // Retrofit call
            ApiResult.Success(res)
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun getThreadById(threadId: String): ApiResult<ThreadResponse> {
        return try {
            val response = apiProvider.threadApi.getThreadById(threadId)
            ApiResult.Success(response)
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun fetchOrderedSparks(threadId: String): ApiResult<List<SparkResponse>> {
        return try {
            val response = apiProvider.threadApi.fetchOrderedSparks(threadId)
            ApiResult.Success(response)
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Unknown error")
        }
    }


}
