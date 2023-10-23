package com.example.reflvy

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.TranslateAnimation
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.reflvy.data.DragonConditionData
import com.example.reflvy.data.EfekStatusDragon
import com.example.reflvy.data.GameStatusManager
import com.example.reflvy.data.NotifyChat
import com.example.reflvy.fragment.DragonStatusFragment
import com.example.reflvy.fragment.OfflinePointFragment
import com.example.reflvy.fragment.OnActionDragonStatus
import com.example.reflvy.utils.GameManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class RoadMapsActivity : AppCompatActivity(), OnActionDragonStatus {

    private lateinit var dragonImageView: LinearLayout
    private lateinit var imageBackground : ImageView
    private lateinit var animationUp: ObjectAnimator
    private lateinit var animationDown: ObjectAnimator

    private lateinit var imageDragon : ImageView

    private lateinit var activityIcon : ImageView
    private lateinit var activityText : TextView
    private lateinit var coinIcon : ImageView
    private lateinit var coinText : TextView
    private lateinit var nameDragon : TextView

    private lateinit var headerDragon : LinearLayout

    private lateinit var containerKeterangan1 : LinearLayout
    private lateinit var textKeterangan1 : TextView

    private lateinit var containerDragonText : LinearLayout
    private lateinit var dragonText : TextView

    private lateinit var bgActivity : FrameLayout

    private lateinit var coinContainer : FrameLayout


    private var keteranganIsActive : Boolean = false
    private var isDragonText : Boolean = false
    private var isNight : Boolean = false
    private lateinit var containerActiveKeterangan : LinearLayout

    private val handler = Handler()
    private var delayInMillis: Long = 0

    private lateinit var dragonStatus : GameStatusManager
    private lateinit var pointStatus : DragonConditionData
    private lateinit var efekStatus : EfekStatusDragon

    private lateinit var btnEfekStatus : ImageView
    private lateinit var btnResetStatus : ImageView
    private lateinit var buyItem : ImageView

    private var totalOfflineCoin : Float = 0f


    private val runnable = object : Runnable {
        override fun run() {
            // Tempatkan fungsi yang ingin dijalankan secara terus-menerus di sini
            GetPlusKoin()
            addCoinView()

            CekContinuosEfekStatus()

            // Jalankan ulang Runnable setelah 5 detik
            handler.postDelayed(this, delayInMillis)
        }
    }

    private val runnable1m = object : Runnable {
        override fun run() {

            handler.postDelayed(this, 60000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_road_maps)

        dragonStatus = GameStatusManager.dragonStatus
        pointStatus = DragonConditionData.dragonConditionData
        efekStatus = EfekStatusDragon.efekNow

        dragonImageView = findViewById(R.id.dragon)
        containerDragonText = findViewById(R.id.container_dragontext)
        dragonText = findViewById(R.id.dragon_text)
        imageBackground = findViewById(R.id.background)
        coinContainer = findViewById(R.id.coin_container)

        imageDragon = findViewById(R.id.gambar_naga)

        coinText = findViewById(R.id.coin_point)
        nameDragon = findViewById(R.id.text_namedragon)

        Footer()
        UpdateGambarNaga()

//                    btnEfekStatus = findViewById(R.id.btn_efekstatus)
//                    btnEfekStatus.setOnClickListener {
//                        DisableEnableClick(false)
//                        val fragmentTransaction = supportFragmentManager.beginTransaction()
//                        val efekStatus = LibraryRoadmapsFragment()
//                        fragmentTransaction.replace(R.id.fragment_container, efekStatus)
//                        fragmentTransaction.addToBackStack(null)
//                        fragmentTransaction.commit()
//                    }
//
//
//                    btnResetStatus = findViewById(R.id.btn_img3)
//                    btnResetStatus.setOnClickListener {
//                        val fragmentTransaction = supportFragmentManager.beginTransaction()
//                        val efekStatus = EfekStatusFragment()
//                        fragmentTransaction.replace(R.id.fragment_container, efekStatus)
//                        fragmentTransaction.addToBackStack(null)
//                        fragmentTransaction.commit()
//                    }
//
//                buyItem = findViewById(R.id.btn_eat)
//                buyItem.setOnClickListener {
//                    val fragmentTransaction = supportFragmentManager.beginTransaction()
//                    val efekStatus = ShopFragment()
//                    fragmentTransaction.replace(R.id.fragment_container, efekStatus)
//                    fragmentTransaction.addToBackStack(null)
//                    fragmentTransaction.commit()
//                }

        UpdateUIPoint()

        AnimationDragon(dragonImageView)
        AnimationDragon(containerDragonText)
        ClickEvent()

        delayInMillis = (pointStatus.speedCoin * 1000).toLong()
        handler.postDelayed(runnable, delayInMillis)
        handler.postDelayed(runnable1m, delayInMillis)

        bgActivity = findViewById(R.id.bg_activity)

        bgActivity.setOnClickListener{
            if (keteranganIsActive){
                keteranganIsActive = false
                HideTooltip(containerActiveKeterangan)
            }
        }
        CekTime()
        CekContinuosEfekStatus()

        totalOfflineCoin = 0f
        GetOfflinePoint(DragonConditionData.dragonConditionData.lastActive)

    }

    private fun UpdateUIPoint(){

        nameDragon.text = dragonStatus.namaDragon
        coinText.text = FloatToShortString(dragonStatus.coinPoint)
    }

    private fun addCoinView() {
        val coinContainer = findViewById<FrameLayout>(R.id.coin_container)

        // Inflate layout XML ke dalam tampilan
        val coinView = LayoutInflater.from(this).inflate(R.layout.roadmap_getcoin, null)

        // Atur margin acak dalam interval marginStart (20 - 320 dp)
        val randomMarginStart = Random.nextInt(100, 1251) // 20 - 320 dp
        val randomMarginEnd = coinContainer.width - coinView.width - randomMarginStart

        val randomTranslationY = Random.nextInt(-800, -201).toFloat()

        // Atur margin bottom acak dalam interval (0 - 30 dp)
        val randomMarginBottom = Random.nextInt(10, 700) // 0 - 30 dp

        val layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )

        layoutParams.marginStart = randomMarginStart
        layoutParams.bottomMargin = randomMarginBottom

        coinView.layoutParams = layoutParams

        // Animasi fade in
        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.duration = 1000 // Durasi animasi fade in 1 detik

        val animation = TranslateAnimation(0f, 0f, 0f, randomTranslationY)
        animation.duration = 2100 // Durasi animasi 3,1 detik
        coinView.startAnimation(animation)

        // Tambahkan tampilan ke FrameLayout
        coinContainer.addView(coinView)

        // Setelah 3 detik, hilangkan dengan animasi fadeout dan hapus tampilan
        Handler().postDelayed({
            val fadeOut = AlphaAnimation(1f, 0f)
            fadeOut.duration = 1000 // Durasi animasi fadeout 1 detik
            coinView.startAnimation(fadeOut)
            fadeOut.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}

                override fun onAnimationEnd(animation: Animation?) {
                    coinContainer.removeView(coinView)
                }

                override fun onAnimationRepeat(animation: Animation?) {}
            })
        }, 2000) // Tunggu hingga animasi selesai (3,1 detik)
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

        coinIcon = findViewById(R.id.coin_icon)
        coinText = findViewById(R.id.coin_point)

        containerKeterangan1 = findViewById(R.id.container_keteranganiconheader)
        textKeterangan1 = findViewById(R.id.text_keterangan1)

        headerDragon = findViewById(R.id.header_icondragon)

        activityIcon.setOnClickListener {
            keteranganIsActive = true
            ShowTooltip(containerKeterangan1, textKeterangan1, "<b>Activity Point :</b> Selesaikan daily activity kamu dan aktifitas lainnya.", false)

            dragonStatus.statLapar = 100f
            dragonStatus.statPengetahuan = 100f
            dragonStatus.statKesehatanFisik = 100f
            dragonStatus.statKesehatanMental = 100f
            dragonStatus.statCinta = 100f
            dragonStatus.statHiburan = 100f
            dragonStatus.statIstirahat = 100f
            dragonStatus.statSosial = 100f

            GameStatusManager.dragonStatus.SaveDataPoint(this)
        }

        coinIcon.setOnClickListener {
            keteranganIsActive = true
            ShowTooltip(containerKeterangan1, textKeterangan1, "<b>Coin Point :</b> Tigkatkan statusmu dan dapatkan lebih banyak koin.", false)
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
        }
    }

    private fun GetPlusKoin(){
        dragonStatus.coinPoint += pointStatus.coinOvertime

        UpdateUIPoint()

        GameStatusManager.dragonStatus.SaveDataStatus(this)
    }

    private fun CekContinuosEfekStatus(){

        if (dragonStatus.statIstirahat >= 20f){
            EfekStatusDragon.continuosStatus[0].isActive = false
        }else{
            EfekStatusDragon.continuosStatus[0].isActive = true
        }

        if (dragonStatus.statLapar >= 20f){
            EfekStatusDragon.continuosStatus[1].isActive = false
        }else{
            EfekStatusDragon.continuosStatus[1].isActive = true
        }

        GameManager.instance.updateEfekNow(this)
        GameManager.instance.SaveEfekStatus(this)
    }

    fun GetOfflinePoint(lastActive: String) {
        // Format tanggal dan waktu
        val dateFormat = SimpleDateFormat("dd-MM-yyyy : HH.mm")

        try {
            // Parse tanggal dan waktu dari string ke objek Date
            if (lastActive == ""){

            }else {

                val lastActiveDate = dateFormat.parse(lastActive)
                val currentDate = Date()

                // Hitung selisih waktu dalam milidetik
                val timeDifferenceMillis = currentDate.time - lastActiveDate.time

                // Konversi selisih waktu dari milidetik ke menit
                val timeDifferenceMinutes = timeDifferenceMillis / (1000 * 60)


                if(timeDifferenceMinutes > 5){
                    var parameterTimeAwal : Int  = 0

                    while (parameterTimeAwal < timeDifferenceMinutes) {
                        parameterTimeAwal += 5

                        CekContinuosEfekStatus()
                        GameManager.instance.updateEfekNow(this)

                        GetCoinOffile()
                        GetStatusOffline(false)
                    }

                    DisableEnableClick(false)
                    val totalCoin = OfflinePointFragment()
                    val bundle = Bundle()
                    bundle.putFloat("totalCoin", totalOfflineCoin)
                    totalCoin.arguments = bundle

                    val fragmentTransaction = supportFragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.fragment_container, totalCoin)
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()

                    totalOfflineCoin = 0f

                    UpdateUIPoint()

                    DragonConditionData.dragonConditionData.lastActive = GetCurrentDateTimeAsString()
                    GameManager.instance.SaveDataConditionStatus(this)

                    GameStatusManager.dragonStatus.SaveDataStatus(this)
                }
            }
        } catch (e: Exception) {
            println("Terjadi kesalahan: ${e.message}")
        }
    }

    private fun GetCoinOffile(){
        val getCoin : Float = (300f * pointStatus.speedCoin) * pointStatus.coinOvertime
        dragonStatus.coinPoint += getCoin.toInt()

        totalOfflineCoin += getCoin
    }

    private fun GetStatusOffline(is1minute : Boolean){
        if (is1minute){

        }else{
            // Stat Lapar
            if (dragonStatus.statLapar <= 19.9999f){
                dragonStatus.statLapar += -((0.4f * 0.3f) - efekStatus.statLapar)
            }else if(dragonStatus.statLapar >= 20f && dragonStatus.statLapar <= 39.9999f){
                dragonStatus.statLapar += -((0.4f * 0.4f) - efekStatus.statLapar)
            }else if(dragonStatus.statLapar >= 40f && dragonStatus.statLapar <= 59.9999f){
                dragonStatus.statLapar += -((0.4f * 0.6f) - efekStatus.statLapar)
            }else if(dragonStatus.statLapar >= 60f && dragonStatus.statLapar <= 79.9999f){
                dragonStatus.statLapar += -((0.4f * 0.8f) - efekStatus.statLapar)
            }else if(dragonStatus.statLapar >= 80f){
                dragonStatus.statLapar += -((0.4f * 1f) - efekStatus.statLapar)
            }

            // Stat Pengetahuan
            if (dragonStatus.statPengetahuan <= 19.9999f){
                dragonStatus.statPengetahuan += -((0.25f * 0.3f) - efekStatus.statPengetahuan)
            }else if(dragonStatus.statPengetahuan >= 20f && dragonStatus.statPengetahuan <= 39.9999f){
                dragonStatus.statPengetahuan += -((0.25f * 0.4f) - efekStatus.statPengetahuan)
            }else if(dragonStatus.statPengetahuan >= 40f && dragonStatus.statPengetahuan <= 59.9999f){
                dragonStatus.statPengetahuan += -((0.25f * 0.6f) - efekStatus.statPengetahuan)
            }else if(dragonStatus.statPengetahuan >= 60f && dragonStatus.statPengetahuan <= 79.9999f){
                dragonStatus.statPengetahuan += -((0.25f * 0.8f) - efekStatus.statPengetahuan)
            }else if(dragonStatus.statPengetahuan >= 80f){
                dragonStatus.statPengetahuan += -((0.25f * 1f) - efekStatus.statPengetahuan)
            }

            // Stat Kesehatan Fisik
            if (dragonStatus.statKesehatanFisik <= 19.9999f){
                dragonStatus.statKesehatanFisik += -((0.18f * 0.3f) - efekStatus.statKesehatanFisik)
            }else if(dragonStatus.statKesehatanFisik >= 20f && dragonStatus.statKesehatanFisik <= 39.9999f){
                dragonStatus.statKesehatanFisik += -((0.18f * 0.4f) - efekStatus.statKesehatanFisik)
            }else if(dragonStatus.statKesehatanFisik >= 40f && dragonStatus.statKesehatanFisik <= 59.9999f){
                dragonStatus.statKesehatanFisik += -((0.18f * 0.6f) - efekStatus.statKesehatanFisik)
            }else if(dragonStatus.statKesehatanFisik >= 60f && dragonStatus.statKesehatanFisik <= 79.9999f){
                dragonStatus.statKesehatanFisik += -((0.18f * 0.8f) - efekStatus.statKesehatanFisik)
            }else if(dragonStatus.statKesehatanFisik >= 80f){
                dragonStatus.statKesehatanFisik += -((0.18f * 1f) - efekStatus.statKesehatanFisik)
            }

            // Stat Kesehatan mental
            if (dragonStatus.statKesehatanMental <= 19.9999f){
                dragonStatus.statKesehatanMental += -((0.11f * 0.3f) - efekStatus.statKesehatanMental)
            }else if(dragonStatus.statKesehatanMental >= 20f && dragonStatus.statKesehatanMental <= 39.9999f){
                dragonStatus.statKesehatanMental += -((0.11f * 0.4f) - efekStatus.statKesehatanMental)
            }else if(dragonStatus.statKesehatanMental >= 40f && dragonStatus.statKesehatanMental <= 59.9999f){
                dragonStatus.statKesehatanMental += -((0.11f * 0.6f) - efekStatus.statKesehatanMental)
            }else if(dragonStatus.statKesehatanMental >= 60f && dragonStatus.statKesehatanMental <= 79.9999f){
                dragonStatus.statKesehatanMental += -((0.11f * 0.8f) - efekStatus.statKesehatanMental)
            }else if(dragonStatus.statKesehatanMental >= 80f){
                dragonStatus.statKesehatanMental += -((0.11f * 1f) - efekStatus.statKesehatanMental)
            }

            // Stat Cinta
            if (dragonStatus.statCinta <= 19.9999f){
                dragonStatus.statCinta += -((0.12f * 0.4f) - efekStatus.statCinta)
            }else if(dragonStatus.statCinta >= 20f && dragonStatus.statCinta <= 39.9999f){
                dragonStatus.statCinta += -((0.12f * 0.4f) - efekStatus.statCinta)
            }else if(dragonStatus.statCinta >= 40f && dragonStatus.statCinta <= 59.9999f){
                dragonStatus.statCinta += -((0.12f * 0.6f) - efekStatus.statCinta)
            }else if(dragonStatus.statCinta >= 60f && dragonStatus.statCinta <= 79.9999f){
                dragonStatus.statCinta += -((0.12f * 0.8f) - efekStatus.statCinta)
            }else if(dragonStatus.statCinta >= 80f){
                dragonStatus.statCinta += -((0.12f * 1f) - efekStatus.statCinta)
            }

            // Stat Hiburan
            if (dragonStatus.statHiburan <= 19.9999f){
                dragonStatus.statHiburan += -((0.11f * 0.3f) - efekStatus.statHiburan)
            }else if(dragonStatus.statHiburan >= 31f && dragonStatus.statHiburan <= 39.9999f){
                dragonStatus.statHiburan += -((0.11f * 0.4f) - efekStatus.statHiburan)
            }else if(dragonStatus.statHiburan >= 40f && dragonStatus.statHiburan <= 59.9999f){
                dragonStatus.statHiburan += -((0.11f * 0.6f) - efekStatus.statHiburan)
            }else if(dragonStatus.statHiburan >= 60f && dragonStatus.statHiburan <= 79.9999f){
                dragonStatus.statHiburan += -((0.11f * 0.8f) - efekStatus.statHiburan)
            }else if(dragonStatus.statHiburan >= 80f){
                dragonStatus.statHiburan += -((0.11f * 1f) - efekStatus.statHiburan)
            }

            // Stat Istirahat
            if (dragonStatus.statIstirahat <= 19.9999f){
                dragonStatus.statIstirahat += -((0.31f * 0.3f) - efekStatus.statIstirahat)
            }else if(dragonStatus.statIstirahat >= 20f && dragonStatus.statIstirahat <= 39.9999f){
                dragonStatus.statIstirahat += -((0.31f * 0.4f) - efekStatus.statIstirahat)
            }else if(dragonStatus.statIstirahat >= 40f && dragonStatus.statIstirahat <= 59.9999f){
                dragonStatus.statIstirahat += -((0.31f * 0.6f) - efekStatus.statIstirahat)
            }else if(dragonStatus.statIstirahat >= 60f && dragonStatus.statIstirahat <= 79.9999f){
                dragonStatus.statIstirahat += -((0.31f * 0.8f) - efekStatus.statIstirahat)
            }else if(dragonStatus.statIstirahat >= 80f){
                dragonStatus.statIstirahat += -((0.31f * 1f) - efekStatus.statIstirahat)
            }

            // Stat Sosial
            if (dragonStatus.statSosial <= 19.9999f){
                dragonStatus.statSosial += -((0.15f * 0.3f) - efekStatus.statSosial)
            }else if(dragonStatus.statSosial >= 31f && dragonStatus.statSosial <= 39.9999f){
                dragonStatus.statSosial += -((0.15f * 0.4f) - efekStatus.statSosial)
            }else if(dragonStatus.statSosial >= 40f && dragonStatus.statSosial <= 59.9999f){
                dragonStatus.statSosial += -((0.15f * 0.6f) - efekStatus.statSosial)
            }else if(dragonStatus.statSosial >= 60f && dragonStatus.statSosial <= 79.9999f){
                dragonStatus.statSosial += -((0.15f * 0.8f) - efekStatus.statSosial)
            }else if(dragonStatus.statSosial >= 80f){
                dragonStatus.statSosial += -((0.15f * 1f) - efekStatus.statSosial)
            }

        }

        GameStatusManager.dragonStatus.SaveDataPoint(this)
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

    fun GetCurrentDateTimeAsString(): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy : HH.mm")
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }

    fun FloatToShortString(value: Float): String {
        if (value >= 1000) {
            val thousands = value / 1000
            return String.format("%.0fk", thousands)
        }
        return String.format("%.0f", value)
    }



    private fun DisableEnableClick(kondisi : Boolean){
        activityIcon.isEnabled = kondisi
        coinIcon.isEnabled = kondisi
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

    }

    override fun IsEvolution(gambar: Int) {
        imageDragon.setImageResource(gambar)
    }

    override fun MinusCoin() {
        coinText.text = FloatToShortString(dragonStatus.coinPoint)
    }

    private fun UpdateGambarNaga(){
        if (GameStatusManager.dragonStatus.levelDragon == 1){
            imageDragon.setImageResource(R.drawable.roadmap_lv1)
        }else if (GameStatusManager.dragonStatus.levelDragon == 2) {
            imageDragon.setImageResource(R.drawable.roadmap_lv2)
        }else if (GameStatusManager.dragonStatus.levelDragon == 3) {
            imageDragon.setImageResource(R.drawable.roadmap_lv3)
        }else if (GameStatusManager.dragonStatus.levelDragon == 4) {
            imageDragon.setImageResource(R.drawable.roadmap_lv4)
        }else if (GameStatusManager.dragonStatus.levelDragon == 5) {
            imageDragon.setImageResource(R.drawable.roadmap_lv5)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        DragonConditionData.dragonConditionData.lastActive = GetCurrentDateTimeAsString()
        GameManager.instance.SaveDataConditionStatus(this)

        handler.removeCallbacks(runnable)
        handler.removeCallbacks(runnable1m)
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