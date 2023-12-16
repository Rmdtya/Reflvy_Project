package com.crysure.reflvy.data

data class DataDaily(
    var dataNomor : Int,
    var status : String,
    var namaKegiatan: String,
    var kategori : String,
    var lamaKegiatan: Int,
    var waktuMulai : String,
    var waktuSelesai : String,
    var proses : Boolean,
    var terlewat : Boolean,
    var tampilkanNotif : Boolean,
    var progresNow : Int,
    var senin : Boolean,
    var selasa : Boolean,
    var rabu : Boolean,
    var kamis : Boolean,
    var jumat : Boolean,
    var sabtu : Boolean,
    var minggu : Boolean,
    var progresColor: Int,
    var tanggalMulai : String,
    var tanggalSelesai : String,
    var startMinute : Int,
    var endMinutes : Int
){
    companion object {
        val dataKegiatan = mutableListOf<DataDaily>()
    }
}
