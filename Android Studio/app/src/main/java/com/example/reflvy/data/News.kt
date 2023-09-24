package com.example.reflvy.data

import android.os.Parcel
import android.os.Parcelable

data class News(
    val jenis: List<Int> = emptyList(),
    val description: List<String> = emptyList(),
    val img: List<String> = emptyList(),
    val title: List<String> = emptyList(),
    val paragraphs: List<String> = emptyList(),
    val date: List<String> = emptyList()
){
    companion object {
        val newsList = mutableListOf<News>()
    }
}