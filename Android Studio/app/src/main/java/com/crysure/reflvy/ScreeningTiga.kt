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
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.crysure.reflvy.data.EventScreening
import com.crysure.reflvy.data.NotifyChat
import com.crysure.reflvy.data.SaveDataScreening
import com.crysure.reflvy.data.Screening
import com.crysure.reflvy.data.User
import com.crysure.reflvy.databinding.ActivityScreeningTigaBinding
import com.google.common.reflect.TypeToken
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val PREF_NAME = "LastScreenTiga"
private const val PREF_SAVE_DATA_KEY = "saveDataTigas"

class ScreeningTiga : AppCompatActivity() {
    private lateinit var binding: ActivityScreeningTigaBinding
    private lateinit var inflater: LayoutInflater
    private lateinit var linearContainer: LinearLayout

    private var historyChat : MutableList<String> = mutableListOf()
    private var fromBot : MutableList<Boolean> = mutableListOf()
    private var timeText : MutableList<Boolean> = mutableListOf()
    private var historyTime : MutableList<String> = mutableListOf()
    private var showIcon : MutableList<Boolean> = mutableListOf()
    private var loadTimeIndex: Int = 0
    private val db = Firebase.firestore
    val handler = Handler(Looper.getMainLooper())

    companion object{
        var indexKe : Int = 0
        var maxValue : Int = 0
        var nilaiScreening : Int = 0
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScreeningTigaBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        LoadData(this)

        Footer()

        ScreeningTiga.indexKe = 0
        ScreeningTiga.maxValue = Screening.screeningTiga.size
    }

    private fun ShowTemplateBot(text : String, icon : Boolean, time : Boolean){

        val templateChatbot: View = inflater.inflate(R.layout.template_chatbot, null)
        binding.chatContainer.addView(templateChatbot)

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

        showIcon.add(icon)
        timeText.add(time)
        historyChat.add(text)
        historyTime.add(stringTime)
        fromBot.add(true)
    }

