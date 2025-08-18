package com.shrujan.loomina.data.repository

import com.shrujan.loomina.data.remote.api.RetrofitProvider
import com.shrujan.loomina.data.remote.dto.UserResponse
import com.shrujan.loomina.utils.ApiResult
import okio.IOException
import retrofit2.HttpException

class UserRepository (
    private val apiProvider: RetrofitProvider = RetrofitProvider
) {

    suspend fun getCurrentUser(token: String): ApiResult<UserResponse> {
        return try{
            val res = apiProvider.userApi.getCurrentUser("Bearer $token")
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