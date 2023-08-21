package com.example.reflvy

import UserViewModel
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.reflvy.data.User
import com.example.reflvy.utils.ApplicationManager

class SettingsActivity : AppCompatActivity() {

    lateinit var applicationManager : ApplicationManager
    private lateinit var profile : ImageView
    private lateinit var userName : TextView
    private lateinit var userEmail : TextView
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val pengaturan: ImageView = findViewById(R.id.pengaturan)
        userName = findViewById(R.id.username)
        userEmail = findViewById(R.id.user_email)

        val bantuanIMG: ImageView = findViewById(R.id.bantuan)
        val hubungiKami: ImageView = findViewById(R.id.hubungi_kami)

        profile = findViewById(R.id.profile)

        profile.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }

        pengaturan.setOnClickListener {
            val intent = Intent(this, Pengaturan::class.java)
            startActivity(intent)
        }

        bantuanIMG.setOnClickListener {
            val intentBantuan = Intent(this, BantuanActivity::class.java)
            startActivity(intentBantuan)
        }

        hubungiKami.setOnClickListener {
            val intent = Intent(this, ChatUsActivity::class.java)
            startActivity(intent)
        }

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        // Observasi LiveData dan perbarui UI saat data berubah
        userViewModel.userLiveData.observe(this, { updatedUser ->
            userEmail.text = updatedUser.email
            userName.text = updatedUser.userName
        })

        // Load data pertama kali
        val sharedPreferences = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
        userViewModel.updateUserData(sharedPreferences)
    }

    override fun onBackPressed() {
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
        finish() // Optional: Menutup activity saat ini agar tidak kembali lagi dengan tombol kembali
    }
}