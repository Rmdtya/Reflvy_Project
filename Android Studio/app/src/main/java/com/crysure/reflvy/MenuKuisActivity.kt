package com.crysure.reflvy

import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.crysure.reflvy.data.NotifyChat
import com.crysure.reflvy.data.PackKuis

class MenuKuisActivity : AppCompatActivity() {


    private lateinit var container : LinearLayout
    private lateinit var inflater: LayoutInflater
    private lateinit var textMood : TextView

    private lateinit var jenis1 : LinearLayout
    private lateinit var jenis2 : LinearLayout
    private lateinit var jenis3 : LinearLayout

    private var jenisActive : Int = 1

    private var aktif : Int = 0
    private var nonAktif : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_kuis)

        Footer()

        container = findViewById(R.id.container)
        inflater = LayoutInflater.from(this)

        jenis1 = findViewById(R.id.jenis1)
        jenis2 = findViewById(R.id.jenis2)
        jenis3 = findViewById(R.id.jenis3)

        LoadProgres()

        aktif = ContextCompat.getColor(this, R.color.kuisjenis_aktif)
        nonAktif = ContextCompat.getColor(this, R.color.transparant)

        jenis1.backgroundTintList = ColorStateList.valueOf(aktif)
        ShowPack(1)
        jenisActive = 1

        jenis1.setOnClickListener {
            if (jenisActive != 1){
                ResetActive()
                ShowPack(1)
                jenis1.backgroundTintList = ColorStateList.valueOf(aktif)
                jenisActive = 1
            }
        }

        jenis2.setOnClickListener {
            if (jenisActive != 2){
                ResetActive()
                ShowPack(2)
                jenis2.backgroundTintList = ColorStateList.valueOf(aktif)
                jenisActive = 2
            }
        }

        jenis3.setOnClickListener {
            if (jenisActive != 3){
                ResetActive()
                ShowPack(3)
                jenis3.backgroundTintList = ColorStateList.valueOf(aktif)
                jenisActive = 3
            }
        }
    }

    private fun LoadProgres(){
        PackKuis.packKuis[1].status = true
    }

    private fun ShowPack(jenis : Int){

        ClearContainer()

        var index : Int = 0
        for (pack in PackKuis.packKuis){
            if(pack.jenis == jenis){
                val template: View = inflater.inflate(R.layout.template_packkuis, null)
                container.addView(template)

                val textTitle : TextView = template.findViewById(R.id.text_title)
                val textSoal : TextView = template.findViewById(R.id.text_soal)

                val maxLength = 35 // Panjang maksimum yang diinginkan
                val title = pack.title // Ambil judul dari pack

                if (title.length <= maxLength) {
                    textTitle.text = title // Tidak perlu memotong judul jika sudah cukup pendek
                } else {
                    textTitle.text = title.substring(0, maxLength) + "..."
                }

                textSoal.text = pack.jmlSoal.toString() + " Soal"

                if(index == 0){

                }else if(pack.status){

                }else{
                    val imageIcon : ImageView = template.findViewById(R.id.img_icon)
                    imageIcon.setImageResource(R.drawable.kuis_iconlock)
                }

                template.setOnClickListener {
                    val intent = Intent(this, GameplayKuisActivity::class.java)
                    intent.putExtra("INDEX", pack.pack)
                    startActivity(intent)
                }

                index++
            }
        }
    }

    private fun ClearContainer() {
        container.removeAllViews()
    }

    private fun ResetActive(){
        jenis1.backgroundTintList = ColorStateList.valueOf(nonAktif)
        jenis2.backgroundTintList = ColorStateList.valueOf(nonAktif)
        jenis3.backgroundTintList = ColorStateList.valueOf(nonAktif)
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