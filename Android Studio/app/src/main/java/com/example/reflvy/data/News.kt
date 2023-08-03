package com.example.reflvy.data

import android.os.Parcel
import android.os.Parcelable

data class News(
    val newsID: Int,
    val description: String = "",
    val img: String = "",
    val title: String = "",
    val paragraphs: List<String> = emptyList(),
    val date: String = ""
){
    companion object {
        val newsList = mutableListOf<News>()
    }
}