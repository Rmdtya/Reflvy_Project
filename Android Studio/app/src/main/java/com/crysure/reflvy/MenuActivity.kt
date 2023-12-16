package com.crysure.reflvy

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import com.crysure.reflvy.data.ActiveLogin
import com.crysure.reflvy.data.NotifyChat
import com.crysure.reflvy.data.User
import com.crysure.reflvy.fragment.HintListener
import com.crysure.reflvy.fragment.StartingFragment
import com.crysure.reflvy.utils.ApplicationManager

class MenuActivity : AppCompatActivity(), HintListener {

    private var backPressedTime: Long = 0
    private val backPressedInterval = 2000 // Interval waktu dalam milidetik untuk menekan tombol back dua kali
    private val MENU_INFO_REQUEST_CODE = 1001

    private lateinit var scroller : NestedScrollView
    private lateinit var textName : TextView

    private lateinit var sharedPreferencesLogin : SharedPreferences

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

        scroller = findViewById(R.id.container_scroll)

        sharedPreferencesLogin = getSharedPreferences("login_status", Context.MODE_PRIVATE)
        val isStarting = sharedPreferencesLogin.getBoolean("isStarting", false)

        Footer(true)

        textName = findViewById(R.id.textname)
        textName.text = "Halo " + User.userData.userName

        if(!isStarting){
            Footer(false)
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            val startingFragment = StartingFragment()
            fragmentTransaction.replace(R.id.fragment_container, startingFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }




//        val textScreenning : TextView = findViewById(R.id.text_menu6_2)

        val menuScreening: LinearLayout = findViewById(R.id.menu_screening)
        menuScreening.setOnClickListener {
            val intent = Intent(this, ScreeningActivity::class.java)
            startActivity(intent)
        }

        val menuScreenAgain: LinearLayout = findViewById(R.id.menu_screeninglagi)
        menuScreenAgain.setOnClickListener {
            val intent = Intent(this, ScreeningActivity::class.java)
            startActivity(intent)
        }

        val textNilaiScreening : TextView = findViewById(R.id.text_nilaiscreening)
        textNilaiScreening.text = User.userData.nilaiScreening2.toString()

        val menuDailyActivity: LinearLayout = findViewById(R.id.menu_dailyactivity)
        menuDailyActivity.setOnClickListener {

            if(ActiveLogin.infoActive.activeNow){
                val intent = Intent(this, DasboardDailyActivity::class.java)
                startActivity(intent)
            }else{
                val intent = Intent(this, DailyCheckin::class.java)
                startActivity(intent)
            }
        }

        val menuSchedulling: LinearLayout = findViewById(R.id.menu_schedulling)
        menuSchedulling.setOnClickListener {

            if(ActiveLogin.infoActive.activeNow){
                val intent = Intent(this, SchedullingActivity::class.java)
                startActivity(intent)
            }else{
                val intent = Intent(this, DailyCheckin::class.java)
                startActivity(intent)
            }
        }

        val menuVideo: LinearLayout = findViewById(R.id.menu_video)
        menuVideo.setOnClickListener {
            val intent = Intent(this, VideoActivity::class.java)
            startActivity(intent)
        }

        val menuRoadmaps : LinearLayout = findViewById(R.id.menu_roadmaps)
        menuRoadmaps.setOnClickListener {
            val intent = Intent(this, RoadMapsActivity::class.java)
            startActivity(intent)
        }

//        val menuKuis : LinearLayout = findViewById(R.id.menu_kuis)
//        menuKuis.setOnClickListener {
//            val intent = Intent(this, MenuKuisActivity::class.java)
//            startActivity(intent)
//        }

        val menuPodcast: LinearLayout = findViewById(R.id.menu_podcast)
        menuPodcast.setOnClickListener {
            val intent = Intent(this, PlaylistActivity::class.java)
            startActivity(intent)
        }

        val menuInfo: LinearLayout = findViewById(R.id.menu_info)
        menuInfo.setOnClickListener {
            val intent = Intent(this, MenuInfoActivity::class.java)
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


    private fun Footer(kondisi : Boolean){
        val includedLayout = findViewById<View>(R.id.footer)

        val active = includedLayout.findViewById<LinearLayout>(R.id.active1)
        active.visibility = View.VISIBLE

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

        bot.isEnabled = kondisi
        settings.isEnabled = kondisi
    }

    override fun GetScroll() {
        scroller.smoothScrollTo(0, 1600)
    }

    override fun GetBackScroll() {
        scroller.fullScroll(View.FOCUS_UP)
    }

    override fun GetEnd() {
        scroller.fullScroll(View.FOCUS_UP)

        val editorLogin = sharedPreferencesLogin.edit()
        editorLogin.putBoolean("isStarting", true)
        editorLogin.apply()

        Footer(true)
    }
}