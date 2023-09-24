package com.example.reflvy.data

data class DataMisi(
    val indexNomor : Int,
    val namaMisi : String,
    val deskripsiMisi : String,
    val jumlahBintang : Int,
    val namaSubMisi: List<String> = emptyList(),
    var deskripsiSubMisi : List<String> = emptyList(),
    var progresNow : List<Int> = emptyList(),
    var maxProgres : List<Int> = emptyList(),
    var statusMisi : List<Boolean> = emptyList(),
    val klikable : List<String> = emptyList(),
){
    companion object {
        var dataMisiAplikasi = mutableListOf<DataMisi>()
//
//        init {
//            // Inisialisasi dan isi data untuk screening1
//            val misi1 = DataMisi(
//                0,
//                "Ekspedisi Pertama",
//                "Selesaikan semua misinya dan rasakan manfaatnya",
//                1,
//                listOf("Selesaikan Screening Pertama", "Lakukan Screening kedua untuk mengetahui tingkat kecanduanmu", "Buat Jadwal Keseharianmnu", "Selesaikan 20 Kegiatan Harian", "Login selama 5 hari"),
//                listOf("Lakukan Screening Pertama Saat User Pertama kali menggunakan aplikasi", "Screening 2 dilakukan untuk mengetahui tingkat kecanduan seseorang terhadap pornografi", "Atur dan tambahkan jadwal keseharianmu untuk harimu yang lebih baik", "Selesaikan semua kegiatanmu untuk masa depan yang lebih cerah", "Cek mood mu hari ini dalam dailycheck - in"),
//                listOf(0, 0, 0, 0, 0),
//                listOf(1, 1, 5, 10, 3),
//                listOf(false, false, false, false),
//                listOf("", "Screening2", "", "Pohon")
//            )
//            dataMisiAplikasi.add(misi1)
//        }
    }
}
