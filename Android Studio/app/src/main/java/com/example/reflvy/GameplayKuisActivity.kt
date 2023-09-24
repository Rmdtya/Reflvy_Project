package com.example.reflvy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.example.reflvy.data.DataKuis
import com.example.reflvy.data.NotifyChat
import com.example.reflvy.data.PackKuis

class GameplayKuisActivity : AppCompatActivity() {

    private lateinit var textSoal : TextView
    private lateinit var textNomor : TextView
    private lateinit var textBenar : TextView
    private lateinit var textSalah : TextView
    private lateinit var textOpsiA : TextView
    private lateinit var textOpsiB : TextView
    private lateinit var textOpsiC : TextView
    private lateinit var textOpsiD : TextView

    private lateinit var containerGambar : LinearLayout
    private lateinit var gambarSoal : ImageView

    private lateinit var barBenar : ProgressBar
    private lateinit var barSalah : ProgressBar
    private lateinit var btnNext : LinearLayout

    private lateinit var linearA : LinearLayout
    private lateinit var linearB : LinearLayout
    private lateinit var linearC : LinearLayout
    private lateinit var linearD : LinearLayout

    private var soalKe : Int = 0
    private var totalBenar : Int = 0
    private var totalSalah : Int = 0
    private var hasilAkhir : Int =0
    private var indexSoal : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gameplay_kuis)

      Footer()

        val index = intent.getIntExtra("INDEX", 0)
        InisialisasiUI()

        LoadPack(index)
        soalKe = 0
        totalBenar = 0
        totalSalah = 0

        btnNext.setOnClickListener {
            indexSoal++
            LoadSoal(indexSoal)
            ResetOpsiJawaban()
            soalKe++
        }

    }

    private fun LoadPack(pack : Int){
        val index = PackKuis.packKuis[pack].startIndex
        val totalSoal = PackKuis.packKuis[pack].jmlSoal

        barBenar.max = totalSoal
//        barSalah.max = totalSoal

        LoadSoal(index)
        indexSoal = index
    }

    private fun LoadSoal(index : Int){
        val soalYang = soalKe + 1
        val soal = DataKuis.dataKuis[index].soal
        val imageStatus = DataKuis.dataKuis[index].image
        val link = DataKuis.dataKuis[index].linkImage
        val a = DataKuis.dataKuis[index].opsi[0]
        val b = DataKuis.dataKuis[index].opsi[1]
        val c = DataKuis.dataKuis[index].opsi[2]
        val d = DataKuis.dataKuis[index].opsi[3]


        SetUI(soalYang,soal ,imageStatus ,link , totalBenar, totalSalah,
           a ,b ,c ,d)
    }

    private fun SetUI(no : Int, soal : String, image : Boolean, imageLink : String, bnr : Int, slh : Int, a : String, b : String, c : String, d : String){

        textSoal.text = no.toString()
        textSoal.text = soal
//        textBenar.text = bnr.toString()
//        textSalah.text = slh.toString()

        barBenar.progress = bnr
//        barSalah.progress = slh

        textOpsiA.text = "A.  " + a
        textOpsiB.text = "B.  " + b
        textOpsiC.text = "C.  " + c
        textOpsiD.text = "D.  " + d

        if(image){

        }else{
            containerGambar.visibility = View.GONE
        }

        btnNext.visibility = View.GONE
        UIOpsiJawaban(true)
        JawabSoal()
    }

    private fun InisialisasiUI(){
        textSoal = findViewById(R.id.text_soal)

//        textBenar = findViewById(R.id.text_benar)
        barBenar = findViewById(R.id.bar_banar)

        textOpsiA = findViewById(R.id.opsi_a)
        textOpsiB = findViewById(R.id.opsi_b)
        textOpsiC = findViewById(R.id.opsi_c)
        textOpsiD = findViewById(R.id.opsi_d)

        containerGambar = findViewById(R.id.container_image)
        gambarSoal = findViewById(R.id.image_soal)

        btnNext = findViewById(R.id.next_btn)

        linearA = findViewById(R.id.linear_a)
        linearB = findViewById(R.id.linear_b)
        linearC = findViewById(R.id.linear_c)
        linearD = findViewById(R.id.linear_d)

    }

    private fun UIOpsiJawaban(kondisi : Boolean){
        textOpsiA.isEnabled = kondisi
        textOpsiB.isEnabled = kondisi
        textOpsiC.isEnabled = kondisi
        textOpsiD.isEnabled = kondisi
    }

    private fun JawabSoal(){
        textOpsiA.setOnClickListener{
            CekKebenaran(0)
            UIOpsiJawaban(false)
        }

        textOpsiB.setOnClickListener{
            CekKebenaran(1)
            UIOpsiJawaban(false)
        }

        textOpsiC.setOnClickListener{
            CekKebenaran(2)
            UIOpsiJawaban(false)
        }

        textOpsiD.setOnClickListener{
            CekKebenaran(3)
            UIOpsiJawaban(false)
        }
    }

    private fun CekKebenaran(opsi : Int){
        val jawabanBenar = DataKuis.dataKuis[indexSoal].jawaban

        if (opsi == 0){
            linearA.setBackgroundResource(R.drawable.kuis_strokesalah)
        }else if(opsi == 1){
            linearB.setBackgroundResource(R.drawable.kuis_strokesalah)
        }else if(opsi == 2){
            linearC.setBackgroundResource(R.drawable.kuis_strokesalah)
        }else if(opsi == 3){
            linearD.setBackgroundResource(R.drawable.kuis_strokesalah)
        }

        if(opsi == jawabanBenar){
            totalBenar++
        }else{
            totalSalah++
        }

        if (jawabanBenar == 0){
            linearA.setBackgroundResource(R.drawable.kuis_strokebenar)
        }else if(jawabanBenar == 1){
            linearB.setBackgroundResource(R.drawable.kuis_strokebenar)
        }else if(jawabanBenar == 2){
            linearC.setBackgroundResource(R.drawable.kuis_strokebenar)
        }else if(jawabanBenar == 3){
            linearD.setBackgroundResource(R.drawable.kuis_strokebenar)
        }

        btnNext.visibility = View.VISIBLE
    }

    private fun ResetOpsiJawaban(){
        linearA.setBackgroundResource(R.drawable.kuis_strokeopsi)
        linearB.setBackgroundResource(R.drawable.kuis_strokeopsi)
        linearC.setBackgroundResource(R.drawable.kuis_strokeopsi)
        linearD.setBackgroundResource(R.drawable.kuis_strokeopsi)
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