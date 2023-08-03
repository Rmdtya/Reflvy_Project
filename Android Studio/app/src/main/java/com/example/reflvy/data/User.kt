package com.example.reflvy.data

import android.content.Context
import android.content.SharedPreferences
import android.os.Parcel
import android.os.Parcelable

data class User(
    var userID: String = "",
    var email: String = "",
    var userName: String = "",
    var vpnUsing: List<String> = emptyList()
){
    companion object {
        val userData = User()
    }

    fun loadFromSharedPreferences(sharedPreferences: SharedPreferences) {
        userID = sharedPreferences.getString("userId", "") ?: ""
        email = sharedPreferences.getString("userEmail", "") ?: ""
        // Load other properties from SharedPreferences if needed
    }
}