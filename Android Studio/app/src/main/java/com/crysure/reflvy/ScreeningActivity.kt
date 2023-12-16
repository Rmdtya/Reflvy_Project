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
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.NestedScrollView
import com.crysure.reflvy.data.EventScreening
import com.crysure.reflvy.data.NotifyChat
import com.crysure.reflvy.data.SaveDataScreening
import com.crysure.reflvy.data.Screening
import com.crysure.reflvy.data.User
import com.crysure.reflvy.fragment.TingkatKecanduanFragment
import com.crysure.reflvy.utils.GameEventManager
import com.google.common.reflect.TypeToken
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
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
    private lateinit var template_nilai : ConstraintLayout
    private lateinit var textNilai : TextView
    private lateinit var textTingkat : TextView
    private lateinit var textKeterangan : TextView
    private lateinit var btn_selengkapnya : TextView
    private lateinit var btnSelesai : LinearLayout

    private lateinit var templateUlangi : ConstraintLayout
    private lateinit var textNilaiUlangi : TextView
    private lateinit var textTingkatUlangi : TextView
    private lateinit var textKeteranganUlangi : TextView
    private lateinit var btnYesUlangi : TextView
    private lateinit var btnNoUlangi : TextView
    private lateinit var btnSelengkapnyaUlangi : TextView

    private var historyChat : MutableList<String> = mutableListOf()
    private var fromBot : MutableList<Boolean> = mutableListOf()
    private var timeText : MutableList<Boolean> = mutableListOf()
    private var historyTime : MutableList<String> = mutableListOf()
    private var showIcon : MutableList<Boolean> = mutableListOf()
    private var loadTimeIndex: Int = 0
    private val db = Firebase.firestore

    private lateinit var schrollCotainer : NestedScrollView

    val handler = Handler(Looper.getMainLooper())

    companion object{
        var indexKe : Int = 0
        var maxValue : Int = 0
        var nilaiScreening : Int = 0
    }

    private lateinit var jawabContainer : LinearLayout
    private lateinit var jawabContainerBawah : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screening)

        Footer()
        linearContainer = findViewById(R.id.chat_container)
        jawabContainer = findViewById(R.id.jawab_container)
        jawabContainerBawah = findViewById(R.id.jawab_containerbawah)
        inflater = LayoutInflater.from(this)
        seekBar = findViewById(R.id.progres_bar)
        schrollCotainer = findViewById(R.id.schroll_container)

        template_nilai = findViewById(R.id.template_nilai)
        textNilai = findViewById(R.id.text_nilai)
        textTingkat = findViewById(R.id.text_tingkat)
        textKeterangan = findViewById(R.id.text_keterangantingkat)
        btn_selengkapnya = findViewById(R.id.btn_selengkapnya)

        btn_selengkapnya.setOnClickListener {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            val filterFragment = TingkatKecanduanFragment()
            fragmentTransaction.replace(R.id.fragment_container, filterFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        templateUlangi = findViewById(R.id.template_ulangi)
        textNilaiUlangi = findViewById(R.id.text_nilaiulangi)
        textTingkatUlangi = findViewById(R.id.text_tingkatulangi)
        textKeteranganUlangi = findViewById(R.id.text_keterangantingkatulangi)
        btnYesUlangi = findViewById(R.id.ulangi_yes)
        btnNoUlangi = findViewById(R.id.ulangi_no)
        btnSelengkapnyaUlangi = findViewById(R.id.btn_selengkapnyaulangi)

        btnSelengkapnyaUlangi.setOnClickListener {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            val filterFragment = TingkatKecanduanFragment()
            fragmentTransaction.replace(R.id.fragment_container, filterFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        persentasiBar = findViewById(R.id.persentasi)
        persentasiBar.text = "0%"

        indexKe = 0
        maxValue = Screening.screenData.size


        template_nilai.visibility = View.GONE

        LoadData(this)
        SetSeekbar(maxValue)

        val iconBot : ImageView = findViewById(R.id.icon_bot)

        iconBot.setOnClickListener {
            SharedPrefsUtil.RemoveDataByKey(this)
            indexKe = 0
        }


        if (User.userData.screeningDua){
            templateUlangi.visibility = View.VISIBLE

            val nilai = User.userData.nilaiScreening2
            textNilaiUlangi.text = nilai.toString()

            btnNoUlangi.setOnClickListener {
                templateUlangi.visibility = View.GONE
            }

            btnYesUlangi.setOnClickListener {
                SharedPrefsUtil.RemoveDataByKey(this)

                UpdateUserInfo(0, false)
                nilaiScreening = 0

                indexKe = 0

                val intent = Intent(this, ScreeningActivity::class.java)
                startActivity(intent)
                finishAffinity()

                UpdateUserInfo(nilaiScreening, false)
            }
        }

        btnSelesai =findViewById(R.id.btn_selesai)
        btnSelesai.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }


    }

    private fun ShowTemplateBot(text : String, icon : Boolean, time : Boolean){

        val templateChatbot: View = inflater.inflate(R.layout.template_chatbot, null)
        linearContainer.addView(templateChatbot)

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

        val jumlahData = Screening.screenData[ind].soalbot.size - 1

        if (jumlahData == 0 && icon == true){
            val theText = Screening.screenData[ind].soalbot[0]

            ShowTemplateBot(theText, true, true)

        }else if(jumlahData >= 1) {
            for (i in Screening.screenData[ind].soalbot.indices) {

                var theText = Screening.screenData[ind].soalbot[i]

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
        for (i in Screening.screenData[ind].textJawab.indices) {
            val templateJawab : View = inflater.inflate(R.layout.template_jawabchat, null)
            if(i >= 3){
                jawabContainerBawah.addView(templateJawab)
            }else{
                jawabContainer.addView(templateJawab)
            }

            val textJawab : TextView = templateJawab.findViewById(R.id.jawaban)
            textJawab.text = Screening.screenData[ind].textJawab[i]

            templateJawab.setOnClickListener{
                TampilkanJawaban(ind, i)
                jawabContainer.removeAllViews()
                jawabContainerBawah.removeAllViews()
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

        nilaiScreening += Screening.screenData[ind].nilai[ke]

        val text : TextView = templateChatuser.findViewById(R.id.answer_user)
        text.text = theText

        val textTime : TextView = templateChatuser.findViewById(R.id.time_user)
        textTime.text = GetTime()

        historyChat.add(theText)
        fromBot.add(false)
        timeText.add(true)
        historyTime.add(GetTime())
        showIcon.add(false)

        schrollCotainer.post {
            schrollCotainer.fullScroll(ScrollView.FOCUS_DOWN)
        }

        handler.postDelayed({
            // Fungsi yang akan dijalankan setelah jeda 2 detik
            NextEventDialog(ind, ke)
        }, 500)
    }

    private fun NextEventDialog(ind : Int, ke: Int){
        var eventNumber : Int = Screening.screenData[ind].event[ke]

        if( eventNumber == -1){
            Toast.makeText(this, "Sesi Selesai", Toast.LENGTH_SHORT).show()
        }else if(eventNumber == 1){
            ShowEventRespon1(ind)
        }else if(eventNumber == 2){
            ShowEventRespon2(ind)
        }
        else{
            ShowEventRespon0(ind)
        }
    }

    private fun ShowEventRespon0(ind : Int){

        if(EventScreening.eventScreenData[ind].eventRespon0 == null){

        }else {

            val jumlahData = EventScreening.eventScreenData[ind].eventRespon0.size - 1

            if (jumlahData == 0) {
                var theText = EventScreening.eventScreenData[ind].eventRespon0[0]

                ShowTemplateBot(theText, true, true)

            } else if (jumlahData >= 1) {
                for (i in EventScreening.eventScreenData[ind].eventRespon0.indices) {
                    var theText: String = EventScreening.eventScreenData[ind].eventRespon0[i]

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

        schrollCotainer.post {
            schrollCotainer.fullScroll(ScrollView.FOCUS_DOWN)
        }
    }

    private fun ShowEventRespon1(ind : Int){

        if(EventScreening.eventScreenData[ind].eventRespon1 == null){

        }else {

            val jumlahData = EventScreening.eventScreenData[ind].eventRespon1.size - 1

            if (jumlahData == 0) {
                var theText = EventScreening.eventScreenData[ind].eventRespon1[0]

                ShowTemplateBot(theText, true, true)

            } else if (jumlahData >= 1) {
                for (i in EventScreening.eventScreenData[ind].eventRespon1.indices) {
                    var theText: String = EventScreening.eventScreenData[ind].eventRespon1[i]

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

        schrollCotainer.post {
            schrollCotainer.fullScroll(ScrollView.FOCUS_DOWN)
        }
    }

    private fun ShowEventRespon2(ind : Int){

        if(EventScreening.eventScreenData[ind].eventRespon2 == null){

        }else {

            val jumlahData = EventScreening.eventScreenData[ind].eventRespon2.size - 1

            if (jumlahData == 0) {
                var theText = EventScreening.eventScreenData[ind].eventRespon2[0]

                ShowTemplateBot(theText, true, true)

            } else if (jumlahData >= 1) {
                for (i in EventScreening.eventScreenData[ind].eventRespon2.indices) {
                    var theText: String = EventScreening.eventScreenData[ind].eventRespon2[i]

                    if (i == 0) {
                        ShowTemplateBot(theText, true, false)

                    } else if (i == jumlahData) {
                        ShowTemplateBot(theText, false, true)

                    } else {
                        ShowTemplateBot(theText, false, false)
                    }
                }
            }
            //JawabEventSoal(ind)

            CheckNextIndex(ind)
        }

        schrollCotainer.post {
            schrollCotainer.fullScroll(ScrollView.FOCUS_DOWN)
        }
    }

    private fun CheckNextIndex(ind : Int){
        indexKe++
        handler.postDelayed({
            SetProgresSeekbar(indexKe)

            if(indexKe >= maxValue){
                Toast.makeText(this, "Selesai", Toast.LENGTH_SHORT).show()
                TampilkanNilai()
            }else{
                ShowChatBot(indexKe, true)
            } }, 500)
    }

    private fun SetSeekbar(value : Int){
        seekBar.max = value
    }

    private fun SetProgresSeekbar(ind : Int){
        seekBar.progress = ind
        persentasiBar.text = GetPersentase(ind, maxValue) + "%"
    }

    fun GetPersentase(currentValue: Int, maxValue: Int): String {
        if (currentValue < 0 || maxValue <= 0) {
            throw IllegalArgumentException("Nilai saat ini dan nilai maksimal harus lebih besar dari 0")
        }
        val percentage = (currentValue.toDouble() / maxValue) * 100
        val formattedPercentage = if (percentage > 100.0) 100.0 else percentage
        return formattedPercentage.toInt().toString()
    }


    fun TampilkanNilai(){
        template_nilai.visibility = View.VISIBLE
        textNilai.text = nilaiScreening.toString()

        if (nilaiScreening <= 25){
            textTingkat.text = "Kamu Normal, Keren :)"
            textKeterangan.text = "Kamu tidak perlu khawatir karena rasa ingin tahu itu manusiawi dan kamu masih dalam batas wajar. Namun, tetap waspada ya! jangan sampai kamu terjebak dalam bahaya kecanduan pornografi."
        }else if(nilaiScreening >= 26 && nilaiScreening <= 49){
            textTingkat.text = "Hati-hati, kamu mulai kecanduan loh:("
            textKeterangan.text = "Kamu dianjurkan untuk melakukan pembatasan aktivitas online, nih. Coba juga yuk belajar pencegahan bahaya kecanduan pornografi melalui dialog terbuka dengan para profesional klinis."
        }else if(nilaiScreening >= 50 && nilaiScreening <= 69){
            textTingkat.text = "Kamu Terdeteksi Kecanduann nih:("
            textKeterangan.text = "kamu perlu perawatan untuk mencegah kecanduan yang lebih luas juga pemantauan yang cermat terhadap perilaku online, nih. Jangan ragu untuk melanjutkan ke tahap treatment berikutnya, ya!"
        }else if(nilaiScreening >= 70){
            textTingkat.text = "Kecanduanmu sudah tinggi, nih:("
            textKeterangan.text = "Kamu sangat dianjurkan untuk melakukan perawatan dan pemantauan untuk menghentikan kecanduan dengan bantuan para profesional, nih. Jangan ragu untuk melanjutkan ke tahap treatment berikutnya, ya!"
        }

        try {
            val documentReference = db.collection("users").document(User.userData.userID)

            val updateData = hashMapOf(
                "screeningDua" to true,
                "nilaiScreening2" to nilaiScreening
            )

            documentReference.set(updateData, SetOptions.merge())
                .addOnSuccessListener {
                    //UpdateUserInfo(uname, tlp, tgl, selectedGender)
                    //UpdateLayout()
                    UpdateUserInfo(nilaiScreening, true)
                }
                .addOnFailureListener {
                    // Gagal mengganti data
                }
        }catch (e:Exception){
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }


        GameEventManager.instance.CekEventKegiatanNegatif(this)
    }

    private fun UpdateUserInfo(nilai : Int, status : Boolean){

        val sharedPreferences = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)

        User.userData.nilaiScreening2 = nilai
        User.userData.screeningDua = status
        val editor = sharedPreferences.edit()
        editor.putBoolean("screening2", status)
        editor.putInt("nilaiScreening2", nilai)
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

                indexKe = data.pertanyaan
                nilaiScreening = data.nilaiTerakir
                SaveDataScreening.lastData.add(saveData)

                println("Pertanyaan : " + SaveDataScreening.lastData[0].pertanyaan)
                println("History Obrolan: " + SaveDataScreening.lastData[0].historyObrolan)
                println("From Bot ?:" + SaveDataScreening.lastData[0].fromBot)
                println("textTime ?: + " + SaveDataScreening.lastData[0].textTime)
                println("nilai Terakhir: " + SaveDataScreening.lastData[0].nilaiTerakir)
                println("--------------------")

            }
            ShowLoadHistory()
        } else {
            // Data tidak ditemukan di SharedPreferences
            println("Data tidak ditemukan.")
            ShowChatBot(indexKe, true)
        }

    }

    private fun InsertIntoSaveData(){
        SaveDataScreening.lastData.clear()
        val saveData = SaveDataScreening(
            pertanyaan = indexKe,
            historyObrolan = historyChat,
            fromBot = fromBot,
            textTime = timeText,
            getTime = historyTime,
            icon = showIcon,
            nilaiTerakir = nilaiScreening // Ganti dengan nilai terakhir yang sesuai
        )
        SharedPrefsUtil.RemoveDataByKey(this)
        SaveDataScreening.lastData.add(saveData)
        SavingData(this)
    }

    private fun ShowLoadHistory(){
        loadTimeIndex = 0
        for (i in SaveDataScreening.lastData[0].historyObrolan.indices) {
            if(SaveDataScreening.lastData[0].fromBot[i] == true){

                if(SaveDataScreening.lastData[0].textTime[i] == true){
                    ShowLoadBotChat(i, SaveDataScreening.lastData[0].icon[i], true)
                    loadTimeIndex++
                }else{
                    ShowLoadBotChat(i, SaveDataScreening.lastData[0].icon[i], false)
                }

            }else if(SaveDataScreening.lastData[0].fromBot[i] == false){
                ShowLoadUserChat(i)
            }
        }
        if(indexKe >= maxValue){
            Toast.makeText(this, "Sesi Telah Selesai", Toast.LENGTH_SHORT).show()
        }else{
            JawabSoal(indexKe)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Panggil fungsi yang ingin Anda eksekusi ketika activity dihancurkan di sini
        // Misalnya, menyimpan data terakhir ke SharedPreferences atau melakukan pembersihan
        if(indexKe == 0){
            SharedPrefsUtil.RemoveDataByKey(this)
        }else if(indexKe >= 1){
            InsertIntoSaveData()
        }
    }

    private fun ShowLoadBotChat(index : Int, icon : Boolean, time : Boolean){
        val templateChatbot: View = inflater.inflate(R.layout.template_chatbot, null)
        linearContainer.addView(templateChatbot)

        val soal : TextView = templateChatbot.findViewById(R.id.answer_bot)
        val iconBot: ImageView = templateChatbot.findViewById(R.id.icon_bot)
        val textTime: TextView = templateChatbot.findViewById(R.id.time_bot)

        val theText = SaveDataScreening.lastData[0].historyObrolan[index]

        val getTheTime : String = SaveDataScreening.lastData[0].getTime[loadTimeIndex]

        soal.text = theText
        textTime.text = getTheTime

        if(!icon){
            iconBot.visibility = View.INVISIBLE
        }

        if(!time){
            textTime.visibility = View.GONE
        }else{
            textTime.text = SaveDataScreening.lastData[0].getTime[loadTimeIndex]
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

        var theText = SaveDataScreening.lastData[0].historyObrolan[index]

        val text : TextView = templateChatuser.findViewById(R.id.answer_user)
        text.text = theText

        val getTheTime : String = SaveDataScreening.lastData[0].getTime[loadTimeIndex]

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