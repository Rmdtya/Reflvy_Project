package com.example.reflvy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView

class SchedullingActivity : AppCompatActivity() {

    private lateinit var progressView : View
    private lateinit var clockLayout : ImageView

    private lateinit var addButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedulling)

        addButton = findViewById(R.id.addbutton)

        addButton.setOnClickListener{
            // val dialogFragment = MyDialogFragment()
            // dialogFragment.show(supportFragmentManager, "MyDialogFragment")
        }

    }
}