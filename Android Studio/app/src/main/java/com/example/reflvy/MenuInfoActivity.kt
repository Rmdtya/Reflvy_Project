package com.example.reflvy

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.reflvy.data.News
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.bumptech.glide.Glide

class MenuInfoActivity : AppCompatActivity() {

    private val db = Firebase.firestore
    private lateinit var linearContainer: LinearLayout
    private lateinit var inflater: LayoutInflater

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

        linearContainer = findViewById(R.id.list_berita)
        inflater = LayoutInflater.from(this)

    ShowNews()

    }

    fun ShowNews() {
        ClearNews()

        for (news in News.newsList) {
            val template: View = inflater.inflate(R.layout.template_info2, null)
            linearContainer.addView(template)

            val image: ImageView = template.findViewById(R.id.img)
            val text: TextView = template.findViewById(R.id.title)
            val deskripsi: TextView = template.findViewById(R.id.deskripsi)

            // Ubah gambar dan teks di setiap template
            Glide.with(this)
                .load(news.img)
                .into(image)

            text.text = news.title
            deskripsi.text = news.description

            template.setOnClickListener {
                handleTemplateClick(news.newsID)
            }
        }
    }

    private fun handleTemplateClick(index: Int) {
        val intent = Intent(this, NewsActivity::class.java)
        intent.putExtra("key", index)
        startActivity(intent)
    }

    fun ClearNews() {
        linearContainer.removeAllViews()
    }

}

