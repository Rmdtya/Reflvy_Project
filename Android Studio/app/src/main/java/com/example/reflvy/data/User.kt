package com.example.reflvy.data

import android.content.SharedPreferences
import com.google.common.reflect.TypeToken
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson

data class User(
    var userID: String = "",
    var email: String = "",
    var userName: String = "",
    var gender: String = "",
    var tanggalLahir: String = "",
    var telepon: String = "",
    var screeningHistory: List<String>? = emptyList(),
    var vpnUsing: List<String> = emptyList()

){
    private val db = Firebase.firestore
    companion object {
        val userData = User()
    }


    fun loadFromSharedPreferences(sharedPreferences: SharedPreferences) {
        userID = sharedPreferences.getString("userId", "") ?: ""
        email = sharedPreferences.getString("userEmail", "") ?: ""
        userName = sharedPreferences.getString("userName", "") ?: ""
        gender = sharedPreferences.getString("userGender", "") ?: ""
        tanggalLahir = sharedPreferences.getString("tanggalLahir", "") ?: ""
        telepon = sharedPreferences.getString("telepon", "") ?: ""
        // Load other properties from SharedPreferences if needed

        val screeningJson = sharedPreferences.getString("userScreening", "")

        val gson = Gson()
        screeningHistory = gson.fromJson(screeningJson, object : TypeToken<ArrayList<String>>() {}.type)
    }
}