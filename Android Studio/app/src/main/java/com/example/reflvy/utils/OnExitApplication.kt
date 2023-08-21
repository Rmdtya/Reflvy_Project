package com.example.reflvy.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.reflvy.R
import com.example.reflvy.SplashScreen

class OnExitApplication : Service() {

    private val handler = Handler()
    private val delayInMillis: Long = 5000 // 5 detik (dalam milidetik)

    private val notificationManager: NotificationManagerCompat by lazy {
        NotificationManagerCompat.from(this)
    }

    private val notificationChannelId = "VPN_NOTIFICATION_CHANNEL_ID"
    private val notificationChannelName = "VPN Notification Channel"
    private val notificationId = 1 // ID unik untuk setiap notifikasi

    // Memulai menjalankan fungsi berjalan setiap 5 detik
    private val runnable = object : Runnable {
        override fun run() {
            // Tempatkan fungsi yang ingin dijalankan secara terus-menerus di sini
            myFunction()

            // Jalankan ulang Runnable setelah 5 detik
            handler.postDelayed(this, delayInMillis)
        }
    }

    override fun onCreate() {
        super.onCreate()
        startForeground(notificationId, createNotification())
        handler.postDelayed(runnable, delayInMillis)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return Service.START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
    }

    private fun myFunction() {
        // Implementasikan fungsi yang ingin dijalankan secara terus-menerus di latar belakang di sini
        // ...

        // Contoh: Tampilkan notifikasi setiap 5 detik
        val intent = Intent(this, SplashScreen::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(this, notificationChannelId)
            .setSmallIcon(R.drawable.reflvy_logo)
            .setContentTitle("VPN Terdeteksi")
            .setContentText("Yuk Isi Waktumu Dengan Kegiatan Yang Positif")
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                notificationChannelId,
                notificationChannelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(notificationId, builder.build())
    }

    private fun createNotification(): Notification {
        // Membuat notifikasi foreground agar layanan tetap berjalan bahkan ketika aplikasi tidak berjalan
        val notificationBuilder = NotificationCompat.Builder(this, notificationChannelId)
            .setContentTitle("VPN Handler")
            .setContentText("Layanan VPNHandler sedang berjalan...")
            .setSmallIcon(R.drawable.reflvy_logo)
            .setPriority(NotificationCompat.PRIORITY_LOW)

        return notificationBuilder.build()
    }
}