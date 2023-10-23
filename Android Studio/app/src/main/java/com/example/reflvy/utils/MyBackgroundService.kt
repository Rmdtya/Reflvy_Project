package com.example.reflvy.utils

import AppUsageStatisticsHelper
import android.app.AppOpsManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import com.example.reflvy.MainActivity
import com.example.reflvy.R
import com.example.reflvy.data.DataNotification
import com.example.reflvy.data.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Calendar
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class MyBackgroundService : Service() {
    companion object {
        var isServiceRunning = false
        private var totalActiveTime = 0L
        var isScreenActive : Boolean = false
        var userID : String = ""
    }

    private val handler = Handler()
    private val delayInMillis: Long = 10000 // 5 detik (dalam milidetik)
    private val db = Firebase.firestore
    private var starting : Boolean = false

    private val CHANNEL_ID_NOTIFIKASI = "NotifikasiKegiatan"
    private val NOTIFICATION_ID_NOTIFIKASI = 25

    private lateinit var powerManager : PowerManager


    // Memulai menjalankan fungsi berjalan setiap 5 detik
    private val runnable = object : Runnable {
        override fun run() {
            // Tempatkan fungsi yang ingin dijalankan secara terus-menerus di sinit
            try {
                CheckVPN()
            }catch (e:Exception){

            }

            // Jalankan ulang Runnable setelah 5 detik
            handler.postDelayed(this, delayInMillis)
        }
    }

    private val runnablePengawasan = object : Runnable {
        override fun run() {

            try {
                if (powerManager.isInteractive) {
                    isScreenActive = true

                    if (hasUsageStatsPermission()) {
                        // Mengakses data penggunaan aplikasi dan waktu aktif layar
                        val usageStatsHelper = AppUsageStatisticsHelper(this@MyBackgroundService)
                        val screenOnTime = usageStatsHelper.getScreenOnTime()
                        // Tampilkan hasil ke dalam TextView
                        val screenActive = "${formatDuration(screenOnTime)}"

                        val usageStatsList = getUsageStatsList(this@MyBackgroundService)
                        val currentApp = getCurrentApp(usageStatsList)
                        val text = "${currentApp?.packageName}"

                        getAllInstalledAppsUsageInfo(screenActive, text, this@MyBackgroundService)

                    } else {

                    }
                } else {
                    if(!isScreenActive){
                        println("Tidak Di Record")
                    }else{
                        isScreenActive = false

                        if (hasUsageStatsPermission()) {
                            // Mengakses data penggunaan aplikasi dan waktu aktif layar
                            val usageStatsHelper = AppUsageStatisticsHelper(this@MyBackgroundService)
                            val screenOnTime = usageStatsHelper.getScreenOnTime()
                            // Tampilkan hasil ke dalam TextView
                            val screenActive = "${formatDuration(screenOnTime)}"

                            getAllInstalledAppsUsageInfo(screenActive, "nonaktif", this@MyBackgroundService)

                        } else {

                        }
                    }
                }
            }catch (e:Exception){

            }

            // Jalankan ulang Runnable setelah 5 detik
            handler.postDelayed(this, 60000)
        }
    }

    private fun getAllInstalledAppsUsageInfo(screenActive: String, appActive: String, context: Context, ) {
        val usageStatsManager =
            context.getSystemService(USAGE_STATS_SERVICE) as UsageStatsManager
        val packageManager = context.packageManager
        val currentTime = System.currentTimeMillis()
        val installedAppsUsageStats = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_BEST,
            0,
            currentTime
        )

        val appUsageInfoMap = mutableMapOf<String, Long>()

        for (usageStats in installedAppsUsageStats) {
            val packageName = usageStats.packageName
            val totalTimeInForeground = usageStats.totalTimeInForeground

            // Update total waktu penggunaan untuk aplikasi dengan packageName
            appUsageInfoMap[packageName] = maxOf(appUsageInfoMap[packageName] ?: 0L, totalTimeInForeground)
        }

        for ((packageName, totalTime) in appUsageInfoMap) {
            // Tambahkan kondisi: hanya aplikasi yang berjalan minimal 1 menit yang dimasukkan
            if (totalTime >= 60 * 1000) { // 1 menit dalam milidetik
                val appUsageInfo = HistoryAppUsage(packageName, totalTime)
                HistoryAppUsage.historyApp.add(appUsageInfo)
            }
        }

        updateFirestore(screenActive, appActive, userID)
    }


    override fun onCreate() {
        super.onCreate()

        powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager

        var sharedPreferences: SharedPreferences = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
        User.userData.loadFromSharedPreferences(sharedPreferences)
        userID = User.userData.userID

        handler.postDelayed(runnable, delayInMillis)
        handler.postDelayed(runnablePengawasan, 60000)
        createNotificationChannel()
        CreateNotificationChannelKegiatan()
        isServiceRunning = true
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        // Memulai layanan sebagai foreground service
        val channelId = "vpn_channel"
        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Reflvy Service")
            .setContentText("Service is running...")
            .setSmallIcon(R.drawable.reflvy_64x)
            .setSound(null)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSilent(true)
            .build()

        startForeground(1, notification)


        DataNotification.dataNotifikasi.clear()
        ApplicationManager.instance.LoadNotifikasiKegiatan(this@MyBackgroundService)

        val executorService: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()

        val runnable = Runnable {
            val currentDate = Calendar.getInstance()
            val currentMinutes = (currentDate.get(Calendar.HOUR_OF_DAY) * 60) + currentDate.get(Calendar.MINUTE)
            val currentDayOfWeek = currentDate.get(Calendar.DAY_OF_WEEK)

            // Memeriksa dataKegiatan yang sudah dimuat dari SharedPreferences
            for (dataNotifikasi in DataNotification.dataNotifikasi) {
                // Memeriksa apakah hari saat ini aktif dalam dataNotifikasi
                val isCurrentDayActive = when (currentDayOfWeek) {
                    Calendar.SUNDAY -> dataNotifikasi.minggu
                    Calendar.MONDAY -> dataNotifikasi.senin
                    Calendar.TUESDAY -> dataNotifikasi.selasa
                    Calendar.WEDNESDAY -> dataNotifikasi.rabu
                    Calendar.THURSDAY -> dataNotifikasi.kamis
                    Calendar.FRIDAY -> dataNotifikasi.jumat
                    Calendar.SATURDAY -> dataNotifikasi.sabtu
                    else -> false
                }

                if (isCurrentDayActive && ((dataNotifikasi.startMinute >= currentMinutes && dataNotifikasi.startMinute <= currentMinutes + 3) || dataNotifikasi.startMinute == currentMinutes - 60)) {
                    // Data memiliki startMinute yang sama dengan menit jam saat ini
                    showNotification("Notifikasi Pengingat Jadwal", "Waktunya untuk: ${dataNotifikasi.namaKegiatan}")
                }
            }
        }

        executorService.scheduleAtFixedRate(runnable, 0, 59, TimeUnit.SECONDS)

        // Kembalikan konstanta START_STICKY agar layanan dimulai kembali jika dihentikan
        return START_STICKY
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Reflvy Services"
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
                        //Toast.makeText(this, "gagal vpn", Toast.LENGTH_SHORT).show()
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

    private fun showNotification(title: String, message: String) {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, NOTIFICATION_ID_NOTIFIKASI, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID_NOTIFIKASI)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.reflvy_64x)
            .setContentIntent(pendingIntent)
            .build()

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID_NOTIFIKASI, notification)
    }

    private fun CreateNotificationChannelKegiatan() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notifikasi Kegiatan"
            val descriptionText = "Kanal notifikasi untuk kegiatan"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID_NOTIFIKASI, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun hasUsageStatsPermission(): Boolean {
        val appOps = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            android.os.Process.myUid(),
            packageName
        )
        return mode == AppOpsManager.MODE_ALLOWED
    }

    private fun formatDuration(milliseconds: Long): String {
        val seconds = (milliseconds / 1000) % 60
        val minutes = (milliseconds / (1000 * 60)) % 60
        val hours = (milliseconds / (1000 * 60 * 60)) % 24

        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    private fun getUsageStatsList(context: Context): List<UsageStats> {
        val usageStatsManager =
            context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val currentTime = System.currentTimeMillis()
        val oneHourAgo = currentTime - (60 * 60 * 1000) // Interval waktu, contoh: 1 jam

        return usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY,
            oneHourAgo,
            currentTime
        )
    }

    private fun getCurrentApp(usageStatsList: List<UsageStats>): UsageStats? {
        var mostRecentUsageStats: UsageStats? = null
        val currentTime = System.currentTimeMillis()

        for (usageStats in usageStatsList) {
            if (mostRecentUsageStats == null || usageStats.lastTimeUsed > mostRecentUsageStats.lastTimeUsed) {
                if (usageStats.lastTimeUsed <= currentTime) {
                    mostRecentUsageStats = usageStats
                }
            }
        }

        return mostRecentUsageStats
    }

    fun updateFirestore(totalScreenActive : String, appActive : String, userID : String) {
        val docRef = db.collection("users").document(userID)

        val historyAppList = HistoryAppUsage.historyApp

        val packageNameList = historyAppList.map { it.packageName }
        val durasiList = historyAppList.map { it.durasi }

        // Menyiapkan data untuk update Firestore
        val updateData = mapOf(
            "activePhone" to totalScreenActive,
            "historyAplikasi" to packageNameList,
            "durasiAplikasi" to durasiList,
            "aplikasiAktif" to appActive
        )

        docRef.update(updateData)
            .addOnSuccessListener {
                // Data berhasil diupdate
                // Tambahkan kode yang sesuai di sini
            }
            .addOnFailureListener {
                // Terjadi kesalahan saat mengupdate data
                // Tambahkan kode penanganan kesalahan di sini
            }
    }

}

data class HistoryAppUsage(
    val packageName: String,
    val durasi: Long
){
    companion object{
        val historyApp = mutableListOf<HistoryAppUsage>()
    }
}

