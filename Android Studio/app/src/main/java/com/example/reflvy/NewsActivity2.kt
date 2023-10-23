package com.example.reflvy

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.reflvy.data.News
import com.example.reflvy.data.NotifyChat
import com.github.chrisbanes.photoview.PhotoView

class NewsActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news2)

        Footer()

        val receivedData = intent.getIntExtra("key", 0)
//
//        val data = newsList[0].newsID
//
        ShowNews(receivedData)
    }

    fun ShowNews(indexNews: Int) {

        val image: PhotoView = findViewById(R.id.img_info)
        val title: TextView = findViewById(R.id.judul)
        val deskripsi: TextView = findViewById(R.id.deskripsi)

        // Ubah gambar dan teks di setiap template
        Glide.with(this)
            .load(News.newsList[0].img[indexNews])
            .into(image)

        image.setOnPhotoTapListener { view, x, y ->
            // Code yang dijalankan saat pengguna melakukan tap pada gambar
        }

        // Atau Anda dapat mengaktifkan fitur zoom dengan double tap
        image.setOnViewTapListener { view, x, y ->
            // Code yang dijalankan saat pengguna melakukan double tap pada gambar
        }


//        title.text = News.newsList[0].title[indexNews]
//
//        deskripsi.text = News.newsList[0].paragraphs[indexNews]

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

        val notifIcon : ImageView = includedLayout.findViewById(R.id.icon_notif)

        if (NotifyChat.notify){
            notifIcon.visibility = View.VISIBLE
        }else{
            notifIcon.visibility = View.INVISIBLE
        }
    }
}