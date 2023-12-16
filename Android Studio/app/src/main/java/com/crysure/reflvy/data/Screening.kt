package com.crysure.reflvy.data

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
        val screenningSatuData = mutableListOf<Screening>()
        val screenData = mutableListOf<Screening>()
        val screeningTiga = mutableListOf<Screening>()

        init {

            val ScreenSatu01 = Screening(
                pertanyaan = 1,
                soalbot = listOf("Halo ${User.userData.userName}, Selamat datang di Reflvy.",
                    "Perkenalkan saya Igor yang akan menemani sesi saat ini.",
                    "Sebelum menjelajah lebih jauh aplikasi Reflvy apa kamu tahu apa itu Reflvy?"),
                textJawab = listOf("Tidak Tahu", "Sed.ikit Tahu", "Saya Tahu"),
                nilai = listOf(0, 1, 2),
                jawabUser = listOf("Saya belum tahu aplikasi Reflvy", "Saya sedikit tahu aplikasi Reflvy", "Saya tahu fungsi utama dari aplikasi Reflvy"),
                event = listOf(0, 1, 1)
            )
            screenningSatuData.add(ScreenSatu01)

            val ScreenSatu02 = Screening(
                pertanyaan = 2,
                soalbot = listOf("Saya ingin bertanya, dimana kamu tahu aplikasi Reflvy ini?"),
                textJawab = listOf("PlayStore", "Instagram", "Teman Saya"),
                nilai = listOf(0, 0, 0),
                jawabUser = listOf("Saya belum tahu aplikasi Reflvy", "Saya sedikit tahu aplikasi Reflvy", "Saya tahu fungsi utama dari aplikasi Reflvy"),
                event = listOf(1, 1, 1)
            )
            screenningSatuData.add(ScreenSatu02)

            val ScreenSatu03 = Screening(
                pertanyaan = 3,
                soalbot = listOf("Selanjutnya akan dilanjutkan sesi tanya jawab untuk mengulas dan mengetahui seberapa tahu kamu tentang pornografi", "Baik, mungkin akan saya mulai sekarang", "Kamu tau gak sih apa itu pornografi?"),
                textJawab = listOf("Ya, tau", "Tidak Tahu"),
                nilai = listOf(1, 0),
                jawabUser = listOf("Ya, aku tahu apa itu pornografi, Pornografi adalah konten seksual yang dapat membangkitkan birahi. ", "Aku tidak tahu apa itu pornografi"),
                event = listOf(0, 1)
            )
            screenningSatuData.add(ScreenSatu03)

            val ScreenSatu04 = Screening(
                pertanyaan = 4,
                soalbot = listOf("Apakah kamu tahu dampak apa yang timbul pada seorang remaja jika mengakses konten pornografi?"),
                textJawab = listOf("Tentu saja", "Tidak"),
                nilai = listOf(1, 0),
                jawabUser = listOf("Ya, tentu saja aku tahu hal itu. ", " Bisa tolong jelaskan?"),
                event = listOf(0, 1)
            )
            screenningSatuData.add(ScreenSatu04)

            val ScreenSatu05 = Screening(
                pertanyaan = 5,
                soalbot = listOf("Apakah kamu mengetahui alasan mengapa seseorang mengkonsumsi konten pornografi?"),
                textJawab = listOf("Saya Tahu", "Tidak Tahu"),
                nilai = listOf(1, 0),
                jawabUser = listOf("Ya, aku sudah tahu alasannya.", " Saya kurang mengetahui alasannya"),
                event = listOf(0, 1)
            )
            screenningSatuData.add(ScreenSatu05)

            val ScreenSatu06 = Screening(
                pertanyaan = 6,
                soalbot = listOf("Apakah kamu tahu peran teknologi internet dalam penyebaran konten pornografi?"),
                textJawab = listOf("Aku Tahu", "Tidak Tahu"),
                nilai = listOf(1, 0),
                jawabUser = listOf("Ya aku tahu, pornografi bisa dengan mudah diakses dan tersebar melalui intenet", "Saya tidak tahu, bisa tolong beritahu?"),
                event = listOf(0, 1)
            )
            screenningSatuData.add(ScreenSatu06)

            val ScreenSatu07 = Screening(
                pertanyaan = 7,
                soalbot = listOf("Apakah kamu tahu  tahap-tahap efek pornografi bagi orang yang mengkonsumsi pornografi?"),
                textJawab = listOf("Sedikit Tahu", "Tidak Tahu"),
                nilai = listOf(1, 0),
                jawabUser = listOf("Ya aku sedikit tahu tentang itu", "Aku tidak terlalu mengetahui tahapan-tahapan itu"),
                event = listOf(0, 1)
            )
            screenningSatuData.add(ScreenSatu07)

            val ScreenSatu08 = Screening(
                pertanyaan = 8,
                soalbot = listOf("Apakah kamu mengetahui bagaimana tanda-tanda seorang remaja yang kecanduan pornografi?"),
                textJawab = listOf("Kurang lebih", "Tidak Tahu"),
                nilai = listOf(1, 0),
                jawabUser = listOf("Ya, kurang lebih aku mengetahui ciri-cirinya", "Sedikit yang aku tahu, tapi tolong berikan info lengkapnya"),
                event = listOf(0, 1)
            )
            screenningSatuData.add(ScreenSatu08)

            val ScreenSatu09 = Screening(
                pertanyaan = 9,
                soalbot = listOf("Apakah kamu tahu dampak buruk jika kamu kecanduan pornografi yang bisa merugikan bagi orang lain?"),
                textJawab = listOf("Saya tahu","Sedikit Tahu", "Tidak Tahu"),
                nilai = listOf(1, 0, 0),
                jawabUser = listOf("Oh ya, aku sudah tahu.", "Belum terlalu tahu detailnya", "Saya Tidak Tahu"),
                event = listOf(0, 1, 1)
            )
            screenningSatuData.add(ScreenSatu09)

            val ScreenSatu10 = Screening(
                pertanyaan = 10,
                soalbot = listOf("Sedikit info yang perlu kamu ketahui. Sebuah survei menyatakan bahwa setiap tahunya ada 72 juta pengunjung website pornografi. Dalam setiap detiknya 28,000 pengguna internet melihat konten pornografi. Dua per tiga para penikmat pornografi di internet ini adalah laki-laki dan sisanya adalah perempuan. Kelompok usia 12-17 tahun adalah konsumen terbesar pornografi di internet.", "Terima kasih telah menyelesaikan sesi ini. Bersama Reflvy mari kita bersinergi untuk menciptakan generasi yang berkualitas. Merdeka!!!"),
                textJawab = listOf("Sama-sama","Thankyou Igor"),
                nilai = listOf(0, 0),
                jawabUser = listOf("Sama-sama.", "Tentu, terima kasih juga Igor atas informasinya"),
                event = listOf(0, 0)
            )
            screenningSatuData.add(ScreenSatu10)


            // Inisialisasi dan isi data untuk screening1
            val pertanyaan0 = Screening(
                pertanyaan = 1,
                soalbot = listOf("Halo, ${User.userData.userName} Kutebak kamu belum melakukan  screening nih. Yuk ikut screening dulu.",
                                "Ketik ‘ayo mulai’ untuk memulai screeningnya, Samantha.",
                                "PENTING: Jawaban yang kamu berikan nanti mesti  didasarkan pada perilaku kamu selama satu tahun  terakhir. Semua respon yang kamu berikan dijamin  kerahasiannya. Jadi, jangan takutuntuk jujur pada  dirimu sendiri ya!"),
                textJawab = listOf("Mulai", "Tidak Sekarang"),
                nilai = listOf(0, 0),
                jawabUser = listOf("Ayo mulai sekarang!!", "Saya tidak siap"),
                event = listOf(0, -1)
            )
            screenData.add(pertanyaan0)

            // Inisialisasi dan isi data untuk screening2
            val pertanyaan1 = Screening(
                pertanyaan = 1,
                soalbot = listOf("Pernahkah kamu mengggunakan pornografi / masturbasi untuk merubah suasana hati kamu ketika ada masalah?"),
                textJawab = listOf("Gak Pernah", "1-2 Kali", "Jarang", "Kadang", "Sering"),
                nilai = listOf(0, 1, 2, 3, 4),
                jawabUser = listOf("Tidak Pernah", "1-2 Kali", "Jarang", "Kadang", "Sering"),
                event = listOf(0, 0, 1, 1, 2)
            )
            screenData.add(pertanyaan1)

            val pertanyaan2 = Screening(
                pertanyaan = 2,
                soalbot = listOf("Apakah kamu jadi merasa gelisah ketika sedang mencoba untuk mengurangi atau berhenti menonton film pornografi?"),
                textJawab = listOf("Gak Pernah", "1-2 Kali", "Jarang", "Kadang", "Sering"),
                nilai = listOf(0, 1, 2, 3, 4),
                jawabUser = listOf("Tidak Pernah", "1-2 Kali", "Jarang", "Kadang", "Sering"),
                event = listOf(0, 0, 1, 1, 2)
            )
            screenData.add(pertanyaan2)

            val pertanyaan3 = Screening(
                pertanyaan = 3,
                soalbot = listOf("Apakah kadang-kadang pikiran seksual lebih sering mengganggu pikiranmu dari pada cita-citamu?"),
                textJawab = listOf("Gak Pernah", "1-2 Kali", "Jarang", "Kadang", "Sering"),
                nilai = listOf(0, 1, 2, 3, 4),
                jawabUser = listOf("Tidak Pernah", "1-2 Kali", "Jarang", "Kadang", "Sering"),
                event = listOf(0, 0, 1, 1, 2)
            )
            screenData.add(pertanyaan3)

            val pertanyaan4 = Screening(
                pertanyaan = 4,
                soalbot = listOf("Apakah terkadang kamu merasakan dorongan dalam diri sendiri untuk menonton pornografi?"),
                textJawab = listOf("Gak Pernah", "1-2 Kali", "Jarang", "Kadang", "Sering"),
                nilai = listOf(0, 1, 2, 3, 4),
                jawabUser = listOf("Tidak Pernah", "1-2 Kali", "Jarang", "Kadang", "Sering"),
                event = listOf(0, 0, 1, 1, 2)
            )
            screenData.add(pertanyaan4)

            val pertanyaan5 = Screening(
                pertanyaan = 5,
                soalbot = listOf("Apakah kamu pernah merasa hampa atau malu setelah menonton pornografi / maturbasi, dan pernahkah kamu mengharapkan untuk bisa berhenti melakukan itu?"),
                textJawab = listOf("Gak Pernah", "1-2 Kali", "Jarang", "Kadang", "Sering"),
                nilai = listOf(0, 1, 2, 3, 4),
                jawabUser = listOf("Tidak Pernah", "1-2 Kali", "Jarang", "Kadang", "Sering"),
                event = listOf(0, 0, 1, 1, 2)
            )
            screenData.add(pertanyaan5)

            val pertanyaan6 = Screening(
                pertanyaan = 6,
                soalbot = listOf("Pernahkah kamu  berjanji pada dirimu sendiri untuk tidak akan lagi menonton pornografi atau melakukan masturbasi?"),
                textJawab = listOf("Gak Pernah", "1-2 Kali", "Jarang", "Kadang", "Sering"),
                nilai = listOf(0, 1, 2, 3, 4),
                jawabUser = listOf("Tidak Pernah", "1-2 Kali", "Jarang", "Kadang", "Sering"),
                event = listOf(0, 0, 1, 1, 2)
            )
            screenData.add(pertanyaan6)

            val pertanyaan7 = Screening(
                pertanyaan = 7,
                soalbot = listOf("Apakah kamu pernah  berbohong kepada keluargamu atau orang lain tentang menonton pornografi?"),
                textJawab = listOf("Gak Pernah", "1-2 Kali", "Jarang", "Kadang", "Sering"),
                nilai = listOf(0, 1, 2, 3, 4),
                jawabUser = listOf("Tidak Pernah", "1-2 Kali", "Jarang", "Kadang", "Sering"),
                event = listOf(0, 0, 1, 1, 2)
            )
            screenData.add(pertanyaan7)

            val pertanyaan8 = Screening(
                pertanyaan = 8,
                soalbot = listOf("Apakah kamu setuju bahwa penggunaan pornografi pernah mengganggu, membatasi, atau mengurangi pengalaman hidupmu dalam berbagai aspek?"),
                textJawab = listOf("Gak Pernah", "1-2 Kali", "Jarang", "Kadang", "Sering"),
                nilai = listOf(0, 1, 2, 3, 4),
                jawabUser = listOf("Tidak Pernah", "1-2 Kali", "Jarang", "Kadang", "Sering"),
                event = listOf(0, 0, 1, 1, 2)
            )
            screenData.add(pertanyaan8)

            val pertanyaan9 = Screening(
                pertanyaan = 9,
                soalbot = listOf("Apakah kamu pernah sengaja begadang atau bangun di malam hari untuk menonton pornografi?"),
                textJawab = listOf("Gak Pernah", "1-2 Kali", "Jarang", "Kadang", "Sering"),
                nilai = listOf(0, 1, 2, 3, 4),
                jawabUser = listOf("Tidak Pernah", "1-2 Kali", "Jarang", "Kadang", "Sering"),
                event = listOf(0, 0, 1, 1, 2)
            )
            screenData.add(pertanyaan9)

            val pertanyaan10 = Screening(
                pertanyaan = 10,
                soalbot = listOf("Apakah kamu pernah dengan sengaja menghapus atau menyembunyikan riwayat pencarian untuk menghindari terdeteksinya aktivitas menonton pornografi?"),
                textJawab = listOf("Gak Pernah", "1-2 Kali", "Jarang", "Kadang", "Sering"),
                nilai = listOf(0, 1, 2, 3, 4),
                jawabUser = listOf("Tidak Pernah", "1-2 Kali", "Jarang", "Kadang", "Sering"),
                event = listOf(0, 0, 1, 1, 2)
            )
            screenData.add(pertanyaan10)

            val pertanyaan11 = Screening(
                pertanyaan = 11,
                soalbot = listOf("Apakah kamu pernah mencoba untuk mewajarkan, membenarkan, meminimalkan, atau membuat alasan tentang penggunaan pornografi?"),
                textJawab = listOf("Gak Pernah", "1-2 Kali", "Jarang", "Kadang", "Sering"),
                nilai = listOf(0, 1, 2, 3, 4),
                jawabUser = listOf("Tidak Pernah", "1-2 Kali", "Jarang", "Kadang", "Sering"),
                event = listOf(0, 0, 1, 1, 2)
            )
            screenData.add(pertanyaan11)

            val pertanyaan12 = Screening(
                pertanyaan = 12,
                soalbot = listOf("Apakah hampir setiap kali kamu mengakses internet, kamu akhirnya menonton pornografi?"),
                textJawab = listOf("Gak Pernah", "1-2 Kali", "Jarang", "Kadang", "Sering"),
                nilai = listOf(0, 1, 2, 3, 4),
                jawabUser = listOf("Tidak Pernah", "1-2 Kali", "Jarang", "Kadang", "Sering"),
                event = listOf(0, 0, 1, 1, 2)
            )
            screenData.add(pertanyaan12)

            val pertanyaan13 = Screening(
                pertanyaan = 13,
                soalbot = listOf("Pernahkah kamu merasa khawatir ada yang salah denganmu karena pikiran dan perasaan seksualmu?"),
                textJawab = listOf("Gak Pernah", "1-2 Kali", "Jarang", "Kadang", "Sering"),
                nilai = listOf(0, 1, 2, 3, 4),
                jawabUser = listOf("Tidak Pernah", "1-2 Kali", "Jarang", "Kadang", "Sering"),
                event = listOf(0, 0, 1, 1, 2)
            )
            screenData.add(pertanyaan13)

            val pertanyaan14 = Screening(
                pertanyaan = 14,
                soalbot = listOf("Pernahkah kamu ikut terlibat dalam obrolan seksual online, mengirim email, mengunggah atau berkomunikasi dengan pesan yang merujuk pada hal-hal seksual?"),
                textJawab = listOf("Gak Pernah", "1-2 Kali", "Jarang", "Kadang", "Sering"),
                nilai = listOf(0, 1, 2, 3, 4),
                jawabUser = listOf("Tidak Pernah", "1-2 Kali", "Jarang", "Kadang", "Sering"),
                event = listOf(0, 0, 1, 1, 2)
            )
            screenData.add(pertanyaan14)

            val pertanyaan15 = Screening(
                pertanyaan = 15,
                soalbot = listOf("Pernahkah kamu berbohong, mencuri, memanipulasi orang lain, atau membuat pilihan lain yang tidak bijaksana untuk menonton pornografi?"),
                textJawab = listOf("Gak Pernah", "1-2 Kali", "Jarang", "Kadang", "Sering"),
                nilai = listOf(0, 1, 2, 3, 4),
                jawabUser = listOf("Tidak Pernah", "1-2 Kali", "Jarang", "Kadang", "Sering"),
                event = listOf(0, 0, 1, 1, 2)
            )
            screenData.add(pertanyaan15)

            val pertanyaan16 = Screening(
                pertanyaan = 16,
                soalbot = listOf("Seberapa sering kamu menonton pornografi melebihi batas waktu yang kamu niatkan?"),
                textJawab = listOf("Gak Pernah", "1-2 Kali", "Jarang", "Kadang", "Sering"),
                nilai = listOf(0, 1, 2, 3, 4),
                jawabUser = listOf("Tidak Pernah", "1-2 Kali", "Jarang", "Kadang", "Sering"),
                event = listOf(0, 0, 1, 1, 2)
            )
            screenData.add(pertanyaan16)

            val pertanyaan17 = Screening(
                pertanyaan = 17,
                soalbot = listOf("Apakah kamu merasa senang ketika menonton semakin banyak jenis pornografi?"),
                textJawab = listOf("Gak Pernah", "1-2 Kali", "Jarang", "Kadang", "Sering"),
                nilai = listOf(0, 1, 2, 3, 4),
                jawabUser = listOf("Tidak Pernah", "1-2 Kali", "Jarang", "Kadang", "Sering"),
                event = listOf(0, 0, 1, 1, 2)
            )
            screenData.add(pertanyaan17)

            val pertanyaan18 = Screening(
                pertanyaan = 18,
                soalbot = listOf("Pernahkah kamu merasa bahwa pikiran atau perasaan seksualmu mengganggu keyakinan moral, agama, atau nilai-nilai keluargamu?"),
                textJawab = listOf("Gak Pernah", "1-2 Kali", "Jarang", "Kadang", "Sering"),
                nilai = listOf(0, 1, 2, 3, 4),
                jawabUser = listOf("Tidak Pernah", "1-2 Kali", "Jarang", "Kadang", "Sering"),
                event = listOf(0, 0, 1, 1, 2)
            )
            screenData.add(pertanyaan18)

            val pertanyaan19 = Screening(
                pertanyaan = 19,
                soalbot = listOf("Pernahkah kamu merasa merasa kotor atau tidak pantas karena pikiran dan perilaku seksualmu?"),
                textJawab = listOf("Gak Pernah", "1-2 Kali", "Jarang", "Kadang", "Sering"),
                nilai = listOf(0, 1, 2, 3, 4),
                jawabUser = listOf("Tidak Pernah", "1-2 Kali", "Jarang", "Kadang", "Sering"),
                event = listOf(0, 0, 1, 1, 2)
            )
            screenData.add(pertanyaan19)

            val pertanyaan20 = Screening(
                pertanyaan = 20,
                soalbot = listOf("Pernahkah kamu mencari sesuatu yang akan bisa merangsangmu secara seksual secara sengaja?"),
                textJawab = listOf("Gak Pernah", "1-2 Kali", "Jarang", "Kadang", "Sering"),
                nilai = listOf(0, 1, 2, 3, 4),
                jawabUser = listOf("Tidak Pernah", "1-2 Kali", "Jarang", "Kadang", "Sering"),
                event = listOf(0, 0, 1, 1, 2)
            )
            screenData.add(pertanyaan20)

            val pertanyaan21 = Screening(
                pertanyaan = 21,
                soalbot = listOf("Pernakah kamu mencari kesempatan untuk menyendiri supaya kamu bisa menonton pornografi atau melakukan masturbasi?"),
                textJawab = listOf("Gak Pernah", "1-2 Kali", "Jarang", "Kadang", "Sering"),
                nilai = listOf(0, 1, 2, 3, 4),
                jawabUser = listOf("Tidak Pernah", "1-2 Kali", "Jarang", "Kadang", "Sering"),
                event = listOf(0, 0, 1, 1, 2)
            )
            screenData.add(pertanyaan21)

            val pertanyaan22 = Screening(
                pertanyaan = 22,
                soalbot = listOf("Pernahkah kamu melakukan masturbasi saat menonton pornografi?\""),
                textJawab = listOf("Gak Pernah", "1-2 Kali", "Jarang", "Kadang", "Sering"),
                nilai = listOf(0, 1, 2, 3, 4),
                jawabUser = listOf("Tidak Pernah", "1-2 Kali", "Jarang", "Kadang", "Sering"),
                event = listOf(0, 0, 1, 1, 2)
            )
            screenData.add(pertanyaan22)

            val pertanyaan23 = Screening(
                pertanyaan = 23,
                soalbot = listOf("Kalau kamu tahu bahwa nonton film porno bisa membahayakan mental atau merusak hubungan, apa iya kamu masih mau terus menontonnya?"),
                textJawab = listOf("Gak Pernah", "1-2 Kali", "Jarang", "Kadang", "Sering"),
                nilai = listOf(0, 1, 2, 3, 4),
                jawabUser = listOf("Tidak Pernah", "1-2 Kali", "Jarang", "Kadang", "Sering"),
                event = listOf(0, 0, 1, 1, 2)
            )
            screenData.add(pertanyaan23)

            val pertanyaan24 = Screening(
                pertanyaan = 24,
                soalbot = listOf("Apa kamu tetap ingin melanjutkan menonton pornografi meskipun sudah menghadapi masalah terkait dengan tontonan tersebut?"),
                textJawab = listOf("Gak Pernah", "1-2 Kali", "Jarang", "Kadang", "Sering"),
                nilai = listOf(0, 1, 2, 3, 4),
                jawabUser = listOf("Tidak Pernah", "1-2 Kali", "Jarang", "Kadang", "Sering"),
                event = listOf(0, 0, 1, 1, 2)
            )
            screenData.add(pertanyaan24)

            val pertanyaan25 = Screening(
                pertanyaan = 25,
                soalbot = listOf("Pernahkah kamu mengalami usaha berulang kali yang tidak berhasil dalam upaya mengendalikan, mengurangi, atau bahkan berhenti menonton film porno?"),
                textJawab = listOf("Gak Pernah", "1-2 Kali", "Jarang", "Kadang", "Sering"),
                nilai = listOf(0, 1, 2, 3, 4),
                jawabUser = listOf("Tidak Pernah", "1-2 Kali", "Jarang", "Kadang", "Sering"),
                event = listOf(0, 0, 1, 1, 2)
            )
            screenData.add(pertanyaan25)




        }
    }
}