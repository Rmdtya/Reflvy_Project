package com.example.reflvy.data

data class DataKuis(
    val index : Int,
    val soal : String,
    val image : Boolean,
    val linkImage : String,
    val opsi: List<String> = emptyList(),
    val jawaban : Int
){
    companion object {
        val dataKuis = mutableListOf<DataKuis>()

        init {
            // Inisialisasi dan isi data untuk screening1
            val s1 = DataKuis(
                0,
                "Apa yang selalu datang tapi tidak pernah tiba?",
                false,
                "",
                listOf("Hujan", "Senyum", "Mobil", "Pohon"),
                1
            )
            dataKuis.add(s1)

            val s2 = DataKuis(
                1,
                "Apa yang membuat orang merasa lelah saat berdiri?",
                false,
                "",
                listOf("Lantai yang keras", "Sepatu yang tidak nyaman", "Berdiri terlalu lama", "Berdiri di atas satu kaki"),
                3
            )
            dataKuis.add(s2)

            val s3 = DataKuis(
                2,
                "Apa yang selalu di belakang Anda, tapi tidak pernah bisa Anda kejar?",
                false,
                "",
                listOf("Waktu", "Uang", "Kebahagiaan", "Masa depan"),
                0
            )
            dataKuis.add(s3)

            val s4 = DataKuis(
                3,
                "Apa yang bisa Anda pegang, tetapi tidak bisa Anda sentuh atau lihat?",
                false,
                "",
                listOf("Angin", "Suara", "Pikiran", "Ilusi"),
                2
            )
            dataKuis.add(s4)

            val s5 = DataKuis(
                4,
                "Apa yang bisa membuat Anda tertawa ketika hujan turun?",
                false,
                "",
                listOf("Payung", "Terpal", "Baju hujan", "Tetesan air hujan"),
                0
            )
            dataKuis.add(s5)

            val s6 = DataKuis(
                5,
                "Apa yang bisa terbang tanpa sayap?",
                false,
                "",
                listOf("Pesawat", "Burung", "Waktu", "Bayangan"),
                3
            )
            dataKuis.add(s6)

            val s7 = DataKuis(
                6,
                "Apa yang selalu ada di dalam Anda saat Anda tertidur?",
                false,
                "",
                listOf("Impian", "Cinta", "Udara", "Ketakutan"),
                2
            )
            dataKuis.add(s7)

            val s8 = DataKuis(
                7,
                "Apa yang tidak akan pernah bisa Anda miliki, tetapi selalu Anda bawa?",
                false,
                "",
                listOf("Mimpi", "Kendaraan", "Ponsel", "Keinginan"),
                0
            )
            dataKuis.add(s8)

            val s9 = DataKuis(
                8,
                "Apa yang selalu ada di depan Anda tetapi tidak bisa Anda lihat?",
                false,
                "",
                listOf("Masa depan", "Kaca", "Langit", "Kehadiran Anda sendiri"),
                0
            )
            dataKuis.add(s9)

            val s10 = DataKuis(
                9,
                "Apa yang bisa Anda beli, tetapi tidak pernah Anda gunakan?",
                false,
                "",
                listOf("Sepatu", "Pakaian dalam", "Uang koin", "Nama belakang Anda"),
                3
            )
            dataKuis.add(s10)
        }
    }
}