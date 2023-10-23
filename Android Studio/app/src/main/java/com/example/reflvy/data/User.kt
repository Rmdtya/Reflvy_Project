package com.example.reflvy.data

import android.content.SharedPreferences
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

data class User(
    var userID: String = "",
    var email: String = "",
    var userName: String = "",
    var gender: String = "",
    var tanggalLahir: String = "",
    var telepon: String = "",
    var userReveral : String = "",
    var vpnUsing: List<String> = emptyList(),
    var screeningSatu : Boolean = false,
    var screeningDua : Boolean = false,
    var screeningTiga : Boolean = false,
    var screeningEmpat : Boolean = false,
    var nilaiScreening1 : Int = 0,
    var nilaiScreening2 : Int = 0,
    var nilaiScreening3 : Int = 0,
    var nilaiScreening4 : Int = 0,
    var reveralCodeAnak : String = "",
    var jumlahReveral : Int = 0,
    var piliRole : Boolean = false,
    var isOrangTua : Boolean = false
){
    private val db = Firebase.firestore
    companion object {
        val userData = User()
    }


    fun loadFromSharedPreferences(sharedPreferences: SharedPreferences) {

        userID = sharedPreferences.getString("userId", "") ?: ""
        email = sharedPreferences.getString("userEmail", "") ?: ""
        userReveral = sharedPreferences.getString("userReveral", "") ?: ""
        reveralCodeAnak = sharedPreferences.getString("reveralAnak", "") ?: ""
        userName = sharedPreferences.getString("userName", "") ?: ""
        gender = sharedPreferences.getString("userGender", "") ?: ""
        tanggalLahir = sharedPreferences.getString("tanggalLahir", "") ?: ""
        telepon = sharedPreferences.getString("telepon", "") ?: ""

        screeningSatu = sharedPreferences.getBoolean("screening1", false)
        screeningDua = sharedPreferences.getBoolean("screening2", false)
        screeningTiga = sharedPreferences.getBoolean("screening3", false)
        screeningEmpat = sharedPreferences.getBoolean("screening4", false)
        // Load other properties from SharedPreferences if needed

        nilaiScreening1 = sharedPreferences.getInt("nilaiScreening1", 0)
        nilaiScreening2 = sharedPreferences.getInt("nilaiScreening2", 0)
        nilaiScreening3 = sharedPreferences.getInt("nilaiScreening3", 0)
        nilaiScreening4 = sharedPreferences.getInt("nilaiScreening4", 0)

        isOrangTua = sharedPreferences.getBoolean("isOrangTua", false)
        piliRole = sharedPreferences.getBoolean("pilihRole", false)
        jumlahReveral = sharedPreferences.getInt("jumlahReveral", 0)
    }
}