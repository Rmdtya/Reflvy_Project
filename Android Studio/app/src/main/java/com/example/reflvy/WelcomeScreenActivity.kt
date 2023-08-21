package com.example.reflvy

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class WelcomeScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_screen)

        val button : Button = findViewById(R.id.button)

        button.setOnClickListener{

            val sharedPreferences = getSharedPreferences("login_status", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putBoolean("isWelcome", true)
            editor.apply()

            val intent = Intent(this, SigninActivity::class.java)
            startActivity(intent)

            finishAffinity()
        }
    }
}