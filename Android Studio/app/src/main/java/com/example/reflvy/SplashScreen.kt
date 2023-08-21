package com.example.reflvy

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.ui.Modifier
import com.example.reflvy.data.Music
import com.example.reflvy.data.News
import com.example.reflvy.data.User
import com.example.reflvy.utils.ApplicationManager

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.hide()

        val sharedPreferencesPengaturan = getSharedPreferences("PENGATURAN", Context.MODE_PRIVATE)
        var vpnFromPref = sharedPreferencesPengaturan.getBoolean("vpnMode", true)

        val nightModeFromPref = sharedPreferencesPengaturan.getBoolean("nightMode", false)
        if(nightModeFromPref){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{

        }

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

        val screeningSharedPreferences = getSharedPreferences("SCREENING_DATA", Context.MODE_PRIVATE)
        val isLoaded = screeningSharedPreferences.getBoolean("isLoaded", false)

        val isMusicServiceActive = MusicService.isServiceRunning(this)

        if (!isMusicServiceActive) {

            Music.playList.clear()
            News.newsList.clear()

            ApplicationManager.instance.loadNewsData()
            ApplicationManager.instance.loadMusic()

//            if(isLoaded){
//                Toast.makeText(this, "Sudah Terload", Toast.LENGTH_SHORT).show()
//
//                val sharedPreferencesLogin = getSharedPreferences("SCREENING_DATA", Context.MODE_PRIVATE)
//                val editorLogin = sharedPreferencesLogin.edit()
//                editorLogin.putBoolean("isLoaded", false)
//                editorLogin.apply()
//
//            }else{
//                Toast.makeText(this, "Belum Terload", Toast.LENGTH_SHORT).show()
//                ApplicationManager.instance.loadScreeningData(0)
//                ApplicationManager.instance.loadEventScreeningData(0)
//
//                ApplicationManager.instance.SaveDataScreening(this)
//
//                val sharedPreferencesLogin = getSharedPreferences("SCREENING_DATA", Context.MODE_PRIVATE)
//                val editorLogin = sharedPreferencesLogin.edit()
//                editorLogin.putBoolean("isLoaded", true)
//                editorLogin.apply()
//            }
        }

        //ApplicationManager.instance.OnClearData()

        var handler = Handler(Looper.getMainLooper())
        handler.postDelayed({

            val sharedPreferences = getSharedPreferences("login_status", Context.MODE_PRIVATE)
            val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
            val isWelcome = sharedPreferences.getBoolean("isWelcome", false)

            // Jika pengguna telah login sebelumnya, langsung beralih ke halaman utama atau halaman beranda
            if (isLoggedIn) {
                var sharedPreferences: SharedPreferences
                sharedPreferences = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
                User.userData.loadFromSharedPreferences(sharedPreferences)

                val intent = Intent(this, MenuActivity::class.java)
                startActivity(intent)

            } else {
                if(!isWelcome){
                    val intent = Intent(this, WelcomeScreenActivity::class.java)
                    startActivity(intent)
                }else{
                    val intent = Intent(this, SigninActivity::class.java)
                    startActivity(intent)
                }
            }
            finish()
        }, 3000)
    }
}