package com.example.reflvy.utils

import com.example.reflvy.data.VideoResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface YouTubeApiService {
    @GET("videos")
    suspend fun getVideoDetails(
        @Query("key") apiKey: String,
        @Query("part") part: String,
        @Query("id") videoId: String
    ): Response<VideoResponse>
}