package com.crysure.reflvy

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.crysure.reflvy.data.ActiveLogin
import com.crysure.reflvy.data.DataHistoryActivity
import com.crysure.reflvy.data.NotifyChat
import com.crysure.reflvy.databinding.ActivityHistoryTotalBinding

class HistoryTotalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryTotalBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryTotalBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        SetSpendTime()
        Footer()
    }

    private fun SetSpendTime(){
        if(ActiveLogin.infoActive != null){

            if(DataHistoryActivity.dataHistoryActivity.isNotEmpty() || DataHistoryActivity.dataHistoryActivity != null){

                val totalSpendTimeSum = DataHistoryActivity.dataHistoryActivity.sumBy { it.totalSpendTime } + ActiveLogin.infoActive.totalSpendTime
                val waktu : String = FormatMinutesToTime(totalSpendTimeSum)

                if(totalSpendTimeSum <= 0){
                    binding.textTotalspendtime.text = "Belum Ada Kegiatan"
                }else{
                    binding.textTotalspendtime.text = waktu
                }

                binding.textTotalactive.text = "Total Aktif : " + ActiveLogin.infoActive.totalActive.toString() + " Hari"

                SetKegiatanSelengkapnya()
            }
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

        val kegiatanList = listOf(
            Kegiatan(binding.totalK1, ActiveLogin.infoActive.kegiatan1Bekerja),
            Kegiatan(binding.totalK2, ActiveLogin.infoActive.kegiatan2BelajarFormal),
            Kegiatan(binding.totalK3, ActiveLogin.infoActive.kegiatan3Membaca),
            Kegiatan(binding.totalK4, ActiveLogin.infoActive.kegiatan4Bersantai),
            Kegiatan(binding.totalK5, ActiveLogin.infoActive.kegiatan5Istirahat),
            Kegiatan(binding.totalK6, ActiveLogin.infoActive.kegiatan6Belanja),
            Kegiatan(binding.totalK7, ActiveLogin.infoActive.kegiatan7Bermusik),
            Kegiatan(binding.totalK8, ActiveLogin.infoActive.kegiatan8Beribadah),
            Kegiatan(binding.totalK9, ActiveLogin.infoActive.kegiatan9BermainGame),
            Kegiatan(binding.totalK10, ActiveLogin.infoActive.kegiatan10HiburanDigital),
            Kegiatan(binding.totalK11, ActiveLogin.infoActive.kegiatan11OperasiKomputer),
            Kegiatan(binding.totalK12, ActiveLogin.infoActive.kegiatan12PekerjaanRumah),
            Kegiatan(binding.totalK13, ActiveLogin.infoActive.kegiatan13Komunitas),
            Kegiatan(binding.totalK14, ActiveLogin.infoActive.kegiatan14Bersosialisasi),
            Kegiatan(binding.totalK15, ActiveLogin.infoActive.kegiatan15Healing),
            Kegiatan(binding.totalK16, ActiveLogin.infoActive.kegiatan16Olahraga),
            Kegiatan(binding.totalK17, ActiveLogin.infoActive.kegiatan17Liburan),
            Kegiatan(binding.totalK18, ActiveLogin.infoActive.kegiatan18Lainnya),
        )

        var k1 = 0
        var k2 = 0
        var k3 = 0
        var k4 = 0
        var k5 = 0
        var k6 = 0
        var k7 = 0
        var k8 = 0
        var k9 = 0
        var k10 = 0
        var k11 = 0
        var k12 = 0
        var k13 = 0
        var k14 = 0
        var k15 = 0
        var k16 = 0
        var k17 = 0
        var k18 = 0

        if (DataHistoryActivity.dataHistoryActivity.isNotEmpty() || DataHistoryActivity.dataHistoryActivity != null) {
            k1 = DataHistoryActivity.dataHistoryActivity.sumBy { it.kegiatan1Bekerja }
            k2 = DataHistoryActivity.dataHistoryActivity.sumBy { it.kegiatan2BelajarFormal }
            k3 = DataHistoryActivity.dataHistoryActivity.sumBy { it.kegiatan3Membaca }
            k4 = DataHistoryActivity.dataHistoryActivity.sumBy { it.kegiatan4Bersantai }
            k5 = DataHistoryActivity.dataHistoryActivity.sumBy { it.kegiatan5Istirahat }
            k6 = DataHistoryActivity.dataHistoryActivity.sumBy { it.kegiatan6Belanja }
            k7 = DataHistoryActivity.dataHistoryActivity.sumBy { it.kegiatan7Bermusik }
            k8 = DataHistoryActivity.dataHistoryActivity.sumBy { it.kegiatan8Beribadah }
            k9 = DataHistoryActivity.dataHistoryActivity.sumBy { it.kegiatan9BermainGame }
            k10 = DataHistoryActivity.dataHistoryActivity.sumBy { it.kegiatan10HiburanDigital }
            k11 = DataHistoryActivity.dataHistoryActivity.sumBy { it.kegiatan11OperasiKomputer }
            k12 = DataHistoryActivity.dataHistoryActivity.sumBy { it.kegiatan12PekerjaanRumah }
            k13 = DataHistoryActivity.dataHistoryActivity.sumBy { it.kegiatan13Komunitas }
            k14 = DataHistoryActivity.dataHistoryActivity.sumBy { it.kegiatan14Bersosialisasi }
            k15 = DataHistoryActivity.dataHistoryActivity.sumBy { it.kegiatan15Healing }
            k16 = DataHistoryActivity.dataHistoryActivity.sumBy { it.kegiatan16Olahraga }
            k17 = DataHistoryActivity.dataHistoryActivity.sumBy { it.kegiatan17Liburan }
            k18 = DataHistoryActivity.dataHistoryActivity.sumBy { it.kegiatan18Lainnya }
        }

        val kegiatanArray = arrayOf(k1, k2, k3, k4, k5, k6, k7, k8, k9, k10, k11, k12, k13, k14, k15, k16, k17, k18)

        for ((index, kegiatan) in kegiatanList.withIndex()) {
            kegiatan.textView.text = (kegiatan.totalSpeend + kegiatanArray[index]).toString() + " Menit"
        }

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

    data class Kegiatan(val textView : TextView, val totalSpeend : Int)
}