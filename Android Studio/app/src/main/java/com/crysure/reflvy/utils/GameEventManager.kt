package com.crysure.reflvy.utils

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import com.crysure.reflvy.data.ActiveLogin
import com.crysure.reflvy.data.DataEventGame
import com.crysure.reflvy.data.DataHistoryActivity
import com.crysure.reflvy.data.User
import com.google.common.reflect.TypeToken
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson

class GameEventManager : Application() {
    private val db = Firebase.firestore

    companion object {
        val instance: GameEventManager by lazy { GameEventManager() }
    }

    override fun onCreate() {
        super.onCreate()
    }

    fun CekEventKegiatanNegatif(context: Context){
        var totalKegiatanBermainGame = 0

        for (data in DataHistoryActivity.dataHistoryActivity) {
            totalKegiatanBermainGame += data.kegiatan9BermainGame
        }

        if (ActiveLogin.infoActive.totalActive >= 5){
            val spendTime = (totalKegiatanBermainGame + ActiveLogin.infoActive.kegiatan9BermainGame) / ActiveLogin.infoActive.totalActive

            Toast.makeText(context, spendTime.toString(), Toast.LENGTH_SHORT).show()

            if (ActiveLogin.infoActive.kegiatan9BermainGame > 180 && spendTime >= 300){
                if (!DataEventGame.isNegatifEvent[0].isActive){
                    if (!DataEventGame.isNegatifEvent[0].hasSucces){
                        DataEventGame.isNegatifEvent[0].isActive = true
                    }
                }
            }
        }

        Toast.makeText(context, DataEventGame.isNegatifEvent[0].isActive.toString(), Toast.LENGTH_SHORT).show()


        if (User.userData.nilaiScreening2 >= 50){
            if (!DataEventGame.isNegatifEvent[1].isActive){
                if (!DataEventGame.isNegatifEvent[1].hasSucces){
                    DataEventGame.isNegatifEvent[1].isActive = true
                }
            }
        }

        SaveEventStatus(context)
    }


    fun SaveEventStatus(context: Context){
        ClearEventStatus(context)
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("EventStatus", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val gson = Gson()
        val dataEfekPotitif = gson.toJson(DataEventGame.isPositfEvent)
        val dataEfekNegatif = gson.toJson(DataEventGame.isNegatifEvent)
        editor.putString("data_efekstatuspositif", dataEfekPotitif)
        editor.putString("data_efekstatusnegatif", dataEfekNegatif)

        editor.apply()
    }

    fun LoadEventStatus(context: Context) {
        val sharedPreferences = context.getSharedPreferences("EventStatus", Context.MODE_PRIVATE)
        val gson = Gson()
        val dataStatusPositif = sharedPreferences.getString("data_efekstatuspositif", null)
        val dataStatusNegatif = sharedPreferences.getString("data_efekstatusnegatif", null)

        if (dataStatusPositif != null) {
            val type = object : TypeToken<List<DataEventGame>>() {}.type
            val dataEfek: List<DataEventGame> = gson.fromJson(dataStatusPositif, type)
            DataEventGame.isPositfEvent.clear()
            DataEventGame.isPositfEvent.addAll(dataEfek)
        }

        if (dataStatusNegatif != null) {
            val type = object : TypeToken<List<DataEventGame>>() {}.type
            val dataEfek: List<DataEventGame> = gson.fromJson(dataStatusNegatif, type)
            DataEventGame.isNegatifEvent.clear()
            DataEventGame.isNegatifEvent.addAll(dataEfek)
        }
    }

    fun ClearEventStatus(context: Context){
        val sharedPreferences = context.getSharedPreferences("EventStatus", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }


}