package com.shrujan.loomina.data.repository

import android.content.Context
import com.shrujan.loomina.data.remote.api.RetrofitProvider
import com.shrujan.loomina.data.remote.dto.StoryRequest
import com.shrujan.loomina.data.remote.dto.StoryResponse
import com.shrujan.loomina.utils.ApiResult
import retrofit2.HttpException
import java.io.IOException


class StoryRepository(context: Context) {
    private val apiProvider = RetrofitProvider.getInstance(context)

    suspend fun createStory(
        request: StoryRequest
    ): ApiResult<StoryResponse> {
        return try {
            val res = apiProvider.storyApi.createStory(request)

            ApiResult.Success(res)
        } catch (e: HttpException) {
            ApiResult.Error(e.response()?.errorBody()?.string() ?: "HTTP ${e.code()}")
        } catch (e: IOException) {
            ApiResult.Error("Network error. Check your connection.")
        } catch (e: Exception) {
            ApiResult.Error("Unexpected error: ${e.message ?: "unknown"}")
        }
    }

    suspend fun getMyStories(): ApiResult<List<StoryResponse>> {
        return try {
            val res = apiProvider.storyApi.getMyStories() // Retrofit call
            ApiResult.Success(res)
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Unknown error")
        }
    }

}
