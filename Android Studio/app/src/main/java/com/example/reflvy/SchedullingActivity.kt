package com.example.reflvy

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.NetworkResponse
import com.android.volley.Response
import com.android.volley.RetryPolicy
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.reflvy.data.DataDaily
import com.example.reflvy.data.DataNotification
import com.example.reflvy.data.NotifyChat
import com.example.reflvy.fragment.AddActivityFragment
import com.example.reflvy.fragment.AddActivityListener
import com.example.reflvy.fragment.EditActivityFragment
import com.example.reflvy.fragment.FilterSchedulling
import com.example.reflvy.utils.ApplicationManager
import me.tankery.lib.circularseekbar.CircularSeekBar
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.Random

class SchedullingActivity : AppCompatActivity(), AddActivityListener {

    private lateinit var progressView : View
    private lateinit var clockLayout : ImageView

    private lateinit var linearContainer : LinearLayout
    private lateinit var clockContainerNight : ConstraintLayout
    private lateinit var clockContainerDay : ConstraintLayout
    private lateinit var inflater: LayoutInflater

    private lateinit var addButton : LinearLayout
    private lateinit var filterButton : LinearLayout

    private lateinit var handler: Handler
    private lateinit var minutesImageKanan : ImageView
    private lateinit var hoursImageKanan : ImageView
    private lateinit var minutesImageKiri : ImageView
    private lateinit var hoursImageKiri : ImageView
    private var isAM : Boolean = true

    private var isfragmentNotActive : Boolean = true

    private val stringURLEndPoint = "https://api.openai.com/v1/chat/completions"
    private val stringAPIKey = "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"

    companion object{
        var sn : Boolean = false
        var sl : Boolean = false
        var rb : Boolean = false
        var km : Boolean = false
        var jm : Boolean = false
        var st : Boolean = false
        var mg : Boolean = false
        var progres : String = "semua"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedulling)

        Footer()

        minutesImageKanan = findViewById(R.id.menitJarumImageViewKanan)
        hoursImageKanan = findViewById(R.id.jamJarumImageViewKanan)

        minutesImageKiri = findViewById(R.id.menitJarumImageViewKiri)
        hoursImageKiri = findViewById(R.id.jamJarumImageViewKiri)

        addButton = findViewById(R.id.btn_add)
        linearContainer = findViewById(R.id.container_kegiatan)
        clockContainerNight = findViewById(R.id.night_container)
        clockContainerDay = findViewById(R.id.day_container)
        inflater = LayoutInflater.from(this)
        filterButton = findViewById(R.id.btn_filter)

        ShowDailyActivity()

