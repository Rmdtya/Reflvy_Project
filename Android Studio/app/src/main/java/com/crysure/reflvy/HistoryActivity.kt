package com.crysure.reflvy

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.crysure.reflvy.data.ActiveLogin
import com.crysure.reflvy.data.DataHistoryActivity.Companion.dataHistoryActivity
import com.crysure.reflvy.data.NotifyChat
import com.crysure.reflvy.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var container1 : LinearLayout
    private lateinit var container2 : LinearLayout
    private lateinit var container3 : LinearLayout
    private lateinit var container4 : LinearLayout
    private lateinit var container5 : LinearLayout
    private lateinit var container6 : LinearLayout
    private lateinit var inflater: LayoutInflater
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        inflater = LayoutInflater.from(this)

        SetSpendTime()
        Footer()
    }

    private fun SetSpendTime(){
        if(ActiveLogin.infoActive != null){
            val waktu : String = FormatMinutesToTime(ActiveLogin.infoActive.totalSpendTime)
            binding.textTotalspendtime.text = waktu

            if(ActiveLogin.infoActive.totalSpendTime == 0){
                binding.textTotalspendtime.text = "Belum Ada Kegiatan"
            }

            if (dataHistoryActivity.isNotEmpty()) {
                val lastTotalSpendTime = dataHistoryActivity.last().totalSpendTime

                var perbandingan : Int = HitungPerbandingan(ActiveLogin.infoActive.totalSpendTime.toDouble(), lastTotalSpendTime.toDouble())

                if (perbandingan < 0){
                    val colorNegatif = ContextCompat.getColorStateList(this, R.color.historyactivity_negatif)
                    binding.imgPembanding.backgroundTintList = colorNegatif
                    perbandingan = perbandingan * -1

                    binding.textPembanding.text = perbandingan.toString() + "%"
                }else{
                    val colorPositif = ContextCompat.getColorStateList(this, R.color.historyactivity_positif)
                    binding.imgPembanding.backgroundTintList = colorPositif

                    binding.textPembanding.text = perbandingan.toString() + "%"
                }

            }

            val textMood : String = ActiveLogin.infoActive.moodNow
            binding.textMood.text = textMood

            SetImageCategory(textMood, binding.imgMood)
            SetKegiatanSelengkapnya()


            binding.btnSelengkapnya.setOnClickListener {
                val intent = Intent(this, HistoryTotalActivity::class.java)
                startActivity(intent)
            }
        }
    }

    fun HitungPerbandingan(value1: Double, value2: Double): Int {
        // Menghitung perbedaan persentase
        val percentageDifference = ((value1 - value2) / value2) * 100

        return percentageDifference.toInt()
    }

    private fun SetImageCategory(mood : String, moodImage : ImageView){
        if(mood == "Gelisah"){
            moodImage.setImageResource(R.drawable.mood_gelisah)
        }else if(mood == "Marah"){
            moodImage.setImageResource(R.drawable.mood_marah)
        }else if(mood == "Sedih"){
            moodImage.setImageResource(R.drawable.mood_sedih)
        }else if(mood == "Kecewa"){
            moodImage.setImageResource(R.drawable.mood_kecewa)
        }else if(mood == "Bosan"){
            moodImage.setImageResource(R.drawable.mood_bosan)
        }else if(mood == "Seperti Biasa"){
            moodImage.setImageResource(R.drawable.mood_netral)
        }else if(mood == "Senang"){
            moodImage.setImageResource(R.drawable.mood_senang)
        }else if(mood == "Bersemangat"){
            moodImage.setImageResource(R.drawable.mood_bersemangat)
        }else if(mood == "Riang"){
            moodImage.setImageResource(R.drawable.mood_riang)
        }else if(mood == "Bahagia"){
            moodImage.setImageResource(R.drawable.mood_bahagia)
        }else{
            Toast.makeText(this, "error image", Toast.LENGTH_SHORT).show()
        }
    }

    fun FormatMinutesToTime(minutes: Int): String {
        val hours = minutes / 60
        val remainingMinutes = minutes % 60

        val hoursText = if (hours == 1) {
            "1 Jam"
        } else if (hours > 1) {
            "$hours Jam"
        } else {
            ""
        }

        val minutesText = if (remainingMinutes == 1) {
            "1 Menit"
        } else if (remainingMinutes > 1) {
            "$remainingMinutes Menit"
        } else {
            ""
        }

        val separator = if (!hoursText.isEmpty() && !minutesText.isEmpty()) {
            " "
        } else {
            ""
        }

        return "$hoursText$separator$minutesText"
    }

    private fun SetKegiatanSelengkapnya(){

        var indexKolom : Int = 0
        container1 = binding.baris1
        container2 = binding.baris2
        container3 = binding.baris3
        container4 = binding.baris4
        container5 = binding.baris5
        container6 = binding.baris6

        val kegiatanList = listOf(
            Kegiatan(R.drawable.dailyicom_01bekerja, ActiveLogin.infoActive.kegiatan1Bekerja),
            Kegiatan(R.drawable.dailyicom_02belajarformal, ActiveLogin.infoActive.kegiatan2BelajarFormal),
            Kegiatan(R.drawable.dailyicom_03membaca, ActiveLogin.infoActive.kegiatan3Membaca),
            Kegiatan(R.drawable.dailyicom_04bersantai, ActiveLogin.infoActive.kegiatan4Bersantai),
            Kegiatan(R.drawable.dailyicom_05istirahat, ActiveLogin.infoActive.kegiatan5Istirahat),
            Kegiatan(R.drawable.dailyicom_06belanja, ActiveLogin.infoActive.kegiatan6Belanja),
            Kegiatan(R.drawable.dailyicom_07bermusik, ActiveLogin.infoActive.kegiatan7Bermusik),
            Kegiatan(R.drawable.dailyicom_08beribadah, ActiveLogin.infoActive.kegiatan8Beribadah),
            Kegiatan(R.drawable.dailyicom_09bermaingame, ActiveLogin.infoActive.kegiatan9BermainGame),
            Kegiatan(R.drawable.dailyicom_10hiburandigital, ActiveLogin.infoActive.kegiatan10HiburanDigital),
            Kegiatan(R.drawable.dailyicom_11operasikomputer, ActiveLogin.infoActive.kegiatan11OperasiKomputer),
            Kegiatan(R.drawable.dailyicom_12pekerjaanrumah, ActiveLogin.infoActive.kegiatan12PekerjaanRumah),
            Kegiatan(R.drawable.dailyicom_13komunitas, ActiveLogin.infoActive.kegiatan13Komunitas),
            Kegiatan(R.drawable.dailyicom_14bersosialisasi, ActiveLogin.infoActive.kegiatan14Bersosialisasi),
            Kegiatan(R.drawable.dailyicom_15healing, ActiveLogin.infoActive.kegiatan15Healing),
            Kegiatan(R.drawable.dailyicom_16olahraga, ActiveLogin.infoActive.kegiatan16Olahraga),
            Kegiatan(R.drawable.dailyicom_17liburan, ActiveLogin.infoActive.kegiatan17Liburan),
            Kegiatan(R.drawable.dailyicon_lainnya, ActiveLogin.infoActive.kegiatan18Lainnya),
        )

        val jumlah = kegiatanList.count { it.durasi > 0 }

        for (kegiatan in kegiatanList) {
            if (kegiatan.durasi > 0) {
                var template: View = inflater.inflate(R.layout.template_historyactivity, null)

                if(indexKolom < 3){
                    template = inflater.inflate(R.layout.template_historyactivity, container1, false)
                    if(indexKolom == 1 && jumlah == 2){
                        val layoutParams = template.layoutParams as ViewGroup.MarginLayoutParams
                        layoutParams.marginStart = 10
                    }else if(indexKolom == 1 && jumlah >= 3){
                        val layoutParams = template.layoutParams as ViewGroup.MarginLayoutParams
                        layoutParams.marginStart = 10
                        layoutParams.marginEnd = 10
                    }
                    container1.addView(template)
                    binding.baris1.visibility = View.VISIBLE

                }else if(indexKolom >= 3 && indexKolom < 6){
                    template= inflater.inflate(R.layout.template_historyactivity, container2, false)
                    if(indexKolom == 4 && jumlah == 5){
                        val layoutParams = template.layoutParams as ViewGroup.MarginLayoutParams
                        layoutParams.marginStart = 10
                    } else if(indexKolom == 4 && jumlah >= 6){
                        val layoutParams = template.layoutParams as ViewGroup.MarginLayoutParams
                        layoutParams.marginStart = 10
                        layoutParams.marginEnd = 10
                    }
                    container2.addView(template)
                    binding.baris2.visibility = View.VISIBLE

                }else if(indexKolom >= 6 && indexKolom < 9){
                    template = inflater.inflate(R.layout.template_historyactivity, container3, false)
                    if(indexKolom == 7 && jumlah == 8){
                        val layoutParams = template.layoutParams as ViewGroup.MarginLayoutParams
                        layoutParams.marginStart = 10
                    }else if(indexKolom == 7 && jumlah >= 9){
                        val layoutParams = template.layoutParams as ViewGroup.MarginLayoutParams
                        layoutParams.marginStart = 10
                        layoutParams.marginEnd = 10
                    }
                    container3.addView(template)
                    binding.baris3.visibility = View.VISIBLE

                }else if(indexKolom >= 9 && indexKolom < 12){
                    template = inflater.inflate(R.layout.template_historyactivity, container4, false)
                    if(indexKolom == 10 && jumlah == 11){
                        val layoutParams = template.layoutParams as ViewGroup.MarginLayoutParams
                        layoutParams.marginStart = 5
                    }else if(indexKolom == 10 && jumlah >= 12){
                        val layoutParams = template.layoutParams as ViewGroup.MarginLayoutParams
                        layoutParams.marginStart = 10
                        layoutParams.marginEnd = 10
                    }
                    container4.addView(template)
                    binding.baris4.visibility = View.VISIBLE

                }else if(indexKolom >= 12 && indexKolom < 15){
                    template = inflater.inflate(R.layout.template_historyactivity, container5, false)
                    if(indexKolom == 13 && jumlah == 14){
                        val layoutParams = template.layoutParams as ViewGroup.MarginLayoutParams
                        layoutParams.marginStart = 10
                    } else if(indexKolom == 13 && jumlah >= 15){
                        val layoutParams = template.layoutParams as ViewGroup.MarginLayoutParams
                        layoutParams.marginStart = 10
                        layoutParams.marginEnd = 10
                    }
                    container5.addView(template)
                    binding.baris5.visibility = View.VISIBLE

                }else if(indexKolom >= 15 && indexKolom < 18){
                    template = inflater.inflate(R.layout.template_historyactivity, container6, false)
                    if(indexKolom == 16 && jumlah == 17){
                        val layoutParams = template.layoutParams as ViewGroup.MarginLayoutParams
                        layoutParams.marginStart = 10
                    } else if(indexKolom == 16 && jumlah >= 18){
                        val layoutParams = template.layoutParams as ViewGroup.MarginLayoutParams
                        layoutParams.marginStart = 10
                        layoutParams.marginEnd = 10
                    }
                    container6.addView(template)
                    binding.baris6.visibility = View.VISIBLE
                }

                val textMinute: TextView = template.findViewById(R.id.text_menit)
                val imageIcon: ImageView = template.findViewById(R.id.img_icon)

                imageIcon.setImageResource(kegiatan.iconResId)
                textMinute.text = "${kegiatan.durasi} Menit"
                indexKolom++
            }
        }
    }

    data class Kegiatan(val iconResId: Int, val durasi: Int)

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