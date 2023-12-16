package com.crysure.reflvy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.crysure.reflvy.data.NotifyChat
import com.crysure.reflvy.data.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PengawasanInputActivity : AppCompatActivity() {
    private lateinit var textInput : EditText
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pengawasan_input)

        textInput = findViewById(R.id.text_code)

        Footer()

        val btnInvite = findViewById<LinearLayout>(R.id.btn_invite)
        btnInvite.setOnClickListener {
            val code = textInput.text.toString()
            if (code.isNotEmpty()){
                LoadDataFromFirestore(code)
            }else{
                Toast.makeText(this, "Field tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun LoadDataFromFirestore(reveralCode : String){
        val userDocRef = db.collection("ReveralCode").document(reveralCode)
        userDocRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val userID = documentSnapshot.getString("userId") ?: ""

                    val jumlah = User.userData.jumlahReveral + 1

                    val sharedPreferences = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("reveralAnak", userID)
                    editor.putInt("jumlahReveral", jumlah)
                    editor.apply()

                    User.userData.loadFromSharedPreferences(sharedPreferences)

                    val intent = Intent(this, PengawasanActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Kode Tidak Ditemukan", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
        finishAffinity()
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