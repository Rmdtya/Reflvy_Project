package com.example.reflvy.data

data class ActiveLogin(
    var totalActive : Int = 0,
    var activeNow : Boolean,
    var lastActive : String,
    var moodNow : String,
    var totalSpendTime : Int,
    var kegiatan1Bekerja : Int,
    var kegiatan2BelajarFormal : Int,
    var kegiatan3Membaca : Int,
    var kegiatan4Bersantai : Int,
    var kegiatan5Istirahat : Int,
    var kegiatan6Belanja : Int,
    var kegiatan7Bermusik : Int,
    var kegiatan8Beribadah : Int,
    var kegiatan9BermainGame : Int,
    var kegiatan10HiburanDigital : Int,
    var kegiatan11OperasiKomputer : Int,
    var kegiatan12PekerjaanRumah : Int,
    var kegiatan13Komunitas : Int,
    var kegiatan14Bersosialisasi : Int,
    var kegiatan15Healing : Int,
    var kegiatan16Olahraga : Int,
    var kegiatan17Liburan : Int,
    var kegiatan18Lainnya : Int,
){
    companion object {
        var infoActive = ActiveLogin(0, false, "", "", 0, 0, 0, 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0, 0)
    }
}



