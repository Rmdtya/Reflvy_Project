package com.example.reflvy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.example.reflvy.data.ActiveLogin
import com.example.reflvy.data.NotifyChat
import com.example.reflvy.databinding.ActivityDailyCheckinBinding
import com.example.reflvy.databinding.FooterStyle1Binding
import com.example.reflvy.utils.ApplicationManager
import me.tankery.lib.circularseekbar.CircularSeekBar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DailyCheckin : AppCompatActivity() {

    lateinit var binding: ActivityDailyCheckinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDailyCheckinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Footer()

        binding.seekBar.setOnSeekBarChangeListener(object : CircularSeekBar.OnCircularSeekBarChangeListener{
            override fun onProgressChanged(
                circularSeekBar: CircularSeekBar?,
                progress: Float,
                fromUser: Boolean,
            ) {
                val value = progress.toInt()

                if(value >= 0 && value <= 9){
                    binding.moodImage.setImageResource(R.drawable.mood_gelisah)
                    binding.moodText.text = "Gelisah"
                    binding.moodText2.text = "Aku merasa gelisah karena tekanan dan keadaan saat ini"

                }else if(value >= 10 && value <= 19){
                    binding.moodImage.setImageResource(R.drawable.mood_marah)
                    binding.moodText.text = "Marah"
                    binding.moodText2.text = "Emosiku gak setabil hari ini, seringkali marah"

                }else if(value >= 20 && value <= 29){
                    binding.moodImage.setImageResource(R.drawable.mood_sedih)
                    binding.moodText.text = "Sedih"
                    binding.moodText2.text = "Hari ini ada sesuatu yang membuatku sedih"

                }else if(value >= 30 && value <= 39){
                    binding.moodImage.setImageResource(R.drawable.mood_kecewa)
                    binding.moodText.text = "Kecewa"
                    binding.moodText2.text = "Aku sedikit kecewa hari ini"

                }else if(value >= 40 && value <= 49){
                    binding.moodImage.setImageResource(R.drawable.mood_bosan)
                    binding.moodText.text = "Bosan"
                    binding.moodText2.text = "Bosan banget hari ini"

                }else if(value >= 50 && value <= 59){
                    binding.moodImage.setImageResource(R.drawable.mood_netral)
                    binding.moodText.text = "Seperti Biasa"
                    binding.moodText2.text = "Aku menjalani hari ini seperti biasanya"

                }else if(value >= 60 && value <= 69){
                    binding.moodImage.setImageResource(R.drawable.mood_senang)
                    binding.moodText.text = "Senang"
                    binding.moodText2.text = "Aku senang hari ini karena sesuatu"

                }else if(value >= 70 && value <= 79){
                    binding.moodImage.setImageResource(R.drawable.mood_bersemangat)
                    binding.moodText.text = "Bersemangat"
                    binding.moodText2.text = "Aku menjalani hari ini dengan bersemangat"

                }else if(value >= 80 && value <= 89){
                    binding.moodImage.setImageResource(R.drawable.mood_riang)
                    binding.moodText.text = "Riang"
                    binding.moodText2.text = "Moodku merasa riang dan gembira hari ini"

                }else if(value >= 90 && value <= 100){
                    binding.moodImage.setImageResource(R.drawable.mood_bahagia)
                    binding.moodText.text = "Bahagia"
                    binding.moodText2.text = "Aku merasa bahagia banget hari ini"

                }
            }

            override fun onStartTrackingTouch(seekBar: CircularSeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: CircularSeekBar?) {
                println("seek bar ${seekBar!!.progress}")
            }

        })

        binding.btnConfirm.setOnClickListener {
            if(binding.moodText.text.toString().trim() == "Pilih Mood Mu"){
                Toast.makeText(this@DailyCheckin, "Silahkan pilih mood mu hari ini", Toast.LENGTH_SHORT).show()
            }else{
                val mood: String = binding.moodText.text.toString()

                val currentDate = Calendar.getInstance().time
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val tglNow = dateFormat.format(currentDate).trim()

                ActiveLogin.infoActive.totalActive++
                ActiveLogin.infoActive.activeNow = true
                ActiveLogin.infoActive.lastActive = tglNow
                ActiveLogin.infoActive.moodNow = mood

                //ActiveLogin.infoActive.updateInfo( ActiveLogin.infoActive.totalActive++, true, tglNow, mood)

                ApplicationManager.instance.SaveDataLogin(this@DailyCheckin)
                GetNotifChat(mood)

                val intent = Intent(this, DasboardDailyActivity::class.java)
                startActivity(intent)
                finishAffinity()
            }
        }

        binding.btnBack.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }

    }

    private fun GetNotifChat(mood : String){
        if(mood == "Gelisah"){
            val chat1 = NotifyChat("Kamu Gelisah Hari Ini? Apa karena masalah akademik atau masalah dalam diri kamu?", "bot", true, false, "", false, "")
            NotifyChat.notifChat.add(chat1)

            val chat2 = NotifyChat("Aku Saranin kamu membaca artikel berikut. Mungkin setelah membaca ini rasa gelisah kamu mereda. <i><b>Klik Disini</b></i>.", "bot", false, false, "", true, "artikel")
            NotifyChat.notifChat.add(chat2)

            val time : String = GetTime()
            val chat3 = NotifyChat("Atau Mungkin Kamu butuh teman untuk berbagi cerita, aku bisa dengerin curhatan kamu.", "bot", false, true, time, false, "")
            NotifyChat.notifChat.add(chat3)

            ApplicationManager.instance.ActiveNotif(this)
            ApplicationManager.instance.SaveNotifPrefs(this)

        }else if(mood == "Marah"){

        }else if(mood == "Sedih"){

        }else if(mood == "Kecewa"){

        }else if(mood == "Bosan"){

        }else if(mood == "Seperti Biasa"){

        }else if(mood == "Senang"){

        }else if(mood == "Bersemangat"){

        }else if(mood == "Riang"){

        }else if(mood == "Bahagia"){

        }else{

        }

    }

    private fun GetTime(): String {
        val currentTime = Date()
        val format = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val timeString = format.format(currentTime)

        return timeString
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