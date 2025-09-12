package com.shrujan.loomina.data.remote.api

import com.shrujan.loomina.data.remote.dto.ThreadRequest
import com.shrujan.loomina.data.remote.dto.ThreadResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ThreadApi {

    @POST("api/v1/threads/create")
    suspend fun createThread(@Body body: ThreadRequest): ThreadResponse
}