    private fun ShowChatBot(ind : Int, icon : Boolean){

        val jumlahData = Screening.screeningTiga[ind].soalbot.size - 1

        if (jumlahData == 0 && icon == true){
            val theText = Screening.screeningTiga[ind].soalbot[0]

            ShowTemplateBot(theText, true, true)

        }else if(jumlahData >= 1) {
            for (i in Screening.screeningTiga[ind].soalbot.indices) {

                var theText = Screening.screeningTiga[ind].soalbot[i]

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
        for (i in Screening.screeningTiga[ind].textJawab.indices) {
            val templateJawab : View = inflater.inflate(R.layout.template_jawabchat, null)

                binding.jawabContainer.addView(templateJawab)

            val textJawab : TextView = templateJawab.findViewById(R.id.jawaban)
            textJawab.text = Screening.screeningTiga[ind].textJawab[i]

            templateJawab.setOnClickListener{
                TampilkanJawaban(ind, i)
                binding.jawabContainer.removeAllViews()
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

        var theText = Screening.screeningTiga[ind].jawabUser[ke]

        ScreeningTiga.nilaiScreening += Screening.screeningTiga[ind].nilai[ke]

        val text : TextView = templateChatuser.findViewById(R.id.answer_user)
        text.text = theText

        val textTime : TextView = templateChatuser.findViewById(R.id.time_user)
        textTime.text = GetTime()

        historyChat.add(theText)
        fromBot.add(false)
        timeText.add(true)
        historyTime.add(GetTime())
        showIcon.add(false)

        binding.schrollContainer.post {
            binding.schrollContainer.fullScroll(ScrollView.FOCUS_DOWN)
        }

        handler.postDelayed({
            // Fungsi yang akan dijalankan setelah jeda 2 detik
            NextEventDialog(ind, ke)
        }, 500)
    }

    private fun NextEventDialog(ind : Int, ke: Int){
        var eventNumber : Int = Screening.screeningTiga[ind].event[ke]

        if( eventNumber == -1){
            Toast.makeText(this, "Sesi Selesai", Toast.LENGTH_SHORT).show()
        }else if(eventNumber == 1){
            ShowEventRespon1(ind)
        }
        else{
            ShowEventRespon0(ind)
        }
    }

    private fun ShowEventRespon0(ind : Int){

        if(EventScreening.eventScreenTiga[ind].eventRespon0 == null){

        }else {

            val jumlahData = EventScreening.eventScreenTiga[ind].eventRespon0.size - 1

            if (jumlahData == 0) {
                var theText = EventScreening.eventScreenTiga[ind].eventRespon0[0]

                ShowTemplateBot(theText, true, true)

            } else if (jumlahData >= 1) {
                for (i in EventScreening.eventScreenTiga[ind].eventRespon0.indices) {
                    var theText: String = EventScreening.eventScreenTiga[ind].eventRespon0[i]

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

        binding.schrollContainer.post {
            binding.schrollContainer.fullScroll(ScrollView.FOCUS_DOWN)
        }
    }

    private fun ShowEventRespon1(ind : Int){

        if(EventScreening.eventScreenTiga[ind].eventRespon1 == null){

        }else {

            val jumlahData = EventScreening.eventScreenTiga[ind].eventRespon1.size - 1

            if (jumlahData == 0) {
                var theText = EventScreening.eventScreenTiga[ind].eventRespon1[0]

                ShowTemplateBot(theText, true, true)

            } else if (jumlahData >= 1) {
                for (i in EventScreening.eventScreenTiga[ind].eventRespon1.indices) {
                    var theText: String = EventScreening.eventScreenTiga[ind].eventRespon1[i]

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

        binding.schrollContainer.post {
            binding.schrollContainer.fullScroll(ScrollView.FOCUS_DOWN)
        }
    }


    private fun CheckNextIndex(ind : Int){
        ScreeningTiga.indexKe++
        handler.postDelayed({

            if(ScreeningTiga.indexKe >= ScreeningTiga.maxValue){
                Toast.makeText(this, "Selesai", Toast.LENGTH_SHORT).show()
            }else{
                ShowChatBot(ScreeningTiga.indexKe, true)
            } }, 500)
    }

    fun TampilkanNilai(){
        UpdateUserInfo(ScreeningTiga.nilaiScreening, true)
    }

    private fun UpdateUserInfo(nilai : Int, status : Boolean){

        val sharedPreferences = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)

        User.userData.nilaiScreening3 = User.userData.nilaiScreening3
        User.userData.screeningTiga = status
        val editor = sharedPreferences.edit()
        editor.putBoolean("screening3", status)
        editor.putInt("nilaiScreening3", nilai)
        editor.apply()

        User.userData.loadFromSharedPreferences(sharedPreferences)
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
            SaveDataScreening.lastDataTiga.clear()
            val sharedPreferences = getSharedPreferences(context)
            val editor = sharedPreferences.edit()
            editor.remove(PREF_SAVE_DATA_KEY)
            editor.apply()
        }
    }

    private fun SavingData(context: Context){
        SharedPrefsUtil.saveSaveDataList(context, SaveDataScreening.lastDataTiga)
    }

    private fun LoadData(context: Context) {
        // Menghapus data sebelum memuat data baru

        val loadedDataList = SharedPrefsUtil.loadSaveDataList(context)
        if (loadedDataList.isNotEmpty()) {
            // Membersihkan lastData sebelum menambahkan data baru
            SaveDataScreening.lastDataTiga.clear()

            for (data in loadedDataList) {
                val saveData = SaveDataScreening(
                    pertanyaan = data.pertanyaan,
                    historyObrolan = data.historyObrolan,
                    fromBot = data.fromBot,
                    textTime = data.textTime,
                    getTime = data.getTime,
                    icon = data.icon,
                    nilaiTerakir = data.nilaiTerakir
                )

                ScreeningTiga.indexKe = data.pertanyaan
                ScreeningTiga.nilaiScreening = data.nilaiTerakir
                SaveDataScreening.lastDataTiga.add(saveData)

                println("Pertanyaan : " + SaveDataScreening.lastDataTiga[0].pertanyaan)
                println("History Obrolan: " + SaveDataScreening.lastDataTiga[0].historyObrolan)
                println("From Bot ?:" + SaveDataScreening.lastDataTiga[0].fromBot)
                println("textTime ?: + " + SaveDataScreening.lastDataTiga[0].textTime)
                println("nilai Terakhir: " + SaveDataScreening.lastDataTiga[0].nilaiTerakir)
                println("--------------------")

            }
            ShowLoadHistory()
        } else {
            // Data tidak ditemukan di SharedPreferences
            println("Data tidak ditemukan.")
            ShowChatBot(ScreeningTiga.indexKe, true)
        }

    }

    private fun InsertIntoSaveData(){
        SaveDataScreening.lastDataTiga.clear()
        val saveData = SaveDataScreening(
            pertanyaan = ScreeningTiga.indexKe,
            historyObrolan = historyChat,
            fromBot = fromBot,
            textTime = timeText,
            getTime = historyTime,
            icon = showIcon,
            nilaiTerakir = User.userData.nilaiScreening3 // Ganti dengan nilai terakhir yang sesuai
        )
        SharedPrefsUtil.RemoveDataByKey(this)
        SaveDataScreening.lastDataTiga.add(saveData)
        SavingData(this)
    }

    private fun ShowLoadHistory(){
        loadTimeIndex = 0
        for (i in SaveDataScreening.lastDataTiga[0].historyObrolan.indices) {
            if(SaveDataScreening.lastDataTiga[0].fromBot[i] == true){

                if(SaveDataScreening.lastDataTiga[0].textTime[i] == true){
                    ShowLoadBotChat(i, SaveDataScreening.lastDataTiga[0].icon[i], true)
                    loadTimeIndex++
                }else{
                    ShowLoadBotChat(i, SaveDataScreening.lastDataTiga[0].icon[i], false)
                }

            }else if(SaveDataScreening.lastDataTiga[0].fromBot[i] == false){
                ShowLoadUserChat(i)
            }
        }
        if(ScreeningTiga.indexKe >= ScreeningTiga.maxValue){
            Toast.makeText(this, "Sesi Telah Selesai", Toast.LENGTH_SHORT).show()
        }else{
            JawabSoal(ScreeningTiga.indexKe)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Panggil fungsi yang ingin Anda eksekusi ketika activity dihancurkan di sini
        // Misalnya, menyimpan data terakhir ke SharedPreferences atau melakukan pembersihan
        if(ScreeningTiga.indexKe == 0){
            SharedPrefsUtil.RemoveDataByKey(this)
        }else if(ScreeningTiga.indexKe >= 1){
            InsertIntoSaveData()
        }
    }

    private fun ShowLoadBotChat(index : Int, icon : Boolean, time : Boolean){
        val templateChatbot: View = inflater.inflate(R.layout.template_chatbot, null)
        linearContainer.addView(templateChatbot)

        val soal : TextView = templateChatbot.findViewById(R.id.answer_bot)
        val iconBot: ImageView = templateChatbot.findViewById(R.id.icon_bot)
        val textTime: TextView = templateChatbot.findViewById(R.id.time_bot)

        val theText = SaveDataScreening.lastDataTiga[0].historyObrolan[index]

        val getTheTime : String = SaveDataScreening.lastDataTiga[0].getTime[loadTimeIndex]

        soal.text = theText
        textTime.text = getTheTime

        if(!icon){
            iconBot.visibility = View.INVISIBLE
        }

        if(!time){
            textTime.visibility = View.GONE
        }else{
            textTime.text = SaveDataScreening.lastDataTiga[0].getTime[loadTimeIndex]
            historyTime.add(getTheTime)
        }

        showIcon.add(icon)
        timeText.add(time)
        historyChat.add(theText)
        fromBot.add(true)
    }

    private fun ShowLoadUserChat(index : Int){
        val templateChatuser: View = inflater.inflate(R.layout.template_chatuser, null)
        linearContainer.addView(templateChatuser)

        var theText = SaveDataScreening.lastDataTiga[0].historyObrolan[index]

        val text : TextView = templateChatuser.findViewById(R.id.answer_user)
        text.text = theText

        val getTheTime : String = SaveDataScreening.lastDataTiga[0].getTime[loadTimeIndex]

        val textTime : TextView = templateChatuser.findViewById(R.id.time_user)
        textTime.text = getTheTime

        historyChat.add(theText)
        fromBot.add(false)
        timeText.add(true)
        historyTime.add(getTheTime)
        showIcon.add(false)
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

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }
}
