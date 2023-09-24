package com.example.reflvy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.NetworkResponse
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.RetryPolicy
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.reflvy.PlayerActivity.Companion.currentIndex
import com.example.reflvy.data.Message
import com.example.reflvy.data.NotifyChat
import com.example.reflvy.model.OpenAIRequestModel
import com.example.reflvy.model.OpenAIResponseModel
import com.example.reflvy.utils.ApplicationManager
import com.example.reflvy.utils.BotResponse
import com.example.reflvy.utils.MessagingAdapter
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BotActivity : AppCompatActivity() {
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

    private lateinit var adapter: MessagingAdapter
    private val botList = listOf("Peter", "Francesca", "Luigi", "Igor")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_bot)


        Footer()

        linearContainer = findViewById(R.id.chat_container)
        inflater = LayoutInflater.from(this)

        send_button = findViewById(R.id.send_button)
        text_box = findViewById(R.id.quest_inp)


        val random = (0..3).random()
        //customBotMessage("Hello! Today you're speaking with ${botList[random]}, how may I help?")
        firstMessageBot("Hello! Hari ini biar aku ${botList[random]} yang mengobrol dengan mu, apa ada yang bisa saya bantu?")

        ClickEvent()
        LoadChat()

        Toast.makeText(this, NotifyChat.notifChat.size.toString(), Toast.LENGTH_SHORT).show()

        ApplicationManager.instance.NonActiveNotif(this)
        //KeyboardHiding()
    }

    fun LoadChat(){
        for(chat in NotifyChat.notifChat){

            if(chat.from == "bot"){
                val templateChatBot: View = inflater.inflate(R.layout.template_chatbot, null)
                linearContainer.addView(templateChatBot)

                BotResponse(templateChatBot, chat, false)
            }else if(chat.from == "user"){
                SendChat(chat.chat, false)
            }
        }
    }

    private fun SendChat(question : String, respon : Boolean){
        val templateChatuser: View = inflater.inflate(R.layout.template_chatuser, null)
        linearContainer.addView(templateChatuser)

        val text : TextView = templateChatuser.findViewById(R.id.answer_user)
        text.text = question

        val textTime : TextView = templateChatuser.findViewById(R.id.time_user)
        textTime.text = GetTime()

        if(respon){
            buttonChatGPT(question)
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
                SendChat(editTextValue, true)
                text_box.setText("")
                text_box.isEnabled = false

                val time : String = GetTime()
                val chat = NotifyChat(editTextValue, "user", false, true, time, false, "")

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
                    }else{
                        text.text = notifyChat.chat
                    }
                }
            }, typingSpeed.toLong())
        }else{
            text.text = notifyChat.chat
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

    fun buttonChatGPT(message: String) {
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

                stringOutput = "$stringOutput$stringText"
                stopTypingAnimation(templateChatBot)
                text.text = ""
                val time : String = GetTime()
                val chat = NotifyChat(stringOutput, "bot", true, true, time, false, "")

                NotifyChat.notifChat.add(chat)
                ApplicationManager.instance.SaveNotifPrefs(this)

                BotResponse(templateChatBot, chat, true)
                text_box.isEnabled = true
            }, Response.ErrorListener { error ->
                val error : String = "Error Response, Coba Beberapa Saat Lagi"
                val time : String = GetTime()
                val chat = NotifyChat(error, "bot", true, true, time, false, "")

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


//    private fun GetResponse(query: String) {
//
//        text_box.setText("")
//        val url02 = "https://api.openai.com/v1/chat/completions"
//        val url = "https://api.openai.com/v1/completions"
//        val type = "gpt-3.5-turbo"
//        val type2 = "text-davinci-003"
//        val queue: RequestQueue = Volley.newRequestQueue(applicationContext)
//
//        val jsonArrayMesaage = JSONArray()
//        val jsonObject = JSONObject()
//        jsonObject.put("model", "gpt-3.5-turbo")
//        jsonObject.put("role", "user")
//        jsonObject.put("content", query)
//        jsonObject.put("max_tokens", 1000)
//        jsonObject.put("temperature", 0)
//
//        jsonObject.put("message", jsonArrayMesaage)
//
//
//        val postRequest =
//            object : JsonObjectRequest(Method.POST, url02, jsonObject, Response.Listener { response ->
//                // Respons dari server sukses, lakukan sesuatu dengan respons di sini.
//                // Contoh: Mengambil teks dari respons dan menampilkannya di txtResponse.
//                val responseMsg =
//                    response.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content")
//
//                // Menghapus karakter baris baru (newline) dari teks respons
//                val cleanedResponseMsg = responseMsg.replace("\n", "")
//
//                BotResponse(cleanedResponseMsg)
//            }, Response.ErrorListener { error ->
//                // Terjadi kesalahan saat koneksi atau server memberikan respons error.
//                Toast.makeText(this, "Gagal Merespon: ${error?.message}", Toast.LENGTH_SHORT).show()
//            }) {
//                override fun getHeaders(): MutableMap<String, String> {
//                    val params: MutableMap<String, String> = HashMap()
//                    params["Content-Type"] = "application/json"
//                    params["Authorization"] = "Bearer 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"
//                    return params
//                }
//            }
//
//        postRequest.setRetryPolicy(
//            DefaultRetryPolicy(
//                5000, // Timeout in milliseconds
//                3,    // Maximum number of retries
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
//            )
//        )
//        queue.add(postRequest)
//    }
}