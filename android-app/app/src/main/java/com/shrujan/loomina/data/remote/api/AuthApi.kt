package com.shrujan.loomina.data.remote.api

import com.shrujan.loomina.data.remote.dto.LoginRequest
import com.shrujan.loomina.data.remote.dto.RegisterRequest
import com.shrujan.loomina.data.remote.dto.TokenResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    // POST /api/v1/auth/login
    @POST("api/v1/auth/login")
    suspend fun login(@Body body: LoginRequest): TokenResponse

    // POST /api/v1/users/register
    @POST("api/v1/users/register")
    suspend fun register(@Body body: RegisterRequest): TokenResponse


}
