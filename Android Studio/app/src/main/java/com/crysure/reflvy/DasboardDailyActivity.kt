package com.crysure.reflvy

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.crysure.reflvy.data.ActiveLogin
import com.crysure.reflvy.data.DataDaily
import com.crysure.reflvy.data.NotifyChat
import java.util.Calendar

class DasboardDailyActivity : AppCompatActivity() {

    private lateinit var containerBelum : LinearLayout
    private lateinit var containerSudah : LinearLayout
    private lateinit var inflater: LayoutInflater
    private lateinit var textMood : TextView
    private lateinit var dashboardList : LinearLayout

    private lateinit var btn_all : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dasboard_daily)

        Footer()

        btn_all = findViewById(R.id.btn_all)
        inflater = LayoutInflater.from(this)
        containerBelum = findViewById(R.id.container_belum)
        containerSudah = findViewById(R.id.container_sudah)
        textMood = findViewById(R.id.mood_text)
        dashboardList = findViewById(R.id.btn_dashboard)

        textMood.text = ActiveLogin.infoActive.moodNow

        SetImageMood(ActiveLogin.infoActive.moodNow)

        ShowDailyActivity()

        btn_all.setOnClickListener {
            val intent = Intent(this, OldDailyActivity::class.java)
            startActivity(intent)
        }

        dashboardList.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }
    }

    fun ShowDailyActivity(){

        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        when (dayOfWeek) {
            Calendar.MONDAY -> {

                val kegiatanSenin = DataDaily.dataKegiatan.filter { it.senin }
                // Tampilkan data kegiatan yang memiliki senin = true
                kegiatanSenin.forEach { data ->
                    if(data.status == "belum"){
                        // Lakukan sesuatu dengan data, misalnya mencetaknya
                        if(!data.proses){
                            ShowDailyNotClear(data.namaKegiatan, data.kategori, data.lamaKegiatan, data.progresNow)
                        }else if(data.proses){
                            ShowDailyClear(data.namaKegiatan, data.kategori, data.lamaKegiatan, data.progresNow)
                        }
                    }
                }

            }
            Calendar.TUESDAY -> {

                val kegiatanSenin = DataDaily.dataKegiatan.filter { it.selasa }
                // Tampilkan data kegiatan yang memiliki senin = true
                kegiatanSenin.forEach { data ->
                    // Lakukan sesuatu dengan data, misalnya mencetaknya
                    if(data.status == "belum"){
                        // Lakukan sesuatu dengan data, misalnya mencetaknya
                        if(!data.proses){
                            ShowDailyNotClear(data.namaKegiatan, data.kategori, data.lamaKegiatan, data.progresNow)
                        }else if(data.proses){
                            ShowDailyClear(data.namaKegiatan, data.kategori, data.lamaKegiatan, data.progresNow)
                        }
                    }
                }

            }
            Calendar.WEDNESDAY -> {

                val kegiatanSenin = DataDaily.dataKegiatan.filter { it.rabu }
                // Tampilkan data kegiatan yang memiliki senin = true
                kegiatanSenin.forEach { data ->
                    // Lakukan sesuatu dengan data, misalnya mencetaknya
                    if(data.status == "belum"){
                        // Lakukan sesuatu dengan data, misalnya mencetaknya
                        if(!data.proses){
                            ShowDailyNotClear(data.namaKegiatan, data.kategori, data.lamaKegiatan, data.progresNow)
                        }else if(data.proses){
                            ShowDailyClear(data.namaKegiatan, data.kategori, data.lamaKegiatan, data.progresNow)
                        }
                    }
                }

            }
            Calendar.THURSDAY -> {

                val kegiatanSenin = DataDaily.dataKegiatan.filter { it.kamis }
                // Tampilkan data kegiatan yang memiliki senin = true
                kegiatanSenin.forEach { data ->
                    // Lakukan sesuatu dengan data, misalnya mencetaknya
                    if(data.status == "belum"){
                        // Lakukan sesuatu dengan data, misalnya mencetaknya
                        if(!data.proses){
                            ShowDailyNotClear(data.namaKegiatan, data.kategori, data.lamaKegiatan, data.progresNow)
                        }else if(data.proses){
                            ShowDailyClear(data.namaKegiatan, data.kategori, data.lamaKegiatan, data.progresNow)
                        }
                    }
                }

            }

            Calendar.FRIDAY -> {

            val kegiatanSenin = DataDaily.dataKegiatan.filter { it.jumat }
            // Tampilkan data kegiatan yang memiliki senin = true
            kegiatanSenin.forEach { data ->
                // Lakukan sesuatu dengan data, misalnya mencetaknya
                if(data.status == "belum"){
                    // Lakukan sesuatu dengan data, misalnya mencetaknya
                    if(!data.proses){
                        ShowDailyNotClear(data.namaKegiatan, data.kategori, data.lamaKegiatan, data.progresNow)
                    }else if(data.proses){
                        ShowDailyClear(data.namaKegiatan, data.kategori, data.lamaKegiatan, data.progresNow)
                    }
                }
            }

        }

            Calendar.SATURDAY ->{

            val kegiatanSenin = DataDaily.dataKegiatan.filter { it.sabtu }
            // Tampilkan data kegiatan yang memiliki senin = true
            kegiatanSenin.forEach { data ->
                // Lakukan sesuatu dengan data, misalnya mencetaknya
                if(data.status == "belum"){
                    // Lakukan sesuatu dengan data, misalnya mencetaknya
                    if(!data.proses){
                        ShowDailyNotClear(data.namaKegiatan, data.kategori, data.lamaKegiatan, data.progresNow)
                    }else if(data.proses){
                        ShowDailyClear(data.namaKegiatan, data.kategori, data.lamaKegiatan, data.progresNow)
                    }
                }
            }

        }

            Calendar.SUNDAY -> {

            val kegiatanSenin = DataDaily.dataKegiatan.filter { it.minggu }
            // Tampilkan data kegiatan yang memiliki senin = true
            kegiatanSenin.forEach { data ->
                // Lakukan sesuatu dengan data, misalnya mencetaknya
                if(data.status == "belum"){
                    // Lakukan sesuatu dengan data, misalnya mencetaknya
                    if(!data.proses){
                        ShowDailyNotClear(data.namaKegiatan, data.kategori, data.lamaKegiatan, data.progresNow)
                    }else if(data.proses){
                        ShowDailyClear(data.namaKegiatan, data.kategori, data.lamaKegiatan, data.progresNow)
                    }
                }
            }

        }
            else -> {
                // Hari lainnya (Jumat, Sabtu, Minggu)
                // Tambahkan kode yang ingin Anda jalankan untuk hari-hari lainnya di sini
            }
        }
    }

    fun ShowDailyClear(namaKegiatan: String, kategori: String, lama: Int, progres: Int) {
        val template: View = inflater.inflate(R.layout.template_activitydashboard, null)
        containerSudah.addView(template)

        val progressBar: ProgressBar = template.findViewById(R.id.bar) // Menggunakan template.findViewById
        val textName: TextView = template.findViewById(R.id.text_name)
        val textTime: TextView = template.findViewById(R.id.text_duration)

        val imageicon : ImageView = template.findViewById(R.id.image_icon)
        SetImageCategory(kategori, imageicon)

        progressBar.progress = progres
        progressBar.max = lama




        textName.text = namaKegiatan
        textTime.text = progres.toString() + " / " + lama.toString() + " Hari"
    }

    fun ShowDailyNotClear(namaKegiatan : String, kategori : String, lama : Int, progres : Int){
        val template: View = inflater.inflate(R.layout.template_activitydashboard, null)
        containerBelum.addView(template)

        val progressBar: ProgressBar = template.findViewById(R.id.bar) // Ganti dengan ID yang sesuai
        val textName : TextView = template.findViewById(R.id.text_name)
        val textTime : TextView = template.findViewById(R.id.text_duration)

//        progressBar.max = lama // Set nilai maksimum (max)
//        progressBar.progress = 2 // Set nilai progress

        val imageicon : ImageView = template.findViewById(R.id.image_icon)
        SetImageCategory(kategori, imageicon)

        progressBar.progress = progres
        progressBar.max = lama


        textName.text = namaKegiatan
        textTime.text = progres.toString() + " / " + lama.toString() + " Hari"
    }

    private fun SetImageMood(mood : String){

        val moodImage : ImageView = findViewById(R.id.img_mood)

        if(mood == "Gelisah"){
            moodImage.setImageResource(R.drawable.mood_gelisah)
        }else if(mood == "Marah"){
            moodImage.setImageResource(R.drawable.mood_marah)
        }else if(mood == "Sedih"){
            moodImage.setImageResource(R.drawable.mood_sedih)
        }else if(mood == "Kecewa"){
            moodImage.setImageResource(R.drawable.mood_kecewa)
        }else if(mood == "Bosan"){
            moodImage.setImageResource(R.drawable.mood_bosan)
        }else if(mood == "Seperti Biasa"){
            moodImage.setImageResource(R.drawable.mood_netral)
        }else if(mood == "Senang"){
            moodImage.setImageResource(R.drawable.mood_senang)
        }else if(mood == "Bersemangat"){
            moodImage.setImageResource(R.drawable.mood_bersemangat)
        }else if(mood == "Riang"){
            moodImage.setImageResource(R.drawable.mood_riang)
        }else if(mood == "Bahagia"){
            moodImage.setImageResource(R.drawable.mood_bahagia)
        }else{
            Toast.makeText(this, "error image", Toast.LENGTH_SHORT).show()
        }
    }

    private fun SetImageCategory(kategori : String, img : ImageView){
        if (kategori == "bekerja"){
            img.setImageResource(R.drawable.dailyicom_01bekerja)
        }else if (kategori == "belajar formal"){
            img.setImageResource(R.drawable.dailyicom_02belajarformal)
        }else if (kategori == "membaca"){
            img.setImageResource(R.drawable.dailyicom_03membaca)
        }else if (kategori == "bersantai"){
            img.setImageResource(R.drawable.dailyicom_04bersantai)
        }else if (kategori == "istirahat"){
            img.setImageResource(R.drawable.dailyicom_05istirahat)
        }else if (kategori == "belanja"){
            img.setImageResource(R.drawable.dailyicom_06belanja)
        }else if (kategori == "bermusik"){
            img.setImageResource(R.drawable.dailyicom_07bermusik)
        }else if (kategori == "beribadah"){
            img.setImageResource(R.drawable.dailyicom_08beribadah)
        }else if (kategori == "bermain game"){
            img.setImageResource(R.drawable.dailyicom_09bermaingame)
        }else if (kategori == "akses handphone"){
            img.setImageResource(R.drawable.dailyicom_10hiburandigital)
        }else if (kategori == "operasi komputer"){
            img.setImageResource(R.drawable.dailyicom_11operasikomputer)
        }else if (kategori == "pekerjaan rumah"){
            img.setImageResource(R.drawable.dailyicom_12pekerjaanrumah)
        }else if (kategori == "komunitas"){
            img.setImageResource(R.drawable.dailyicom_13komunitas)
        }else if (kategori == "bersosialisasi"){
            img.setImageResource(R.drawable.dailyicom_14bersosialisasi)
        }else if (kategori == "healing"){
            img.setImageResource(R.drawable.dailyicom_15healing)
        }else if (kategori == "olahraga"){
            img.setImageResource(R.drawable.dailyicom_16olahraga)
        }else if (kategori == "liburan"){
            img.setImageResource(R.drawable.dailyicom_17liburan)
        }else{
            img.setImageResource(R.drawable.dailyicon_lainnya)
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, MenuActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }

    private fun Footer(){
        val includedLayout = findViewById<View>(R.id.footer)
        val home: ImageView = includedLayout.findViewById(R.id.home_icon)
        home.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }

        val bot: ImageView = includedLayout.findViewById(R.id.bot_icon)
        bot.setOnClickListener {
            val intent = Intent(this, BotActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }

        val settings: ImageView = includedLayout.findViewById(R.id.setting_icon)
        settings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }

        val btn_back : ImageView = findViewById(R.id.icon_back)
        btn_back.setOnClickListener {
            onBackPressed()
        }

        val notifIcon : ImageView = includedLayout.findViewById(R.id.icon_notif)

        if (NotifyChat.notify){
            notifIcon.visibility = View.VISIBLE
        }else{
            notifIcon.visibility = View.INVISIBLE
        }
    }
}