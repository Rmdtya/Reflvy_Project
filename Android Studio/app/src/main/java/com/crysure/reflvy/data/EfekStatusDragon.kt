package com.crysure.reflvy.data

data class EfekStatusDragon(
    var namaStatus : String,
    var deskripsiStatus : String,
    var rarityStatus : Int,
    var durasiStatus : Int,
    var isActive : Boolean,
    var activityPoint : Float,
    var coinPoint : Float,
    var speedCoin : Float,
    var statLapar : Float,
    var statPengetahuan : Float,
    var statKesehatanFisik : Float,
    var statKesehatanMental : Float,
    var statCinta : Float,
    var statHiburan : Float,
    var statIstirahat : Float,
    var statSosial : Float
){
    companion object {
        var statusNegatif = mutableListOf<EfekStatusDragon>()
        var statusPositf = mutableListOf<EfekStatusDragon>()

        var continuosStatus = mutableListOf<EfekStatusDragon>()

        var efekNow = EfekStatusDragon("status", "", 1, 0, false,0f, 0f, 0f, 0f,
            0f, 0f, 0f, 0f, 0f, 0f, 0f)

        val listStatus = mutableListOf<EfekStatusDragon>()

        init {
            // Inisialisasi dan isi data untuk screening1
            val e01 = EfekStatusDragon(
                "Kurang Tidur",
                "Waktu yang kurang?",
                1,
                120,
                false,
                -1f,
                -0.05f,
                -0.05f,
                0f,
                0f,
                -0.005f,
                -0.003f,
                -0.005f,
                0f,
                -0.0001f,
                0f,
            )
            listStatus.add(e01)
            continuosStatus.add(e01)

            val e02 = EfekStatusDragon(
                "Kurang Makan",
                "Waktu yang kurang?",
                1,
                120,
                false,
                -1f,
                -70f,
                -0.01f,
                -0.001f,
                0f,
                -0.005f,
                -0.001f,
                0f,
                0f,
                0f,
                0f,
            )
            listStatus.add(e02)
            continuosStatus.add(e02)
        }
    }
}