        addButton.setOnClickListener{
            EnableDisableClick(false)
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            val addActivityFragment = AddActivityFragment()
            fragmentTransaction.replace(R.id.fragment_container, addActivityFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        filterButton.setOnClickListener {
            EnableDisableClick(false)
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            val filterFragment = FilterSchedulling()
            fragmentTransaction.replace(R.id.fragment_container, filterFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val clockNow = (hour * 60) + + minute

        if (clockNow >= 0 && clockNow <= 720) {
            isAM = true
            minutesImageKiri.visibility = View.VISIBLE
            hoursImageKiri.visibility = View.VISIBLE

            minutesImageKanan.visibility = View.INVISIBLE
            hoursImageKanan.visibility = View.INVISIBLE
        } else {
            isAM = false
            minutesImageKiri.visibility = View.INVISIBLE
            hoursImageKiri.visibility = View.INVISIBLE

            minutesImageKanan.visibility = View.VISIBLE
            hoursImageKanan.visibility = View.VISIBLE
        }

        handler = Handler()

        // Jalankan fungsi updateClockHands setiap 30 detik
        handler.postDelayed(updateClockHandsRunnable, 0)
    }

    override fun onDataAdded(namaKegiatan : String, lama : Int, mulai : String, selesai : String, senin : Boolean, selasa : Boolean, rabu : Boolean, kamis : Boolean, jumat : Boolean, sabtu : Boolean, minggu : Boolean) {

        EnableDisableClick(true)

        val index = DataDaily.dataKegiatan.size + 1
        val colorCode = getRandomColor()

        val sdf = SimpleDateFormat("HH.mm", Locale.getDefault())
        val startDate = sdf.parse(mulai)
        val endDate = sdf.parse(selesai)
        val startMinutes = (startDate.hours * 60) + startDate.minutes
        val endMinutes = (endDate.hours * 60) + endDate.minutes

        val currentDate = Date()
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val now = dateFormat.format(currentDate)

        val kegiatan = DataDaily(index, "belum", namaKegiatan, "Olahraga", lama, mulai, selesai, false,0,  senin, selasa, rabu, kamis, jumat, sabtu, minggu, colorCode, now, "", startMinutes, endMinutes)

        var query = "Jawab dengan 2 kata : bekerja,belajar formal,membaca,bersantai,istirahat,belanja,bermusik,beribadah,bermain game,hiburan digital,operasi komputer,pekerjaan rumah,komunitas,bersosialisai,healing,olahraga,hiburan,lainnya. dari kategori diatas, $namaKegiatan termasuk atau mendekati mana?"
        ChatGPT(query, kegiatan)
    }

    override fun GetBack() {
        EnableDisableClick(true)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun OnDataEdit(index: Int, namaKegiatan: String, lama: Int, mulai: String, selesai: String, senin: Boolean, selasa: Boolean, rabu: Boolean, kamis: Boolean, jumat: Boolean, sabtu: Boolean, minggu: Boolean
    ) {
        EnableDisableClick(true)
        val sdf = SimpleDateFormat("HH.mm", Locale.getDefault())
        val startDate = sdf.parse(mulai)
        val endDate = sdf.parse(selesai)
        val startMinutes = (startDate.hours * 60) + startDate.minutes
        val endMinutes = (endDate.hours * 60) + endDate.minutes

        DataDaily.dataKegiatan[index].waktuMulai = mulai
        DataDaily.dataKegiatan[index].waktuSelesai = selesai
        DataDaily.dataKegiatan[index].senin = senin
        DataDaily.dataKegiatan[index].selasa = selasa
        DataDaily.dataKegiatan[index].rabu = rabu
        DataDaily.dataKegiatan[index].kamis = kamis
        DataDaily.dataKegiatan[index].jumat = jumat
        DataDaily.dataKegiatan[index].sabtu = sabtu
        DataDaily.dataKegiatan[index].minggu = minggu
        DataDaily.dataKegiatan[index].startMinute = startMinutes
        DataDaily.dataKegiatan[index].endMinutes = endMinutes

        ApplicationManager.instance.SaveKegiatan(this)
        DataNotification.dataNotifikasi.clear()
        DataNotification.CopyDataKegiatan()
        ApplicationManager.instance.SaveNotifikasiKegiatan(this)

        ClearContainer()
        OnFilterSchedulling(progres, sn, sl, rb, km, jm, st, mg)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun OnDelete(index: Int) {
        EnableDisableClick(true)
        removeDataDaily(index)

        ApplicationManager.instance.SaveKegiatan(this)
        DataNotification.dataNotifikasi.clear()
        DataNotification.CopyDataKegiatan()
        ApplicationManager.instance.SaveNotifikasiKegiatan(this)

        ClearContainer()
        OnFilterSchedulling(progres, sn, sl, rb, km, jm, st, mg)
    }

    fun getDayString(index: Int): String {
        val days = listOf("Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu")
        return days[index]
    }

    fun generateDayText(selectedDays: List<Boolean>): String {
        val trueIndices = selectedDays.mapIndexedNotNull { index, value ->
            if (value) index else null
        }
        val numTrue = trueIndices.size

        if (numTrue >= 3) {
            val consecutiveRanges = mutableListOf<List<Int>>()
            var currentRange = mutableListOf<Int>()

            for (index in trueIndices) {
                if (currentRange.isEmpty() || index == currentRange.last() + 1) {
                    currentRange.add(index)
                } else {
                    consecutiveRanges.add(currentRange.toList())
                    currentRange.clear()
                    currentRange.add(index)
                }
            }
            consecutiveRanges.add(currentRange.toList())

            val formattedRanges = consecutiveRanges.map { range ->
                if (range.size > 1) {
                    "${getDayString(range.first())}-${getDayString(range.last())}"
                } else {
                    getDayString(range.first())
                }
            }
            return formattedRanges.joinToString(", ")
        } else if (numTrue == 7) {
            return "Senin - Minggu"
        } else {
            return trueIndices.joinToString(", ") { getDayString(it) }
        }
    }


    fun addTimeSeekBar(startTime: String, endTime: String, colorCode : Int) {

        val sdf = SimpleDateFormat("HH.mm", Locale.getDefault())
        val startDate = sdf.parse(startTime)
        val endDate = sdf.parse(endTime)

        if (startDate != null && endDate != null) {
            val startMinutes = (startDate.hours * 60) + startDate.minutes
            val endMinutes = (endDate.hours * 60) + endDate.minutes

            val isStartTimeInRangeDay = startMinutes >= 0 && startMinutes <= 720
            val isEndTimeInRangeDay = endMinutes >= 0 && endMinutes <= 720

            val isStartTimeInRangeNight = startMinutes > 720 && startMinutes <= 1440
            val isEndTimeInRangeNight = endMinutes >= 720 && endMinutes <= 1440

            val durationMinutes = endMinutes - startMinutes

            // Determine which clockContainer to add the view based on the start time
            if (isStartTimeInRangeDay && isEndTimeInRangeDay && durationMinutes <= 720 && endMinutes >= startMinutes) {

                    val template: View = inflater.inflate(R.layout.template_clock, clockContainerNight, false)
                    val circularSeekBar: CircularSeekBar = template.findViewById(R.id.seekBar)
                    clockContainerNight.addView(template)

                    circularSeekBar.progress = durationMinutes.toFloat()

                    val intervalMinutes = startMinutes
                    val rotationDegrees = (intervalMinutes.toFloat() / 2).toInt()

                    circularSeekBar.rotation = rotationDegrees.toFloat()
                    circularSeekBar.isEnabled = false


                    circularSeekBar.circleProgressColor = colorCode

            } else if (isStartTimeInRangeNight && isEndTimeInRangeNight && durationMinutes <= 720 && endMinutes >= startMinutes) {

                val template: View = inflater.inflate(R.layout.template_clock, clockContainerDay, false)
                val circularSeekBar: CircularSeekBar = template.findViewById(R.id.seekBar)
                clockContainerDay.addView(template)

                circularSeekBar.progress = durationMinutes.toFloat()

                val intervalMinutes = startMinutes
                val rotationDegrees = (intervalMinutes.toFloat() / 2).toInt()

                circularSeekBar.rotation = rotationDegrees.toFloat()
                circularSeekBar.isEnabled = false
                circularSeekBar.circleProgressColor = colorCode

            }else if (startMinutes <= 720 && endMinutes >= 720) {
                // Add to both clockContainerNight and clockContainerDay
                val templateNight: View = inflater.inflate(R.layout.template_clock, clockContainerNight, false)
                val circularSeekBarNight: CircularSeekBar = templateNight.findViewById(R.id.seekBar)
                clockContainerNight.addView(templateNight)

                val templateDay: View = inflater.inflate(R.layout.template_clock, clockContainerDay, false)
                val circularSeekBarDay: CircularSeekBar = templateDay.findViewById(R.id.seekBar)
                clockContainerDay.addView(templateDay)

                // Calculate the duration for each container
                val nightDurationMinutes = 720 - startMinutes
                val dayDurationMinutes = durationMinutes - nightDurationMinutes

                circularSeekBarNight.progress = nightDurationMinutes.toFloat()
                circularSeekBarDay.progress = dayDurationMinutes.toFloat()

                val rotationDegreesNight = (startMinutes.toFloat() / 2).toInt()
                val rotationDegreesDay = (720.toFloat() / 2).toInt()

                circularSeekBarNight.rotation = rotationDegreesNight.toFloat()
                circularSeekBarDay.rotation = rotationDegreesDay.toFloat()

                circularSeekBarNight.isEnabled = false
                circularSeekBarDay.isEnabled = false
                circularSeekBarNight.circleProgressColor = colorCode
                circularSeekBarDay.circleProgressColor = colorCode

            }else if (startMinutes >= 720 && endMinutes <= 720) {
                // Add to both clockContainerNight and clockContainerDay
                val templateNight: View = inflater.inflate(R.layout.template_clock, clockContainerNight, false)
                val circularSeekBarNight: CircularSeekBar = templateNight.findViewById(R.id.seekBar)
                clockContainerNight.addView(templateNight)

                val templateDay: View = inflater.inflate(R.layout.template_clock, clockContainerDay, false)
                val circularSeekBarDay: CircularSeekBar = templateDay.findViewById(R.id.seekBar)
                clockContainerDay.addView(templateDay)

                // Calculate the duration for each container
                val dayDurationMinutes = 720 - startMinutes
                val nightDurationMinutes = durationMinutes - dayDurationMinutes

                circularSeekBarNight.progress = nightDurationMinutes.toFloat()
                circularSeekBarDay.progress = dayDurationMinutes.toFloat()

                val rotationDegreesNight = (720.toFloat() / 2).toInt()
                val rotationDegreesDay = (startMinutes.toFloat() / 2).toInt()

                circularSeekBarNight.rotation = rotationDegreesNight.toFloat()
                circularSeekBarDay.rotation = rotationDegreesDay.toFloat()

                circularSeekBarNight.isEnabled = false
                circularSeekBarDay.isEnabled = false
                circularSeekBarNight.circleProgressColor = colorCode
                circularSeekBarDay.circleProgressColor = colorCode

            }
        }
    }

    fun getRandomColor(): Int {
        val random = Random()

        var red = 0
        var green = 0
        var blue = 0

        // Loop hingga mendapatkan warna yang bukan hitam atau putih
        while (red < 50 && green < 50 && blue < 50) {
            red = random.nextInt(256)
            green = random.nextInt(256)
            blue = random.nextInt(256)
        }

        // Buat warna dengan transparansi 50%
        val color = Color.argb(128, red, green, blue)

        return color
    }

    fun ShowDailyActivity(){

        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        when (dayOfWeek) {
            Calendar.MONDAY -> {

                val kegiatan = DataDaily.dataKegiatan
                    .filter { it.senin && it.status == "belum" }
                    .sortedBy { it.startMinute }

                kegiatan.forEach { data ->
                    ShowIt(
                        data, true
                    )
                }
                ResetDay()
                sn = true
            }
            Calendar.TUESDAY -> {

                val kegiatan = DataDaily.dataKegiatan
                    .filter { it.selasa && it.status == "belum" }
                    .sortedBy { it.startMinute }

                kegiatan.forEach { data ->
                    ShowIt(
                        data, true
                    )
                }
                ResetDay()
                sl = true
            }
            Calendar.WEDNESDAY -> {

                val kegiatan = DataDaily.dataKegiatan
                    .filter { it.rabu && it.status == "belum" }
                    .sortedBy { it.startMinute }

                kegiatan.forEach { data ->
                    ShowIt(
                        data, true
                    )
                }
                ResetDay()
                rb = true
            }
            Calendar.THURSDAY -> {

                val kegiatan = DataDaily.dataKegiatan
                    .filter { it.kamis && it.status == "belum" }
                    .sortedBy { it.startMinute }

                kegiatan.forEach { data ->
                    ShowIt(
                        data, true
                    )
                }
                ResetDay()
                km = true
            }Calendar.FRIDAY -> {

            val kegiatan = DataDaily.dataKegiatan
                .filter { it.jumat && it.status == "belum" }
                .sortedBy { it.startMinute }

            kegiatan.forEach { data ->
                ShowIt(
                    data, true
                )
            }
                ResetDay()
                jm = true
            }Calendar.SATURDAY ->{

                val kegiatan = DataDaily.dataKegiatan
                    .filter { it.sabtu && it.status == "belum" }
                    .sortedBy { it.startMinute }

                kegiatan.forEach { data ->
                    ShowIt(
                        data, true
                    )
                }
                ResetDay()
                st = true
            }Calendar.SUNDAY -> {

                val kegiatan = DataDaily.dataKegiatan
                    .filter { it.minggu && it.status == "belum" }
                    .sortedBy { it.startMinute }

                kegiatan.forEach { data ->
                    ShowIt(
                        data, true
                    )
                }
                ResetDay()
                mg = true
            }
            else -> {
                // Hari lainnya (Jumat, Sabtu, Minggu)
                // Tambahkan kode yang ingin Anda jalankan untuk hari-hari lainnya di sini
            }
        }
    }

    private fun ResetDay(){
        sn = false
        sl = false
        rb = false
        km = false
        jm = false
        st = false
        mg = false
    }

    fun ShowIt(datadaily : DataDaily, callBy : Boolean) {

        val template: View = inflater.inflate(R.layout.template_schedulling, null)
        linearContainer.addView(template)

        val textName : TextView = template.findViewById(R.id.text_name)
        val textTime : TextView = template.findViewById(R.id.text_tgl)
        val checkBox : CheckBox = template.findViewById(R.id.proses)
        val conBox : LinearLayout = template.findViewById(R.id.con_box)

        val imageIcon : ImageView = template.findViewById(R.id.icon_image)

        val booleanValues = listOf(datadaily.senin, datadaily.selasa, datadaily.rabu, datadaily.kamis, datadaily.jumat, datadaily.sabtu, datadaily.minggu)
        val day = generateDayText(booleanValues)
        val waktu = datadaily.waktuMulai + " - " + datadaily.waktuSelesai

        SetImageCategory(datadaily.kategori, imageIcon)

        textName.text = datadaily.namaKegiatan
        textTime.text = waktu + " / " + day

        if(!callBy){
            conBox.visibility = View.INVISIBLE
            checkBox.isEnabled = false
        }

        if(datadaily.proses){
            checkBox.isChecked = true
            checkBox.isClickable = false
            checkBox.isEnabled = false
        }else{
            checkBox.isChecked = false
        }

        checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                DataDaily.dataKegiatan[datadaily.dataNomor - 1].proses = true
                DataDaily.dataKegiatan[datadaily.dataNomor - 1].progresNow++
                checkBox.isClickable = false
                checkBox.isEnabled = false

                ApplicationManager.instance.SaveKegiatan(this)
                DataNotification.dataNotifikasi.clear()
                DataNotification.CopyDataKegiatan()
                ApplicationManager.instance.SaveNotifikasiKegiatan(this)
            } else {
                DataDaily.dataKegiatan[datadaily.dataNomor - 1].proses = false
            }
        }

        template.setOnClickListener {
            if(isfragmentNotActive){
                EnableDisableClick(false)
                val editActivityFragment = EditActivityFragment()
                val bundle = Bundle()
                bundle.putInt("index", datadaily.dataNomor - 1)
                editActivityFragment.arguments = bundle

                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.fragment_container, editActivityFragment)
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }

        }

        addTimeSeekBar(datadaily.waktuMulai, datadaily.waktuSelesai, datadaily.progresColor)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun OnFilterSchedulling(status: String, senin: Boolean, selasa: Boolean, rabu: Boolean, kamis: Boolean, jumat: Boolean, sabtu: Boolean, minggu: Boolean
    ) {
        EnableDisableClick(true)
        ClearContainer()

        sn = senin
        sl = selasa
        rb = rabu
        km = kamis
        jm = jumat
        st = sabtu
        mg = minggu

        progres = status

        val selectedDays = mutableListOf<String>()
        if (senin) selectedDays.add("senin")
        if (selasa) selectedDays.add("selasa")
        if (rabu) selectedDays.add("rabu")
        if (kamis) selectedDays.add("kamis")
        if (jumat) selectedDays.add("jumat")
        if (sabtu) selectedDays.add("sabtu")
        if (minggu) selectedDays.add("minggu")

        // Panggil fungsi filter dengan kriteria yang diterima
        val filteredData = filterData(status, selectedDays)

        val hariIni = LocalDate.now().dayOfWeek

        // Membuat map yang memetakan DayOfWeek ke parameter boolean yang sesuai
        val hariMap = mapOf(
            DayOfWeek.MONDAY to senin,
            DayOfWeek.TUESDAY to selasa,
            DayOfWeek.WEDNESDAY to rabu,
            DayOfWeek.THURSDAY to kamis,
            DayOfWeek.FRIDAY to jumat,
            DayOfWeek.SATURDAY to sabtu,
            DayOfWeek.SUNDAY to minggu
        )

        // Memeriksa apakah parameter hari sama dengan hari ini
        if (hariMap[hariIni] == true) {
            // Panggil fungsi a jika parameter hari yang sesuai adalah true
            ShowFilterTrue(filteredData)
        } else {
            // Panggil fungsi b jika parameter hari yang sesuai adalah false
            ShowFilter(filteredData)
        }
    }

    private fun ClearContainer() {
        linearContainer.removeAllViews()
        clockContainerNight.removeAllViews()
        clockContainerDay.removeAllViews()
    }

    fun filterData(status: String, selectedDays: List<String>): List<DataDaily> {
        return DataDaily.dataKegiatan.filter { data ->
            // Cek status
            val statusMatch = when (status) {
                "semua" -> true // Tampilkan semua data tanpa filter
                "belum" -> data.proses == false // Tampilkan data dengan status "belum"
                else -> false // Filter untuk status lainnya
            }

            // Cek hari-hari yang dipilih
            val daysMatch = selectedDays.isEmpty() || selectedDays.all { day ->
                when (day) {
                    "senin" -> data.senin
                    "selasa" -> data.selasa
                    "rabu" -> data.rabu
                    "kamis" -> data.kamis
                    "jumat" -> data.jumat
                    "sabtu" -> data.sabtu
                    "minggu" -> data.minggu
                    else -> false
                }
            }

            // Gabungkan kriteria status dan hari-hari yang dipilih
            statusMatch && daysMatch
        }
    }

    private fun ShowFilter(filteredData: List<DataDaily>) {
        for (data in filteredData) {
            ShowIt(
                data, false
            )
        }
    }

    private fun ShowFilterTrue(filteredData: List<DataDaily>) {
        for (data in filteredData) {
            ShowIt(
                data, true
            )
        }
    }

    private val updateClockHandsRunnable = object : Runnable {
        override fun run() {
            updateClockHands()

            // Jalankan kembali fungsi ini setiap 30 detik
            handler.postDelayed(this, 30000) // 30000 millisekon (30 detik)
        }
    }

    fun removeDataDaily(indexToRemove: Int) {
        if (indexToRemove < 0 || indexToRemove >= DataDaily.dataKegiatan.size) {
            // Jika indeks yang dihapus tidak valid, lepaskan error atau lakukan penanganan sesuai kebutuhan.
            println("Indeks yang dihapus tidak valid.")
            return
        }

        // Hapus elemen dengan indeks yang diberikan.
        DataDaily.dataKegiatan.removeAt(indexToRemove)

        // Perbarui indeks elemen-elemen yang ada di atas indeks yang dihapus.
        for (i in indexToRemove until DataDaily.dataKegiatan.size) {
            DataDaily.dataKegiatan[i].dataNomor = i + 1
        }
    }

    private fun updateClockHands() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        // Menghitung sudut rotasi untuk jarum jam (dalam format derajat)
        val hourRotation = (hour % 12 + minute / 60.0) * 360 / 12

        // Menghitung sudut rotasi untuk jarum menit (dalam format derajat)
        val minuteRotation = minute.toFloat() * 360 / 60

        // Mengatur rotasi untuk gambar jarum jam dan jarum menit
        if (isAM) {
            hoursImageKiri.rotation = hourRotation.toFloat()
            minutesImageKiri.rotation = minuteRotation
        } else {
            hoursImageKanan.rotation = hourRotation.toFloat()
            minutesImageKanan.rotation = minuteRotation
        }
    }

    fun ChatGPT(message: String, data : DataDaily) {
        val jsonObject = JSONObject()

        var stringOutput = ""

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

                Toast.makeText(this, stringOutput, Toast.LENGTH_SHORT).show()

                GetCategory(stringOutput, data)

            }, Response.ErrorListener { error ->
                stringOutput = "error"

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

    private fun GetCategory(response : String, data : DataDaily){
        var category : String
        if (response.toLowerCase().contains("bekerja")) {                                           //1
            category = "bekerja"

        } else if (response.toLowerCase().contains("belajar formal")) {
            category = "belajar formal"

        }else if (response.toLowerCase().contains("membaca")) {
            category = "membaca"

        }else if (response.toLowerCase().contains("bersantai")) {
            category = "bersantai"

        }else if (response.toLowerCase().contains("istirahat")) {
            category = "istirahat"

        }else if (response.toLowerCase().contains("belanja")) {
            category = "belanja"

        }else if (response.toLowerCase().contains("bermusik")) {
            category = "bermusik"

        }else if (response.toLowerCase().contains("beribadah")) {
            category = "beribadah"

        }else if (response.toLowerCase().contains("bermain game")) {
            category = "bermain game"

        }else if (response.toLowerCase().contains("hiburan digital")) {
            category = "hiburan digital"

        }else if (response.toLowerCase().contains("operasi komputer")) {
            category = "operasi komputer"

        }else if (response.toLowerCase().contains("pekerjaan rumah")) {
            category = "pekerjaan rumah"

        }else if (response.toLowerCase().contains("komunitas")) {
            category = "komunitas"

        }else if (response.toLowerCase().contains("bersosialisasi")) {
            category = "bersosialisasi"

        }else if (response.toLowerCase().contains("healing")) {
            category = "healing"

        }else if (response.toLowerCase().contains("olahraga")) {
            category = "olahraga"

        }else if (response.toLowerCase().contains("liburan")) {
            category = "liburan"

        }else{
            category = "lainnya"
        }

        Toast.makeText(this, category, Toast.LENGTH_SHORT).show()

        val dataDaily = data.copy(kategori = category)

        DataDaily.dataKegiatan.add(dataDaily)

        ApplicationManager.instance.SaveKegiatan(this)
        DataNotification.dataNotifikasi.clear()
        DataNotification.CopyDataKegiatan()
        ApplicationManager.instance.SaveNotifikasiKegiatan(this)

        ClearContainer()
        ShowDailyActivity()
    }

    private fun SetImageCategory(kategori : String, img : ImageView){
        if (kategori == "bekerja"){
            img.setImageResource(R.drawable.dailyicom_01bekerja)
        }else if (kategori == "belajar formal"){
            img.setImageResource(R.drawable.dailyicom_02belajarformal)
        }else if (kategori == "membaca"){
            img.setImageResource(R.drawable.dailyicom_03membaca)
        }else if (kategori == "bersantai"){
            img.setImageResource(R.drawable.dailyicom_04bersantai)
        }else if (kategori == "istirahat"){
            img.setImageResource(R.drawable.dailyicom_05istirahat)
        }else if (kategori == "belanja"){
            img.setImageResource(R.drawable.dailyicom_06belanja)
        }else if (kategori == "bermusik"){
            img.setImageResource(R.drawable.dailyicom_07bermusik)
        }else if (kategori == "beribadah"){
            img.setImageResource(R.drawable.dailyicom_08beribadah)
        }else if (kategori == "bermain game"){
            img.setImageResource(R.drawable.dailyicom_09bermaingame)
        }else if (kategori == "hiburan digital"){
            img.setImageResource(R.drawable.dailyicom_10hiburandigital)
        }else if (kategori == "operasi komputer"){
            img.setImageResource(R.drawable.dailyicom_11operasikomputer)
        }else if (kategori == "pekerjaan rumah"){
            img.setImageResource(R.drawable.dailyicom_12pekerjaanrumah)
        }else if (kategori == "komunitas"){
            img.setImageResource(R.drawable.dailyicom_13komunitas)
        }else if (kategori == "bersosialisasi"){
            img.setImageResource(R.drawable.dailyicom_14bersosialisasi)
        }else if (kategori == "healing"){
            img.setImageResource(R.drawable.dailyicom_15healing)
        }else if (kategori == "olahraga"){
            img.setImageResource(R.drawable.dailyicom_16olahraga)
        }else if (kategori == "liburan"){
            img.setImageResource(R.drawable.dailyicom_17liburan)
        }else{
            img.setImageResource(R.drawable.dailyicon_lainnya)
        }
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
        // Aktifkan kembali objek yang dapat diklik (Enable) di sini
        EnableDisableClick(true)
    }

    private fun EnableDisableClick(kondisi : Boolean){
        isfragmentNotActive = kondisi

        addButton.isEnabled = kondisi
        filterButton.isEnabled = kondisi
    }
}
