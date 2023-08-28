package com.example.reflvy.data

import com.example.reflvy.VideoActivity

data class YoutubeVideo(
    val videoID: Int,
    val url: String,
    val playlist: Int
){
    companion object {
        val videoList = mutableListOf<YoutubeVideo>()
    }
}