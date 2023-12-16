package com.crysure.reflvy.data

data class YoutubeVideo(
    val videoID: Int,
    val url: String,
    val playlist: Int
){
    companion object {
        val videoList = mutableListOf<YoutubeVideo>()
    }
}