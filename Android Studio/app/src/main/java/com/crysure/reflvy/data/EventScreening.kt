package com.crysure.reflvy.data

data class EventScreening(
    val pertanyaan : Int,
    val eventRespon0 : List<String> = emptyList(),
    val eventRespon1 : List<String> = emptyList(),
    val eventRespon2 : List<String> = emptyList(),
    val opsiRespon : List<String> = emptyList(),
    val tampilanRespon : List<String> = emptyList()
){
    companion object {
        val eventScreenDataSatu = mutableListOf<EventScreening>()
        val eventScreenData = mutableListOf<EventScreening>()
        val eventScreenTiga = mutableListOf<EventScreening>()

        init {

            val screenSatu_01 = EventScreening(
                pertanyaan = 1,
                eventRespon0 = listOf("Mungkin kamu belum tahu apa itu Reflvy. Secara garis besar, Reflvy adalah sebuah aplikasi yang berfungsi untuk mendeteksi, mencegah dan menanggulangi kecanduan pornografi"),
                eventRespon1 = listOf("Mungkin kamu sudah lumayan tahu aplikasi Reflvy.", "Secara garis besar, Reflvy adalah sebuah aplikasi yang berfungsi untuk mendeteksi, mencegah dan menanggulangi kecanduan pornografi"),
                eventRespon2 = emptyList(),
                opsiRespon = emptyList(),
                tampilanRespon = emptyList()
            )
            eventScreenDataSatu.add(screenSatu_01)

            val screenSatu_02 = EventScreening(
                pertanyaan = 2,
                eventRespon0 = emptyList(),
                eventRespon1 = listOf("Terima kasih atas informasinya"),
                eventRespon2 = emptyList(),
                opsiRespon = emptyList(),
                tampilanRespon = emptyList()
            )
            eventScreenDataSatu.add(screenSatu_02)

            val screenSatu_03 = EventScreening(
                pertanyaan = 2,
                eventRespon0 = listOf("Tepat sekali. Secara maksud dan tujuan, penjelasan dari kamu sudah mewakili pengertian dari pornografi"),
                eventRespon1 = listOf("Di dalam Undang-Undang Nomor 44 Tahun 2008, arti pornografi adalah gambar, sketsa, ilustrasi, foto, tulisan, suara, bunyi, gambar bergerak, animasi, kartun, percakapan, gerak tubuh, atau bentuk pesan lainnya melalui berbagai bentuk media komunikasi dan/atau pertunjukan di muka umum, yang memuat kecabulan atau eksploitasi seksual yang melanggar norma kesusilaan dalam masyarakat.", "Intinya, pornografi adalah konten seksual yang dapat membangkitkan birahi."),
                eventRespon2 = emptyList(),
                opsiRespon = emptyList(),
                tampilanRespon = emptyList()
            )
            eventScreenDataSatu.add(screenSatu_03)

            val screenSatu_04 = EventScreening(
                pertanyaan = 2,
                eventRespon0 = listOf("Keren si! kamu memiliki pengetahuan yang luas. Kamu selangkah lebih depan untuk terhindar dari kecanduan pornografi"),
                eventRespon1 = listOf("Tentu, aku akan menjelaskan dampak yang timbul jika mengakses konten pornografi. Mudahnya dalam mengakses film/video porno memungkinkan seorang remaja secara bebas menonton sehingga menimbulkan kecenderungan bagi remaja untuk menonton film porno secara berulang-ulang, yang berdampak pada sulitnya berkonsetrasi dalam belajar. Akibat dari sulitnya berkonsentrasi mengakibatkan hasil belajar siswa rendah.", "Kamu bisa cari informasi lain di platform lainnya mengenai sisi negatif dari mengonsumsi pornografi."),
                eventRespon2 = emptyList(),
                opsiRespon = emptyList(),
                tampilanRespon = emptyList()
            )
            eventScreenDataSatu.add(screenSatu_04)

            val screenSatu_05 = EventScreening(
                pertanyaan = 2,
                eventRespon0 = listOf("Bagus, aku rasa asumsi kamu benar dengan yang aku pikirkan. Pada awalnya seseorang mengakses konten pornografi karena merasa penasaran dan pada akhirnya jatuh pada kondisi kecanduan"),
                eventRespon1 = listOf("Pada awalnya, seseorang mengakses konten pornografi karena merasa penasaran karena ada banyak konten pornografi yang sangat mudah dijumpai di lini masa Twitter, Instagram sampai TikTok. Belum lagi kehadiran sejumlah situs penyedia konten pornografi. Otak akan mengingat apa yang membuat seseorang merasa senang. Oleh karena seseorang tersebut tidak memiliki dasar perlindungan (pendidikan agama yang kurang, perhatian keluarga yang kurang), maka otak tersebut akan mendorongnya untuk mengulang melihat konten pornografi, hal yang membuatnya merasa senang. Secara alamiah, dopamin akan dialirkan sistem limbik ke PFC (Pre Frontal Cortex). Tepat di belakang dahi, ada bagian otak yang sangat spesial yang disebut sebagai Pre Frontal Cortex (PFC).", "Menurut Jordan Grafman, PFC hanya terdapat di otak manusia dan dirancang khusus agar manusia memiliki akhlak. PFC merupakan bagian otak yang memiliki peran strategis sehingga dikatakan sebagai pimpinan otak. Beberapa fungsi dari PFC antara lain: berkonsentrasi, memahami esensi benar dan salah, mengendalikan diri, berpikir kritis, merencanakan masa depan, menimbang baik dan buruk. Setiap kali mengakses pornografi dan orang yang kecanduan pornografi akan mengalirkan dopamin secara berlebihan sehingga membanjiri PFC. PFC akan menjadi tidak aktif karena terendam dopamin. Jika PFC terus menerus tidak aktif karena terendam dopamin, lama kelamaan PFC akan mengerut dan fungsinya terganggu, sistem limbik justru akan berkembang semakin besar dan dopamin semakin banyak diproduksi, anak yang kecanduan akan terus mencari kesenangan dan menjadi pelanggan pornografi seumur hidup."),
                eventRespon2 = emptyList(),
                opsiRespon = emptyList(),
                tampilanRespon = emptyList()
            )
            eventScreenDataSatu.add(screenSatu_05)

            val screenSatu_06 = EventScreening(
                pertanyaan = 2,
                eventRespon0 = listOf("Tepat sekali"),
                eventRespon1 = listOf("Kemajuan teknologi selain membawa dampak positif juga membuka akses terhadap konten negatif seperti konten pornografi. Publikasi pornografi melalui jaringan internet bukan hal yang aneh dan baru. Kecanggihan teknologi informasi dan komunikasi merupakan instrumen yang menunjang penyebaran pornografi. Kemudahan akses tersebut menyebabkan tidak hanya orang dewasa yang dapat melihat konten pornografi, bahkan anak-anak di bawah umur juga dapat dengan mudah mengakses konten tersebut"),
                eventRespon2 = emptyList(),
                opsiRespon = emptyList(),
                tampilanRespon = emptyList()
            )
            eventScreenDataSatu.add(screenSatu_06)

            val screenSatu_07 = EventScreening(
                pertanyaan = 2,
                eventRespon0 = listOf("Mungkin argumen kamu sama dengan yang saya ketahui", "Cline, 1986 dalam Catur (2008), menyebutkan bahwa ada tahap-tahap efek pornografi bagi mereka yang mengkonsumsi pornografi. Efek mengkonsumsi pornografi tidak terjadi secara langsung, dapat dilihat setelah beberapa waktu dalam jangka panjang. Tahap-tahap yang dialami oleh orang yang mengkonsumsi pornografi adalah:", "Tahap Addiction (kecanduan). Sekali seseorang menyukai materi pornografi, orang tersebut akan mengalami ketagihan. Pada saat orang tersebut tidak mengkonsumsi pornografi akan mengakibatkan kegelisahan.", "Tahap Escalation (eskalasi). Setelah sekian lama mengkonsumsi media porno, selanjutnya orang tersebut akan mengalami efek eskalasi. Akibatnya seseorang membutuhkan materi seksual yang lebih eksplisit, lebih sensasional", "Tahap Desensitization (desensitisasi). Pada tahap ini, materi pornografi yang dalam norma masyarakat dianggap tabu, immoral, mengejutkan, pelan-pelan akan menjadi sesuatu yang biasa. Orang ini cenderung tidak sensitif lagi terhadap kekerasan seksual.", "Tahap Act-out. Pada tahap ini, seorang pecandu pornografi akan meniru atau menerapkan perilaku seks yang selama ini dilihatnya di media."),
                eventRespon1 = listOf("Cline, 1986 dalam Catur (2008), menyebutkan bahwa ada tahap-tahap efek pornografi bagi mereka yang mengkonsumsi pornografi. Efek mengkonsumsi pornografi tidak terjadi secara langsung, dapat dilihat setelah beberapa waktu dalam jangka panjang. Tahap-tahap yang dialami oleh orang yang mengkonsumsi pornografi adalah:", "Tahap Addiction (kecanduan). Sekali seseorang menyukai materi pornografi, orang tersebut akan mengalami ketagihan. Pada saat orang tersebut tidak mengkonsumsi pornografi akan mengakibatkan kegelisahan.", "Tahap Escalation (eskalasi). Setelah sekian lama mengkonsumsi media porno, selanjutnya orang tersebut akan mengalami efek eskalasi. Akibatnya seseorang membutuhkan materi seksual yang lebih eksplisit, lebih sensasional", "Tahap Desensitization (desensitisasi). Pada tahap ini, materi pornografi yang dalam norma masyarakat dianggap tabu, immoral, mengejutkan, pelan-pelan akan menjadi sesuatu yang biasa. Orang ini cenderung tidak sensitif lagi terhadap kekerasan seksual.", "Tahap Act-out. Pada tahap ini, seorang pecandu pornografi akan meniru atau menerapkan perilaku seks yang selama ini dilihatnya di media."),
                eventRespon2 = emptyList(),
                opsiRespon = emptyList(),
                tampilanRespon = emptyList()
            )
            eventScreenDataSatu.add(screenSatu_07)

            val screenSatu_08 = EventScreening(
                pertanyaan = 2,
                eventRespon0 = listOf("Secara umum, remaja yang kecanduan pornografi kerap menarik diri dari pergaulan atau sering menyendiri, Ketika memandang lawan jenis mereka akan lebih terfokus memandang anggota tubuhnya, mereka akan lebih mudah tersinggung, marah, dan canggung, lalu pada akhirnya Cara berbicara kerap kali menggunakan kata-kata vulgar atau tidak senonoh yang sifatnya seksual."),
                eventRespon1 = listOf("Adapun beberapa tanda bahwa dia kecanduan pornografi adalah: ", "Menarik diri dari pergaulan, lebih banyak menghabiskan waktu sendirian dan sangat sensitif tentang privasi mereka, seperti tidak mengizinkan orang lain masuk ke kamar mereka atau berlama-lama di dalamnya.", "Ketika memandang lawan jenis, mereka akan lebih terfokus memandang anggota tubuhnya. Walaupun agak sulit melihat gejala ini, akan tetapi jika diperhatikan baik-baik maka perilaku seperti ini dapat dikenali.", "Mereka akan lelah karena harus terus menutupi kebiasaan yang mereka nilai buruk ini, sehingga mereka akan lebih mudah tersinggung dan marah, tidak suka ditanya soal dirinya dan sering tidak mau diganggu. Mereka pun akan lebih menyendiri dan memiliki kepercayaan diri yang menurun, serta menjadi pemurung. Adiksi ini menyebabkan mereka tersiksa dan depresi.", "Cara berbicara kerap kali menggunakan kata-kata tidak senonoh yang sifatnya seksual, terutama jika berbicara dengan teman-temannya"),
                eventRespon2 = emptyList(),
                opsiRespon = emptyList(),
                tampilanRespon = emptyList()
            )
            eventScreenDataSatu.add(screenSatu_08)

            val screenSatu_09 = EventScreening(
                pertanyaan = 2,
                eventRespon0 = listOf("Bagus sekali, seseorang yang kecanduan pornografi memiliki dorongan lebih untuk melakukan tindak kekerasan seksual"),
                eventRespon1 = listOf("Kecanduan melihat pornografi merupakan salah satu sebab dari banyaknya tindak kekerasan seksual yang terjadi di Indonesia. Pengetahuan akan suatu manfaat, dan akibat buruk sesuatu hal akan membentuk sikap, kemudian dari sikap itu akan timbul niat. Niat inilah yang selanjutnya akan menentukan apakah kegiatan akan dilakukan atau tidak. Pada masa remaja seseorang sedang mengalami dorongan seksual yang besar. Apabila pengetahuan akibat seringnya mengakses konten pornografi ditambah dengan dorongan seksual yang besar, maka dorongan untuk melakukan hubungan seksual semakin besar. Tidak mustahil jika dorongan seksual yang besar ini akan memicu tindak kekerasan.", "Remaja yang terbiasa mengkonsumsi materi pornografi yang menggambarkan berbagai adegan seksual, dapat terganggu proses pendidikan seksnya. Hal itu dapat diketahui dari cara mereka memandang lawan jenis, kejahatan seksual, hubungan seksual, dan seks pada umumnya. Remaja tersebut akan berkembang menjadi pribadi yang merendahkan lawan jenis secara seksual, memandang seks bebas sebagai perilaku normal dan alami, permisif terhadap perkosaan, bahkan cenderung mengidap berbagai penyimpangan seksual. Menurut Kepala Biro Hukum dan Humas Kementerian Pemberdayaan Perempuan dan Perlindungan Anak, kasus kekerasan terhadap anak masih sangat tinggi, dan yang lebih memprihatinkan lagi, 80 persen merupakan kekerasan seksual yang dipicu oleh kemudahan mengakses pornografi"),
                eventRespon2 = emptyList(),
                opsiRespon = emptyList(),
                tampilanRespon = emptyList()
            )
            eventScreenDataSatu.add(screenSatu_09)

            val screenSatu_10 = EventScreening(
                pertanyaan = 2,
                eventRespon0 = emptyList(),
                eventRespon1 = emptyList(),
                eventRespon2 = emptyList(),
                opsiRespon = emptyList(),
                tampilanRespon = emptyList()
            )
            eventScreenDataSatu.add(screenSatu_10)

















            // Inisialisasi dan isi data untuk eventScreening1
            val pertanyaan0 = EventScreening(
                pertanyaan = 1,
                eventRespon0 = emptyList(),
                eventRespon1 = listOf("Baik, mungkin dilain waktu ya!!"),
                eventRespon2 = emptyList(),
                opsiRespon = emptyList(),
                tampilanRespon = emptyList()
            )
            eventScreenData.add(pertanyaan0)

            val pertanyaan1 = EventScreening(
                pertanyaan = 1,
                eventRespon0 = listOf("Wah, kamu punya cara yang hebat untuk mengatasi masalah! Menghindari penggunaan pornografi atau masturbasi menunjukkan bahwa kamu punya kesadaran tentang kesehatan mental. Ingat, ada banyak cara sehat lainnya untuk merubah suasana hati, seperti berolahraga, mendengarkan musik, atau melakukan hobi yang kamu sukai."),
                eventRespon1 = listOf("Bagus banget kamu sadar kalau ada hubungan antara suasana hati dan tindakan tertentu. Itu tandanya kamu perhatian banget terhadap kesehatan mentalmu. Tetap berusaha untuk menemukan cara-cara sehat lainnya dalam menghadapi masalah ya, seperti menghubungi teman dekat atau berbicara dengan ahli kesehatan mental."),
                eventRespon2 = listOf("Saya mengerti bahwa saat-saat sulit bisa membuat seseorang mencari pelarian. Namun, jika pola ini sering terjadi, mungkin saatnya untuk mencari bantuan lebih lanjut. Jangan ragu untuk berbicara dengan teman, keluarga, atau seorang profesional. Mereka dapat membantu kamu menemukan cara-cara yang lebih sehat untuk mengatasi masalah dan mendukung kesehatan mentalmu. Kamu tidak sendirian dalam perjalanan ini."),
                opsiRespon = emptyList(),
                tampilanRespon = emptyList()
            )
            eventScreenData.add(pertanyaan1)

            val pertanyaan2 = EventScreening(
                pertanyaan = 2,
                eventRespon0 = listOf("Kamu punya cara yang bagus dalam menjaga perhatian pada berbagai hal dalam hidupmu. Penting untuk tetap seimbang dalam menjalani rutinitas dan menjaga pikiranmu tetap fokus."),
                eventRespon1 = listOf("Wajar kalau pikiran semacam itu kadang muncul. Tapi jangan lupa bahwa hidupmu terdiri dari banyak aspek, jadi mencoba menemukan keseimbangan antara pikiran tersebut dan hal-hal lainnya bisa membantu kamu merasa lebih terhubung dengan diri sendiri."),
                eventRespon2 = listOf("Terkadang pikiran bisa terfokus pada hal ini. Ingatlah bahwa kamu memiliki kendali atas pikiranmu. Jika kamu merasa pikiran ini mengganggu atau membuat kamu merasa kurang nyaman, pertimbangkan berbicara dengan seseorang yang bisa memberikan dukungan dan pandangan dari sudut pandang yang berbeda."),
                opsiRespon = emptyList(),
                tampilanRespon = emptyList()
            )
            eventScreenData.add(pertanyaan2)

            val pertanyaan3 = EventScreening(
                pertanyaan = 3,
                eventRespon0 = listOf("Tampaknya kamu mampu menjaga fokus pada hal-hal lain dalam hidupmu. Bagus, karena menjaga keseimbangan antara pikiran dan tujuanmu adalah hal yang penting."),
                eventRespon1 = listOf("Pikiran semacam itu kadang mungkin muncul, namun tetap penting untuk tetap terhubung dengan cita-citamu. Dengan mencari keseimbangan antara pikiran dan tujuan, kamu bisa lebih mudah meraih apa yang diinginkan."),
                eventRespon2 = listOf("Penting untuk diingat bahwa pikiran semacam itu bisa muncul pada banyak orang. Namun, cobalah tetap mengarahkan energimu ke arah cita-cita yang ingin kamu raih. Jika merasa terganggu, pertimbangkan berbicara dengan teman atau orang dewasa yang bisa memberikan pandangan yang lebih luas atau coba sibukkan pikiranmu dengan kegiatan yang positif."),
                opsiRespon = emptyList(),
                tampilanRespon = emptyList()
            )
            eventScreenData.add(pertanyaan3)

            val pertanyaan4 = EventScreening(
                pertanyaan = 4,
                eventRespon0 = listOf("Kamu keren banget bisa tetap ngarahin pikiranmu ke hal-hal positif! Terus jaga semangat kamu untuk tetap melakukan aktivitas yang membuatmu senang dan nyaman."),
                eventRespon1 = listOf("Jangan khawatir kalau kadang-kadang pikiran gini muncul. Itu sesuatu yang umum terjadi. Yang penting, terus fokus pada hal-hal yang positif dan yang bisa bikin kamu merasa baik tentunya tidak dengan mengonsumsi konten pornografi."),
                eventRespon2 = listOf("Kalau kamu merasa pikiran ini muncul terlalu sering dan mengganggu, kamu coba cari cara buat ngelola dan mengalihkannya karena ini bisa berdampak buruk sama kesehatan mental kamu . Inget, banyak hal positif di sekitarmu yang bisa bikin kamu bahagia. Kalo butuh, ngobrol sama seseorang yang bisa kamu percaya, kayak teman atau orang dewasa yang bisa bantuin kamu."),
                opsiRespon = emptyList(),
                tampilanRespon = emptyList()
            )
            eventScreenData.add(pertanyaan4)

            val pertanyaan5 = EventScreening(
                pertanyaan = 5,
                eventRespon0 = listOf("Perlu kamu ketahui jika suatu saat perasaan hampa atau keinginan untuk berhenti melakukan itu muncul, ingatlah bahwa kamu selalu memiliki kemampuan untuk melakukan perubahan positif dalam hidupmu."),
                eventRespon1 = listOf("Terkadang, perasaan hampa atau malu bisa muncul setelah melakukan tindakan semacam itu. Merasa seperti ini adalah hal yang normal, terutama jika itu merusak keseimbangan dalam hidupmu. Jika kamu pernah berharap untuk berhenti dan merasa sulit melakukannya sendiri, itu bagus karena itu merupakan awal supaya kamu bisa terlepas dari kecanduan konten pornografi."),
                eventRespon2 = listOf("Jika kamu sering merasa hampa atau malu setelah nonton porno/masturbasi dan ingin berhenti tetapi merasa kesulitan, ini bisa menjadi pertanda bahwa kebiasaan ini mungkin mempengaruhi kesejahteraan hidup kamu lho. Berharap untuk berhenti adalah langkah awal yang baik menuju perubahan positif. Pertimbangkan untuk mengatasi tantangan ini dan memberikan panduan dalam mencapai tujuanmu dengan hal positif ya, semangat."),
                opsiRespon = emptyList(),
                tampilanRespon = emptyList()
            )
            eventScreenData.add(pertanyaan5)

            val pertanyaan6 = EventScreening(
                pertanyaan = 6,
                eventRespon0 = listOf("Itu oke, tidak semua orang membuat janji semacam itu. Namun, penting untuk menyadari dampak kebiasaan tertentu pada kesejahteraan fisik dan mental kita. Mengenali dan mengatasi tantangan adalah langkah penting dalam perubahan. Jika kamu merasa ingin lebih sadar tentang pengaruh tontonan tertentu atau ingin mengurangi kebiasaan tersebut, ingatlah bahwa perubahan yang positif dimulai dengan kesadaran dan langkah kecil yang konsisten."),
                eventRespon1 = listOf("Wajar kalau kadang kamu merasa ingin berkomitmen untuk berhenti. Itu adalah tanda kesadaran akan pentingnya kesehatan mental dan perubahan positif. Jika pernah membuat janji seperti itu, ingatlah bahwa mengubah kebiasaan butuh usaha dan dukungan, maka teruslah konsisten untuk mewujud"),
                eventRespon2 = listOf("Sering merasa ingin berhenti menunjukkan kamu mengakui dampak dari kebiasaan tersebut. Perubahan butuh waktu dan dedikasi. Meskipun kamu mungkin sudah membuat janji seperti itu, jika kamu merasa kesulitan untuk memenuhi janji tersebut, jangan ragu merencanakan strategi yang lebih berhasil."),
                opsiRespon = emptyList(),
                tampilanRespon = emptyList()
            )
            eventScreenData.add(pertanyaan6)

            val pertanyaan7 = EventScreening(
                pertanyaan = 7,
                eventRespon0 = listOf("Bagus, tampaknya kamu memiliki komunikasi yang jujur dengan keluarga atau orang lain. Terus mempertahankan kejujuran ini bisa memperkuat hubungan kamu dengan mereka dan membangun lingkungan di mana kamu merasa nyaman berbicara terbuka tentang apa pun yang kamu hadapi."),
                eventRespon1 = listOf("Kadang-kadang, tekanan atau rasa malu bisa membuat seseorang memilih untuk berbohong tentang hal seperti ini. Namun, penting untuk diingat bahwa kejujuran adalah fondasi yang kuat dalam hubungan. Berbicara terbuka tentang perasaanmu dan berbagi pengalaman dengan orang-orang supaya orang lain tahu apa yang kamu alami dan bisa membantu kamu."),
                eventRespon2 = listOf("Memilih untuk berbohong tentang hal ini mungkin mencerminkan adanya perasaan malu atau rasa bersalah. Namun, berbohong bisa memperumit situasi dan merusak kepercayaan. Meskipun terkadang sulit, menghadapi kejujuran dan berbicara terbuka tentang apa yang kamu alami bisa memperkuat hubungan kamu dengan keluarga dan orang lain supaya orang lain tahu apa yang kamu alami dan bisa membantu kamu."),
                opsiRespon = emptyList(),
                tampilanRespon = emptyList()
            )
            eventScreenData.add(pertanyaan7)

            val pertanyaan8 = EventScreening(
                pertanyaan = 8,
                eventRespon0 = listOf("Penggunaan pornografi memang berpengaruh pada berbagai aspek kehidupanmu. Alangkah baiknya dimulai dari sekarang jauhilah segala macam pornografi agar kehidupanmu jauh lebih bermanfaat. Selain untuk diri sendiri lebih baik jika kamu bermanfaat untuk lingkungan sekitar"),
                eventRespon1 = listOf("Terkadang, pornografi sangatlah dekat di kehidupanmu. Sebelum kamu menjadi kecanduan lebih baik berhenti dari sekarang. Kehidupanmu akan lebih bermanfaat akan lebih baik jika mulai berhenti mencari dan menikmati pornografi."),
                eventRespon2 = listOf("Sepertinya kamu sudah terjerumus dalam lingkaran pornografi alangkah lebih baiknya kamu mulai berhenti pada saat ini. Cobalah untuk kembali fokus melakukan kegiatan yang positif supaya kualitas hidupmu lebih baik."),
                opsiRespon = emptyList(),
                tampilanRespon = emptyList()
            )
            eventScreenData.add(pertanyaan8)

            val pertanyaan9 = EventScreening(
                pertanyaan = 9,
                eventRespon0 = listOf("Terima kasih atas kejujuranmu. Tidak semua orang mengalami situasi seperti ini, dan penting untuk mengenali kapan waktu tidur yang cukup dan kualitas tidur yang baik adalah penting. Tetap menjaga pola tidur yang sehat dapat berdampak positif pada kesejahteraanmu supaya tidak berlanjut terkena kecanduan pornografi. "),
                eventRespon1 = listOf("Terkadang, keinginan untuk menonton pornografi bisa mempengaruhi pola tidur kita. Namun, penting untuk diingat bahwa tidur yang cukup dan berkualitas adalah penting bagi kesehatan fisik dan mental kita. Jika hal ini mulai mengganggu pola tidurmu, pertimbangkan untuk merencanakan waktu tidur yang teratur dan menjaga kegiatan yang positif sebelum tidur."),
                eventRespon2 = listOf("Mengorbankan tidur untuk menonton pornografi bisa mengganggu keseimbangan hidup dan kesehatan. Tidur yang cukup sangat penting bagi kesejahteraan fisik dan mental kita. Jika kamu merasa sulit untuk mengatasi kebiasaan ini, pertimbangkan untuk membuat jadwal tidur yang lebih disiplin dan memprioritaskan aktivitas yang mendukung kualitas tidur yang baik supaya kamu juga dapat terhindar dari kecanduan pornografi. "),
                opsiRespon = emptyList(),
                tampilanRespon = emptyList()
            )
            eventScreenData.add(pertanyaan9)

            val pertanyaan10 = EventScreening(
                pertanyaan = 10,
                eventRespon0 = listOf("Tampaknya kamu memiliki transparansi dan komunikasi yang baik dengan orang di sekitarmu. Ini bisa memperkuat hubungan dan membangun kepercayaan. "),
                eventRespon1 = listOf("Terkadang, keinginan untuk menjaga privasi bisa mendorong seseorang untuk menghapus riwayat pencarian atau menutupi aktivitas online. Namun, penting untuk diingat bahwa kepercayaan dan komunikasi jujur adalah dasar yang kuat dalam hubungan. Terbuka tentang kekhawatiranmu dan membicarakannya dengan orang tua atau orang yang kamu percayai bisa membantu mengatasi situasi seperti ini."),
                eventRespon2 = listOf("Jika kamu sering merasa perlu menghapus riwayat pencarian atau menyembunyikan aktivitas online terkait dengan pornografi, itu bisa menunjukkan dorongan kuat untuk menjaga privasi. Namun, terkadang upaya semacam ini bisa memicu rasa khawatir atau ketidakpercayaan dari orang tua atau orang di sekitarmu. Penting untuk membuka komunikasi tentang perasaanmu dan mencari cara yang lebih sehat dan terbuka untuk mengatasi kekhawatiran atau kebutuhan privasimu. "),
                opsiRespon = emptyList(),
                tampilanRespon = emptyList()
            )
            eventScreenData.add(pertanyaan10)

            val pertanyaan11 = EventScreening(
                pertanyaan = 11,
                eventRespon0 = listOf("Mewajarkan penggunaan pornografi secara berlebihan bisa menjadi tanda potensi kecanduan. Saat seseorang merasa perlu untuk membenarkan atau menciptakan alasan demi melanjutkan perilaku tertentu, hal ini bisa menunjukkan adanya dorongan yang sulit dikendalikan. Kecanduan pada pornografi bisa memiliki dampak serius pada kesejahteraan mental dan hubungan."),
                eventRespon1 = listOf("Mewajarkan penggunaan pornografi adalah langkah awal menuju potensi kecanduan. Ini bisa menjadi cara untuk meredakan perasaan bersalah atau menciptakan alasan yang menghaluskan aktivitas tersebut. Kecanduan pada pornografi dapat mengganggu rutinitas harian, mengisolasi individu, dan merusak keseimbangan dalam hidup."),
                eventRespon2 = listOf("Penting untuk diingat bahwa mewajarkan penggunaan pornografi memiliki risiko serius, terutama berkaitan dengan kecanduan. Menggunakan alasan atau justifikasi untuk melanjutkan perilaku ini mungkin menunjukkan dorongan yang lebih dalam. Kecanduan pada pornografi dapat mengganggu kesejahteraan mental, menjauhkan dari nilai-nilai personal, dan merusak hubungan sosial."),
                opsiRespon = emptyList(),
                tampilanRespon = emptyList()
            )
            eventScreenData.add(pertanyaan11)

            val pertanyaan12 = EventScreening(
                pertanyaan = 12,
                eventRespon0 = listOf("Jika kamu tidak merasakan dorongan untuk menonton pornografi setiap kali mengakses internet, itu adalah hal yang bagus. Terus mempertahankan keseimbangan dalam aktivitas online dan offline adalah langkah yang baik untuk menjaga kesejahteraanmu."),
                eventRespon1 = listOf("Terkadang, adanya dorongan untuk menonton pornografi saat mengakses internet mungkin terjadi. Namun, hal ini tidak selalu harus menjadi pola perilaku yang tetap. Jika kamu merasa dorongan ini mengganggu aktivitas atau produktivitasmu, pertimbangkan untuk mengidentifikasi strategi yang membantu mengelola impuls tersebut. "),
                eventRespon2 = listOf("Jika hampir setiap kali kamu mengakses internet akhirnya berujung pada menonton pornografi, ini bisa menjadi pertanda bahwa perilaku ini mungkin sudah mencapai tingkat kecanduan. Kecanduan pada pornografi dapat memiliki dampak yang signifikan pada kesejahteraan mental dan kehidupan sehari-hari. Pertimbangkan untuk membangun pola perilaku yang lebih sehat."),
                opsiRespon = emptyList(),
                tampilanRespon = emptyList()
            )
            eventScreenData.add(pertanyaan12)

            val pertanyaan13 = EventScreening(
                pertanyaan = 13,
                eventRespon0 = listOf("Tampaknya kamu bisa mengendalikan diri kamu dengan baik. Tetap kendalikan dirimu dengan baik ya."),
                eventRespon1 = listOf("Perasaan tersebut sepertinya wajar dialami oleh seusia kamu. Namun, kamu harus yakin dengan diri kamu. Banyak berpikir positif untuk lebih bisa mengendalikan diri kamu dengan baik."),
                eventRespon2 = listOf("Kalau kamu sering merasakan hal tersebut sepertinya kamu harus belajar dalam mengendalikan diri kamu. Berpikir positif serta percaya akan kemampuan diri kamu bisa menjadikan diri kamu untuk lebih bisa dalam mengendalikan diri kamu ke arah yang lebih baik."),
                opsiRespon = emptyList(),
                tampilanRespon = emptyList()
            )
            eventScreenData.add(pertanyaan13)

            val pertanyaan14 = EventScreening(
                pertanyaan = 14,
                eventRespon0 = listOf("Bagus jika kamu merasa nyaman dengan batasan yang kamu tetapkan dalam interaksi online. Merupakan hal yang sehat untuk menjaga privasi, kesehatan mental dan berbicara dengan sopan dalam segala bentuk komunikasi online. "),
                eventRespon1 = listOf("Terkadang, mungkin terjadi ikut serta dalam obrolan seksual online atau sexting. Penting untuk memahami bahwa segala bentuk komunikasi online perlu dilakukan dengan pertimbangan yang cermat karena terlibat dalam obrolan seksual online atau sexting bisa berdampak buruk pada kesejahteraan mental dan privasi, serta meningkatkan risiko kecanduan, oleh karena itu penting untuk menjaga batasan yang sehat dalam interaksi online."),
                eventRespon2 = listOf("Jika kamu sering ikut serta dalam obrolan seksual online atau sexting, ini bisa menunjukkan kecenderungan untuk terlibat dalam perilaku yang lebih eksplisit secara seksual dalam dunia maya. Ingatlah bahwa keamanan dan privasi dalam berinteraksi online adalah hal yang penting karena terlibat dalam obrolan seksual online atau sexting bisa berdampak buruk pada kesejahteraan mental dan privasi, serta meningkatkan risiko kecanduan, oleh karena itu penting untuk menjaga batasan yang sehat dalam interaksi online."),
                opsiRespon = emptyList(),
                tampilanRespon = emptyList()
            )
            eventScreenData.add(pertanyaan14)

            val pertanyaan15 = EventScreening(
                pertanyaan = 15,
                eventRespon0 = listOf("Bagus sekali. Kamu sangat menghargai diri kamu dan orang lain. Tetap jaga kejujuran dan komunikasi agar hidup kamu dapat bermanfaat untuk kamu sendiri serta lingkungan sekitarmu."),
                eventRespon1 = listOf("Terkadang perlunya pengakuan dari orang lain bisa menjerumuskan kamu dan orang disekitarmu ke hal-hal negatif. Mulailah untuk menebarkan hal-hal positif agar hidupmu bermanfaat untuk lingkungan sekitar."),
                eventRespon2 = listOf("Berbuat bohong, memanipulasi orang lain untuk sebuah pengakuan sangatlah tidak baik. Alangkah lebih baiknya jika kamu berhenti untuk berbuat demikian agar orang lain di sekitarmu merasa nyaman. Serta hidupmu menjadi lebih baik dan bermanfaat untuk lingkungan sekitar."),
                opsiRespon = emptyList(),
                tampilanRespon = emptyList()
            )
            eventScreenData.add(pertanyaan15)

            val pertanyaan16 = EventScreening(
                pertanyaan = 16,
                eventRespon0 = listOf("Bagus jika kamu mampu menjaga penggunaan pornografi sesuai dengan batas waktu yang kamu tentukan. Ini menunjukkan kemampuanmu untuk mengendalikan perilaku dan keputusan yang telah kamu buat. Tapi perlu kamu ingat penggunaan pornografi dalam waktu sesingkat apapun itu dapat menyebabkan kecanduan secara signifikat, maka alangkah baiknya kamu tidak mencoba untuk mengonsumsi konten pornografi."),
                eventRespon1 = listOf("Terkadang, mungkin kamu menemui situasi di mana penggunaan pornografi melebihi batas waktu yang kamu rencanakan. Penting untuk memperhatikan perubahan ini dan mencari pemahaman lebih dalam tentang alasan di balik hal ini. Jika merasa sulit mengontrol waktu yang dihabiskan untuk hal tersebut, kamu harus selalu ingat bahwa mengonsumsi konten pornografi itu sangat berbahaya dan dapat membuat kecanduan. Kamu perlu mertimbangkan untuk mengatur pengaturan waktu dan batasan yang lebih jelas."),
                eventRespon2 = listOf("Jika sering kali kamu menonton pornografi melebihi batas waktu yang kamu niatkan, itu bisa menunjukkan adanya kesulitan dalam mengendalikan frekuensi dan durasi penggunaan. Perilaku ini dapat mengindikasikan adanya potensi masalah lebih dalam, seperti kecanduan. Pertimbangkan untuk tidak mencoba mengonsumsi konten pornografi lagi lalu mengatasi tantangan ini dengan strategi yang lebih lebih sehat.  "),
                opsiRespon = emptyList(),
                tampilanRespon = emptyList()
            )
            eventScreenData.add(pertanyaan16)

            val pertanyaan17 = EventScreening(
                pertanyaan = 17,
                eventRespon0 = listOf("Bagus sekali, kalau kamu tidak pernah merasa senang ketika menonton semakin banyak jenis pornografi. Ini menunjukkan bahwa kamu memiliki kesadaran yang kuat terhadap dampak yang mungkin terjadi. Menghindari kesenangan sesaat untuk kepentingan kesejahteraan jangka panjangmu adalah langkah bijak yang perlu diapresiasi."),
                eventRespon1 = listOf("Jika terkadang kamu merasa senang ketika menonton semakin banyak jenis pornografi, penting untuk diingat bahwa kesenangan ini mungkin hanya bersifat sementara. Pikiran dan perasaan yang mulai menikmati bisa membawa risiko, terutama jika berlebihan. "),
                eventRespon2 = listOf("Jika kamu sering merasa senang ketika menonton semakin banyak jenis pornografi, penting untuk mengenali tanda-tanda bahwa kamu mulai terjebak dalam lingkaran perilaku yang berpotensi berbahaya. Kesenangan sesaat bisa berdampak negatif pada kesejahteraanmu dalam jangka panjang. Menghadapi tantangan ini memerlukan tekad dan upaya nyata, cobalah untuk memulai kebiasaan positif yang bisa membantu kamu mengatasi godaan tersebut."),
                opsiRespon = emptyList(),
                tampilanRespon = emptyList()
            )
            eventScreenData.add(pertanyaan17)

            val pertanyaan18 = EventScreening(
                pertanyaan = 18,
                eventRespon0 = listOf("Bagus sekali, terus tingkatkan lagi kualitas ibadahmu. Komunikasi bersama anggota keluarga atau orang lain disekitarmu sangatlah penting."),
                eventRespon1 = listOf("Kamu harus lebih meningkatkan tingkat ibadahmu. Berpikir positif menjadi faktor pendukung agar pikiran kamu tetap positif"),
                eventRespon2 = listOf("Kamu sepertinya harus lebih bisa berkomunikasi dengan orang lain di sekitarmu terutama keluarga mu. Komunikasi menjadi hal sangat penting agar kamu tetap berpikiran positif dalam segala hal"),
                opsiRespon = emptyList(),
                tampilanRespon = emptyList()
            )
            eventScreenData.add(pertanyaan18)

            val pertanyaan19 = EventScreening(
                pertanyaan = 19,
                eventRespon0 = listOf("Perlu kamu ketahui bahwa merasa kotor atau tak layak karena pikiran dan perilaku seksualmu, itu bisa menjadi tanda adanya perasaan bersalah. Hal ini dapat memengaruhi cara kita memandang seksualitas dan memicu pemikiran yang berlebihan."),
                eventRespon1 = listOf("Terkadang, rasa bersalah atau perasaan tak layak bisa muncul akibat pemikiran atau perilaku seksual. Namun, penting untuk diingat bahwa perasaan tersebut bisa bersumber dari norma sosial atau pandangan negatif yang tidak sehat. Merasa kotor tidaklah produktif. Fokuslah pada pendekatan yang lebih positif terhadap diri sendiri dan membantu dirimu merasa lebih baik dengan mendiskusikan perasaan ini dengan orang terpercaya."),
                eventRespon2 = listOf("Jika kamu sering merasa kotor atau tak layak karena pikiran dan perilaku seksualmu, itu bisa menjadi tanda adanya perasaan bersalah yang lebih dalam. Ingatlah bahwa semua orang memiliki pemikiran dan perasaan seksual, dan hal ini adalah bagian alami dari manusia. Jika perasaan ini terus mengganggu, coba untuk berhenti dari kegiatan negtaif tersebut dan mencoba memperbaiki  diri sendiri. "),
                opsiRespon = emptyList(),
                tampilanRespon = emptyList()
            )
            eventScreenData.add(pertanyaan19)

            val pertanyaan20 = EventScreening(
                pertanyaan = 20,
                eventRespon0 = listOf("Yeayy Hebat !! Menghormati dan memahami batasan pribadi adalah tanda kedewasaan dan kebijaksanaan. Teruslah menjaga integritasmu dan tetap fokus pada hal-hal yang memberikan dampak positif dalam hidupmu."),
                eventRespon1 = listOf("Saya mengerti bahwa terkadang, seseorang dapat merasa ingin mencari rangsangan secara sengaja. Namun, perlu diingat bahwa keseimbangan dalam konsumsi konten adalah kunci. Jika kamu merasa itu menjadi kebiasaan yang mengganggu atau merusak, pertimbangkan untuk mengalihkan perhatianmu pada kegiatan yang lebih positif dan produktif ya."),
                eventRespon2 = listOf("Mencari rangsangan secara sengaja bisa menjadi tanda adanya dorongan yang lebih kuat. Penting untuk diingat bahwa konten seksual dapat memiliki dampak pada kesejahteraan mental dan emosional. Jika kamu merasa perlu terus mencari rangsangan cobalah untuk selalu mencari alternatif lain supaya selalu produktif dalam hal positif dan pertimbangkan untuk mencari dukungan dari seseorang yang kamu percayai atau seorang profesional kesehatan mental. Mereka bisa membantu kamu menjaga keseimbangan dan kesejahteraan secara keseluruhan."),
                opsiRespon = emptyList(),
                tampilanRespon = emptyList()
            )
            eventScreenData.add(pertanyaan20)

            val pertanyaan21 = EventScreening(
                pertanyaan = 21,
                eventRespon0 = listOf("Tampaknya kamu memiliki kemampuan untuk menjaga fokus pada kegiatan yang positif dan sehat. Teruslah memprioritaskan aktivitas yang mendukung kesejahteraanmu."),
                eventRespon1 = listOf("Kadang-kadang, saat ingin melakukan hal tertentu, seperti menonton pornografi atau melakukan masturbasi, kita mungkin merasa ingin menyendiri. Ini adalah respons alami, namun ingatlah pentingnya menjaga keseimbangan dalam kegiatan dan hubungan sosial yang positif."),
                eventRespon2 = listOf("Jika kamu merasa sering mencari kesempatan untuk menyendiri demi menonton pornografi atau melakukan masturbasi, ini bisa menunjukkan adanya kebiasaan yang mempengaruhi rutinitas harian dan keseimbangan hidupmu. Cobalah mengidentifikasi situasi atau pikiran yang memicu keinginan ini, sehingga kamu dapat mengambil langkah preventif untuk mengurangi dorongan tersebut.  Bersama dengan itu, coba eksplorasi alternatif yang lebih sehat dan bermanfaat untuk mengisi waktu luangmu. Fokus pada hobi, olahraga, atau aktivitas sosial yang dapat meningkatkan kesejahteraanmu dan memberi pengalaman positif. Membuat jadwal harian atau rencana rutin juga dapat membantu mengalihkan perhatian dari kebiasaan yang tidak diinginkan."),
                opsiRespon = emptyList(),
                tampilanRespon = emptyList()
            )
            eventScreenData.add(pertanyaan21)

            val pertanyaan22 = EventScreening(
                pertanyaan = 22,
                eventRespon0 = listOf("Bagus, jika kamu tidak pernah melakukan masturbasi saat menonton pornografi. Penting untuk selalu mendengarkan dan menghormati batas-batasmu sendiri dalam hal ini. Setiap orang memiliki preferensi yang berbeda dan penting untuk membuat keputusan yang sesuai dengan kenyamanan dan nilai-nilai pribadimu."),
                eventRespon1 = listOf("Terkadang melakukan masturbasi saat menonton pornografi adalah pengalaman yang umum dan itu sudah termasuk ke dalam pengaruh menonton pornografi. Namun, perlu diingat untuk menjaga keseimbangan dan menghindari ketergantungan, kamu harus mengetahui kapan harus memberi diri istirahat dan melibatkan diri dalam aktivitas lain tentunya hal yang positif dapat membantu menjaga kesejahteraan fisik dan mentalmu."),
                eventRespon2 = listOf("Melakukan masturbasi saat menonton pornografi bisa menjadi hal yang sulit diatasi. Penting untuk mencari pendekatan yang sehat dalam mengatasi hal ini, seperti mencari alternatif kegiatan yang lebih bermanfaat. Menghargai usaha untuk mengelola dan menjaga kesejahteraanmu adalah langkah penting menuju keseimbangan yang lebih baik.\n"),
                opsiRespon = emptyList(),
                tampilanRespon = emptyList()
            )
            eventScreenData.add(pertanyaan22)

            val pertanyaan23 = EventScreening(
                pertanyaan = 23,
                eventRespon0 = listOf("Kamu memiliki pandangan yang sangat baik tentang dampak negatif yang bisa ditimbulkan oleh tontonan semacam itu. Tetaplah mempertahankan pandangan ini untuk menjaga kesehatan mental dan hubungan yang positif dalam hidupmu."),
                eventRespon1 = listOf("Terkadang dorongan atau kebiasaan bisa mengalahkan pengetahuan tentang dampak negatifnya. Namun, kamu sudah memiliki kesadaran akan risiko ini, dan itu langkah pertama yang baik menuju perubahan positif. Pertimbangkan untuk merencanakan strategi yang membantu mengatasi dorongan ini dan mengalihkannya ke kegiatan yang lebih sehat dan bermanfaat."),
                eventRespon2 = listOf("Jika kamu sering merasa ingin melanjutkan kebiasaan tersebut meskipun tahu risikonya, itu bisa menunjukkan betapa kuatnya dorongan tersebut. Namun, kamu memiliki kesadaran akan dampak negatifnya, dan itu adalah langkah penting. Kamu tidak perlu menghadapi ini sendirian. Pertimbangkan untuk mencari dukungan dari teman, keluarga, atau seorang profesional kesehatan mental yang bisa membantu kamu merencanakan cara menghadapi tantangan ini dan mengambil langkah menuju kesehatan mental yang lebih baik."),
                opsiRespon = emptyList(),
                tampilanRespon = emptyList()
            )
            eventScreenData.add(pertanyaan23)

            val pertanyaan24 = EventScreening(
                pertanyaan = 24,
                eventRespon0 = listOf("Bagus, tampaknya kamu memiliki kesadaran yang kuat akan dampak dan masalah yang terkait dengan tontonan semacam itu. Teruslah mempertahankan kesadaran ini untuk memastikan kamu menjalani hidup yang sehat dan positif."),
                eventRespon1 = listOf("Terkadang keinginan untuk melanjutkan tontonan semacam itu mungkin muncul meskipun ada masalah terkait. Namun, kamu sudah memiliki kesadaran akan dampaknya, dan itu langkah pertama yang baik menuju perubahan positif. Tetaplah berfokus pada keinginanmu untuk memperbaiki situasi dan mencari dukungan ketika kamu merasa kesulitan."),
                eventRespon2 = listOf("Jika kamu merasa sering ingin melanjutkan kebiasaan tersebut meskipun telah mengalami masalah, ini bisa menjadi pertanda adanya tantangan yang lebih dalam. Ingatlah bahwa kamu tidak sendirian dalam menghadapi hal ini. Mencari dukungan dari teman, keluarga, atau seorang profesional kesehatan mental bisa membantu kamu merancang strategi yang lebih kuat untuk mengatasi dorongan ini. Perubahan bisa sulit, tetapi dengan dukungan yang tepat, kamu bisa meraih keseimbangan dan kesejahteraan yang lebih baik."),
                opsiRespon = emptyList(),
                tampilanRespon = emptyList()
            )
            eventScreenData.add(pertanyaan24)

            val pertanyaan25 = EventScreening(
                pertanyaan = 25,
                eventRespon0 = listOf("Bagus, berarti kamu sudah berhasil buat ngontrol diri kamu supaya berenti nonton film porno. Lanjutkan ya, ini akan membawa kamu semakin lebih baik supaya terhindar dari kecanduan pornografi."),
                eventRespon1 = listOf("Jangan khawatir, karena memang ini membutuhkan waktu dan usaha yang lebih banyak. Terus coba untuk mengontrol diri kamu supaya bisa sampai berenti untuk menonton konten pornografi. "),
                eventRespon2 = listOf("Saya menghargai kejujuranmu dalam mengakui tantangan ini. Mengatasi kebiasaan atau kecanduan tidak selalu mudah, dan perjalanan ini bisa sulit. Pertama-tama, jangan terlalu keras pada dirimu sendiri. Kamu sedang berusaha melakukan perubahan yang positif, dan itu adalah langkah penting. Jika upaya sebelumnya belum berhasil, jangan berkecil hati dan teruslah untuk mencobanya lagi."),
                opsiRespon = emptyList(),
                tampilanRespon = emptyList()
            )
            eventScreenData.add(pertanyaan25)

        }
    }
}