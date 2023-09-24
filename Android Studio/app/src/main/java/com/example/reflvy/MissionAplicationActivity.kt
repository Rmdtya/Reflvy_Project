package com.example.reflvy

import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.reflvy.data.DataMisi

class MissionAplicationActivity : AppCompatActivity() {

    private lateinit var containerSubMisi : LinearLayout
    private lateinit var inflater: LayoutInflater

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mission_aplication)

        containerSubMisi = findViewById(R.id.container)
        inflater = LayoutInflater.from(this)

        val indexNomor = intent.getIntExtra("index", 0)
        Toast.makeText(this, indexNomor.toString(), Toast.LENGTH_SHORT).show()

        ShowMission(indexNomor)

        Toast.makeText(this, DataMisi.dataMisiAplikasi[0].statusMisi[0].toString(), Toast.LENGTH_SHORT).show()
    }


    private fun ShowMission(index : Int){
        for (i in 0 until DataMisi.dataMisiAplikasi[index].namaSubMisi.size){

            val template = inflater.inflate(R.layout.template_submission, null)

            val color = ContextCompat.getColor(this, R.color.mission_clear) // Ganti dengan warna yang Anda inginkan

            val lineAtas : LinearLayout = template.findViewById(R.id.line_atas)
            val circle_shape : LinearLayout = template.findViewById(R.id.icon_shape)
            val clearStatus : ImageView = template.findViewById(R.id.clear_img)

            val textSubmisi : TextView = template.findViewById(R.id.text_submisi)
            val textProgres : TextView = template.findViewById(R.id.text_progres)

            if (DataMisi.dataMisiAplikasi[index].statusMisi[i]){
                lineAtas.backgroundTintList = ColorStateList.valueOf(color)
                circle_shape.backgroundTintList = ColorStateList.valueOf(color)
                clearStatus.visibility = View.VISIBLE
            }

            val progresNow : String = DataMisi.dataMisiAplikasi[index].progresNow[i].toString()
            val maxProgres : String = DataMisi.dataMisiAplikasi[index].maxProgres[i].toString()

            textSubmisi.text = DataMisi.dataMisiAplikasi[index].namaSubMisi[i]
            textProgres.text = progresNow + "/" + maxProgres

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            params.setMargins(0, -46, 0, 0) // Margin top -10dp
            template.layoutParams = params

            containerSubMisi.addView(template)
        }
    }
}