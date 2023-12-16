package com.crysure.reflvy.utils

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.crysure.reflvy.data.DataEventGame
import com.crysure.reflvy.data.DragonConditionData
import com.crysure.reflvy.data.EfekStatusDragon
import com.crysure.reflvy.data.GameStatusManager
import com.google.common.reflect.TypeToken
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson

class GameManager : Application() {
    private val db = Firebase.firestore

    companion object {
        val instance: GameManager by lazy { GameManager() }
    }

    override fun onCreate() {
        super.onCreate()
    }


    fun PlusActivityPoint(jumlah: Float, editor: SharedPreferences.Editor){
        GameStatusManager.dragonStatus.activityPoint += jumlah

        editor.putFloat("activityPoint", GameStatusManager.dragonStatus.coinPoint)
        editor.apply()
    }

    fun PlusArchivmentPoint(jumlah : Int, editor: SharedPreferences.Editor){
        GameStatusManager.dragonStatus.archivmentPoint += jumlah
    }

    fun PlusMinusKoin(jumlah : Float, editor: SharedPreferences.Editor){
        GameStatusManager.dragonStatus.coinPoint += jumlah

        editor.putFloat("coinPoint", GameStatusManager.dragonStatus.coinPoint)
        editor.apply()
    }

    fun PlusStatusPoint(status : EfekStatusDragon, context: Context){
        var dragonStatus = GameStatusManager.dragonStatus

        dragonStatus.statLapar += status.statLapar
        dragonStatus.statPengetahuan += status.statPengetahuan
        dragonStatus.statKesehatanFisik += status.statKesehatanFisik
        dragonStatus.statKesehatanMental += status.statKesehatanMental
        dragonStatus.statCinta += status.statCinta
        dragonStatus.statHiburan += status.statHiburan
        dragonStatus.statIstirahat += status.statIstirahat
        dragonStatus.statSosial += status.statSosial

        GameStatusManager.dragonStatus.SaveDataPoint(context)
    }


    fun SaveDataConditionStatus(context: Context) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("GameStatus", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        // Simpan lastActive dan speedCoin seperti yang telah Anda lakukan
        editor.putString("lastActive", DragonConditionData.dragonConditionData.lastActive)
        editor.putFloat("speedCoin", DragonConditionData.dragonConditionData.speedCoin)
        editor.putFloat("coinOvertime", DragonConditionData.dragonConditionData.coinOvertime)

        editor.apply()
    }

    fun LoadDataConditionStatus(context: Context) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("GameStatus", Context.MODE_PRIVATE)

        // Memuat lastActive dan speedCoin
        val lastActive = sharedPreferences.getString("lastActive", "")
        val speedCoin = sharedPreferences.getFloat("speedCoin", 2.0f)
        val coinOvertime = sharedPreferences.getFloat("coinOvertime", 1.0f)

