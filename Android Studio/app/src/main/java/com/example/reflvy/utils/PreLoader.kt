package com.example.reflvy.utils

import android.app.AppOpsManager
import android.app.UiModeManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.reflvy.SplashScreen

class PreLoader : AppCompatActivity() {

    private val USAGE_STATS_REQUEST_CODE = 101
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferencesPengaturan = getSharedPreferences("PENGATURAN", Context.MODE_PRIVATE)
        var vpnFromPref = sharedPreferencesPengaturan.getBoolean("vpnMode", true)
        var hasIn = sharedPreferencesPengaturan.getBoolean("hasIn", false)


        if (!hasIn){
            val uiModeManager = getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
            if (uiModeManager.nightMode == UiModeManager.MODE_NIGHT_YES) {
                // Mode gelap aktif, atur aplikasi ke mode gelap
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                val editor = sharedPreferencesPengaturan.edit()
                editor.putBoolean("nightMode", true)
                editor.apply()

            } else {
                // Mode terang aktif atau otomatis, atur aplikasi ke mode terang
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

                val editor = sharedPreferencesPengaturan.edit()
                editor.putBoolean("nightMode", false)
                editor.apply()
            }

            val intent = Intent(this, SplashScreen::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }else{

            val intent = Intent(this, SplashScreen::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }


        if (vpnFromPref){
            if (!MyBackgroundService.isServiceRunning) {
                startService(Intent(this, MyBackgroundService::class.java))
            }
        }else{

        }






//        var handler = Handler(Looper.getMainLooper())
//        handler.postDelayed({
//
//            val sharedPreferences = getSharedPreferences("login_status", Context.MODE_PRIVATE)
//            val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
//            val isWelcome = sharedPreferences.getBoolean("isWelcome", false)
//
//            // Jika pengguna telah login sebelumnya, langsung beralih ke halaman utama atau halaman beranda
//            if (isLoggedIn) {
//                var sharedPreferences: SharedPreferences
//                sharedPreferences = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
//                User.userData.loadFromSharedPreferences(sharedPreferences)
//
//                val intent = Intent(this, SplashScreen::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                startActivity(intent)
//
//            } else {
//                if(!isWelcome){
//                    val intent = Intent(this, WelcomeScreenActivity::class.java)
//                    startActivity(intent)
//                }else{
//                    val intent = Intent(this, SigninActivity::class.java)
//                    startActivity(intent)
//                }
//            }
//            finish()
//        }, 200)
    }

    fun requestUsageStatsPermission() {
        val appOps = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            android.os.Process.myUid(),
            packageName
        )
        if (mode != AppOpsManager.MODE_ALLOWED) {
            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            startActivityForResult(intent, USAGE_STATS_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == USAGE_STATS_REQUEST_CODE) {
            val appOps = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            val mode = appOps.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(),
                packageName
            )
            if (mode == AppOpsManager.MODE_ALLOWED) {
                // Izin diberikan oleh pengguna
                // Lanjutkan dengan penggunaan "UsageStatsManager" di sini
            } else {
                // Izin tidak diberikan oleh pengguna
                // Tampilkan pesan atau lakukan tindakan yang sesuai
            }
        }
    }
}