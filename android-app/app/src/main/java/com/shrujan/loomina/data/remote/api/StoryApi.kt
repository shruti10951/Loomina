package com.shrujan.loomina.data.remote.api

import com.shrujan.loomina.data.remote.dto.StoryRequest
import com.shrujan.loomina.data.remote.dto.StoryResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface StoryApi {
    @POST("api/v1/stories/create")
    suspend fun createStory(@Body body: StoryRequest): StoryResponse

    @GET("api/v1/stories/me")
    suspend fun getMyStories(): List<StoryResponse>
}