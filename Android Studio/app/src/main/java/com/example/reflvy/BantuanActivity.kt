package com.example.reflvy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.reflvy.data.NotifyChat

class BantuanActivity : AppCompatActivity() {
    private lateinit var accordion1: LinearLayout
    private lateinit var accordion2: LinearLayout
    private lateinit var accordion3: LinearLayout

    private lateinit var background1 : LinearLayout
    private lateinit var background2 : LinearLayout
    private lateinit var background3 : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bantuan)

        val colorBackground = ContextCompat.getColor(this, R.color.background_bantuan)

        Footer()

        accordion1 = findViewById(R.id.acc_layout1)
        accordion2 = findViewById(R.id.acc_layout2)
        accordion3 = findViewById(R.id.acc_layout3)

        background1  = findViewById(R.id.layout1)
        background2 = findViewById(R.id.layout2)
        background3 = findViewById(R.id.layout3)

        val question1: ImageView = findViewById(R.id.icon_list1)
        val question2: ImageView = findViewById(R.id.icon_list2)
        val question3: ImageView = findViewById(R.id.icon_list3)

        accordion1.visibility = View.VISIBLE
        accordion2.visibility = View.GONE
        accordion3.visibility = View.GONE

        question1.setImageResource(R.drawable.iconlist_on)
        background1.setBackgroundColor(colorBackground)

        SetText()

        question1.setOnClickListener { toggleAccordion(accordion1, question1, background1) }
        question2.setOnClickListener { toggleAccordion(accordion2, question2, background2) }
        question3.setOnClickListener { toggleAccordion(accordion3, question3, background3) }
    }

    fun goBack(view: View) {
        // Kembali ke Activity sebelumnya
        onBackPressed()
    }

    private fun toggleAccordion(layout: LinearLayout, icon : ImageView, background : LinearLayout) {
        val colorBackground = ContextCompat.getColor(this, R.color.background_bantuan)

        if (layout.visibility == View.VISIBLE) {
            layout.visibility = View.GONE
        } else {
            accordion1.visibility = View.GONE
            accordion2.visibility = View.GONE
            accordion3.visibility = View.GONE

            layout.visibility = View.VISIBLE
            val question3: TextView = layout.findViewById(R.id.answer_user)
            icon.setImageResource(R.drawable.iconlist_on)
            background.setBackgroundColor(colorBackground)

            if (background != background1) {
                background1.setBackgroundColor(0)
            }
            if (background != background2) {
                background2.setBackgroundColor(0)
            }
            if (background != background3) {
                background3.setBackgroundColor(0)
            }
        }
    }

    private fun SetText(){
        val answer1: TextView = accordion1.findViewById(R.id.answer_user)
        answer1.text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla a nunc auctor, porttitor lectus in, finibus magna. Donec pharetra rutrum venenatis. Etiam enim metus, interdum et diam eu, maximus pretium purus. Nunc bibendum eu risus ut placerat."


        val answer2: TextView = accordion2.findViewById(R.id.answer_user)
        answer2.text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla a nunc auctor, porttitor lectus in, finibus magna. Donec pharetra rutrum venenatis. Etiam enim metus, interdum et diam eu, maximus pretium purus. Nunc bibendum eu risus ut placerat."


        val answer3: TextView = accordion3.findViewById(R.id.answer_user)
        answer3.text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla a nunc auctor, porttitor lectus in, finibus magna. Donec pharetra rutrum venenatis. Etiam enim metus, interdum et diam eu, maximus pretium purus. Nunc bibendum eu risus ut placerat."

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