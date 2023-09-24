package com.example.reflvy

import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.reflvy.fragment.DragonStatusFragment
import com.example.reflvy.fragment.OnActionDragonStatus
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RoadMapsActivity : AppCompatActivity(), OnActionDragonStatus {

    private lateinit var dragonImageView: LinearLayout
    private lateinit var imageBackground : ImageView
    private lateinit var animationUp: ObjectAnimator
    private lateinit var animationDown: ObjectAnimator

    private lateinit var activityIcon : ImageView
    private lateinit var activityText : TextView
    private lateinit var archivmentIcon : ImageView
    private lateinit var archivmentText : TextView

    private lateinit var headerDragon : LinearLayout

    private lateinit var containerKeterangan1 : LinearLayout
    private lateinit var textKeterangan1 : TextView

    private lateinit var containerDragonText : LinearLayout
    private lateinit var dragonText : TextView

    private lateinit var bgActivity : FrameLayout


    private var keteranganIsActive : Boolean = false
    private var isDragonText : Boolean = false
    private var isNight : Boolean = false
    private lateinit var containerActiveKeterangan : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_road_maps)

        dragonImageView = findViewById(R.id.dragon)
        containerDragonText = findViewById(R.id.container_dragontext)
        dragonText = findViewById(R.id.dragon_text)
        imageBackground = findViewById(R.id.background)

        AnimationDragon(dragonImageView)
        AnimationDragon(containerDragonText)
        ClickEvent()

        bgActivity = findViewById(R.id.bg_activity)

        bgActivity.setOnClickListener{
            if (keteranganIsActive){
                keteranganIsActive = false
                HideTooltip(containerActiveKeterangan)
            }
        }
        CekTime()

    }

    override fun onResume() {
        super.onResume()
        // Mulai animasi ketika aktivitas di-resume
        animationUp.start()
    }

    private fun CekTime(){
        val currentTime = getCurrentTime()
        val isDaytime = isDaytime(currentTime)

        // Ganti gambar berdasarkan kondisi waktu
        if (isDaytime) {
            imageBackground.setImageResource(R.drawable.roadmaps_bg)
        } else {
            isNight = true
            imageBackground.setImageResource(R.drawable.roadmaps_bgnight)
        }
    }

    private fun AnimationDragon(container : LinearLayout){

        // Inisialisasi animasi
        animationUp = ObjectAnimator.ofFloat(
            container,
            "translationY",
            0f,
            -100f
        )
        animationUp.duration = 3000 // Durasi animasi dalam milidetik
        animationUp.interpolator = LinearInterpolator()
        animationUp.repeatMode = ObjectAnimator.REVERSE
        animationUp.repeatCount = ObjectAnimator.INFINITE

        animationDown = ObjectAnimator.ofFloat(
            container,
            "translationY",
            -100f,
            0f
        )
        animationDown.duration = 3000 // Durasi animasi dalam milidetik
        animationDown.interpolator = LinearInterpolator()
        animationDown.repeatMode = ObjectAnimator.REVERSE
        animationDown.repeatCount = ObjectAnimator.INFINITE

        // Mulai animasi pertama (ke atas)
        animationUp.start()
    }

    private fun ClickEvent(){
        activityIcon = findViewById(R.id.activity_icon)
        activityText = findViewById(R.id.activity_point)

        archivmentIcon = findViewById(R.id.archivment_icon)
        archivmentText = findViewById(R.id.archivment_point)

        containerKeterangan1 = findViewById(R.id.container_keteranganiconheader)
        textKeterangan1 = findViewById(R.id.text_keterangan1)

        headerDragon = findViewById(R.id.header_icondragon)

        activityIcon.setOnClickListener {
            keteranganIsActive = true
            ShowTooltip(containerKeterangan1, textKeterangan1, "<b>Activity Point :</b> Selesaikan daily activity kamu dan aktifitas lainnya.", false)
        }

        archivmentIcon.setOnClickListener {
            keteranganIsActive = true
            ShowTooltip(containerKeterangan1, textKeterangan1, "<b>Achievement Point :</b> Selesaikan goals yang kamu buat.", false)
        }

        dragonImageView.setOnClickListener {
            if (!isDragonText){
                isDragonText = true
                ShowTooltip(containerDragonText, dragonText, "Selamat Pagi, Bagaimana Kabarmu?", true)
            }
        }

        headerDragon.setOnClickListener {
                DisableEnableClick(false)
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                val statusDragon = DragonStatusFragment()
                fragmentTransaction.replace(R.id.fragment_container, statusDragon)
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
                DisableEnableClick(false)
        }
    }


    private fun ShowTooltip(container : LinearLayout, textView : TextView, text : String, isDragon : Boolean) {

        container.visibility = View.VISIBLE

        if(!isDragon){
            containerActiveKeterangan = container
        }else{
            Handler(Looper.getMainLooper()).postDelayed({
                HideTooltip(container)
                isDragonText = false
            }, 5000) // 5000 milidetik (5 detik)
        }

        // Animasi fade in dengan mengubah opacity (alpha)
        val fadeInAnimator = ObjectAnimator.ofFloat(container, "alpha", 0f, 1f)
        fadeInAnimator.duration = 300 // Durasi animasi dalam milidetik
        fadeInAnimator.start()

        textView.text = Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
    }

    private fun HideTooltip(container : LinearLayout) {
        // Animasi fade out dengan mengubah opacity (alpha)
        val fadeOutAnimator = ObjectAnimator.ofFloat(container, "alpha", 1f, 0f)
        fadeOutAnimator.duration = 300 // Durasi animasi dalam milidetik
        fadeOutAnimator.start()

        fadeOutAnimator.addUpdateListener {
            if (container.alpha == 0f) {
                // Setelah animasi fade out selesai, sembunyikan keterangan teks
                container.visibility = View.INVISIBLE
            }
        }
    }

    private fun getCurrentTime(): Date {
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val currentTimeString = dateFormat.format(Date())
        return dateFormat.parse(currentTimeString)
    }

    private fun isDaytime(time: Date): Boolean {
        val calendar = java.util.Calendar.getInstance()
        calendar.time = time
        val hourOfDay = calendar.get(java.util.Calendar.HOUR_OF_DAY)
        return hourOfDay >= 5 && hourOfDay < 19
    }

    private fun DisableEnableClick(kondisi : Boolean){
        activityIcon.isEnabled = kondisi
        archivmentIcon.isEnabled = kondisi
        dragonImageView.isEnabled = kondisi
        headerDragon.isEnabled = kondisi
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // Aktifkan kembali objek yang dapat diklik (Enable) di sini
        DisableEnableClick(true)
    }

    override fun BackFunction(back : Boolean) {
        DisableEnableClick(back)
        Toast.makeText(this, back.toString(), Toast.LENGTH_SHORT).show()
    }
}