        // Set data yang dimuat ke dalam objek DragonConditionData
        DragonConditionData.dragonConditionData.lastActive = lastActive.toString()
        DragonConditionData.dragonConditionData.speedCoin = speedCoin
    }

    fun SaveEfekStatus(context: Context){
        ClearEfekStatus(context)
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("EfekStatus", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val gson = Gson()
        val continuosEfek = gson.toJson(EfekStatusDragon.continuosStatus)
        editor.putString("data_efekstatuscontinuos", continuosEfek)

        editor.apply()
    }

    fun LoadEfekStatus(context: Context) {
        val sharedPreferences = context.getSharedPreferences("EfekStatus", Context.MODE_PRIVATE)
        val gson = Gson()
        val dataStatusContinuos = sharedPreferences.getString("data_efekstatuscontinuos", null)

        if (dataStatusContinuos != null) {
            val type = object : TypeToken<List<EfekStatusDragon>>() {}.type
            val dataEfek: List<EfekStatusDragon> = gson.fromJson(dataStatusContinuos, type)
            EfekStatusDragon.continuosStatus.clear()
            EfekStatusDragon.continuosStatus.addAll(dataEfek)
        }
    }

    fun ClearEfekStatus(context: Context){
        val sharedPreferences = context.getSharedPreferences("EfekStatus", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    fun updateEfekNow(context: Context) {
        // Inisialisasi efekNow dengan nilai default

        val efekNow = EfekStatusDragon.efekNow
        // Mengakumulasi nilai coinPoint dan activityPoint dari statusNegatif
        var totalCoinPoint = 0f
        var totalActivityPoint = 0f
        var speedCoin = 0f

        var totalStatLapar = 0f
        var totalStatPengetahuan = 0f
        var totalStatKesehatanFisik = 0f
        var totalStatKesehatanMental = 0f
        var totalStatCinta = 0f
        var totalStatHiburan = 0f
        var totalStatIstirahat = 0f
        var totalStatSosial = 0f

        for (status in DataEventGame.isNegatifEvent) {
            if (status.isActive){
                totalCoinPoint += status.efekEvent.coinPoint
                totalActivityPoint += status.efekEvent.activityPoint
                speedCoin += status.efekEvent.speedCoin
                totalStatLapar += status.efekEvent.statLapar
                totalStatPengetahuan += status.efekEvent.statPengetahuan
                totalStatKesehatanFisik += status.efekEvent.statKesehatanFisik
                totalStatKesehatanMental += status.efekEvent.statKesehatanMental
                totalStatCinta += status.efekEvent.statCinta
                totalStatHiburan += status.efekEvent.statHiburan
                totalStatIstirahat += status.efekEvent.statIstirahat
                totalStatSosial += status.efekEvent.statSosial
            }
        }

        for (status in DataEventGame.isPositfEvent) {
            if(status.isActive){
                totalCoinPoint += status.efekEvent.coinPoint
                totalActivityPoint += status.efekEvent.activityPoint
                speedCoin += status.efekEvent.speedCoin
                totalStatLapar += status.efekEvent.statLapar
                totalStatPengetahuan += status.efekEvent.statPengetahuan
                totalStatKesehatanFisik += status.efekEvent.statKesehatanFisik
                totalStatKesehatanMental += status.efekEvent.statKesehatanMental
                totalStatCinta += status.efekEvent.statCinta
                totalStatHiburan += status.efekEvent.statHiburan
                totalStatIstirahat += status.efekEvent.statIstirahat
                totalStatSosial += status.efekEvent.statSosial
            }
        }

        for (status in EfekStatusDragon.continuosStatus) {
            if (status.isActive){
                    totalCoinPoint += status.coinPoint
                    totalActivityPoint += status.activityPoint
                    speedCoin += status.speedCoin
                    totalStatLapar += status.statLapar
                    totalStatPengetahuan += status.statPengetahuan
                    totalStatKesehatanFisik += status.statKesehatanFisik
                    totalStatKesehatanMental += status.statKesehatanMental
                    totalStatCinta += status.statCinta
                    totalStatHiburan += status.statHiburan
                    totalStatIstirahat += status.statIstirahat
                    totalStatSosial += status.statSosial
                }
            }

        //Toast.makeText(context, count1.toString() + count2.toString() + count3.toString() + count4.toString() + count5.toString() + count6.toString() + count7.toString() + count8.toString() + count9.toString(), Toast.LENGTH_SHORT).show()

        // Mengupdate efekNow dengan hasil kalkulasi
        efekNow.coinPoint = totalCoinPoint
        efekNow.activityPoint = totalActivityPoint
        efekNow.speedCoin = speedCoin
        efekNow.statLapar = totalStatLapar
        efekNow.statPengetahuan = totalStatPengetahuan
        efekNow.statKesehatanFisik = totalStatKesehatanFisik
        efekNow.statKesehatanMental = totalStatKesehatanMental
        efekNow.statCinta = totalStatCinta
        efekNow.statHiburan = totalStatHiburan
        efekNow.statIstirahat = totalStatIstirahat
        efekNow.statSosial = totalStatSosial

    }


}