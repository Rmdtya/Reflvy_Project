package com.crysure.reflvy.data

data class DataNotification(
    val dataNomor : Int,
    var status : String,
    var namaKegiatan: String,
    var kategori : String,
    var lamaKegiatan: Int,
    var waktuMulai : String,
    var waktuSelesai : String,
    var proses : Boolean,
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
    var startMinute : Int
){
    companion object {
        val dataNotifikasi = mutableListOf<DataNotification>()

        fun CopyDataKegiatan(){
            DataDaily.dataKegiatan.forEach { kegiatan ->
                val notifikasi = DataNotification(
                    kegiatan.dataNomor,
                    kegiatan.status,
                    kegiatan.namaKegiatan,
                    kegiatan.kategori,
                    kegiatan.lamaKegiatan,
                    kegiatan.waktuMulai,
                    kegiatan.waktuSelesai,
                    kegiatan.proses,
                    kegiatan.progresNow,
                    kegiatan.senin,
                    kegiatan.selasa,
                    kegiatan.rabu,
                    kegiatan.kamis,
                    kegiatan.jumat,
                    kegiatan.sabtu,
                    kegiatan.minggu,
                    kegiatan.progresColor,
                    kegiatan.tanggalMulai,
                    kegiatan.tanggalSelesai,
                    kegiatan.startMinute
                )

                DataNotification.dataNotifikasi.add(notifikasi)
            }
        }
    }
}
