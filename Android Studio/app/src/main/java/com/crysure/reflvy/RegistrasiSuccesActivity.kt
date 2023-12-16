package com.crysure.reflvy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class RegistrasiSuccesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrasi_succes)

        val btn : Button = findViewById(R.id.button)

        btn.setOnClickListener{
            val intent = Intent(this, SigninActivity::class.java)
            startActivity(intent)

            finishAffinity()
        }
    }
}