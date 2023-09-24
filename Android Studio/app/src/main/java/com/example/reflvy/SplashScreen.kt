package com.example.reflvy

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.example.reflvy.data.ActiveLogin
import com.example.reflvy.data.DataDaily
import com.example.reflvy.data.DataNotification
import com.example.reflvy.data.Music
import com.example.reflvy.data.News
import com.example.reflvy.data.NotifyChat
import com.example.reflvy.data.User
import com.example.reflvy.data.YoutubeVideo
import com.example.reflvy.utils.ApplicationManager
import com.example.reflvy.utils.MusicService
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.hide()

        val sharedPreferencesPengaturan = getSharedPreferences("PENGATURAN", Context.MODE_PRIVATE)
        val editor = sharedPreferencesPengaturan.edit()
        editor.putBoolean("hasIn", true)
        editor.apply()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getCurrentDnsServers(this)
        }

        ApplicationManager.instance.LoadDataLogin(this)

//        Toast.makeText(this, "tanggal kemarin" + ActiveLogin.infoActive.lastActive, Toast.LENGTH_SHORT).show()

        val currentDate = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val tglNow = dateFormat.format(currentDate).trim()

//        Toast.makeText(this, "tanggal sekarang" + tglNow, Toast.LENGTH_SHORT).show()

        if(ActiveLogin.infoActive.lastActive == tglNow){

        }else{
            ActiveLogin.infoActive.activeNow = false
            for (data in DataDaily.dataKegiatan) {
                data.proses = false
            }

            NotifyChat.notifChat.clear()
            ApplicationManager.instance.ClearNotifChat(this)
            ApplicationManager.instance.clearSharedPreferencesNotif(this)
            ApplicationManager.instance.UpdateStatusIfCompleted()
            ApplicationManager.instance.SaveKegiatan(this@SplashScreen)
        }


        val screeningSharedPreferences = getSharedPreferences("SCREENING_DATA", Context.MODE_PRIVATE)
        val isLoaded = screeningSharedPreferences.getBoolean("isLoaded", false)

        val isMusicServiceActive = MusicService.isServiceRunning(this)

        if (!isMusicServiceActive) {

            Music.playList.clear()
            News.newsList.clear()
            YoutubeVideo.videoList.clear()

            ApplicationManager.instance.loadNewsData()
            ApplicationManager.instance.loadMusic()
            ApplicationManager.instance.LoadVideoData()

            DataNotification.dataNotifikasi.clear()
            ApplicationManager.instance.LoadKegiatan(this)

            ApplicationManager.instance.LoadNotifChat(this)
            ApplicationManager.instance.LoadNotifPrefs(this)

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

                if (User.userData.screeningSatu){
                    ApplicationManager.instance.LoadDataMisiAplikasi(this)
                    val intent = Intent(this, MenuActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)

                }else{
                    val intent = Intent(this, ScreeningSatuActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                }

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

    fun getCurrentDnsServers(context: Context) {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val linkProperties = connectivityManager.getLinkProperties(network)

        val dnsServers = linkProperties?.dnsServers
        for (dnsServer in dnsServers!!) {
            Toast.makeText(this, dnsServer.toString(), Toast.LENGTH_SHORT).show()
        }
    }
}