package com.example.reflvy

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.example.reflvy.data.DataMisi

class MenuMissionActivity : AppCompatActivity() {

    private lateinit var misiApliasi : LinearLayout
    private lateinit var misiKamu : LinearLayout

    private lateinit var textMisiApliasi : TextView
    private lateinit var textMisiKamu : TextView

    private var actimeMenu : Boolean = false
    private var nightMode : Boolean = false

    private lateinit var containerMisi : LinearLayout
    private lateinit var inflater: LayoutInflater

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mission)

        misiApliasi = findViewById(R.id.misi_aplikasi)
        misiKamu = findViewById(R.id.misi_kamu)

        textMisiApliasi = findViewById(R.id.text_misiaplikasi)
        textMisiKamu = findViewById(R.id.text_misikamu)

        containerMisi = findViewById(R.id.container)
        inflater = LayoutInflater.from(this)

        MenuContainerStyle(actimeMenu)

        misiApliasi.setOnClickListener {
            if(!actimeMenu){

            }else{
                actimeMenu = false
                MenuContainerStyle(actimeMenu)
            }
        }

        misiKamu.setOnClickListener {
            if(actimeMenu){

            }else{
                actimeMenu = true
                MenuContainerStyle(actimeMenu)
            }
        }

        val sharedPreferences = getSharedPreferences("PENGATURAN", Context.MODE_PRIVATE)
        // Atur status toogle berdasarkan mode saat itu
        nightMode = sharedPreferences.getBoolean("nightMode", false)

        ShowMission()

    }


    private fun ShowMission(){
        for (misi in DataMisi.dataMisiAplikasi) {
            ShowIt(misi)
        }
    }
    private fun ShowIt(misi : DataMisi){

        val template: View = inflater.inflate(R.layout.template_mission, null)
        containerMisi.addView(template)

        val textJudul : TextView = template.findViewById(R.id.text_title)
        val textDeskripsi : TextView = template.findViewById(R.id.text_deskripsi)
        val progresBar : ProgressBar = template.findViewById(R.id.bar_progres)

        textJudul.text = misi.namaMisi
        textDeskripsi.text = misi.deskripsiMisi

        val linearLayout = template.findViewById<LinearLayout>(R.id.template_star)
        val numberOfImageViews = 3 // Ganti dengan jumlah duplikasi yang diinginkan

        for (i in 0 until misi.jumlahBintang) {
            val imageView = ImageView(this)
            val params = LinearLayout.LayoutParams(
                resources.getDimensionPixelSize(R.dimen.star_misionwidth),
                resources.getDimensionPixelSize(R.dimen.star_misionwidth)
            )
            params.setMargins(
                resources.getDimensionPixelSize(R.dimen.star_misionpadding),
                resources.getDimensionPixelSize(R.dimen.star_misionpadding),
                resources.getDimensionPixelSize(R.dimen.star_misionpadding),
                resources.getDimensionPixelSize(R.dimen.star_misionpadding)
            )
            params.gravity = Gravity.CENTER
            imageView.layoutParams = params
            imageView.setImageResource(R.drawable.mission_starimage)

            linearLayout.addView(imageView)
        }

        template.setOnClickListener {
            val intent = Intent(this, MissionAplicationActivity::class.java)
            intent.putExtra("index", misi.indexNomor)
            startActivity(intent)
        }
    }

    private fun MenuContainerStyle(menu : Boolean){

        if(!menu){
            misiApliasi.setBackgroundResource(R.drawable.mission_leftmenuactive)
            misiKamu.setBackgroundResource(R.drawable.mission_rightmenunonactive)

            textMisiApliasi.setTextAppearance(R.style.fontcolor1)
            textMisiKamu.setTextAppearance(R.style.fontcolor3)
        }else if(menu){
            misiApliasi.setBackgroundResource(R.drawable.mission_leftmenunonactive)
            misiKamu.setBackgroundResource(R.drawable.mission_rightmenuactive)

            textMisiApliasi.setTextAppearance(R.style.fontcolor3)
            textMisiKamu.setTextAppearance(R.style.fontcolor1)
        }
    }
}