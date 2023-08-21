package com.example.reflvy

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.reflvy.utils.VPNHandler
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.security.Timestamp
import java.util.Calendar
import java.util.Date

class MyBackgroundService : Service() {

    private val handler = Handler()
    private val delayInMillis: Long = 10000 // 5 detik (dalam milidetik)
    private val db = Firebase.firestore
    private var starting : Boolean = false

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
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Lakukan pekerjaan Anda di sini

        // Memulai layanan sebagai foreground service
        val channelId = "vpn_channel"
        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Reflvy Service")
            .setContentText("Service is running...")
            .setSmallIcon(R.drawable.reflvy_logo)
            .setSound(null)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSilent(true)
            .build()

        startForeground(1, notification)

        // Kembalikan konstanta START_STICKY agar layanan dimulai kembali jika dihentikan
        return START_STICKY
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Reflvy Service"
            val descriptionText = "Service is running.."
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("vpn_channel", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
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

        val currentTime = getCurrentDateTimeAsString()

        if (check01){
            startService(Intent(this, VPNHandler::class.java))

            if (!starting){
                starting = true

                val sharedPreferencesUser = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
                var userID = sharedPreferencesUser.getString("userId", "") ?: ""

                val userDocRef = db.collection("users").document(userID)

                userDocRef.get()
                    .addOnSuccessListener { documentSnapshot ->
                        if (documentSnapshot.exists()) {
                            val existingHistory = documentSnapshot.get("historyvpn") as? ArrayList<String>

                            if (existingHistory != null) {
                                existingHistory.add(currentTime)

                                // Update historyvpn field in Firestore
                                userDocRef.update("historyvpn", existingHistory)
                                    .addOnSuccessListener {
                                        // Berhasil memperbarui historyvpn
                                    }
                                    .addOnFailureListener {
                                        // Gagal memperbarui historyvpn
                                    }
                            } else {
                                // Jika existingHistory null, buat array baru dengan data pertama
                                val newHistory = arrayListOf(currentTime)

                                // Update historyvpn field in Firestore
                                userDocRef.update("historyvpn", newHistory)
                                    .addOnSuccessListener {
                                        // Berhasil memperbarui historyvpn
                                    }
                                    .addOnFailureListener {
                                        // Gagal memperbarui historyvpn
                                    }
                            }
                        }
                    }
                    .addOnFailureListener {
                        // Gagal mengambil dokumen
                        Toast.makeText(this, "gagal vpn", Toast.LENGTH_SHORT).show()
                    }
            }
        } else{
            stopService(Intent(this, VPNHandler::class.java))
            starting = false
        }
    }

    fun CekVPNStatus() : Boolean{
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.getNetworkInfo(ConnectivityManager.TYPE_VPN)!!.isConnectedOrConnecting
    }

    fun getCurrentDateTimeAsString(): String {
        val calendar = Calendar.getInstance()

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1 // Bulan dimulai dari 0 (Januari)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val second = calendar.get(Calendar.SECOND)

        return "$year-$month-$day $hour:$minute:$second"
    }

}