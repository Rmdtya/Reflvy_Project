package com.example.reflvy

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import kotlin.math.log

class MenuActivity : AppCompatActivity() {

    private var backPressedTime: Long = 0
    private val backPressedInterval = 2000 // Interval waktu dalam milidetik untuk menekan tombol back dua kali
    private val MENU_INFO_REQUEST_CODE = 1001

    override fun onBackPressed() {
        if (backPressedTime + backPressedInterval > System.currentTimeMillis()) {
            super.onBackPressed()
            ApplicationManager.instance.ExitApplication()
            finish()
        } else {
            Toast.makeText(this, "Tekan  sekali lagi untuk keluar", Toast.LENGTH_SHORT).show()
        }
        backPressedTime = System.currentTimeMillis()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val textScreenning : TextView = findViewById(R.id.text_menu6_2)

//        val vpnHandler = VPNHandler.getInstance(this) // 'this' adalah konteks dari Activity
//        vpnHandler.startRepeatedFunction()

        val menu2: ImageView = findViewById(R.id.menu2)
        menu2.setOnClickListener {
            val intent = Intent(this, DailyActivity::class.java)
            startActivity(intent)
        }

        val menu3: ImageView = findViewById(R.id.menu3)
        menu3.setOnClickListener {
            val intent = Intent(this, MenuInfoActivity::class.java)
            startActivity(intent)
        }

        val menu5: ImageView = findViewById(R.id.menu5)
        menu5.setOnClickListener {
            val intent = Intent(this, PlaylistActivity::class.java)
            startActivity(intent)
        }

        val settings: ImageView = findViewById(R.id.setting_icon)
        settings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        val logout: ImageView = findViewById(R.id.logout_icon)
        logout.setOnClickListener {
            finishAffinity()
        }

        textScreenning.setOnClickListener{
            val intent = Intent(this, ScreeningActivity::class.java)
            startActivity(intent)
        }
    }

//    fun myFunction() {
////        val intent = Intent(this, HandlerLayout::class.java)
////        startActivity(intent)
//
//        val spDataNews = getSharedPreferences("dataNews_Load", Context.MODE_PRIVATE)
//        val editorDataNews = spDataNews.edit()
//        editorDataNews.putBoolean("isDataNews", false)
//        editorDataNews.apply()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        // Panggil fungsi yang ingin dijalankan saat aplikasi ditutup
//        myFunction()
//    }


}