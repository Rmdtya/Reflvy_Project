package com.crysure.reflvy

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.NetworkResponse
import com.android.volley.Response
import com.android.volley.RetryPolicy
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.crysure.reflvy.data.ActiveLogin
import com.crysure.reflvy.data.DataDaily
import com.crysure.reflvy.data.DataNotification
import com.crysure.reflvy.data.EfekStatusDragon
import com.crysure.reflvy.data.Message
import com.crysure.reflvy.data.NotifyChat
import com.crysure.reflvy.fragment.NotifDataDailyFragment
import com.crysure.reflvy.fragment.OnDailyNotifListener
import com.crysure.reflvy.utils.ApplicationManager
import com.crysure.reflvy.utils.GameEventManager
import com.crysure.reflvy.utils.GameManager
import com.crysure.reflvy.utils.MessagingAdapter
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BotActivity : AppCompatActivity(), OnDailyNotifListener {
    var messagesList = mutableListOf<Message>()
    lateinit var send_button : ImageView
    lateinit var text_box : EditText
    lateinit var container : RecyclerView
    private lateinit var linearContainer: LinearLayout
    private lateinit var inflater: LayoutInflater
    private val stringURLEndPoint = "https://api.openai.com/v1/chat/completions"
    private val stringAPIKey = "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"
    private var stringOutput = ""

    private var handler: Handler? = null
    private var isCurhat : Boolean = false

    private lateinit var adapter: MessagingAdapter
    private val botList = listOf("Peter", "Francesca", "Luigi", "Igor")

    private lateinit var constraintTextEdit : ConstraintLayout
    private lateinit var constraintOpsi : ConstraintLayout
    
    private lateinit var btnCurhat : LinearLayout
    private lateinit var btnTanya : LinearLayout

    private lateinit var schrollCotainer : NestedScrollView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_bot)


        linearContainer = findViewById(R.id.chat_container)
        inflater = LayoutInflater.from(this)
        constraintOpsi = findViewById(R.id.container_opsi)
        constraintTextEdit = findViewById(R.id.container_jawab)

        btnCurhat = findViewById(R.id.btn_curhat)
        btnTanya = findViewById(R.id.btn_tanya)
        schrollCotainer = findViewById(R.id.schroll_container)

        LoadChat()

        send_button = findViewById(R.id.send_button)
        text_box = findViewById(R.id.quest_inp)


        val random = (0..3).random()
        //customBotMessage("Hello! Today you're speaking with ${botList[random]}, how may I help?")
        firstMessageBot("Hello! Hari ini biar aku ${botList[random]} yang mengobrol dengan mu, apa ada yang bisa saya bantu?")

        ClickEvent()
        Footer()

        if(isCurhat){
            constraintTextEdit.visibility = View.VISIBLE
            constraintOpsi.visibility = View.GONE
        }else{
            constraintTextEdit.visibility = View.GONE
            constraintOpsi.visibility = View.VISIBLE
        }

        btnCurhat.setOnClickListener {
            isCurhat = true
            constraintTextEdit.visibility = View.VISIBLE
            constraintOpsi.visibility = View.GONE

            SendChat("Temani aku Curhat", false, true)
            buttonChatGPT("jawab singkat, mulai saat ini anda akan bermain role sebagai orang yang kontra terhadap pornografi sampai saya bilang stop. Berikan respon positif sebagai teman untuk pertanyaan yang akan datang. Dan jika curhatannya berbau pornografi atau melanggar privacy policy kamu tolong tetap berikan respon yang baik. lewati basa-basi role anda dan berikan respon yang singkat dan padat.", false)
        }


        ApplicationManager.instance.NonActiveNotif(this)
        //KeyboardHiding()
    }

    fun LoadChat(){

        val totalIndex = NotifyChat.notifChat.size

        for(chat in NotifyChat.notifChat){

            if(chat.from == "bot"){
                val templateChatBot: View = inflater.inflate(R.layout.template_chatbot, null)
                linearContainer.addView(templateChatBot)

                BotResponse(templateChatBot, chat, false)

                val index = NotifyChat.notifChat.indexOf(chat)

                if(!chat.ditanggapi){
                    val notifDaily = NotifDataDailyFragment()
                    val bundle = Bundle()
                    bundle.putInt("index", index)
                    bundle.putInt("totalIndex", totalIndex)
                    notifDaily.arguments = bundle

                    val fragmentTransaction = supportFragmentManager.beginTransaction()
                    fragmentTransaction.setCustomAnimations(0, R.animator.slide_up)
                    fragmentTransaction.replace(R.id.fragment_container, notifDaily)
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()
                }

            }else if(chat.from == "user"){
                SendChat(chat.chat, false, false)
            }
        }
    }

    private fun SendChat(question : String, respon : Boolean, isQuestion : Boolean){
        val templateChatuser: View = inflater.inflate(R.layout.template_chatuser, null)
        linearContainer.addView(templateChatuser)

        val text : TextView = templateChatuser.findViewById(R.id.answer_user)
        text.text = question

        val textTime : TextView = templateChatuser.findViewById(R.id.time_user)
        textTime.text = GetTime()

        if (isQuestion){
            val chat = NotifyChat(question, "user", false, true, GetTime(), false, "", true, 0)
            NotifyChat.notifChat.add(chat)
        }

        if(respon){
            buttonChatGPT(question, true)
        }

        schrollCotainer.post {
            schrollCotainer.fullScroll(ScrollView.FOCUS_DOWN)
        }
    }

    private fun GetTime(): String {
        val currentTime = Date()
        val format = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val timeString = format.format(currentTime)

        return timeString
    }

    private fun ClickEvent(){
        send_button.setOnClickListener {
            val editTextValue = text_box.text.toString()
            if(editTextValue.isBlank()){
                Toast.makeText(this, "Pertanyaan Tidak Boleh Kosong", Toast.LENGTH_SHORT).show()
            }else{
                SendChat(editTextValue, true, true)
                text_box.setText("")
                text_box.isEnabled = false

                val time : String = GetTime()
                val chat = NotifyChat(editTextValue, "user", false, true, time, false, "", true, 0)

                NotifyChat.notifChat.add(chat)
            }
        }
    }

    private fun BotResponse(template : View, notifyChat : NotifyChat, animation : Boolean){

        val typingSpeed = 15 // Kecepatan mengetik (dalam milidetik)
        var indexType = 0

        val text : TextView = template.findViewById(R.id.answer_bot)

        if(animation){
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed(object : Runnable {
                override fun run() {
                    if (indexType <= notifyChat.chat.length) {
                        val textToShow = notifyChat.chat.substring(0, indexType)
                        text.text = textToShow + "..."
                        indexType++
                        handler.postDelayed(this, typingSpeed.toLong())

                        schrollCotainer.post {
                            schrollCotainer.fullScroll(ScrollView.FOCUS_DOWN)
                        }
                    }else{
                        text.text = notifyChat.chat
                    }
                }
            }, typingSpeed.toLong())
        }else{
            text.text = notifyChat.chat

            schrollCotainer.post {
                schrollCotainer.fullScroll(ScrollView.FOCUS_DOWN)
            }
        }

        if(notifyChat.clickable){
            text.text = Html.fromHtml(notifyChat.chat, Html.FROM_HTML_MODE_LEGACY)
            if(notifyChat.eventClick == "artikel"){
                template.setOnClickListener {
                    val intent = Intent(this, MenuInfoActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                }
            }
        }


        val timeText : TextView = template.findViewById(R.id.time_bot)
        timeText.text = notifyChat.timeString

        if(!notifyChat.iconBot){
            val iconBot : ImageView = template.findViewById(R.id.icon_bot)
            iconBot.visibility = View.INVISIBLE
        }

        if(!notifyChat.time){
            timeText.visibility = View.GONE
        }

    }

    private fun firstMessageBot(message: String){
        val templateChatuser: View = inflater.inflate(R.layout.template_chatbot, null)
        linearContainer.addView(templateChatuser)

        val text : TextView = templateChatuser.findViewById(R.id.answer_bot)
        text.text = message

        val textTime : TextView = templateChatuser.findViewById(R.id.time_bot)
        textTime.text = GetTime()
    }

    fun buttonChatGPT(message: String, responable : Boolean) {
        val jsonObject = JSONObject()

        val templateChatBot: View = inflater.inflate(R.layout.template_chatbot, null)
        linearContainer.addView(templateChatBot)

        val text : TextView = templateChatBot.findViewById(R.id.answer_bot)

        startTypingAnimation(text)

        try {
            jsonObject.put("model", "gpt-3.5-turbo")

            val jsonArrayMessage = JSONArray()
            val jsonObjectMessage = JSONObject()
            jsonObjectMessage.put("role", "user")
            jsonObjectMessage.put("content", message)
            jsonArrayMessage.put(jsonObjectMessage)

            jsonObject.put("messages", jsonArrayMessage)

        } catch (e: JSONException) {
            throw RuntimeException(e)
        }

        val jsonObjectRequest = object : JsonObjectRequest(Method.POST,
            stringURLEndPoint, jsonObject, Response.Listener { response ->

                var stringText: String? = null
                try {
                    stringText = response.getJSONArray("choices")
                        .getJSONObject(0)
                        .getJSONObject("message")
                        .getString("content")
                } catch (e: JSONException) {
                    throw RuntimeException(e)
                }

                var jawaban : String = ""
                jawaban = stringText
                stopTypingAnimation(templateChatBot)
                text.text = ""
                val time : String = GetTime()


                if(responable){
                    val chat = NotifyChat(jawaban, "bot", true, true, time, false, "", true, 0)
                    BotResponse(templateChatBot, chat, true)
                    NotifyChat.notifChat.add(chat)
                }else{
                    val chat = NotifyChat("Tentu saja, curahkan saja semua masalah yang kamu alami. Jika saya mampu, saya akan memberikan beberapa saran yang mungkin dapat membantu", "bot", true, true, time, false, "", true, 0)
                    BotResponse(templateChatBot, chat, true)
                    NotifyChat.notifChat.add(chat)
                }


                ApplicationManager.instance.SaveNotifPrefs(this)

                text_box.isEnabled = true
            }, Response.ErrorListener { error ->
                val error : String = "Error Response, Coba Beberapa Saat Lagi"
                val time : String = GetTime()
                val chat = NotifyChat(error, "bot", true, true, time, false, "", true, 0)

                BotResponse(templateChatBot, chat, true)
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val mapHeader = HashMap<String, String>()
                mapHeader["Authorization"] = "Bearer $stringAPIKey"
                mapHeader["Content-Type"] = "application/json"
                return mapHeader
            }

            override fun parseNetworkResponse(response: NetworkResponse): Response<JSONObject> {
                return super.parseNetworkResponse(response)
            }
        }

        val intTimeoutPeriod = 60000 // 60 seconds timeout duration defined
        val retryPolicy: RetryPolicy = DefaultRetryPolicy(intTimeoutPeriod,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        jsonObjectRequest.retryPolicy = retryPolicy
        Volley.newRequestQueue(applicationContext).add(jsonObjectRequest)
    }

    fun startTypingAnimation(textView: TextView) {

        val typingText = "Sedang mengetik"
        textView.text = ""

        handler = Handler(Looper.getMainLooper())
        handler?.post(runnableTyping(textView, typingText))
    }

    fun stopTypingAnimation(view: View) {
        handler?.removeCallbacksAndMessages(null)
    }

    private fun runnableTyping(textView: TextView, typingText: String) = object : Runnable {
        var dotCount = 0

        override fun run() {
            val textToShow = "$typingText${".".repeat(dotCount)}"
            textView.text = textToShow

            dotCount++
            if (dotCount > 3) {
                dotCount = 0
            }

            handler?.postDelayed(this, 500) // Atur jeda waktu animasi (dalam milidetik)
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, MenuActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }

    override fun BackFunction(back: Boolean) {

    }

    override fun ButtonAction(status: Boolean, index : Int) {
        val indexing = NotifyChat.notifChat[index].index

        NotifyChat.notifChat[index].ditanggapi = true

        if (status){
            val datadaily = DataDaily.dataKegiatan[indexing]
            datadaily.proses = true
            datadaily.progresNow++

            val durasi : Int = datadaily.endMinutes - datadaily.startMinute
            TambahkanKeHistoryActivity(datadaily.kategori, durasi)

        }else{
            DataDaily.dataKegiatan[indexing].terlewat = true
        }

        ApplicationManager.instance.SaveKegiatan(this)
        DataNotification.dataNotifikasi.clear()
        DataNotification.CopyDataKegiatan()
        ApplicationManager.instance.SaveNotifikasiKegiatan(this)

        ApplicationManager.instance.SaveNotifPrefs(this)
    }

    private fun EnableDisableButton(kondisi : Boolean){

    }

    private fun TambahkanKeHistoryActivity(kategori : String, durasi : Int){
        if (kategori == "bekerja"){
            ActiveLogin.infoActive.kegiatan1Bekerja += durasi
            val status = EfekStatusDragon("", "", 0, 0, false, 0.5f * durasi, 0.5f * durasi,
                0f, -((0.05 * durasi) + (EfekStatusDragon.efekNow.statLapar * durasi * 1.5f)).toFloat(),
                ((0.045f * durasi) + (EfekStatusDragon.efekNow.statPengetahuan * durasi * 0.5)).toFloat(),
                ((0.06f * durasi) + (EfekStatusDragon.efekNow.statKesehatanFisik * durasi * 0.5)).toFloat(),
                ((0.04f * durasi) + (EfekStatusDragon.efekNow.statKesehatanMental * durasi * 0.5)).toFloat(),
                ((0.02f * durasi) + (EfekStatusDragon.efekNow.statCinta * durasi * 0.5)).toFloat(),
                -((0.01f * durasi) + (EfekStatusDragon.efekNow.statHiburan * durasi * 0.5)).toFloat(),
                -((0.04f * durasi) + (EfekStatusDragon.efekNow.statIstirahat * durasi * 0.5)).toFloat(),
                ((0.07f * durasi) + (EfekStatusDragon.efekNow.statSosial * durasi * 0.5)).toFloat())

            GameManager.instance.PlusStatusPoint(status, this)

        }else if (kategori == "belajar formal"){
            ActiveLogin.infoActive.kegiatan2BelajarFormal += durasi
            val status = EfekStatusDragon("", "", 0, 0, false, 0.5f * durasi, 0.5f * durasi,
                0f, -((0.02 * durasi) + (EfekStatusDragon.efekNow.statLapar * durasi * 1.5f)).toFloat(),
                ((0.14f * durasi) + (EfekStatusDragon.efekNow.statPengetahuan * durasi * 0.5)).toFloat(),
                -((0.02f * durasi) + (EfekStatusDragon.efekNow.statKesehatanFisik * durasi * 0.5)).toFloat(),
                ((0.03f * durasi) + (EfekStatusDragon.efekNow.statKesehatanMental * durasi * 0.5)).toFloat(),
                ((0.05f * durasi) + (EfekStatusDragon.efekNow.statCinta * durasi * 0.5)).toFloat(),
                -((0.04f * durasi) + (EfekStatusDragon.efekNow.statHiburan * durasi * 0.5)).toFloat(),
                -((0.03f * durasi) + (EfekStatusDragon.efekNow.statIstirahat * durasi * 0.5)).toFloat(),
                ((0.01f * durasi) + (EfekStatusDragon.efekNow.statSosial * durasi * 0.5)).toFloat())

            GameManager.instance.PlusStatusPoint(status, this)

        }else if (kategori == "membaca"){
            ActiveLogin.infoActive.kegiatan3Membaca += durasi
            val status = EfekStatusDragon("", "", 0, 0, false, 0.5f * durasi, 0.5f * durasi,
                0f, -((0.02 * durasi) + (EfekStatusDragon.efekNow.statLapar * durasi * 1.5f)).toFloat(),
                ((0.05f * durasi) + (EfekStatusDragon.efekNow.statPengetahuan * durasi * 0.5)).toFloat(),
                -((0.01f * durasi) + (EfekStatusDragon.efekNow.statKesehatanFisik * durasi * 0.5)).toFloat(),
                ((0.03f * durasi) + (EfekStatusDragon.efekNow.statKesehatanMental * durasi * 0.5)).toFloat(),
                ((0.04f * durasi) + (EfekStatusDragon.efekNow.statCinta * durasi * 0.5)).toFloat(),
                ((0.02f * durasi) + (EfekStatusDragon.efekNow.statHiburan * durasi * 0.5)).toFloat(),
                ((0.03f * durasi) + (EfekStatusDragon.efekNow.statIstirahat * durasi * 0.5)).toFloat(),
                -((0.015f * durasi) + (EfekStatusDragon.efekNow.statSosial * durasi * 0.5)).toFloat())

            GameManager.instance.PlusStatusPoint(status, this)

        }else if (kategori == "bersantai"){
            ActiveLogin.infoActive.kegiatan4Bersantai += durasi
            val status = EfekStatusDragon("", "", 0, 0, false, 0.5f * durasi, 0.5f * durasi,
                0f, -((0.01 * durasi) + (EfekStatusDragon.efekNow.statLapar * durasi * 1.5f)).toFloat(),
                -((0.01f * durasi) + (EfekStatusDragon.efekNow.statPengetahuan * durasi * 0.5)).toFloat(),
                -((0.02f * durasi) + (EfekStatusDragon.efekNow.statKesehatanFisik * durasi * 0.5)).toFloat(),
                ((0.025f * durasi) + (EfekStatusDragon.efekNow.statKesehatanMental * durasi * 0.5)).toFloat(),
                ((0.04f * durasi) + (EfekStatusDragon.efekNow.statCinta * durasi * 0.5)).toFloat(),
                ((0.06f * durasi) + (EfekStatusDragon.efekNow.statHiburan * durasi * 0.5)).toFloat(),
                ((0.08f * durasi) + (EfekStatusDragon.efekNow.statIstirahat * durasi * 0.5)).toFloat(),
                ((0.01f * durasi) + (EfekStatusDragon.efekNow.statSosial * durasi * 0.5)).toFloat())

            GameManager.instance.PlusStatusPoint(status, this)

        }else if (kategori == "istirahat"){
            ActiveLogin.infoActive.kegiatan5Istirahat += durasi
            val status = EfekStatusDragon("", "", 0, 0, false, 0.5f * durasi, 0.5f * durasi,
                0f, ((0.02 * durasi) + (EfekStatusDragon.efekNow.statLapar * durasi * 1.5f)).toFloat(),
                -((0.01f * durasi) + (EfekStatusDragon.efekNow.statPengetahuan * durasi * 0.5)).toFloat(),
                ((0.03f * durasi) + (EfekStatusDragon.efekNow.statKesehatanFisik * durasi * 0.5)).toFloat(),
                ((0.03f * durasi) + (EfekStatusDragon.efekNow.statKesehatanMental * durasi * 0.5)).toFloat(),
                ((0.05f * durasi) + (EfekStatusDragon.efekNow.statCinta * durasi * 0.5)).toFloat(),
                ((0.01f * durasi) + (EfekStatusDragon.efekNow.statHiburan * durasi * 0.5)).toFloat(),
                ((0.12f * durasi) + (EfekStatusDragon.efekNow.statIstirahat * durasi * 0.5)).toFloat(),
                -((0.01f * durasi) + (EfekStatusDragon.efekNow.statSosial * durasi * 0.5)).toFloat())

            GameManager.instance.PlusStatusPoint(status, this)

        }else if (kategori == "belanja"){
            ActiveLogin.infoActive.kegiatan6Belanja += durasi
            val status = EfekStatusDragon("", "", 0, 0, false, 0.5f * durasi, 0.5f * durasi,
                0f, ((0.07 * durasi) + (EfekStatusDragon.efekNow.statLapar * durasi * 1.5f)).toFloat(),
                ((0.01f * durasi) + (EfekStatusDragon.efekNow.statPengetahuan * durasi * 0.5)).toFloat(),
                ((0.02f * durasi) + (EfekStatusDragon.efekNow.statKesehatanFisik * durasi * 0.5)).toFloat(),
                ((0.03f * durasi) + (EfekStatusDragon.efekNow.statKesehatanMental * durasi * 0.5)).toFloat(),
                ((0.09f * durasi) + (EfekStatusDragon.efekNow.statCinta * durasi * 0.5)).toFloat(),
                ((0.06f * durasi) + (EfekStatusDragon.efekNow.statHiburan * durasi * 0.5)).toFloat(),
                ((0.05f * durasi) + (EfekStatusDragon.efekNow.statIstirahat * durasi * 0.5)).toFloat(),
                ((0.04f * durasi) + (EfekStatusDragon.efekNow.statSosial * durasi * 0.5)).toFloat())

            GameManager.instance.PlusStatusPoint(status, this)

        }else if (kategori == "bermusik"){
            ActiveLogin.infoActive.kegiatan7Bermusik += durasi
            val status = EfekStatusDragon("", "", 0, 0, false, 0.5f * durasi, 0.5f * durasi,
                0f, -((0.02 * durasi) + (EfekStatusDragon.efekNow.statLapar * durasi * 1.5f)).toFloat(),
                -((0.01f * durasi) + (EfekStatusDragon.efekNow.statPengetahuan * durasi * 0.5)).toFloat(),
                ((0.01f * durasi) + (EfekStatusDragon.efekNow.statKesehatanFisik * durasi * 0.5)).toFloat(),
                ((0.04f * durasi) + (EfekStatusDragon.efekNow.statKesehatanMental * durasi * 0.5)).toFloat(),
                ((0.08f * durasi) + (EfekStatusDragon.efekNow.statCinta * durasi * 0.5)).toFloat(),
                ((0.09f * durasi) + (EfekStatusDragon.efekNow.statHiburan * durasi * 0.5)).toFloat(),
                ((0.03f * durasi) + (EfekStatusDragon.efekNow.statIstirahat * durasi * 0.5)).toFloat(),
                ((0.02f * durasi) + (EfekStatusDragon.efekNow.statSosial * durasi * 0.5)).toFloat())

            GameManager.instance.PlusStatusPoint(status, this)

        }else if (kategori == "beribadah"){
            ActiveLogin.infoActive.kegiatan8Beribadah += durasi
            val status = EfekStatusDragon("", "", 0, 0, false, 0.5f * durasi, 0.5f * durasi,
                0f, ((0.01 * durasi) + (EfekStatusDragon.efekNow.statLapar * durasi * 1.5f)).toFloat(),
                ((0.01f * durasi) + (EfekStatusDragon.efekNow.statPengetahuan * durasi * 0.5)).toFloat(),
                ((0.04f * durasi) + (EfekStatusDragon.efekNow.statKesehatanFisik * durasi * 0.5)).toFloat(),
                ((0.11 * durasi) + (EfekStatusDragon.efekNow.statKesehatanMental * durasi * 0.5)).toFloat(),
                ((0.09f * durasi) + (EfekStatusDragon.efekNow.statCinta * durasi * 0.5)).toFloat(),
                ((0.01f * durasi) + (EfekStatusDragon.efekNow.statHiburan * durasi * 0.5)).toFloat(),
                ((0.04f * durasi) + (EfekStatusDragon.efekNow.statIstirahat * durasi * 0.5)).toFloat(),
                ((0.01f * durasi) + (EfekStatusDragon.efekNow.statSosial * durasi * 0.5)).toFloat())

            GameManager.instance.PlusStatusPoint(status, this)

        }else if (kategori == "bermain game"){
            ActiveLogin.infoActive.kegiatan9BermainGame += durasi
            val status = EfekStatusDragon("", "", 0, 0, false, 0.5f * durasi, 0.5f * durasi,
                0f, -((0.01 * durasi) + (EfekStatusDragon.efekNow.statLapar * durasi * 1.5f)).toFloat(),
                -((0.05f * durasi) + (EfekStatusDragon.efekNow.statPengetahuan * durasi * 0.5)).toFloat(),
                -((0.07f * durasi) + (EfekStatusDragon.efekNow.statKesehatanFisik * durasi * 0.5)).toFloat(),
                -((0.09f * durasi) + (EfekStatusDragon.efekNow.statKesehatanMental * durasi * 0.5)).toFloat(),
                ((0.03f * durasi) + (EfekStatusDragon.efekNow.statCinta * durasi * 0.5)).toFloat(),
                ((0.15f * durasi) + (EfekStatusDragon.efekNow.statHiburan * durasi * 0.5)).toFloat(),
                ((0.08f * durasi) + (EfekStatusDragon.efekNow.statIstirahat * durasi * 0.5)).toFloat(),
                -((0.03f * durasi) + (EfekStatusDragon.efekNow.statSosial * durasi * 0.5)).toFloat())

            GameManager.instance.PlusStatusPoint(status, this)

        }else if (kategori == "akses handphone"){
            ActiveLogin.infoActive.kegiatan10HiburanDigital += durasi
            val status = EfekStatusDragon("", "", 0, 0, false, 0.5f * durasi, 0.5f * durasi,
                0f, -((0.01 * durasi) + (EfekStatusDragon.efekNow.statLapar * durasi * 1.5f)).toFloat(),
                -((0.03f * durasi) + (EfekStatusDragon.efekNow.statPengetahuan * durasi * 0.5)).toFloat(),
                -((0.08f * durasi) + (EfekStatusDragon.efekNow.statKesehatanFisik * durasi * 0.5)).toFloat(),
                -((0.03f * durasi) + (EfekStatusDragon.efekNow.statKesehatanMental * durasi * 0.5)).toFloat(),
                ((0.03f * durasi) + (EfekStatusDragon.efekNow.statCinta * durasi * 0.5)).toFloat(),
                ((0.08f * durasi) + (EfekStatusDragon.efekNow.statHiburan * durasi * 0.5)).toFloat(),
                ((0.06f * durasi) + (EfekStatusDragon.efekNow.statIstirahat * durasi * 0.5)).toFloat(),
                -((0.01f * durasi) + (EfekStatusDragon.efekNow.statSosial * durasi * 0.5)).toFloat())

            GameManager.instance.PlusStatusPoint(status, this)

        }else if (kategori == "operasi komputer"){
            ActiveLogin.infoActive.kegiatan11OperasiKomputer += durasi
            val status = EfekStatusDragon("", "", 0, 0, false, 0.5f * durasi, 0.5f * durasi,
                0f, -((0.01 * durasi) + (EfekStatusDragon.efekNow.statLapar * durasi * 1.5f)).toFloat(),
                ((0.09f * durasi) + (EfekStatusDragon.efekNow.statPengetahuan * durasi * 0.5)).toFloat(),
                -((0.01f * durasi) + (EfekStatusDragon.efekNow.statKesehatanFisik * durasi * 0.5)).toFloat(),
                ((0.04f * durasi) + (EfekStatusDragon.efekNow.statKesehatanMental * durasi * 0.5)).toFloat(),
                ((0.04f * durasi) + (EfekStatusDragon.efekNow.statCinta * durasi * 0.5)).toFloat(),
                ((0.08f * durasi) + (EfekStatusDragon.efekNow.statHiburan * durasi * 0.5)).toFloat(),
                ((0.04f * durasi) + (EfekStatusDragon.efekNow.statIstirahat * durasi * 0.5)).toFloat(),
                ((0.03f * durasi) + (EfekStatusDragon.efekNow.statSosial * durasi * 0.5)).toFloat())

            GameManager.instance.PlusStatusPoint(status, this)

        }else if (kategori == "pekerjaan rumah"){
            ActiveLogin.infoActive.kegiatan12PekerjaanRumah += durasi
            val status = EfekStatusDragon("", "", 0, 0, false, 0.5f * durasi, 0.5f * durasi,
                0f, -((0.01 * durasi) + (EfekStatusDragon.efekNow.statLapar * durasi * 1.5f)).toFloat(),
                ((0.03f * durasi) + (EfekStatusDragon.efekNow.statPengetahuan * durasi * 0.5)).toFloat(),
                ((0.11f * durasi) + (EfekStatusDragon.efekNow.statKesehatanFisik * durasi * 0.5)).toFloat(),
                ((0.05f * durasi) + (EfekStatusDragon.efekNow.statKesehatanMental * durasi * 0.5)).toFloat(),
                ((0.06f * durasi) + (EfekStatusDragon.efekNow.statCinta * durasi * 0.5)).toFloat(),
                ((0.04f * durasi) + (EfekStatusDragon.efekNow.statHiburan * durasi * 0.5)).toFloat(),
                ((0.05f * durasi) + (EfekStatusDragon.efekNow.statIstirahat * durasi * 0.5)).toFloat(),
                ((0.06f * durasi) + (EfekStatusDragon.efekNow.statSosial * durasi * 0.5)).toFloat())

            GameManager.instance.PlusStatusPoint(status, this)

        }else if (kategori == "komunitas"){
            ActiveLogin.infoActive.kegiatan13Komunitas += durasi
            val status = EfekStatusDragon("", "", 0, 0, false, 0.5f * durasi, 0.5f * durasi,
                0f, ((0.01 * durasi) + (EfekStatusDragon.efekNow.statLapar * durasi * 1.5f)).toFloat(),
                ((0.06f * durasi) + (EfekStatusDragon.efekNow.statPengetahuan * durasi * 0.5)).toFloat(),
                ((0.04f * durasi) + (EfekStatusDragon.efekNow.statKesehatanFisik * durasi * 0.5)).toFloat(),
                ((0.08f * durasi) + (EfekStatusDragon.efekNow.statKesehatanMental * durasi * 0.5)).toFloat(),
                ((0.03f * durasi) + (EfekStatusDragon.efekNow.statCinta * durasi * 0.5)).toFloat(),
                ((0.08f * durasi) + (EfekStatusDragon.efekNow.statHiburan * durasi * 0.5)).toFloat(),
                ((0.05f * durasi) + (EfekStatusDragon.efekNow.statIstirahat * durasi * 0.5)).toFloat(),
                ((0.21f * durasi) + (EfekStatusDragon.efekNow.statSosial * durasi * 0.5)).toFloat())

            GameManager.instance.PlusStatusPoint(status, this)

        }else if (kategori == "bersosialisasi"){
            ActiveLogin.infoActive.kegiatan14Bersosialisasi += durasi
            val status = EfekStatusDragon("", "", 0, 0, false, 0.5f * durasi, 0.5f * durasi,
                0f, ((0.03 * durasi) + (EfekStatusDragon.efekNow.statLapar * durasi * 1.5f)).toFloat(),
                ((0.06f * durasi) + (EfekStatusDragon.efekNow.statPengetahuan * durasi * 0.5)).toFloat(),
                ((0.04f * durasi) + (EfekStatusDragon.efekNow.statKesehatanFisik * durasi * 0.5)).toFloat(),
                ((0.05f * durasi) + (EfekStatusDragon.efekNow.statKesehatanMental * durasi * 0.5)).toFloat(),
                ((0.06f * durasi) + (EfekStatusDragon.efekNow.statCinta * durasi * 0.5)).toFloat(),
                ((0.04f * durasi) + (EfekStatusDragon.efekNow.statHiburan * durasi * 0.5)).toFloat(),
                ((0.06f * durasi) + (EfekStatusDragon.efekNow.statIstirahat * durasi * 0.5)).toFloat(),
                ((0.24f * durasi) + (EfekStatusDragon.efekNow.statSosial * durasi * 0.5)).toFloat())

            GameManager.instance.PlusStatusPoint(status, this)

        }else if (kategori == "healing"){
            ActiveLogin.infoActive.kegiatan15Healing += durasi
            val status = EfekStatusDragon("", "", 0, 0, false, 0.5f * durasi, 0.5f * durasi,
                0f, -((0.01 * durasi) + (EfekStatusDragon.efekNow.statLapar * durasi * 1.5f)).toFloat(),
                ((0.03f * durasi) + (EfekStatusDragon.efekNow.statPengetahuan * durasi * 0.5)).toFloat(),
                ((0.11f * durasi) + (EfekStatusDragon.efekNow.statKesehatanFisik * durasi * 0.5)).toFloat(),
                ((0.25f * durasi) + (EfekStatusDragon.efekNow.statKesehatanMental * durasi * 0.5)).toFloat(),
                ((0.22f * durasi) + (EfekStatusDragon.efekNow.statCinta * durasi * 0.5)).toFloat(),
                ((0.09f * durasi) + (EfekStatusDragon.efekNow.statHiburan * durasi * 0.5)).toFloat(),
                ((0.13f * durasi) + (EfekStatusDragon.efekNow.statIstirahat * durasi * 0.5)).toFloat(),
                ((0.05f * durasi) + (EfekStatusDragon.efekNow.statSosial * durasi * 0.5)).toFloat())

            GameManager.instance.PlusStatusPoint(status, this)

        }else if (kategori == "olahraga"){
            ActiveLogin.infoActive.kegiatan16Olahraga += durasi
            val status = EfekStatusDragon("", "", 0, 0, false, 0.5f * durasi, 0.5f * durasi,
                0f, -((0.05 * durasi) + (EfekStatusDragon.efekNow.statLapar * durasi * 1.5f)).toFloat(),
                ((0.03f * durasi) + (EfekStatusDragon.efekNow.statPengetahuan * durasi * 0.5)).toFloat(),
                ((0.26f * durasi) + (EfekStatusDragon.efekNow.statKesehatanFisik * durasi * 0.5)).toFloat(),
                ((0.06f * durasi) + (EfekStatusDragon.efekNow.statKesehatanMental * durasi * 0.5)).toFloat(),
                ((0.05f * durasi) + (EfekStatusDragon.efekNow.statCinta * durasi * 0.5)).toFloat(),
                ((0.07f * durasi) + (EfekStatusDragon.efekNow.statHiburan * durasi * 0.5)).toFloat(),
                -((0.07f * durasi) + (EfekStatusDragon.efekNow.statIstirahat * durasi * 0.5)).toFloat(),
                ((0.04f * durasi) + (EfekStatusDragon.efekNow.statSosial * durasi * 0.5)).toFloat())

            GameManager.instance.PlusStatusPoint(status, this)

        }else if (kategori == "liburan"){
            ActiveLogin.infoActive.kegiatan17Liburan += durasi
            val status = EfekStatusDragon("", "", 0, 0, false, 0.5f * durasi, 0.5f * durasi,
                0f, ((0.09 * durasi) + (EfekStatusDragon.efekNow.statLapar * durasi * 1.5f)).toFloat(),
                ((0.04f * durasi) + (EfekStatusDragon.efekNow.statPengetahuan * durasi * 0.5)).toFloat(),
                ((0.09f * durasi) + (EfekStatusDragon.efekNow.statKesehatanFisik * durasi * 0.5)).toFloat(),
                ((0.12f * durasi) + (EfekStatusDragon.efekNow.statKesehatanMental * durasi * 0.5)).toFloat(),
                ((0.15f * durasi) + (EfekStatusDragon.efekNow.statCinta * durasi * 0.5)).toFloat(),
                ((0.19f * durasi) + (EfekStatusDragon.efekNow.statHiburan * durasi * 0.5)).toFloat(),
                ((0.11f * durasi) + (EfekStatusDragon.efekNow.statIstirahat * durasi * 0.5)).toFloat(),
                ((0.09f * durasi) + (EfekStatusDragon.efekNow.statSosial * durasi * 0.5)).toFloat())

            GameManager.instance.PlusStatusPoint(status, this)

        }else{
            ActiveLogin.infoActive.kegiatan18Lainnya += durasi
            val status = EfekStatusDragon("", "", 0, 0, false, 0.5f * durasi, 0.5f * durasi,
                0f, ((0.03 * durasi) + (EfekStatusDragon.efekNow.statLapar * durasi * 1.5f)).toFloat(),
                ((0.03f * durasi) + (EfekStatusDragon.efekNow.statPengetahuan * durasi * 0.5)).toFloat(),
                ((0.03f * durasi) + (EfekStatusDragon.efekNow.statKesehatanFisik * durasi * 0.5)).toFloat(),
                ((0.03f * durasi) + (EfekStatusDragon.efekNow.statKesehatanMental * durasi * 0.5)).toFloat(),
                ((0.03f * durasi) + (EfekStatusDragon.efekNow.statCinta * durasi * 0.5)).toFloat(),
                ((0.03f * durasi) + (EfekStatusDragon.efekNow.statHiburan * durasi * 0.5)).toFloat(),
                ((0.03f * durasi) + (EfekStatusDragon.efekNow.statIstirahat * durasi * 0.5)).toFloat(),
                ((0.03f * durasi) + (EfekStatusDragon.efekNow.statSosial * durasi * 0.5)).toFloat())

            GameManager.instance.PlusStatusPoint(status, this)
        }

        ActiveLogin.infoActive.totalSpendTime += durasi
        ApplicationManager.instance.SaveDataHistoryActivity(this)
        GameEventManager.instance.CekEventKegiatanNegatif(this)
    }

    private fun Footer(){
        val includedLayout = findViewById<View>(R.id.footer)
        val home: ImageView = includedLayout.findViewById(R.id.home_icon)
        home.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }

//        val bot: ImageView = includedLayout.findViewById(R.id.bot_icon)
//        bot.setOnClickListener {
//            val intent = Intent(this, BotActivity::class.java)
//            startActivity(intent)
//            finishAffinity()
//        }

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