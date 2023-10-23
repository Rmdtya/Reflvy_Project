package com.example.reflvy

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.reflvy.data.News
import com.example.reflvy.data.NotifyChat
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MenuInfoActivity : AppCompatActivity() {

    private val db = Firebase.firestore
    private lateinit var linearContainer: LinearLayout
    private lateinit var inflater: LayoutInflater

    private lateinit var rekomendasi : LinearLayout
    private lateinit var terbaru : LinearLayout
    private lateinit var terpopuler : LinearLayout
    private lateinit var terhangat : LinearLayout
    private lateinit var terurut : LinearLayout
    private lateinit var semua : LinearLayout


    companion object {
        private var instance: MenuInfoActivity? = null

        // Fungsi untuk mengambil instance MenuInfoActivity
        fun getInstance(): MenuInfoActivity {
            if (instance == null) {
                instance = MenuInfoActivity()
            }
            return instance!!
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_info)

        Footer()

        rekomendasi = findViewById(R.id.rekomendasi_layout)
        terbaru = findViewById(R.id.terbaru_layout)
        terpopuler = findViewById(R.id.terpopuler_layout)
        terhangat = findViewById(R.id.terhangat_layout)
        terurut = findViewById(R.id.terurut_layout)
        semua = findViewById(R.id.semua_layout)

        linearContainer = findViewById(R.id.list_berita)
        inflater = LayoutInflater.from(this)

        FilterInfo()



    ShowNews()

    }

    fun FilterInfo(){
        rekomendasi.setOnClickListener {

        }

        terbaru.setOnClickListener {

        }

        terpopuler.setOnClickListener {

        }

        terhangat.setOnClickListener {

        }

        terurut.setOnClickListener {

        }

        semua.setOnClickListener {

        }
    }

    fun ShowNews() {
        ClearNews()

        val numberOfTitles = News.newsList[0].title.size // Mengambil jumlah judul dari News.newsList

        for (i in 0 until numberOfTitles) {
            val template: View = inflater.inflate(R.layout.template_info2, null)
            linearContainer.addView(template)

            val image: ImageView = template.findViewById(R.id.img)
            val text: TextView = template.findViewById(R.id.title)
            val deskripsi: TextView = template.findViewById(R.id.deskripsi)

            // Ubah gambar dan teks di setiap template
            // Gunakan indeks (i) untuk mengakses elemen dengan indeks yang sama
            Glide.with(this)
                .load(News.newsList[0].img[i])
                .into(image)

            text.text = News.newsList[0].title[i]
            deskripsi.text = News.newsList[0].description[i]

            template.setOnClickListener {
                // Handle klik di sini, Anda dapat menggunakan indeks (i) untuk mengidentifikasi berita yang diklik
                handleTemplateClick(i, News.newsList[0].jenis[i])
            }
        }
    }

    private fun handleTemplateClick(index: Int, jenis : Int) {

        if(jenis == 1){
            val intent = Intent(this, NewsActivity::class.java)
            intent.putExtra("key", index)
            startActivity(intent)
        }else if(jenis == 2){
            val intent = Intent(this, NewsActivity2::class.java)
            intent.putExtra("key", index)
            startActivity(intent)
        }
    }

    fun ClearNews() {
        linearContainer.removeAllViews()
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

