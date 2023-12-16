package com.crysure.reflvy

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.NestedScrollView
import com.crysure.reflvy.data.DataMisi
import com.crysure.reflvy.data.EventScreening
import com.crysure.reflvy.data.Screening
import com.crysure.reflvy.data.User
import com.crysure.reflvy.utils.ApplicationManager
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ScreeningSatuActivity : AppCompatActivity() {

    private lateinit var linearContainer: LinearLayout
    private lateinit var inflater: LayoutInflater
    private lateinit var seekBar: SeekBar
    private lateinit var persentasiBar: TextView
    private lateinit var template_nilai : ConstraintLayout
    private lateinit var textNilai : TextView

    private lateinit var schrollCotainer : NestedScrollView
    private val db = Firebase.firestore

    private lateinit var textTingkat : TextView
    private lateinit var textKeterangan : TextView
    private lateinit var btn_selengkapnya : TextView
    private lateinit var btnSelesai : LinearLayout

    private lateinit var roundedNilai : LinearLayout

    val handler = Handler(Looper.getMainLooper())

    companion object{
        var indexKe : Int = 0
        var maxValue : Int = 0
        var nilaiScreening : Int = 0
    }

    private lateinit var jawabContainer : LinearLayout
    private lateinit var jawabContainerBawah : LinearLayout
    private lateinit var buttonSelesai : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screening)

        schrollCotainer = findViewById(R.id.schroll_container)

        var sharedPreferences: SharedPreferences = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
        User.userData.loadFromSharedPreferences(sharedPreferences)

        Footer()
        linearContainer = findViewById(R.id.chat_container)
        jawabContainer = findViewById(R.id.jawab_container)
        jawabContainerBawah = findViewById(R.id.jawab_containerbawah)
        inflater = LayoutInflater.from(this)
        seekBar = findViewById(R.id.progres_bar)
        buttonSelesai = findViewById(R.id.btn_selesai)

        schrollCotainer = findViewById(R.id.schroll_container)

        template_nilai = findViewById(R.id.template_nilai)
        textNilai = findViewById(R.id.text_nilai)
        textTingkat = findViewById(R.id.text_tingkat)
        textKeterangan = findViewById(R.id.text_keterangantingkat)
        btn_selengkapnya = findViewById(R.id.btn_selengkapnya)
        roundedNilai = findViewById(R.id.rounded_nilai)

        persentasiBar = findViewById(R.id.persentasi)
        persentasiBar.text = "0%"

        indexKe = 0
        maxValue = Screening.screenningSatuData.size


        template_nilai.visibility = View.GONE

        SetSeekbar(maxValue)
        ShowChatBot(indexKe, true)

        jawabContainerBawah.visibility = View.GONE

        buttonSelesai.setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
        }

    }

    private fun ShowTemplateBot(text : String, icon : Boolean, time : Boolean){

        val templateChatbot: View = inflater.inflate(R.layout.template_chatbot, null)
        linearContainer.addView(templateChatbot)

        val soal : TextView = templateChatbot.findViewById(R.id.answer_bot)
        val iconBot: ImageView = templateChatbot.findViewById(R.id.icon_bot)
        val textTime: TextView = templateChatbot.findViewById(R.id.time_bot)

        val stringTime = GetTime()
        soal.text = text
        textTime.text = stringTime

        if(!icon){
            iconBot.visibility = View.INVISIBLE
        }

        if(!time){
            textTime.visibility = View.GONE
        }

        schrollCotainer.post {
            schrollCotainer.fullScroll(ScrollView.FOCUS_DOWN)
        }
    }

    private fun ShowChatBot(ind : Int, icon : Boolean){

        val jumlahData = Screening.screenningSatuData[ind].soalbot.size - 1

        if (jumlahData == 0 && icon == true){
            val theText = Screening.screenningSatuData[ind].soalbot[0]

            ShowTemplateBot(theText, true, true)

        }else if(jumlahData >= 1) {
            for (i in Screening.screenningSatuData[ind].soalbot.indices) {

                var theText = Screening.screenningSatuData[ind].soalbot[i]

                if (i == 0) {
                    ShowTemplateBot(theText, true, false)

                } else if (i == jumlahData) {
                    ShowTemplateBot(theText, false, true)

                } else {
                    ShowTemplateBot(theText, false, false)
                }
            }
        }
        JawabSoal(ind)
    }

    private fun JawabSoal(ind : Int){
        for (i in Screening.screenningSatuData[ind].textJawab.indices) {
            val templateJawab : View = inflater.inflate(R.layout.template_jawabchat, null)
            jawabContainer.addView(templateJawab)

            val textJawab : TextView = templateJawab.findViewById(R.id.jawaban)
            textJawab.text = Screening.screenningSatuData[ind].textJawab[i]

            templateJawab.setOnClickListener{
                TampilkanJawaban(ind, i)
                jawabContainer.removeAllViews()
            }
        }
    }


    private fun GetTime(): String {
        val currentTime = Date()
        val format = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val timeString = format.format(currentTime)

        return timeString
    }

    private fun TampilkanJawaban(ind : Int , ke : Int){
        val templateChatuser: View = inflater.inflate(R.layout.template_chatuser, null)
        linearContainer.addView(templateChatuser)

        var theText = Screening.screenningSatuData[ind].jawabUser[ke]

        nilaiScreening += Screening.screenningSatuData[ind].nilai[ke]

        val text : TextView = templateChatuser.findViewById(R.id.answer_user)
        text.text = theText

        val textTime : TextView = templateChatuser.findViewById(R.id.time_user)
        textTime.text = GetTime()

        handler.postDelayed({
            // Fungsi yang akan dijalankan setelah jeda 2 detik
            NextEventDialog(ind, ke)
        }, 500)
    }

    private fun NextEventDialog(ind : Int, ke: Int){
        var eventNumber : Int = Screening.screenningSatuData[ind].event[ke]

        if( eventNumber == -1){
            Toast.makeText(this, "Sesi Selesai", Toast.LENGTH_SHORT).show()
        }else if(eventNumber == 1){
            ShowEventRespon1(ind)
        }else if(eventNumber == 2){

        }
        else{
            ShowEventRespon0(ind)
        }
    }

    private fun ShowEventRespon0(ind : Int){

        if(EventScreening.eventScreenDataSatu[ind].eventRespon0 == null){

        }else {

            val jumlahData = EventScreening.eventScreenDataSatu[ind].eventRespon0.size - 1

            if (jumlahData == 0) {
                var theText = EventScreening.eventScreenDataSatu[ind].eventRespon0[0]

                ShowTemplateBot(theText, true, true)

            } else if (jumlahData >= 1) {
                for (i in EventScreening.eventScreenDataSatu[ind].eventRespon0.indices) {
                    var theText: String = EventScreening.eventScreenDataSatu[ind].eventRespon0[i]

                    if (i == 0) {
                        ShowTemplateBot(theText, true, false)

                    } else if (i == jumlahData) {
                        ShowTemplateBot(theText, false, true)

                    } else {
                        ShowTemplateBot(theText, false, false)
                    }
                }
            }
        }

        CheckNextIndex(ind)
    }

    private fun ShowEventRespon1(ind : Int){

        if(EventScreening.eventScreenDataSatu[ind].eventRespon1 == null){

        }else {

            val jumlahData = EventScreening.eventScreenDataSatu[ind].eventRespon1.size - 1

            if (jumlahData == 0) {
                var theText = EventScreening.eventScreenDataSatu[ind].eventRespon1[0]

                ShowTemplateBot(theText, true, true)

            } else if (jumlahData >= 1) {
                for (i in EventScreening.eventScreenDataSatu[ind].eventRespon1.indices) {
                    var theText: String = EventScreening.eventScreenDataSatu[ind].eventRespon1[i]

                    if (i == 0) {
                        ShowTemplateBot(theText, true, false)

                    } else if (i == jumlahData) {
                        ShowTemplateBot(theText, false, true)

                    } else {
                        ShowTemplateBot(theText, false, false)
                    }
                }
            }
        }

        CheckNextIndex(ind)
    }

    private fun CheckNextIndex(ind : Int){
        indexKe++
        handler.postDelayed({
            SetProgresSeekbar(indexKe)

            if(indexKe >= maxValue){
                Toast.makeText(this, "Selesai", Toast.LENGTH_SHORT).show()
                TampilkanNilai()
            }else{
                ShowChatBot(indexKe, true)
            } }, 500)
    }

    private fun SetSeekbar(value : Int){
        seekBar.max = value
    }

    private fun SetProgresSeekbar(ind : Int){
        seekBar.progress = ind
        persentasiBar.text = GetPersentase(ind, maxValue) + "%"
    }

    fun GetPersentase(currentValue: Int, maxValue: Int): String {
        if (currentValue < 0 || maxValue <= 0) {
            throw IllegalArgumentException("Nilai saat ini dan nilai maksimal harus lebih besar dari 0")
        }
        val percentage = (currentValue.toDouble() / maxValue) * 100
        val formattedPercentage = if (percentage > 100.0) 100.0 else percentage
        return formattedPercentage.toInt().toString()
    }

    fun TampilkanNilai(){
        template_nilai.visibility = View.VISIBLE
        textNilai.text = nilaiScreening.toString()

            textTingkat.text = "Terima Kasih Telah Menjawab,:)"
            textKeterangan.text = "Kamu akan dialihkan ke menu reflvy"

        btn_selengkapnya.visibility = View.GONE
        roundedNilai.visibility = View.GONE
        btn_selengkapnya.visibility = View.GONE


        try {
            val documentReference = db.collection("users").document(User.userData.userID)

            val updateData = hashMapOf(
                "screeningSatu" to true,
                "nilaiScreening1" to nilaiScreening
            )

            documentReference.set(updateData, SetOptions.merge())
                .addOnSuccessListener {
                    //UpdateUserInfo(uname, tlp, tgl, selectedGender)
                    //UpdateLayout()
                    UpdateUserInfo(nilaiScreening)
                }
                .addOnFailureListener {
                    // Gagal mengganti data
                }

            DataMisi.dataMisiAplikasi[0].statusMisi = DataMisi.dataMisiAplikasi[0].statusMisi.toMutableList().apply {
                set(0, true) // Mengganti nilai pada indeks 0 dengan `true`
            }

            DataMisi.dataMisiAplikasi[0].progresNow = DataMisi.dataMisiAplikasi[0].progresNow.toMutableList().apply {
                set(0, 1) // Mengganti nilai pada indeks 0 dengan `true`
            }
        }catch (e : Exception){
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }

        ApplicationManager.instance.SaveDataMisiAplkasi(this)
    }

    private fun UpdateUserInfo(nilai : Int){

        val sharedPreferences = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)

        val editor = sharedPreferences.edit()
        editor.putBoolean("screening1", true)
        editor.putInt("nilaiScreening1", nilai)
        editor.apply()
    }

    private fun Footer(){
        val includedLayout = findViewById<View>(R.id.footer)
        val home: ImageView = includedLayout.findViewById(R.id.home_icon)
        home.setOnClickListener {
            Toast.makeText(this, "Silahkan Selesaikan Sesi Screening Ini Terlebih Dahulu", Toast.LENGTH_SHORT).show()
        }

        val bot: ImageView = includedLayout.findViewById(R.id.bot_icon)
        bot.setOnClickListener {
            Toast.makeText(this, "Silahkan Selesaikan Sesi Screening Ini Terlebih Dahulu", Toast.LENGTH_SHORT).show()
        }

        val settings: ImageView = includedLayout.findViewById(R.id.setting_icon)
        settings.setOnClickListener {
            Toast.makeText(this, "Silahkan Selesaikan Sesi Screening Ini Terlebih Dahulu", Toast.LENGTH_SHORT).show()
        }

        val btn_back : ImageView = findViewById(R.id.icon_back)
        btn_back.setOnClickListener {
            onBackPressed()
        }
    }
}