package com.example.reflvy

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate

class Pengaturan : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var switchTheme: Switch
    private lateinit var switchVPN: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pengaturan)

        switchVPN = findViewById(R.id.switch1)
        switchTheme = findViewById(R.id.switch4)

        sharedPreferences = getSharedPreferences("PENGATURAN", Context.MODE_PRIVATE)

        // Cek mode saat memulai aktivitas dan atur status toggle berdasarkan mode saat itu
        val currentNightMode = AppCompatDelegate.getDefaultNightMode()
        switchTheme.isChecked = currentNightMode == AppCompatDelegate.MODE_NIGHT_YES


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
            recreate()
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


}