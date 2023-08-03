package com.example.reflvy

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import com.example.reflvy.data.SaveDataScreening
import com.example.reflvy.data.Screening
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


private const val PREF_NAME = "LastScreenData"
private const val PREF_SAVE_DATA_KEY = "saveData"
class ScreeningActivity : AppCompatActivity() {

    private lateinit var linearContainer: LinearLayout
    private lateinit var inflater: LayoutInflater
    private lateinit var seekBar: SeekBar
    private lateinit var persentasiBar: TextView
    private var historyChat : MutableList<String> = mutableListOf()
    private var fromBot : MutableList<Boolean> = mutableListOf()
    private var timeText : MutableList<Boolean> = mutableListOf()

    val handler = Handler(Looper.getMainLooper())

    companion object{
        var indexKe : Int = 0
        var maxValue : Int = 0
    }

    private lateinit var jawabContainer : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screening)

        linearContainer = findViewById(R.id.chat_container)
        jawabContainer = findViewById(R.id.jawab_container)
        inflater = LayoutInflater.from(this)
        seekBar = findViewById(R.id.progres_bar)

        persentasiBar = findViewById(R.id.persentasi)
        persentasiBar.text = "0%"

        LoadData(this)

        indexKe = 0
        maxValue = Screening.screenData.size
        ShowChatBot(indexKe, true)
        SetSeekbar(maxValue)
    }

    private fun ShowChatBot(ind : Int, icon : Boolean){

        val jumlahData = Screening.screenData[ind].soalbot.size - 1

        if (jumlahData == 0 && icon == true){
            val templateChatbot: View = inflater.inflate(R.layout.template_chatbot, null)
            linearContainer.addView(templateChatbot)
            val soal : TextView = templateChatbot.findViewById(R.id.answer)
            val theText = Screening.screenData[ind].soalbot[0]
            soal.text = theText

            val textTime: TextView = templateChatbot.findViewById(R.id.time)
            textTime.text = GetTime()

            historyChat.add(theText)
            fromBot.add(true)
            timeText.add(true)

        }else if(jumlahData >= 1) {
            for (i in Screening.screenData[ind].soalbot.indices) {
                val templateChatbot: View = inflater.inflate(R.layout.template_chatbot, null)

                var theText = Screening.screenData[ind].soalbot[i]

                if (i == 0) {
                    linearContainer.addView(templateChatbot)
                    val textTime: TextView = templateChatbot.findViewById(R.id.time)
                    textTime.visibility = View.GONE

                    val soal : TextView = templateChatbot.findViewById(R.id.answer)
                    soal.text = theText
                    timeText.add(false)

                } else if (i == jumlahData) {
                    linearContainer.addView(templateChatbot)
                    val iconBot: ImageView = templateChatbot.findViewById(R.id.icon_bot)
                    iconBot.visibility = View.INVISIBLE

                    val textTime: TextView = templateChatbot.findViewById(R.id.time)
                    textTime.text = GetTime()

                    val soal : TextView = templateChatbot.findViewById(R.id.answer)
                    soal.text = theText

                    timeText.add(true)

                } else {
                    linearContainer.addView(templateChatbot)
                    val iconBot: ImageView = templateChatbot.findViewById(R.id.icon_bot)
                    iconBot.visibility = View.INVISIBLE

                    val textTime: TextView = templateChatbot.findViewById(R.id.time)
                    textTime.visibility = View.GONE

                    val soal : TextView = templateChatbot.findViewById(R.id.answer)
                    soal.text = theText

                    timeText.add(false)
                }

                historyChat.add(theText)
                fromBot.add(true)
            }
        }
        JawabSoal(ind)
    }

    private fun JawabSoal(ind : Int){
        for (i in Screening.screenData[ind].textJawab.indices) {
            val templateJawab : View = inflater.inflate(R.layout.template_jawabchat, null)
            jawabContainer.addView(templateJawab)

            val textJawab : TextView = templateJawab.findViewById(R.id.jawaban)
            textJawab.text = Screening.screenData[ind].textJawab[i]

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

        var theText = Screening.screenData[ind].jawabUser[ke]

        val text : TextView = templateChatuser.findViewById(R.id.answer)
        text.text = theText

        val textTime : TextView = templateChatuser.findViewById(R.id.time)
        textTime.text = GetTime()

        historyChat.add(theText)
        fromBot.add(false)
        timeText.add(true)

        handler.postDelayed({
            // Fungsi yang akan dijalankan setelah jeda 2 detik
            NextEventDialog(ind, ke)
        }, 2000)
    }

    private fun NextEventDialog(ind : Int, ke: Int){
        if(Screening.screenData[ind].event[ke] == 1){
            ShowEventRespon(ind, ke)
        }else{
            indexKe++
            SetProgresSeekbar(indexKe)

            if(indexKe >= maxValue){
                Toast.makeText(this, "Selesai yang 1", Toast.LENGTH_SHORT).show()
            }else{
                ShowChatBot(indexKe, true)
            }
        }
    }

    private fun ShowEventRespon(ind : Int, ke : Int){
        val jumlahData = Screening.screenData[ind].eventRespon.size - 1

        if (jumlahData == 0){
            val templateChatbot: View = inflater.inflate(R.layout.template_chatbot, null)
            linearContainer.addView(templateChatbot)

            var theText = Screening.screenData[ind].eventRespon[0]
            val soal : TextView = templateChatbot.findViewById(R.id.answer)
            soal.text = theText

            val textTime: TextView = templateChatbot.findViewById(R.id.time)
            textTime.text = GetTime()

            historyChat.add(theText)
            fromBot.add(true)
            timeText.add(true)

        }else if(jumlahData >= 1) {
            for (i in Screening.screenData[ind].eventRespon.indices) {
                val templateChatbot: View = inflater.inflate(R.layout.template_chatbot, null)

                if (i == 0) {
                    linearContainer.addView(templateChatbot)
                    val textTime: TextView = templateChatbot.findViewById(R.id.time)
                    textTime.visibility = View.GONE

                    val soal : TextView = templateChatbot.findViewById(R.id.answer)
                    soal.text = Screening.screenData[ind].eventRespon[i]

                    timeText.add(false)

                } else if (i == jumlahData) {
                    linearContainer.addView(templateChatbot)
                    val iconBot: ImageView = templateChatbot.findViewById(R.id.icon_bot)
                    iconBot.visibility = View.INVISIBLE

                    val textTime: TextView = templateChatbot.findViewById(R.id.time)
                    textTime.text = GetTime()

                    val soal : TextView = templateChatbot.findViewById(R.id.answer)
                    soal.text = Screening.screenData[ind].eventRespon[i]

                    timeText.add(true)

                } else {
                    linearContainer.addView(templateChatbot)
                    val iconBot: ImageView = templateChatbot.findViewById(R.id.icon_bot)
                    iconBot.visibility = View.INVISIBLE

                    val textTime: TextView = templateChatbot.findViewById(R.id.time)
                    textTime.visibility = View.GONE

                    val soal : TextView = templateChatbot.findViewById(R.id.answer)
                    soal.text = Screening.screenData[ind].eventRespon[i]

                    timeText.add(false)
                }
            }
        }

        if(ind == 0){
            Toast.makeText(this, "Sesi Selesai", Toast.LENGTH_SHORT).show()
        }else if(ind != 0){
            handler.postDelayed({
                indexKe++
                SetProgresSeekbar(indexKe)

                if(indexKe >= maxValue){
                    Toast.makeText(this, "Selesai", Toast.LENGTH_SHORT).show()
                }else{
                    ShowChatBot(indexKe, true)
                }
            }, 7000)
        }
    }

    private fun SetSeekbar(value : Int){
        seekBar.max = value
    }

    private fun SetProgresSeekbar(ind : Int){
        seekBar.progress = ind
        persentasiBar.text = GetPersentase(ind, maxValue).toString() + "%"
    }

    fun GetPersentase(currentValue: Int, maxValue: Int): Double {
        if (currentValue < 0 || maxValue <= 0) {
            throw IllegalArgumentException("Nilai saat ini dan nilai maksimal harus lebih besar dari 0")
        }
        val percentage = (currentValue.toDouble() / maxValue) * 100
        return if (percentage > 100.0) 100.0 else percentage
    }

    object SharedPrefsUtil {

        private fun getSharedPreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        }

        fun saveSaveDataList(context: Context, dataList: List<SaveDataScreening>) {
            val sharedPreferences = getSharedPreferences(context)
            val editor = sharedPreferences.edit()
            val gson = Gson()
            val dataListJson = gson.toJson(dataList)
            editor.putString(PREF_SAVE_DATA_KEY, dataListJson)
            editor.apply()
        }

        fun loadSaveDataList(context: Context): List<SaveDataScreening> {
            val sharedPreferences = getSharedPreferences(context)
            val dataListJson = sharedPreferences.getString(PREF_SAVE_DATA_KEY, null)
            val gson = Gson()
            val itemType = object : TypeToken<List<SaveDataScreening>>() {}.type
            return gson.fromJson(dataListJson, itemType) ?: emptyList()
        }

        fun RemoveDataByKey(context: Context) {
            SaveDataScreening.lastData.clear()
            val sharedPreferences = getSharedPreferences(context)
            val editor = sharedPreferences.edit()
            editor.remove(PREF_SAVE_DATA_KEY)
            editor.apply()
        }
    }

    private fun SavingData(context: Context){
        SharedPrefsUtil.saveSaveDataList(context, SaveDataScreening.lastData)
    }

    private fun LoadData(context: Context) {
        // Menghapus data sebelum memuat data baru

        val loadedDataList = SharedPrefsUtil.loadSaveDataList(context)
        if (loadedDataList.isNotEmpty()) {
            // Membersihkan lastData sebelum menambahkan data baru
            SaveDataScreening.lastData.clear()

            var index = 0
            for (data in loadedDataList) {
                val saveData = SaveDataScreening(
                    pertanyaan = data.pertanyaan,
                    historyObrolan = data.historyObrolan,
                    fromBot = data.fromBot,
                    textTime = data.textTime,
                    nilaiTerakir = data.nilaiTerakir
                )

                Toast.makeText(this, index.toString(), Toast.LENGTH_SHORT).show()

                index++

                SaveDataScreening.lastData.add(saveData)

                println("Pertanyaan : " + SaveDataScreening.lastData[0].pertanyaan)
                println("History Obrolan: " + SaveDataScreening.lastData[0].historyObrolan[1])
                println("From Bot ?:" + SaveDataScreening.lastData[0].fromBot)
                println("textTime ?: + " + SaveDataScreening.lastData[0].textTime)
                println("nilai Terakhir: " + SaveDataScreening.lastData[0].nilaiTerakir)
                println("--------------------")
            }
        } else {
            // Data tidak ditemukan di SharedPreferences
            println("Data tidak ditemukan.")
        }

    }

    private fun InsertIntoSaveData(){
        SaveDataScreening.lastData.clear()
        val saveData = SaveDataScreening(
            pertanyaan = indexKe,
            historyObrolan = historyChat,
            fromBot = fromBot,
            textTime = timeText,
            nilaiTerakir = 100 // Ganti dengan nilai terakhir yang sesuai
        )
        SharedPrefsUtil.RemoveDataByKey(this)
        SaveDataScreening.lastData.add(saveData)
        SavingData(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Panggil fungsi yang ingin Anda eksekusi ketika activity dihancurkan di sini
        // Misalnya, menyimpan data terakhir ke SharedPreferences atau melakukan pembersihan
        InsertIntoSaveData()
    }
}