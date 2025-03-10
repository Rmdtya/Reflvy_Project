package com.crysure.reflvy

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.crysure.reflvy.data.NotifyChat
import com.crysure.reflvy.utils.MyBackgroundService

class Pengaturan : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var switchTheme: Switch
    private lateinit var switchVPN: Switch

    private lateinit var switch2 : Switch
    private lateinit var switch3 : Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pengaturan)

        Footer()

        switchVPN = findViewById(R.id.switch1)
        switchTheme = findViewById(R.id.switch4)

        switch2 = findViewById(R.id.switch2)
        switch3 = findViewById(R.id.switch3)

        switch2.isChecked = true
        switch3.isChecked = true

        sharedPreferences = getSharedPreferences("PENGATURAN", Context.MODE_PRIVATE)

        // Atur status toogle berdasarkan mode saat itu
        val nightModeFromPref = sharedPreferences.getBoolean("nightMode", false)
        switchTheme.isChecked = nightModeFromPref

        val vpnFromPref = sharedPreferences.getBoolean("vpnMode", true)
        switchVPN.isChecked = vpnFromPref

        // Set switch listener
        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Switch is checked, set theme to dark (MODE_NIGHT_YES)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                // Switch is unchecked, set theme to light (MODE_NIGHT_NO)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            // Simpan preferensi pengguna tentang mode tema
            val editor = sharedPreferences.edit()
            editor.putBoolean("nightMode", isChecked)
            editor.apply()


            // Re-create the activity to apply the new theme
        }

        switchVPN.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                startService(Intent(this, MyBackgroundService::class.java))
            } else{
                stopService(Intent(this, MyBackgroundService::class.java))
            }
            val editor = sharedPreferences.edit()
            editor.putBoolean("vpnMode", isChecked)
            editor.apply()
        }

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