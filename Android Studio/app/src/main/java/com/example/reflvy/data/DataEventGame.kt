package com.example.reflvy.data

data class DataEventGame(
    var indexEvent : Int,
    var namaEvent : String,
    var isActive : Boolean,
    var rarityEvent : Int,
    var progresEvent : Int,
    var maxProgresEvent : Int,
    var efekEvent : EfekStatusDragon,
    val pertanyaan : String,
    val opsiJawaban: List<String> = emptyList(),
    var notifyNow : Boolean,
    var hasSucces : Boolean
) {
    companion object {
        var isNegatifEvent = mutableListOf<DataEventGame>()
        var isPositfEvent = mutableListOf<DataEventGame>()

        init {
            // Inisialisasi dan isi data untuk screening1
            val event01 = DataEventGame(
                0,
                "Kecanduan Game",
                false,
                3,
                0,
                200,
                EfekStatusDragon("","",0,0, false, 0f, -1f,
                -0.002f, 0f,-0.003f, -0.005f, -0.004f, 0f,
                    0f, -0.003f, -0.002f),
                "Berapa Jam Kamu Bermain Game Kemarin dan Hari Ini?",
                listOf("Kurang dari 1 Jam", "1 - 3 Jam", "Lebih Dari 3 Jam"),
                false,
                false
            )
            isNegatifEvent.add(event01)

            val event02 = DataEventGame(
                0,
                "Kecanduan Pornografi",
                false,
                3,
                0,
                500,
                EfekStatusDragon("","",0,0, false, -0.05f, -1f,
                    -0.005f, 0f,-0.006f, -0.007f, -0.007f,0f,
                    0f, -0.003f, -0.001f),
                "Apakah Kamu Menonton / Mengakses Konten Pornografi Kemaren?",
                listOf("Iya", "Tidak"),
                false,
                false
            )
            isNegatifEvent.add(event02)
        }
    }
}
