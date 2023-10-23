package com.example.reflvy

import UserViewModel
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.reflvy.data.NotifyChat
import com.example.reflvy.data.User

class SettingsActivity : AppCompatActivity() {

    private lateinit var userViewModel: UserViewModel
    private val USAGE_STATS_REQUEST_CODE = 101


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        Footer()

        val userName = findViewById<TextView>(R.id.text_username)
        val userEmail = findViewById<TextView>(R.id.text_email)

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        userViewModel.userLiveData.observe(this) { updatedUser ->
            userEmail.text = updatedUser.email
            userName.text = updatedUser.userName
        }

        // Load data pertama kali
        val sharedPreferences = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
        userViewModel.updateUserData(sharedPreferences)

        val profile = findViewById<TextView>(R.id.btn_edit)
        profile.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }

        val pengaturan= findViewById<ImageView>(R.id.icon_pengaturan)
        pengaturan.setOnClickListener {
            val intent = Intent(this, Pengaturan::class.java)
            startActivity(intent)
        }

        val bantuan = findViewById<ImageView>(R.id.icon_bantuan)
        bantuan.setOnClickListener {
            val intent = Intent(this, BantuanActivity::class.java)
            startActivity(intent)
        }

        val statistic = findViewById<ImageView>(R.id.icon_statistik)
        statistic.setOnClickListener {
            val intent = Intent(this, StatisticPenggunaanActivity::class.java)
            startActivity(intent)
        }


        val pengawasanOT = findViewById<ImageView>(R.id.icon_pengawasan)
        pengawasanOT.setOnClickListener {
            if (hasUsageStatsPermission()){
                if(User.userData.piliRole){
                    if (User.userData.isOrangTua){
                        if (User.userData.jumlahReveral > 0){
                            val intent = Intent(this, PengawasanOrangTuaActivity::class.java)
                            startActivity(intent)
                        }else{
                            val intent = Intent(this, PengawasanInputActivity::class.java)
                            startActivity(intent)
                        }
                    }else{
                        val intent = Intent(this, PengawasanAnakActivity::class.java)
                        startActivity(intent)
                    }
                }else{
                    val intent = Intent(this, PengawasanActivity::class.java)
                    startActivity(intent)
                }
            }else{
                requestUsageStatsPermission()
            }

        }

        val tentangKami = findViewById<ImageView>(R.id.icon_tentangkami)
        tentangKami.setOnClickListener {

        }

        val hubungiKami = findViewById<ImageView>(R.id.icon_hubungikami)
        hubungiKami.setOnClickListener {
            val intent = Intent(this, ChatUsActivity::class.java)
            startActivity(intent)
        }

        val kebijakanPrivasi = findViewById<ImageView>(R.id.icon_kebijakan)
        kebijakanPrivasi.setOnClickListener {

        }

        val syaratKetentuan = findViewById<ImageView>(R.id.icon_syarat)
        syaratKetentuan.setOnClickListener {

        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, MenuActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
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

//        val settings: ImageView = includedLayout.findViewById(R.id.setting_icon)
//        settings.setOnClickListener {
//            val intent = Intent(this, SettingsActivity::class.java)
//            startActivity(intent)
//            finishAffinity()
//        }

        val notifIcon : ImageView = includedLayout.findViewById(R.id.icon_notif)

        if (NotifyChat.notify){
            notifIcon.visibility = View.VISIBLE
        }else{
            notifIcon.visibility = View.INVISIBLE
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
}