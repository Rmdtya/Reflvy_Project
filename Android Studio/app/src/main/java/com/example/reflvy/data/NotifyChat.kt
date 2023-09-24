package com.example.reflvy.data

import android.content.Context
import android.content.SharedPreferences
import com.google.common.reflect.TypeToken
import com.google.gson.Gson

data class NotifyChat(
    val chat : String,
    val from: String,
    val iconBot : Boolean,
    val time : Boolean,
    val timeString : String,
    val clickable : Boolean,
    val eventClick : String
) {
    companion object {
        val notifChat = mutableListOf<NotifyChat>()
        var notify : Boolean = false
    }
}
