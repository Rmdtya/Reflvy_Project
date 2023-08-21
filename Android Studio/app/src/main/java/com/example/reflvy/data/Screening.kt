package com.example.reflvy.data

import com.google.gson.Gson

data class Screening(
    val pertanyaan : Int,
    val soalbot: List<String> = emptyList(),
    val textJawab: List<String> = emptyList(),
    val nilai: List<Int> = emptyList(),
    val jawabUser: List<String> = emptyList(),
    val event :  List<Int> = emptyList()
)
{
    companion object {
        val screenData = mutableListOf<Screening>()

        init {
            // Inisialisasi dan isi data untuk screening1
            val pertanyaan1 = Screening(
                pertanyaan = 1,
                soalbot = listOf("Halo, Anton! Kutebak kamu belum melakukan  screening nih. Yuk ikut screening dulu.",
                                "Ketik ‘ayo mulai’ untuk memulai screeningnya, Samantha.",
                                "PENTING: Jawaban yang kamu berikan nanti mesti  didasarkan pada perilaku kamu selama satu tahun  terakhir. Semua respon yang kamu berikan dijamin  kerahasiannya. Jadi, jangan takutuntuk jujur pada  dirimu sendiri ya!"),
                textJawab = listOf("Mulai", "Tidak Sekarang"),
                nilai = listOf(0, 0),
                jawabUser = listOf("Ayo mulai sekarang!!", "Saya tidak siap"),
                event = listOf(0, -1)
            )
            screenData.add(pertanyaan1)

            // Inisialisasi dan isi data untuk screening2
            val pertanyaan2 = Screening(
                pertanyaan = 2,
                soalbot = listOf("Apakah kamu tau apa itu pornografi?"),
                textJawab = listOf("Saya Tahu", "Tidak Tahu"),
                nilai = listOf(0, 1),
                jawabUser = listOf("Ya, saya tahu. Pornografi merujuk pada materi seksual eksplisit yang dimaksudkan untuk menarik perhatian seksual dan memuaskan hasrat seksual. Materi pornografi ini sering kali berisi gambar, video, cerita, atau audio yang menampilkan adegan seksual yang eksplisit dan tampilan tubuh telanjang atau setengah telanjang.",
                                    "Saya tidak tahu"),
                event = listOf(0, 1)
            )
            screenData.add(pertanyaan2)

            // Inisialisasi dan isi data untuk screening3
            val pertanyaan3 = Screening(
                pertanyaan = 3,
                soalbot = listOf("Apakah Anda pernah merasa tertarik atau penasaran untuk mencari konten pornografi?"),
                textJawab = listOf("Tidak", "Kadang-kadang", "Sering"),
                nilai = listOf(5, 15, 25),
                jawabUser = listOf("Saya tidak terlalu terlalu tertarik dengan konten - konten yang berbau vulgar",
                                    "Disaat tertentu, saya kadang kala merasa ingin mencari konten - konten yang vulgar",
                                    "Sering kali saya merasa ingin mengakses konten - konten yang berbau vulgar"),
                event = listOf(0, 1, 2)
            )
            screenData.add(pertanyaan3)
        }
    }
}