package com.shrujan.loomina.data.remote.api

import com.shrujan.loomina.data.remote.dto.ThreadRequest
import com.shrujan.loomina.data.remote.dto.ThreadResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ThreadApi {

    @POST("api/v1/threads/create")
    suspend fun createThread(@Body body: ThreadRequest): ThreadResponse

    @GET("api/v1/threads/me")
    suspend fun getMyThreads(): List<ThreadResponse>

    @GET("api/v1/threads/{id}")
    suspend fun getThreadById(@Path("id") threadId: String): ThreadResponse

}
