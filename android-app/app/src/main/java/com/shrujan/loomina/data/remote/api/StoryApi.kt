package com.shrujan.loomina.data.remote.api

import com.shrujan.loomina.data.remote.dto.StoryRequest
import com.shrujan.loomina.data.remote.dto.StoryResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface StoryApi {
    @POST("api/v1/stories/")
    suspend fun createStory(@Body body: StoryRequest): StoryResponse

    @GET("api/v1/stories/me")
    suspend fun getMyStories(): List<StoryResponse>

    @GET("api/v1/stories/{id}")
    suspend fun getStoryById(@Path("id") storyId: String) : StoryResponse
}