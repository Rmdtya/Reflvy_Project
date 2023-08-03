package com.example.reflvy

import android.graphics.Typeface
import android.graphics.text.LineBreaker
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.reflvy.data.Music
import com.example.reflvy.data.News

class NewsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        val receivedData = intent.getIntExtra("key", 0)
//
//        val data = newsList[0].newsID
//
//        Toast.makeText(this, data, Toast.LENGTH_SHORT).show()
        ShowNews(receivedData)
    }

    fun ShowNews(indexNews: Int) {

        val image: ImageView = findViewById(R.id.img_info)
        val title: TextView = findViewById(R.id.title_text)
        val linearLayoutContainer: LinearLayout = findViewById(R.id.list_paragraf)
        val textViewTemplate = findViewById<TextView>(R.id.template_paragraf) // Replace 'template_paragraf' with the actual ID of your template TextView

        for (news in News.newsList) {
            if (news.newsID == indexNews) {

                // Ubah gambar dan teks di setiap template
                Glide.with(this)
                    .load(news.img)
                    .into(image)

                title.text = news.title

                for (i in news.paragraphs.indices) {
                    val textView = TextView(this)
                    textView.layoutParams = textViewTemplate.layoutParams // Set the same layout params as the template TextView

                    textView.text = news.paragraphs[i]

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
        }
    }
}