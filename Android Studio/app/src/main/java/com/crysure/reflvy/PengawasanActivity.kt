package com.crysure.reflvy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.crysure.reflvy.data.NotifyChat

class PengawasanActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pengawasan)

        Footer()

            val btnAnak = findViewById<LinearLayout>(R.id.btn_anak)
            btnAnak.setOnClickListener {
                UpdateRole(false)
                val intent = Intent(this, PengawasanAnakActivity::class.java)
                startActivity(intent)
                finishAffinity()
            }

            val btnOT = findViewById<LinearLayout>(R.id.btn_ot)
            btnOT.setOnClickListener {
                UpdateRole(true)
                val intent = Intent(this, PengawasanInputActivity::class.java)
                startActivity(intent)
                finishAffinity()
            }
        }

    private fun UpdateRole(role : Boolean){
        val sharedPreferences = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)

        val editor = sharedPreferences.edit()
        editor.putBoolean("pilihRole", true)
        editor.putBoolean("isOrangTua", role)
        editor.apply()
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