package com.shrujan.loomina.data.remote.api

import com.shrujan.loomina.data.remote.dto.LoginRequest
import com.shrujan.loomina.data.remote.dto.RegisterRequest
import com.shrujan.loomina.data.remote.dto.TokenResponse
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * AuthApi defines authentication-related API endpoints.
 * Retrofit will generate the implementation at runtime.
 *
 * Endpoints:
 * - Login
 * - Register
 */
interface AuthApi {

    /**
     * Sends a login request to the backend.
     *
     * Endpoint: POST /api/v1/auth/login
     * @param body request body containing email and password
     * @return TokenResponse containing access token if login is successful
     */
    @POST("api/v1/auth/login")
    suspend fun login(@Body body: LoginRequest): TokenResponse

    /**
     * Sends a registration request to the backend.
     *
     * Endpoint: POST /api/v1/users/register
     * @param body request body containing email, username, and password
     * @return TokenResponse containing access token if registration is successful
     */
    @POST("api/v1/users/register")
    suspend fun register(@Body body: RegisterRequest): TokenResponse
}
