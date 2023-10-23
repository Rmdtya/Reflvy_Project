package com.example.reflvy

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.reflvy.data.NotifyChat
import com.example.reflvy.data.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PengawasanStatisticAnakActivity : AppCompatActivity() {

    private val db = Firebase.firestore
    private lateinit var containerScroll : LinearLayout
    private lateinit var inflater: LayoutInflater

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pengawasan_statistic_anak)

        HistoryPenggunaan.historyApp.clear()
        containerScroll = findViewById(R.id.container_statistic)
        inflater = LayoutInflater.from(this)

        Footer()

        val userDocRef = db.collection("users").document(User.userData.reveralCodeAnak)
        userDocRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val historyAplikasiList = documentSnapshot.get("historyAplikasi") as? List<String>
                    val durasiList = documentSnapshot.get("durasiAplikasi") as? List<Long>
                    val totalActivity = documentSnapshot.getString("activePhone") ?: ""
                    val aplikasiAktif = documentSnapshot.getString("aplikasiAktif") ?: ""

                    // Pastikan kedua list tidak null dan memiliki ukuran yang sama
                    if (historyAplikasiList != null && durasiList != null && historyAplikasiList.size == durasiList.size) {
                        for (i in historyAplikasiList.indices) {
                            val packageName = historyAplikasiList[i]
                            val durasi = durasiList[i]

                            // Buat objek HistoryAppUsage dan tambahkan ke dalam list
                            val historyAppUsage = HistoryPenggunaan(packageName, durasi)
                            HistoryPenggunaan.historyApp.add(historyAppUsage)
                        }
                    }

                    ShowPackageListTemplate()
                    ShowTotalAndAplikasi(totalActivity, aplikasiAktif)

                } else {
                    // Dokumen tidak ditemukan
                }
            }
            .addOnFailureListener { exception ->
                // Tangani kesalahan saat mengambil data
            }

    }

    fun ShowTotalAndAplikasi(total : String, aktif : String){
        val textTotal = findViewById<TextView>(R.id.text_totalactive)
        val textAplikasi = findViewById<TextView>(R.id.text_application)

        val totalText = formatWaktu(total)

        textTotal.text = totalText
        textAplikasi.text = aktif
    }

    fun ShowPackageListTemplate(){
        val packageManager = packageManager

        for (aplikasi in HistoryPenggunaan.historyApp){
            val template: View = inflater.inflate(R.layout.template_statistic, null)
            containerScroll.addView(template)

            val imageIcon: ImageView = template.findViewById(R.id.icon_image) // Menggunakan template.findViewById
            val packageName: TextView = template.findViewById(R.id.text_name)
            val durasi : TextView = template.findViewById(R.id.text_durasi)


            val appIcon = getApplicationIconByPackageName(aplikasi.packageName, packageManager)

            imageIcon.setImageDrawable(appIcon)

            val namePack = aplikasi.packageName
            packageName.text = namePack

            durasi.text = "Total Durasi Aktif : ${formatDuration(aplikasi.durasi)}"
        }

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

    private fun formatDuration(milliseconds: Long): String {
        val seconds = (milliseconds / 1000) % 60
        val minutes = (milliseconds / (1000 * 60)) % 60
        val hours = (milliseconds / (1000 * 60 * 60)) % 24

        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    fun formatWaktu(waktu: String): String {
        val parts = waktu.split(":")
        if (parts.size == 3) {
            val jam = parts[0].toIntOrNull() ?: 0
            val menit = parts[1].toIntOrNull() ?: 0
            val detik = parts[2].toIntOrNull() ?: 0

            val jamText = if (jam == 1) "jam" else "jam"
            val menitText = if (menit == 1) "menit" else "menit"

            val hasilFormat = StringBuilder()
            if (jam > 0) {
                hasilFormat.append("$jam $jamText ")
            }
            if (menit > 0) {
                hasilFormat.append("$menit $menitText")
            }

            return hasilFormat.toString().trim()
        }

        return "Format waktu tidak valid"
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

        val notifIcon : ImageView = includedLayout.findViewById(R.id.icon_notif)

        if (NotifyChat.notify){
            notifIcon.visibility = View.VISIBLE
        }else{
            notifIcon.visibility = View.INVISIBLE
        }
    }

}

data class HistoryPenggunaan(
    val packageName: String,
    val durasi: Long
){
    companion object{
        val historyApp = mutableListOf<HistoryPenggunaan>()
    }
}