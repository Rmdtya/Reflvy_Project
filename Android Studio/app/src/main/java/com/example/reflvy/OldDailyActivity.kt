package com.example.reflvy

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.reflvy.data.DataDaily
import com.example.reflvy.data.NotifyChat
import com.example.reflvy.fragment.FilterActivity
import com.example.reflvy.fragment.FilterActivityListener

class OldDailyActivity : AppCompatActivity(), FilterActivityListener {

    private lateinit var container : LinearLayout
    private lateinit var inflater: LayoutInflater
    private lateinit var btn_filter : LinearLayout

    private lateinit var searchBar : EditText

    private lateinit var buttonAdd : TextView

    companion object{
        var sn : Boolean = false
        var sl : Boolean = false
        var rb : Boolean = false
        var km : Boolean = false
        var jm : Boolean = false
        var st : Boolean = false
        var mg : Boolean = false
        var stat : String = "semua"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_old_daily)

        Footer()

        container = findViewById(R.id.container_history)
        inflater = LayoutInflater.from(this)

        searchBar = findViewById(androidx.appcompat.R.id.search_bar)

        btn_filter = findViewById(R.id.btn_filter)
        buttonAdd = findViewById(R.id.add_btn)

        ShowDailyActivity()

        SearchData()

        btn_filter.setOnClickListener {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            val filterActivity = FilterActivity()
            fragmentTransaction.replace(R.id.fragment_container, filterActivity)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
            EnableDisableButton(false)
        }

        buttonAdd.setOnClickListener {
            val intent = Intent(this, SchedullingActivity::class.java)
            startActivity(intent)
        }

    }

    fun ShowDailyActivity(){
        for (data in DataDaily.dataKegiatan) {
            ShowHistory(data.namaKegiatan, data.kategori, data.status, data.tanggalMulai, data.tanggalSelesai)
        }
    }

    private fun ShowHistory(nama : String, kategori : String, status : String, mulai : String, selesai : String){
        val template: View = inflater.inflate(R.layout.template_oldactivity, null)
        container.addView(template)

        val textName : TextView = template.findViewById(R.id.text_name)
        val textTime : TextView = template.findViewById(R.id.text_tgl)
        val textStatus : TextView = template.findViewById(R.id.text_status)

        textName.text = nama

        val image_icon : ImageView = template.findViewById(R.id.image_icon)
        SetImageCategory(kategori, image_icon)

        if(status == "belum"){
            textTime.text = mulai + " - ..."
            textStatus.text = "OnProgres"
        }else if(status == "selesai"){
            textTime.text = mulai + " - " + selesai
            textStatus.text = "Complete"
        }else {
            textTime.text = mulai + " - " + selesai
            textStatus.text = "Dibatalkan"
        }
    }

    private fun SearchData(){

        searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val keyword = s.toString().trim()
                FilterDataByKeyword(keyword)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    fun FilterDataByKeyword(keyword: String) {
        val filteredData = DataDaily.dataKegiatan.filter { data ->
            data.namaKegiatan.contains(keyword, ignoreCase = true)
        }

        // Hapus tampilan yang sudah ada di container sebelum menambahkan yang baru
        container.removeAllViews()

        // Tampilkan data yang sesuai dengan kata kunci
        for (data in filteredData) {
            ShowHistory(data.namaKegiatan, data.kategori, data.status, data.tanggalMulai, data.tanggalSelesai)
        }
    }

    override fun onBackPressed() {
        // Aktifkan kembali elemen UI yang dinonaktifkan saat tombol di dalam Fragment ditekan
        EnableDisableButton(true)

        // Panggil metode onBackPressed dari superclass
        super.onBackPressed()
    }

    override fun OnFilter(status: String, senin: Boolean, selasa: Boolean, rabu: Boolean, kamis: Boolean, jumat: Boolean, sabtu: Boolean, minggu: Boolean
    ) {
        EnableDisableButton(true)
        ClearContainer()

        sn = senin
        sl = selasa
        rb = rabu
        km = kamis
        jm = jumat
        st = sabtu
        mg = minggu

        stat = status

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
        ShowFilter(filteredData)
    }

    override fun GetBack() {
        EnableDisableButton(true)
    }

    fun filterData(status: String, selectedDays: List<String>): List<DataDaily> {
        return DataDaily.dataKegiatan.filter { data ->
            // Cek status
            val statusMatch = when (status) {
                "semua" -> true // Tampilkan semua data tanpa filter
                "belum" -> data.status == "belum" // Tampilkan data dengan status "belum"
                "selesai" -> data.status == "selesai" // Tampilkan data dengan status "selesai"
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
            val template: View = inflater.inflate(R.layout.template_oldactivity, null)
            container.addView(template)

            val textName: TextView = template.findViewById(R.id.text_name)
            val textTime: TextView = template.findViewById(R.id.text_tgl)
            val textStatus: TextView = template.findViewById(R.id.text_status)

            val image_icon : ImageView = template.findViewById(R.id.image_icon)

            SetImageCategory(data.kategori, image_icon)

            textName.text = data.namaKegiatan
            // Tambahkan logika lain sesuai kebutuhan Anda untuk mengisi textTime dan textStatus

            if(data.status == "belum"){
                textTime.text = data.waktuMulai + " - ..."
                textStatus.text = "OnProgres"
            }else if(data.status == "selesai"){
                textTime.text = data.waktuMulai + " - " + data.waktuSelesai
                textStatus.text = "Complete"
            }else {
                textTime.text = data.waktuMulai + " - " + data.waktuSelesai
                textStatus.text = "Dibatalkan"
            }
        }
    }

    private fun ClearContainer() {
        container.removeAllViews()
    }

    private fun EnableDisableButton(kondisi : Boolean){
        btn_filter.isEnabled = kondisi
        searchBar.isEnabled = kondisi
    }

    private fun ResetDayFilter(){
        sn = false
        sl = false
        rb = false
        km = false
        jm = false
        st = false
        mg = false
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
        }else if (kategori == "akses handphone"){
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
}