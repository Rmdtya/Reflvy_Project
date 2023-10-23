package com.example.reflvy

import AppUsageStatisticsHelper
import android.app.AppOpsManager
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Process
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.reflvy.data.NotifyChat
import java.util.Calendar

class StatisticPenggunaanActivity : AppCompatActivity() {

    private lateinit var phoneActive : TextView


    private lateinit var containerScroll : LinearLayout
    private lateinit var inflater: LayoutInflater

    private val USAGE_STATS_REQUEST_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistic_penggunaan)

        containerScroll = findViewById(R.id.container_statistic)
        inflater = LayoutInflater.from(this)
        AppInfo.infoApp.clear()
        AppUsageInfo.infoUsage.clear()

        phoneActive = findViewById(R.id.text_totalactive)
        Footer()

        if (hasUsageStatsPermission()) {
            // Mengakses data penggunaan aplikasi dan waktu aktif layar
            val usageStatsHelper = AppUsageStatisticsHelper(this)
            val screenOnTime = usageStatsHelper.getScreenOnTime()
            // Tampilkan hasil ke dalam TextView
            phoneActive.text = "${convertMillisToDuration(screenOnTime)}"

//            val packageManager = packageManager
//            val installedApps = packageManager.getInstalledPackages(0)
//            for (packageInfo in installedApps) {
//                if (packageInfo.applicationInfo != null &&
//                    ((packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) == 0
//                            || (packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) == 1) &&
//                    packageManager.getLaunchIntentForPackage(packageInfo.packageName) != null) {
//
//                    // Mendapatkan ikon aplikasi
//                    val appIcon = packageManager.getApplicationIcon(packageInfo.applicationInfo)
//                    val usageStatsHelper = AppUsageStatisticsHelper(this)
//
//                    val appUsageTime = usageStatsHelper.getAppUsageTime(packageInfo.packageName) // Ganti dengan paket Instagram
//
//                    // Membuat objek AppInfo dan menambahkannya ke dalam list
//                    val appInfo = AppInfo(packageInfo.packageName, appIcon, appUsageTime)
//                    AppInfo.infoApp.add(appInfo)
//                }
//            }
//
//            ShowData()

            getAllInstalledAppsUsageInfoForToday(this)



        } else {
            // Jika izin belum diberikan, minta izin
            requestUsageStatsPermission()
        }

    }

    private fun ShowData(){

        val appInfoWithLongestDuration = AppInfo.infoApp.maxByOrNull { it.durasi }
        if (appInfoWithLongestDuration != null) {
            ShowDurasiTerbanyak(appInfoWithLongestDuration)
        }

        val sortedInfoApp = AppInfo.infoApp.sortedByDescending { it.durasi }

        if (sortedInfoApp.isNotEmpty()) {
            val filteredInfoApp = sortedInfoApp.drop(1)
            for (packageApp in filteredInfoApp) {
                ShowPackageTemplate(packageApp)
            }
        }
    }


    fun ShowDurasiTerbanyak(packageName : AppInfo){
        val imageIcon = findViewById<ImageView>(R.id.img_activeterbanyak)
        imageIcon.setImageDrawable(packageName.appIcon)

        val textDurasi = convertMillisToDuration(packageName.durasi)
        val text = findViewById<TextView>(R.id.text_activeterbanyak)
        text.text = textDurasi
    }


    fun ShowPackageTemplate(namaPackage : AppInfo){
        val template: View = inflater.inflate(R.layout.template_statistic, null)
        containerScroll.addView(template)

        val imageIcon: ImageView = template.findViewById(R.id.icon_image) // Menggunakan template.findViewById
        val packageName: TextView = template.findViewById(R.id.text_name)
        val durasi : TextView = template.findViewById(R.id.text_durasi)

        imageIcon.setImageDrawable(namaPackage.appIcon)

        val namePack = removeUnwantedWords(namaPackage.packageName)
        packageName.text = namePack

        durasi.text = "Total Durasi Aktif : ${formatDuration(namaPackage.durasi)}"
    }

    private fun hasUsageStatsPermission(): Boolean {
        val appOps = getSystemService(APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            Process.myUid(),
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

    fun removeUnwantedWords(input: String): String {
        val wordsToRemove = setOf("com", "google", "android", "apps", "example")
        val parts = input.split(".")
        val result = parts.filter { it !in wordsToRemove }.joinToString("")
        return result
    }

    fun requestUsageStatsPermission() {
        val appOps = getSystemService(APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            Process.myUid(),
            packageName
        )
        if (mode != AppOpsManager.MODE_ALLOWED) {
            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            startActivityForResult(intent, USAGE_STATS_REQUEST_CODE)
        }
    }


    private fun getUsageStatsList(context: Context): List<UsageStats> {
        val usageStatsManager =
            context.getSystemService(USAGE_STATS_SERVICE) as UsageStatsManager
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

    private fun getUsageStatsForToday(context: Context): List<UsageStats> {
        val usageStatsManager =
            context.getSystemService(USAGE_STATS_SERVICE) as UsageStatsManager
        val packageManager = context.packageManager
        val currentTime = System.currentTimeMillis()

        val startOfDay = getStartOfDay(currentTime) // Waktu mulai hari ini
        val endOfDay = currentTime // Waktu saat ini

        val installedAppsUsageStats = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY,
            startOfDay,
            endOfDay
        )

        return installedAppsUsageStats.filter { usageStats ->
            // Filter hanya untuk hari ini
            usageStats.totalTimeInForeground > 0
        }
    }

    private fun getStartOfDay(timeInMillis: Long): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeInMillis
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    private fun getAllInstalledAppsUsageInfoForToday(context: Context) {
        val installedAppsUsageStats = getUsageStatsForToday(context)
        val packageManager = context.packageManager

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
                val appIcon = getApplicationIconByPackageName(packageName, packageManager)
                val appUsageInfo = AppUsageInfo(packageName, totalTime, appIcon)
                AppUsageInfo.infoUsage.add(appUsageInfo)
            }
        }

        ShowList()
    }



    fun getApplicationIconByPackageName(packageName: String, packageManager: PackageManager): Drawable? {
        try {
            val icon = packageManager.getApplicationIcon(packageName)
            return icon
        } catch (ne: PackageManager.NameNotFoundException) {
            // Tangani pengecualian jika nama paket tidak ditemukan
        }
        return null
    }

    fun loadAppIcon(context: Context, packageName: String): Drawable? {
        try {
            val packageManager = context.packageManager
            val applicationInfo = packageManager.getApplicationInfo(packageName, 0)
            return Glide.with(context).load(applicationInfo.loadIcon(packageManager)).submit().get()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun convertMillisToDuration(millis: Long): String {
        val seconds = millis / 1000
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60

        val hourString = if (hours > 0) "$hours jam" else ""
        val minuteString = if (minutes > 0) "$minutes menit" else ""

        return when {
            hourString.isNotEmpty() && minuteString.isNotEmpty() -> "$hourString $minuteString"
            hourString.isNotEmpty() -> hourString
            minuteString.isNotEmpty() -> minuteString
            else -> "0 menit"
        }
    }

    private fun ShowList(){

        val appInfoWithLongestDuration = AppUsageInfo.infoUsage.maxByOrNull { it.usageTimeMillis }
        if (appInfoWithLongestDuration != null) {
            ShowDurasiListTerbanyak(appInfoWithLongestDuration)
        }

        val sortedInfoApp = AppUsageInfo.infoUsage.sortedByDescending { it.usageTimeMillis }

        if (sortedInfoApp.isNotEmpty()) {
            val filteredInfoApp = sortedInfoApp.drop(1)
            for (packageApp in filteredInfoApp) {
                ShowPackageListTemplate(packageApp)
            }

            println("help")
        }

    }


    fun ShowDurasiListTerbanyak(packageName : AppUsageInfo){
        val imageIcon = findViewById<ImageView>(R.id.img_activeterbanyak)

        try {
            val packageManager = packageManager
            val icon = getApplicationIconByPackageName("com.android.instagram", packageManager )
            imageIcon.setImageDrawable(icon)

        }catch (e : Exception){
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }

        val textDurasi = convertMillisToDuration(packageName.usageTimeMillis)
        val text = findViewById<TextView>(R.id.text_activeterbanyak)
        text.text = textDurasi

        val textname = findViewById<TextView>(R.id.text_namaapkterbanyak)
        textname.text = packageName.packageName
    }


    fun ShowPackageListTemplate(namaPackage : AppUsageInfo){
        val template: View = inflater.inflate(R.layout.template_statistic, null)
        containerScroll.addView(template)

        val imageIcon: ImageView = template.findViewById(R.id.icon_image) // Menggunakan template.findViewById
        val packageName: TextView = template.findViewById(R.id.text_name)
        val durasi : TextView = template.findViewById(R.id.text_durasi)

        imageIcon.setImageDrawable(namaPackage.appIcon)

        val namePack = namaPackage.packageName
        packageName.text = namePack

        durasi.text = "Total Durasi Aktif : ${formatDuration(namaPackage.usageTimeMillis)}"
    }

    private fun Footer(){
        val includedLayout = findViewById<View>(R.id.footer)
        val home: ImageView = includedLayout.findViewById(R.id.home_icon)
        home.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }

        val bot: ImageView = includedLayout.findViewById(R.id.bot_icon)
        bot.setOnClickListener {
            val intent = Intent(this, BotActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }

        val settings: ImageView = includedLayout.findViewById(R.id.setting_icon)
        settings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }

        val btn_back : ImageView = findViewById(R.id.icon_back)
        btn_back.setOnClickListener {
            onBackPressed()
        }

        val notifIcon : ImageView = includedLayout.findViewById(R.id.icon_notif)

        if (NotifyChat.notify){
            notifIcon.visibility = View.VISIBLE
        }else{
            notifIcon.visibility = View.INVISIBLE
        }
    }

}

data class AppInfo(
    val packageName: String,
    val appIcon: Drawable,
    val durasi : Long){

    companion object{
        val infoApp = mutableListOf<AppInfo>()
    }
}

data class AppUsageInfo(
    val packageName: String,
    val usageTimeMillis: Long,
    val appIcon: Drawable?
){
    companion object{
        val infoUsage = mutableListOf<AppUsageInfo>()
    }
}