package com.example.reflvy

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.reflvy.SplashScreen

class MyBackgroundService : Service() {

    private val handler = Handler()
    private val delayInMillis: Long = 10000 // 5 detik (dalam milidetik)

    // Memulai menjalankan fungsi berjalan setiap 5 detik
    private val runnable = object : Runnable {
        override fun run() {
            // Tempatkan fungsi yang ingin dijalankan secara terus-menerus di sini
            CheckVPN()

            // Jalankan ulang Runnable setelah 5 detik
            handler.postDelayed(this, delayInMillis)
        }
    }

    override fun onCreate() {
        super.onCreate()
        handler.postDelayed(runnable, delayInMillis)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
    }

    private fun CheckVPN() {

        var check01 = CekVPNStatus()

        if (check01){
            startService(Intent(this, VPNHandler::class.java))
        } else{
            stopService(Intent(this, VPNHandler::class.java))
        }
    }

    fun CekVPNStatus() : Boolean{
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.getNetworkInfo(ConnectivityManager.TYPE_VPN)!!.isConnectedOrConnecting
    }

}