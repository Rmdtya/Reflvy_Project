package com.crysure.reflvy.data

data class Music(
    val playlistID : Int,
    val title: List<String> = emptyList(),
    val vocalist: List<String> = emptyList(),
    val img: List<String> = emptyList(),
    val cover: List<String> = emptyList(),
    val src: List<String> = emptyList()
){
    companion object {
        val playList = mutableListOf<Music>()
    }
}