package com.example.reflvy

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.example.reflvy.data.Music
import com.example.reflvy.data.News
import com.example.reflvy.data.User
import kotlinx.coroutines.delay

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.hide()

        val sharedPreferencesPengaturan = getSharedPreferences("PENGATURAN", Context.MODE_PRIVATE)
        var vpnFromPref = sharedPreferencesPengaturan.getBoolean("vpnMode", true)

        if (vpnFromPref){
            startService(Intent(this, MyBackgroundService::class.java))
        }else{

        }

//        val sharedPreferencesLoad = getSharedPreferences("LOAD_LAYOUT", Context.MODE_PRIVATE)
//        val isLoaded = sharedPreferencesLoad.getBoolean("isLoaded", false)
//
//        if(!isLoaded){
//            ApplicationManager.instance.loadNewsData()
//            ApplicationManager.instance.loadMusic()
//        }

        val isMusicServiceActive = MusicService.isServiceRunning(this)

        if (!isMusicServiceActive) {

            Music.playList.clear()
            News.newsList.clear()

            ApplicationManager.instance.loadNewsData()
            ApplicationManager.instance.loadMusic()
            ApplicationManager.instance.loadScreeningData(0)
        }

        //ApplicationManager.instance.OnClearData()

        var handler = Handler(Looper.getMainLooper())
        handler.postDelayed({

            val sharedPreferences = getSharedPreferences("login_status", Context.MODE_PRIVATE)
            val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

            // Jika pengguna telah login sebelumnya, langsung beralih ke halaman utama atau halaman beranda
            if (isLoggedIn) {

                var sharedPreferences: SharedPreferences
                sharedPreferences = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
                User.userData.loadFromSharedPreferences(sharedPreferences)

                val intent = Intent(this, MenuActivity::class.java)
                startActivity(intent)

            } else {
                val intent = Intent(this, SigninActivity::class.java)
                startActivity(intent)
            }
            finish()
        }, 3000)
    }
}