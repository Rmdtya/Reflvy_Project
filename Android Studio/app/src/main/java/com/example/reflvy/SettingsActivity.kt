package com.example.reflvy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.reflvy.data.User

class SettingsActivity : AppCompatActivity() {

    lateinit var applicationManager : ApplicationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val pengaturan: ImageView = findViewById(R.id.pengaturan)
        val userName: TextView = findViewById(R.id.username)
        val userEmail: TextView = findViewById(R.id.user_email)

        val bantuanIMG: ImageView = findViewById(R.id.bantuan)

        pengaturan.setOnClickListener {
            val intent = Intent(this, Pengaturan::class.java)
            startActivity(intent)
        }

        bantuanIMG.setOnClickListener {
            val intentBantuan = Intent(this, BantuanActivity::class.java)
            startActivity(intentBantuan)
        }


        // Ubah textview menjadi email dari ApplicationManager
        userEmail.setText(User.userData.email)
        userName.setText(User.userData.userID)

    }
}