package com.crysure.reflvy

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.crysure.reflvy.data.ActiveLogin
import com.crysure.reflvy.data.DataDaily
import com.crysure.reflvy.data.DataNotification
import com.crysure.reflvy.data.GameStatusManager
import com.crysure.reflvy.data.Music
import com.crysure.reflvy.data.News
import com.crysure.reflvy.data.NotifyChat
import com.crysure.reflvy.data.User
import com.crysure.reflvy.data.YoutubeVideo
import com.crysure.reflvy.utils.ApplicationManager
import com.crysure.reflvy.utils.GameEventManager
import com.crysure.reflvy.utils.GameManager
import com.crysure.reflvy.utils.MusicService
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

        ApplicationManager.instance.LoadDataLogin(this)
        ApplicationManager.instance.LoadDataHistoryActivity(this)
        ApplicationManager.instance.LoadTotalHistoryActivity(this)


        val currentDate = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val tglNow = dateFormat.format(currentDate).trim()



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

            GameStatusManager.dragonStatus.LoadDataStatus(this)
            //GameManager.instance.updateEfekNow(this)

            GameStatusManager.dragonStatus.LoadDataPoint(this)
            GameManager.instance.LoadDataConditionStatus(this)

            GameEventManager.instance.LoadEventStatus(this)
        }

        if(ActiveLogin.infoActive.lastActive == tglNow){

        }else{
            ActiveLogin.infoActive.activeNow = false
            for (data in DataDaily.dataKegiatan) {
                data.proses = false
                data.tampilkanNotif = false
                data.terlewat = false
            }

            NotifyChat.notifChat.clear()
            ApplicationManager.instance.ClearNotifChat(this)
            ApplicationManager.instance.clearSharedPreferencesNotif(this)
            ApplicationManager.instance.UpdateStatusIfCompleted()
            ApplicationManager.instance.SaveKegiatan(this@SplashScreen)
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

                    GetNotifDataDaily()

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

    fun GetNotifDataDaily(){
        val dataHariIni = DataDaily.dataKegiatan.filter { dailyData ->
            val calendar = Calendar.getInstance()
            val currentDay = calendar.get(Calendar.DAY_OF_WEEK)

            when (currentDay) {
                Calendar.MONDAY -> dailyData.senin
                Calendar.TUESDAY -> dailyData.selasa
                Calendar.WEDNESDAY -> dailyData.rabu
                Calendar.THURSDAY -> dailyData.kamis
                Calendar.FRIDAY -> dailyData.jumat
                Calendar.SATURDAY -> dailyData.sabtu
                Calendar.SUNDAY -> dailyData.minggu
                else -> false
            }
        }

        for (dailyData in dataHariIni) {
            val calendar = Calendar.getInstance()
            val currentMinutes = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE)

            // Mengubah nilai tampilkanNotif jika endMinutes telah terlewat
            if (dailyData.endMinutes < currentMinutes) {
                if(!dailyData.proses){
                    if(!dailyData.tampilkanNotif){
                        dailyData.tampilkanNotif = true

                        val time = ConvertMinutesToTime(dailyData.endMinutes)

                        val chat = NotifyChat("Kamu Melewatkan Jadwal ${dailyData.namaKegiatan}", "bot", true, true, time, false, "dailyNotif", false, dailyData.dataNomor - 1)
                        NotifyChat.notifChat.add(chat)

                        ApplicationManager.instance.ActiveNotif(this)
                        ApplicationManager.instance.SaveNotifPrefs(this)
                    }
                }
                ApplicationManager.instance.SaveKegiatan(this)
            }
        }
    }

    fun ConvertMinutesToTime(minutes: Int): String {
        val hours = minutes / 60
        val minutesPart = minutes % 60
        return String.format("%02d:%02d", hours, minutesPart)
    }

}