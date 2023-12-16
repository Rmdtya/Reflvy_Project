package com.crysure.reflvy.data

data class PackKuis(
    val pack : Int,
    val jenis: Int,
    var status : Boolean,
    val title: String,
    val jmlSoal: Int,
    val startIndex : Int,
    val endIndex : Int,
    val keterangan : String
){
    companion object {
        val packKuis = mutableListOf<PackKuis>()

        init {
            // Inisialisasi dan isi data untuk screening1
            val pack1 = PackKuis(
                pack = 0,
                jenis = 1,
                status = true,
                title = "Kuis Dasar Seputar Pornografi",
                jmlSoal= 10,
                startIndex = 0,
                endIndex = 9,
                keterangan = ""
            )
            packKuis.add(pack1)

            val pack2 = PackKuis(
                pack = 1,
                jenis = 1,
                status = false,
                title = "Kenali Bahaya Pornografi",
                jmlSoal= 10,
                startIndex = 10,
                endIndex = 19,
                keterangan = "Selesaikan dulu Kuis Dasar Pornografi"
            )
            packKuis.add(pack2)

            val pack3 = PackKuis(
                pack = 2,
                jenis = 1,
                status = true,
                title = "Seberapa Mengenal Anda Tentang Indonesia Tercinta",
                jmlSoal= 10,
                startIndex = 20,
                endIndex = 29,
                keterangan = ""
            )
            packKuis.add(pack3)

            val pack4 = PackKuis(
                pack = 3,
                jenis = 2,
                status = true,
                title = "Kuis Kata Gaul Part 1",
                jmlSoal= 10,
                startIndex = 30,
                endIndex = 39,
                keterangan = ""
            )
            packKuis.add(pack4)

            val pack5 = PackKuis(
                pack = 4,
                jenis = 2,
                status = true,
                title = "Kuis Kata Gaul Part 2",
                jmlSoal= 10,
                startIndex = 40,
                endIndex = 49,
                keterangan = ""
            )
            packKuis.add(pack5)

            val pack6 = PackKuis(
                pack = 5,
                jenis = 3,
                status = true,
                title = "Tatarucingan Part 1",
                jmlSoal= 10,
                startIndex = 50,
                endIndex = 59,
                keterangan = ""
            )
            packKuis.add(pack6)

            val pack7 = PackKuis(
                pack = 6,
                jenis = 3,
                status = true,
                title = "Tatarucingan Part 2",
                jmlSoal= 10,
                startIndex = 60,
                endIndex = 69,
                keterangan = ""
            )
            packKuis.add(pack7)
        }
    }
}