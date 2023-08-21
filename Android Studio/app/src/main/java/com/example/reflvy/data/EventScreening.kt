package com.example.reflvy.data

import com.google.gson.Gson

data class EventScreening(
    val pertanyaan : Int,
    val eventRespon0 : List<String> = emptyList(),
    val eventRespon1 : List<String> = emptyList(),
    val eventRespon2 : List<String> = emptyList(),
    val opsiRespon : List<String> = emptyList(),
    val tampilanRespon : List<String> = emptyList()
){
    companion object {
        val eventScreenData = mutableListOf<EventScreening>()

        init {
            // Inisialisasi dan isi data untuk eventScreening1
            val pertanyaan01 = EventScreening(
                pertanyaan = 1,
                eventRespon0 = emptyList(),
                eventRespon1 = listOf("Baik, mungkin dilain waktu ya!!"),
                eventRespon2 = emptyList(),
                opsiRespon = emptyList(),
                tampilanRespon = emptyList()
            )
            eventScreenData.add(pertanyaan01)

            // Inisialisasi dan isi data untuk eventScreening2
            val pertanyaan02 = EventScreening(
                pertanyaan = 2,
                eventRespon0 = emptyList(),
                eventRespon1 = listOf("Pornografi merujuk pada materi seksual eksplisit yang dimaksudkan untuk menarik perhatian seksual dan memuaskan hasrat seksual. Materi pornografi ini sering kali berisi gambar, video, cerita, atau audio yang menampilkan adegan seksual yang eksplisit dan tampilan tubuh telanjang atau setengah telanjang.",
                                        "Sifat pornografi yang eksplisit dan seksual membuatnya menjadi kontroversial dan sering kali dianggap tabu dalam beberapa budaya dan masyarakat. Keberadaan pornografi telah memicu banyak perdebatan tentang dampaknya terhadap individu dan masyarakat."),
                eventRespon2 = listOf("Event 2 - Respon 2.1", "Event 2 - Respon 2.2"),
                opsiRespon = listOf("Opsi 2.1", "Opsi 2.2"),
                tampilanRespon = listOf("Tampilan 2.1", "Tampilan 2.2")
            )
            eventScreenData.add(pertanyaan02)

            // Inisialisasi dan isi data untuk eventScreening3
            val pertanyaan03 = EventScreening(
                pertanyaan = 3,
                eventRespon0 = listOf("Event 3 - Respon 0.1", "Event 3 - Respon 0.2"),
                eventRespon1 = listOf("Rasa ketertarikan pada lawan jenis memang wajar bagi manusia. Namun mendeskripsikan dan menyalurkan rasa ketertarikan menjadi nafsu dan berakhir dengan tindakan menonton pornografi adalah cara yang salah.",
                                    "Untuk mengatasi rasa ketertarikan yang berlebih atau nafsu kamu bisa melakukan beberapa kegiatan yang positif untuk mengontrol diri."),
                eventRespon2 = listOf("Kapan biasanya rasa itu muncul di benak kamu?"),
                opsiRespon = listOf("Di malam hari", "Tiba - tiba muncul"),
                tampilanRespon = listOf("Rasa ketertarikan tersebut seringkali muncul di malam hari. Di liputi dengan pikiran negatif, lalu didorong dengan nafsu akhirnya...",
                                        "Tiba - tiba rasa itu muncul dan menjadi nafsu yang mendorong untuk mencari konten.")
            )
            eventScreenData.add(pertanyaan03)

        }
    }
}