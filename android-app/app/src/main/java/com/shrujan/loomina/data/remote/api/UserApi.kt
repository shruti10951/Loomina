package com.shrujan.loomina.data.remote.api

import com.shrujan.loomina.data.remote.dto.UserResponse
import retrofit2.http.GET
import retrofit2.http.Header

// Retrofit API interface for User-related requests
interface UserApi {

    @GET("/api/v1/users/me")
    suspend fun getCurrentUser(): UserResponse

}
