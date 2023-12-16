package com.crysure.reflvy.data

import android.content.Context
import android.content.SharedPreferences
import com.crysure.reflvy.R

data class GameStatusManager(
    var levelDragon : Int = 1,
    var namaDragon : String = "",
    var activityPoint : Float = 0f,
    var archivmentPoint : Int = 0,
    var coinPoint : Float = 0f,
    var statLapar : Float = 100f,
    var statPengetahuan : Float = 100f,
    var statKesehatanFisik : Float = 100f,
    var statKesehatanMental : Float = 100f,
    var statCinta : Float = 100f,
    var statHiburan : Float = 100f,
    var statIstirahat : Float = 100f,
    var statSosial : Float = 100f,
    var progres : Int = 0,
    var maxProgres : Int = 0
){
    companion object {
        var dragonStatus = GameStatusManager()
    }

        fun SaveDataStatus(context: Context) {
            val sharedPreferences: SharedPreferences = context.getSharedPreferences("GameStatus", Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()

            editor.putInt("levelDragon", dragonStatus.levelDragon)
            editor.putString("namaDragon", dragonStatus.namaDragon)
            editor.putFloat("activityPoint", dragonStatus.activityPoint)
            editor.putInt("archivmentPoint", dragonStatus.archivmentPoint)
            editor.putFloat("coinPoint", dragonStatus.coinPoint)
            editor.putInt("progres", dragonStatus.progres)

            editor.apply()
        }

        fun LoadDataStatus(context: Context) {
            val data = dragonStatus
            val sharedPreferences: SharedPreferences = context.getSharedPreferences("GameStatus", Context.MODE_PRIVATE)

            val levelDragon = sharedPreferences.getInt("levelDragon", 1)
            val namaDragon = sharedPreferences.getString("namaDragon", "Dragon")
            val activityPoint = sharedPreferences.getFloat("activityPoint", 0f)
            val archivmentPoint = sharedPreferences.getInt("archivmentPoint", 0)
            val coinPoint = sharedPreferences.getFloat("coinPoint", 0f)
            val progres = sharedPreferences.getInt("progres", 0)

            data.levelDragon = levelDragon
            data.namaDragon = namaDragon.toString()
            data.activityPoint = activityPoint
            data.archivmentPoint = archivmentPoint
            data.coinPoint = coinPoint
            data.progres = progres
        }

        fun SaveDataPoint(context: Context) {
            val sharedPreferences: SharedPreferences = context.getSharedPreferences("GameStatus", Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()

            editor.putFloat("statLapar", GameStatusManager.dragonStatus.statLapar)
            editor.putFloat("statPengetahuan", GameStatusManager.dragonStatus.statPengetahuan)
            editor.putFloat("statKesehatanFisik", GameStatusManager.dragonStatus.statKesehatanFisik)
            editor.putFloat("statKesehatanMental", GameStatusManager.dragonStatus.statKesehatanMental)
            editor.putFloat("statCinta", GameStatusManager.dragonStatus.statCinta)
            editor.putFloat("statHiburan", GameStatusManager.dragonStatus.statHiburan)
            editor.putFloat("statIstirahat", GameStatusManager.dragonStatus.statIstirahat)
            editor.putFloat("statSosial", GameStatusManager.dragonStatus.statSosial)

            editor.apply()
        }

        fun LoadDataPoint(context: Context) {
            val data = dragonStatus
            val sharedPreferences: SharedPreferences = context.getSharedPreferences("GameStatus", Context.MODE_PRIVATE)

            val statLapar = sharedPreferences.getFloat("statLapar", 100f)
            val statPengetahuan = sharedPreferences.getFloat("statPengetahuan", 100f)
            val statKesehatanFisik = sharedPreferences.getFloat("statKesehatanFisik", 100f)
            val statKesehatanMental = sharedPreferences.getFloat("statKesehatanMental", 100f)
            val statCinta = sharedPreferences.getFloat("statCinta", 100f)
            val statHiburan = sharedPreferences.getFloat("statHiburan", 100f)
            val statIstirahat = sharedPreferences.getFloat("statIstirahat", 100f)
            val statSosial = sharedPreferences.getFloat("statSosial", 100f)

            data.statLapar = statLapar
            data.statPengetahuan = statPengetahuan
            data.statKesehatanFisik = statKesehatanFisik
            data.statKesehatanMental = statKesehatanMental
            data.statCinta = statCinta
            data.statHiburan = statHiburan
            data.statIstirahat = statIstirahat
            data.statSosial = statSosial
        }
}


data class EvolusiDragon(
    val levelDragon : Int,
    var biayaUpgrade : List<Float>,
    val maxProgres: Int,
    val drawable : Int
){
    companion object {
        val evolusiDragon = mutableListOf<EvolusiDragon>()

        init {
            val evolusi1 = EvolusiDragon(
                2,
                listOf(50f, 100f, 150f, 200f, 250f, 300f, 350f, 400f, 450f, 500f, 1000f),
                10,
                R.drawable.roadmap_lv2
            )
            evolusiDragon.add(evolusi1)

            val evolusi2 = EvolusiDragon(
                3,
                listOf(1000f, 1500f, 2000f, 2500f, 3500f, 4500f, 5500f, 7000f, 9500f, 15000f, 17000f, 19000f, 21000f, 24000f, 25000f, 30000f),
                15,
                R.drawable.roadmap_lv3
            )
            evolusiDragon.add(evolusi2)

            val evolusi3 = EvolusiDragon(
                4,
                listOf(25000f, 28000f, 32000f, 35000f, 38000f, 42000f, 47000f, 51000f, 55000f, 61000f, 66000f, 69000f, 72000f, 83000f, 100000f, 150000f),
                15,
                R.drawable.roadmap_lv4
            )
            evolusiDragon.add(evolusi3)
        }
    }
}
