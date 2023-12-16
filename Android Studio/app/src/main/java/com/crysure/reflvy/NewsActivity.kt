package com.crysure.reflvy

import android.content.Intent
import android.graphics.text.LineBreaker
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.crysure.reflvy.data.News
import com.crysure.reflvy.data.NotifyChat

class NewsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        Footer()

        val receivedData = intent.getIntExtra("key", 0)
        ShowNews(receivedData)
    }

    fun ShowNews(indexNews: Int) {

        val image: ImageView = findViewById(R.id.img_info)
        val title: TextView = findViewById(R.id.title_text)
        val linearLayoutContainer: LinearLayout = findViewById(R.id.list_paragraf)
        val textViewTemplate = findViewById<TextView>(R.id.template_paragraf)

                Glide.with(this)
                    .load(News.newsList[0].img[indexNews])
                    .into(image)

                title.text = News.newsList[0].title[indexNews]

                val paragraphText = News.newsList[0].paragraphs[indexNews]

                val paragraphs = paragraphText.split("#")
                for (paragraph in paragraphs) {

                    val textView = TextView(this)
                    textView.layoutParams = textViewTemplate.layoutParams // Set the same layout params as the template TextView
                    textView.text = paragraph.trim()

                    textView.textSize = 14f // Change the value as needed
                    textView.typeface = textViewTemplate.typeface
                    textView.setTextColor(textViewTemplate.currentTextColor)

                    textView.visibility = View.VISIBLE
                    textView.textAlignment = View.TEXT_ALIGNMENT_TEXT_START

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        textView.justificationMode = LineBreaker.JUSTIFICATION_MODE_INTER_WORD
                    }

                    linearLayoutContainer.addView(textView)
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
}