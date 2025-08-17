package com.shrujan.loomina.data.repository

import com.shrujan.loomina.data.remote.api.RetrofitProvider
import com.shrujan.loomina.data.remote.dto.LoginRequest
import com.shrujan.loomina.data.remote.dto.RegisterRequest
import com.shrujan.loomina.data.remote.dto.TokenResponse
import com.shrujan.loomina.utils.ApiResult
import okio.IOException
import retrofit2.HttpException

class AuthRepository (
    private val apiProvider: com.shrujan.loomina.data.remote.api.RetrofitProvider = RetrofitProvider
) {
    suspend fun login(email: String, password: String): ApiResult<TokenResponse>{
        return try {
            val res = apiProvider.authApi.login(LoginRequest(email, password))
            ApiResult.Success(res)
        } catch (e: HttpException) {
            // Server responded with error (401, 409)
            ApiResult.Error(e.response()?.errorBody()?.string() ?: "HTTP ${e.code()}")
        } catch (e: IOException) {
            // Network error / no internet / timeout
            ApiResult.Error("Network error. Check your connection.")
        } catch (e: Exception) {
            ApiResult.Error("Unexpected error: ${e.message ?: "unknown"}")
        }
    }

    suspend fun register(email: String, username: String, password: String): ApiResult<TokenResponse>{
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