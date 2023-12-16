package com.crysure.reflvy.model

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.DefaultRetryPolicy
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.crysure.reflvy.BotActivity
import com.crysure.reflvy.MenuActivity
import com.crysure.reflvy.R
import com.crysure.reflvy.SettingsActivity
import com.crysure.reflvy.utils.MessagingAdapter
import com.crysure.reflvy.data.Message
import com.crysure.reflvy.utils.BotResponse
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import org.json.JSONObject

class ManualChatBotActivity : AppCompatActivity() {

    var messagesList = mutableListOf<Message>()
    lateinit var send_button : ImageView
    lateinit var text_box : EditText
    lateinit var container : RecyclerView
    private lateinit var linearContainer: LinearLayout
    private lateinit var inflater: LayoutInflater

    private lateinit var adapter: MessagingAdapter
    private val botList = listOf("Peter", "Francesca", "Luigi", "Igor")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_bot)

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

        linearContainer = findViewById(R.id.chat_container)
        inflater = LayoutInflater.from(this)

        send_button = findViewById(R.id.send_button)
        text_box = findViewById(R.id.quest_inp)



        val random = (0..3).random()
        //customBotMessage("Hello! Today you're speaking with ${botList[random]}, how may I help?")
        firstMessageBot("Hello! Hari ini biar aku ${botList[random]} yang mengobrol dengan mu, apa ada yang bisa saya bantu?")

        ClickEvent()
        //KeyboardHiding()
    }

    private fun SendChat(question : String){
        val templateChatuser: View = inflater.inflate(R.layout.template_chatuser, null)
        linearContainer.addView(templateChatuser)

        val text : TextView = templateChatuser.findViewById(R.id.answer_user)
        text.text = question

        val textTime : TextView = templateChatuser.findViewById(R.id.time_user)
        textTime.text = GetTime()

        val response = BotResponse.basicResponses(question)

        if(response == "idk"){
            GetResponse(question)
            //Toast.makeText(this, "Bot Processing", Toast.LENGTH_SHORT).show()
        }else {
            BotResponse(response)
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
                SendChat(editTextValue)
            }
        }
    }

    private fun BotResponse(answer : String){
        val templateChatuser: View = inflater.inflate(R.layout.template_chatbot, null)
        linearContainer.addView(templateChatuser)

        val text : TextView = templateChatuser.findViewById(R.id.answer_bot)
        text.text = answer

        val textTime : TextView = templateChatuser.findViewById(R.id.time_bot)
        textTime.text = GetTime()
    }


    private fun firstMessageBot(message: String){
        val templateChatuser: View = inflater.inflate(R.layout.template_chatbot, null)
        linearContainer.addView(templateChatuser)

        val text : TextView = templateChatuser.findViewById(R.id.answer_bot)
        text.text = message

        val textTime : TextView = templateChatuser.findViewById(R.id.time_bot)
        textTime.text = GetTime()
    }

//    private fun KeyboardHiding(){
//        KeyboardVisibilityEvent.setEventListener(this, object : KeyboardVisibilityEventListener {
//            override fun onVisibilityChanged(isOpen: Boolean) {
//                if (isOpen) {
//                    // Hide the components when the keyboard appears
//                    footer.visibility = View.GONE
//                    menu_icon.visibility = View.GONE
//                    setting_icon.visibility = View.GONE
//                } else {
//                    // Show the components when the keyboard disappears
//                    footer.visibility = View.VISIBLE
//                    menu_icon.visibility = View.VISIBLE
//                    setting_icon.visibility = View.VISIBLE
//                }
//            }
//        })
//    }


    private fun GetResponse(query: String) {

        text_box.setText("")
        val url02 = "https://api.openai.com/v1/chat/completions"
        val url = "https://api.openai.com/v1/completions"
        val queue: RequestQueue = Volley.newRequestQueue(applicationContext)
        val jsonObject = JSONObject()
        jsonObject.put("model", "text-davinci-003")
        jsonObject.put("prompt", query)
        jsonObject.put("max_tokens", 100)
        jsonObject.put("temperature", 0)

        val postRequest =
            object : JsonObjectRequest(Method.POST, url, jsonObject, Response.Listener { response ->
                // Respons dari server sukses, lakukan sesuatu dengan respons di sini.
                // Contoh: Mengambil teks dari respons dan menampilkannya di txtResponse.
                val responseMsg =
                    response.getJSONArray("choices").getJSONObject(0).getString("text")

                // Menghapus karakter baris baru (newline) dari teks respons
                val cleanedResponseMsg = responseMsg.replace("\n", "")

                BotResponse(cleanedResponseMsg)
            }, Response.ErrorListener { error ->
                // Terjadi kesalahan saat koneksi atau server memberikan respons error.
                Toast.makeText(this, "Gagal Merespon: ${error?.message}", Toast.LENGTH_SHORT).show()
            }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val params: MutableMap<String, String> = HashMap()
                    params["Content-Type"] = "application/json"
                    params["Authorization"] = "Bearer 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"
                    return params
                }
            }

        postRequest.setRetryPolicy(
            DefaultRetryPolicy(
                5000, // Timeout in milliseconds
                3,    // Maximum number of retries
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
        )
        queue.add(postRequest)
    }

}