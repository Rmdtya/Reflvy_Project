package com.example.reflvy

import UserViewModel
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.reflvy.data.NotifyChat
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

        Footer()


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

        val notifIcon : ImageView = includedLayout.findViewById(R.id.icon_notif)

        if (NotifyChat.notify){
            notifIcon.visibility = View.VISIBLE
        }else{
            notifIcon.visibility = View.INVISIBLE
        }
    }
}