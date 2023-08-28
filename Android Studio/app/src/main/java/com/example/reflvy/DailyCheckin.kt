package com.example.reflvy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.reflvy.databinding.ActivityDailyCheckinBinding
import me.tankery.lib.circularseekbar.CircularSeekBar

class DailyCheckin : AppCompatActivity() {

    lateinit var binding: ActivityDailyCheckinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDailyCheckinBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

    }
}