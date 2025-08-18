package com.shrujan.loomina.data.remote.api

import com.shrujan.loomina.data.remote.dto.UserResponse
import retrofit2.http.GET
import retrofit2.http.Header

// Retrofit API interface for User-related requests
interface UserApi {

    /**
     * Fetches the currently logged-in user's details from the backend.
     *
     * @param token Authorization token passed in the HTTP header.
     *        Example: "Bearer <jwt_token>"
     * @return UserResponse containing user information (id, username, email, etc.)
     *
     * This function is marked `suspend` because it makes a network request
     * and should be called from a coroutine or another suspend function.
     */
    @GET("/api/v1/users/me")
    suspend fun getCurrentUser(
        @Header("Authorization") token: String // Adds Authorization header to the request
    ): UserResponse
}
