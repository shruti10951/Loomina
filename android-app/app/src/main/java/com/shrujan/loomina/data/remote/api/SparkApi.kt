package com.shrujan.loomina.data.remote.api

import com.shrujan.loomina.data.remote.dto.SparkRequest
import com.shrujan.loomina.data.remote.dto.SparkResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface SparkApi {

    @POST("/api/v1/threads/{threadId}/sparks/create")
    suspend fun createSpark(
        @Path("threadId") threadId: String,
        @Body body: SparkRequest
    ): SparkResponse



}


