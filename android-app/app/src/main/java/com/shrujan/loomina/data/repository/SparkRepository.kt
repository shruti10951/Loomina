package com.shrujan.loomina.data.repository

import android.content.Context
import com.shrujan.loomina.data.remote.api.RetrofitProvider
import com.shrujan.loomina.data.remote.dto.SparkLikeResponse
import com.shrujan.loomina.data.remote.dto.SparkRequest
import com.shrujan.loomina.data.remote.dto.SparkResponse
import com.shrujan.loomina.utils.ApiResult
import retrofit2.HttpException
import java.io.IOException

class SparkRepository(context: Context) {
    private val apiProvider = RetrofitProvider.getInstance(context)

    suspend fun createSpark(
        threadId: String,
        sparkText: String,
        previousSparkId: String? = null,
        isStart: Boolean = false,
        isSensitive: Boolean = false
    ): ApiResult<SparkResponse> {
        return try {
            val res = apiProvider.sparkApi.createSpark(
                body = SparkRequest(
                    sparkText = sparkText,
                    previousSparkId = previousSparkId,
                    isStart = isStart,
                    isSensitive = isSensitive,
                    threadId = threadId
                )
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

    suspend fun getSparkById(sparkId: String): ApiResult<SparkResponse> {
        return try {
            val response = apiProvider.sparkApi.getSparkById(sparkId)
            ApiResult.Success(response)
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Unknown error")
        }
    }


    suspend fun toggleLike(sparkId: String): ApiResult<SparkLikeResponse> {
        return try {
            val response = apiProvider.sparkApi.likeSpark(sparkId)
            ApiResult.Success(response)
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Unknown error")
        }
    }

}
