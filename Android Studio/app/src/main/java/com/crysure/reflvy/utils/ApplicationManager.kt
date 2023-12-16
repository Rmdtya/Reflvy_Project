package com.crysure.reflvy.utils

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import com.crysure.reflvy.data.ActiveLogin
import com.crysure.reflvy.data.DataDaily
import com.crysure.reflvy.data.DataHistoryActivity
import com.crysure.reflvy.data.DataMisi
import com.crysure.reflvy.data.DataNotification
import com.crysure.reflvy.data.Music
import com.crysure.reflvy.data.News
import com.crysure.reflvy.data.News.Companion.newsList
import com.crysure.reflvy.data.NotifyChat
import com.crysure.reflvy.data.YoutubeVideo
import com.google.common.reflect.TypeToken
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ApplicationManager : Application() {
    private val db = Firebase.firestore

    companion object {
        val instance: ApplicationManager by lazy { ApplicationManager() }
    }

    private var textEmail: String = ""

    fun setEmail(email: String) {
        this.textEmail = email
    }

    fun cekFunction() {
        //Toast.makeText(this, "Help Me", Toast.LENGTH_SHORT).show()
    }

    fun getEmail(): String {
        return textEmail
    }

    override fun onCreate() {
        super.onCreate()
    }

//    fun LoadDataManager(){
//            loadNewsData()
//
//            val sharedPreferences = getSharedPreferences("data_news", Context.MODE_PRIVATE)
//            val editor = sharedPreferences.edit()
//            editor.putBoolean("isDataNewsLoad", true)
//            editor.apply()
//    }

    fun OnClearData(){

    }

    fun loadNewsData() {

        var index : Int
        index = 0;

        db.collection("news")
            .get()
            .addOnSuccessListener { querySnapshot ->

                for (documentSnapshot in querySnapshot) {
                    val jenis = documentSnapshot.get("jenis") as? List<Int> ?: emptyList()
                    val judul = documentSnapshot.get("judul") as? List<String> ?: emptyList()
                    val deskripsi = documentSnapshot.get("deskripsi") as? List<String> ?: emptyList()
                    val img = documentSnapshot.get("image") as? List<String> ?: emptyList()
                    val paragraphs = documentSnapshot.get("paragraf") as? List<String> ?: emptyList()
                    val date = documentSnapshot.get("tanggal") as? List<String> ?: emptyList()


                    val news = News(jenis, deskripsi, img, judul, paragraphs, date)
                    News.newsList.add(news)

                    index = index + 1
                }
            }
            .addOnFailureListener { exception ->
                Log.d("MenuInfoActivity", "Error getting documents: ", exception)
                Toast.makeText(this, exception.toString(), Toast.LENGTH_SHORT).show()
            }
    }

    fun loadMusic() {
        var index : Int = 0;

        db.collection("music")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val playlistPod = mutableListOf<com.crysure.reflvy.data.Music>()

                for (documentSnapshot in querySnapshot) {
                    val titleList = documentSnapshot.get("title") as? List<String> ?: emptyList()
                    val imgList = documentSnapshot.get("img") as? List<String> ?: emptyList()
                    val cover_img = documentSnapshot.get("cover_img") as? List<String> ?: emptyList()
                    val vcl = documentSnapshot.get("Vocalist") as? List<String> ?: emptyList()
                    val srcList = documentSnapshot.get("src") as? List<String> ?: emptyList()

//                    for ((index, title) in titleList.withIndex()) {
//                        musicID[index] = tempID
//
//                        tempID++
//                    }

                    val music = Music(index, titleList, vcl, imgList,cover_img, srcList)
                    Music.playList.add(music)
                    index = index + 1
                }
            }
            .addOnFailureListener { exception ->
                Log.d("MenuMusicActivity", "Error getting documents: ", exception)
                Toast.makeText(this, "Data tidak terload", Toast.LENGTH_SHORT).show()
            }
    }

    fun LoadVideoData() {

        db.collection("video")
            .document("youtubevideo")
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val url = documentSnapshot.get("url") as? List<String> ?: emptyList()
                val playlist = documentSnapshot.get("playlist") as? List<Int> ?: emptyList()

                for (index in url.indices) {
                    val link = url[index]
                    val setPly = playlist[index]
                    // Lakukan sesuatu dengan link dan setPly

                    val video = YoutubeVideo(index, link, setPly)
                    YoutubeVideo.videoList.add(video)
                }
            }
            .addOnFailureListener { exception ->
//                Log.d("MenuInfoActivity", "Error getting document: ", exception)
//                Toast.makeText(this, exception.toString(), Toast.LENGTH_SHORT).show()
            }
    }


    fun ExitApplication(){
        newsList.clear()

        YoutubeVideo.videoList.clear()
    }


    fun SaveNews(context: Context) {
        clearSharedPreferencesNews(context) // Hapus semua data sebelum menyimpan

        val sharedPreferences = context.getSharedPreferences("datanews_preferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val gson = Gson()
        val dataKegiatanJson = gson.toJson(DataDaily.dataKegiatan)
        editor.putString("data_news", dataKegiatanJson)

        editor.apply()
    }

    fun LoadNews(context: Context) {
        val sharedPreferences = context.getSharedPreferences("datanews_preferences", Context.MODE_PRIVATE)
        val gson = Gson()
        val dataKegiatanJson = sharedPreferences.getString("data_news", null)

        if (dataKegiatanJson != null) {
            val type = object : TypeToken<List<News>>() {}.type
            val dataNewsList: List<News> = gson.fromJson(dataKegiatanJson, type)
            News.newsList.clear()
            News.newsList.addAll(dataNewsList)
        }
    }

    fun clearSharedPreferencesNews(context: Context) {
        val sharedPreferences = context.getSharedPreferences("datanews_preferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    fun SaveKegiatan(context: Context) {
        clearSharedPreferences(context) // Hapus semua data sebelum menyimpan

        val sharedPreferences = context.getSharedPreferences("datakegiatan_preferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val gson = Gson()
        val dataKegiatanJson = gson.toJson(DataDaily.dataKegiatan)
        editor.putString("data_kegiatan", dataKegiatanJson)

        editor.apply()
    }

    fun LoadKegiatan(context: Context) {
        val sharedPreferences = context.getSharedPreferences("datakegiatan_preferences", Context.MODE_PRIVATE)
        val gson = Gson()
        val dataKegiatanJson = sharedPreferences.getString("data_kegiatan", null)

        if (dataKegiatanJson != null) {
            val type = object : TypeToken<List<DataDaily>>() {}.type
            val dataKegiatanList: List<DataDaily> = gson.fromJson(dataKegiatanJson, type)
            DataDaily.dataKegiatan.clear()
            DataDaily.dataKegiatan.addAll(dataKegiatanList)
        }
    }

    fun clearSharedPreferences(context: Context) {
        val sharedPreferences = context.getSharedPreferences("datakegiatan_preferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }


    fun SaveDataLogin(context: Context) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("LoginInfo", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        editor.putInt("totalActive", ActiveLogin.infoActive.totalActive)
        editor.putBoolean("activeNow", ActiveLogin.infoActive.activeNow)
        editor.putString("lastActive", ActiveLogin.infoActive.lastActive)
        editor.putString("moodNow", ActiveLogin.infoActive.moodNow)
        editor.putBoolean("notifTerlewat", ActiveLogin.infoActive.notifTerlewat)

        editor.apply()
    }

    fun LoadDataLogin(context: Context) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("LoginInfo", Context.MODE_PRIVATE)

        val totalActive = sharedPreferences.getInt("totalActive", 0)
        val activeNow = sharedPreferences.getBoolean("activeNow", false)
        val lastActive = sharedPreferences.getString("lastActive", "")
        val moodNow = sharedPreferences.getString("moodNow", "")
        val notifTerlewat = sharedPreferences.getBoolean("notifTerlewat", false)

        ActiveLogin.infoActive.totalActive = totalActive
        ActiveLogin.infoActive.activeNow = activeNow
        ActiveLogin.infoActive.lastActive = lastActive.toString()
        ActiveLogin.infoActive.moodNow = moodNow.toString()
        ActiveLogin.infoActive.notifTerlewat = notifTerlewat
    }

    fun SaveDataHistoryActivity(context: Context){
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("LoginInfo", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        editor.putInt("totalSpendTime", ActiveLogin.infoActive.totalSpendTime)
        editor.putInt("kegiatan1", ActiveLogin.infoActive.kegiatan1Bekerja)
        editor.putInt("kegiatan2", ActiveLogin.infoActive.kegiatan2BelajarFormal)
        editor.putInt("kegiatan3", ActiveLogin.infoActive.kegiatan3Membaca)
        editor.putInt("kegiatan4", ActiveLogin.infoActive.kegiatan4Bersantai)
        editor.putInt("kegiatan5", ActiveLogin.infoActive.kegiatan5Istirahat)
        editor.putInt("kegiatan6", ActiveLogin.infoActive.kegiatan6Belanja)
        editor.putInt("kegiatan7", ActiveLogin.infoActive.kegiatan7Bermusik)
        editor.putInt("kegiatan8", ActiveLogin.infoActive.kegiatan8Beribadah)
        editor.putInt("kegiatan9", ActiveLogin.infoActive.kegiatan9BermainGame)
        editor.putInt("kegiatan10", ActiveLogin.infoActive.kegiatan10HiburanDigital)
        editor.putInt("kegiatan11", ActiveLogin.infoActive.kegiatan11OperasiKomputer)
        editor.putInt("kegiatan12", ActiveLogin.infoActive.kegiatan12PekerjaanRumah)
        editor.putInt("kegiatan13", ActiveLogin.infoActive.kegiatan13Komunitas)
        editor.putInt("kegiatan14", ActiveLogin.infoActive.kegiatan14Bersosialisasi)
        editor.putInt("kegiatan15", ActiveLogin.infoActive.kegiatan15Healing)
        editor.putInt("kegiatan16", ActiveLogin.infoActive.kegiatan16Olahraga)
        editor.putInt("kegiatan17", ActiveLogin.infoActive.kegiatan17Liburan)
        editor.putInt("kegiatan18", ActiveLogin.infoActive.kegiatan18Lainnya)

        editor.apply()
    }

    fun LoadDataHistoryActivity(context: Context) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("LoginInfo", Context.MODE_PRIVATE)

        val totalSpendTime = sharedPreferences.getInt("totalSpendTime", 0)
        val k1 = sharedPreferences.getInt("kegiatan1", 0)
        val k2 = sharedPreferences.getInt("kegiatan2", 0)
        val k3 = sharedPreferences.getInt("kegiatan3", 0)
        val k4 = sharedPreferences.getInt("kegiatan4", 0)
        val k5 = sharedPreferences.getInt("kegiatan5", 0)
        val k6 = sharedPreferences.getInt("kegiatan6", 0)
        val k7 = sharedPreferences.getInt("kegiatan7", 0)
        val k8 = sharedPreferences.getInt("kegiatan8", 0)
        val k9 = sharedPreferences.getInt("kegiatan9", 0)
        val k10 = sharedPreferences.getInt("kegiatan10", 0)
        val k11 = sharedPreferences.getInt("kegiatan11", 0)
        val k12 = sharedPreferences.getInt("kegiatan12", 0)
        val k13 = sharedPreferences.getInt("kegiatan13", 0)
        val k14 = sharedPreferences.getInt("kegiatan14", 0)
        val k15 = sharedPreferences.getInt("kegiatan15", 0)
        val k16 = sharedPreferences.getInt("kegiatan16", 0)
        val k17 = sharedPreferences.getInt("kegiatan17", 0)
        val k18 = sharedPreferences.getInt("kegiatan18", 0)

        ActiveLogin.infoActive.totalSpendTime = totalSpendTime
        ActiveLogin.infoActive.kegiatan1Bekerja = k1
        ActiveLogin.infoActive.kegiatan2BelajarFormal = k2
        ActiveLogin.infoActive.kegiatan3Membaca = k3
        ActiveLogin.infoActive.kegiatan4Bersantai = k4
        ActiveLogin.infoActive.kegiatan5Istirahat = k5
        ActiveLogin.infoActive.kegiatan6Belanja = k6
        ActiveLogin.infoActive.kegiatan7Bermusik = k7
        ActiveLogin.infoActive.kegiatan8Beribadah = k8
        ActiveLogin.infoActive.kegiatan9BermainGame = k9
        ActiveLogin.infoActive.kegiatan10HiburanDigital = k10
        ActiveLogin.infoActive.kegiatan11OperasiKomputer = k11
        ActiveLogin.infoActive.kegiatan12PekerjaanRumah = k12
        ActiveLogin.infoActive.kegiatan13Komunitas = k13
        ActiveLogin.infoActive.kegiatan14Bersosialisasi = k14
        ActiveLogin.infoActive.kegiatan15Healing = k15
        ActiveLogin.infoActive.kegiatan16Olahraga = k16
        ActiveLogin.infoActive.kegiatan17Liburan = k17
        ActiveLogin.infoActive.kegiatan18Lainnya = k18
    }

    fun ResetHistoryActivity(context: Context){

        val data = ActiveLogin.infoActive

        val dataSebelumnya = DataHistoryActivity(data.lastActive, data.moodNow, data.totalSpendTime, data.kegiatan1Bekerja, data.kegiatan2BelajarFormal,
            data.kegiatan3Membaca, data.kegiatan4Bersantai, data.kegiatan5Istirahat, data.kegiatan6Belanja, data.kegiatan7Bermusik,
            data.kegiatan8Beribadah, data.kegiatan9BermainGame, data.kegiatan10HiburanDigital, data.kegiatan11OperasiKomputer,
            data.kegiatan12PekerjaanRumah, data.kegiatan13Komunitas, data.kegiatan14Bersosialisasi, data.kegiatan15Healing,
            data.kegiatan16Olahraga, data.kegiatan17Liburan, data.kegiatan18Lainnya)

        DataHistoryActivity.dataHistoryActivity.add(dataSebelumnya)

        SaveTotalHistoryActivity(context)

        var resetData = ActiveLogin(data.totalActive, false, data.lastActive, "", 0, false,0, 0, 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0, 0)

        ActiveLogin.infoActive = resetData
    }

    fun SaveTotalHistoryActivity(context: Context) {

        clearTotalHistorySharedPreferences(context) // Hapus semua data sebelum menyimpan

        val sharedPreferences = context.getSharedPreferences("datahistoryactivity_preferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val gson = Gson()
        val dataHistoryJson = gson.toJson(DataHistoryActivity.dataHistoryActivity)
        editor.putString("data_history", dataHistoryJson)

        editor.apply()
    }

    fun LoadTotalHistoryActivity(context: Context) {
        val sharedPreferences = context.getSharedPreferences("datahistoryactivity_preferences", Context.MODE_PRIVATE)
        val gson = Gson()
        val dataHistoryJson = sharedPreferences.getString("data_history", null)

        if (dataHistoryJson != null) {
            val type = object : TypeToken<List<DataHistoryActivity>>() {}.type
            val dataHistoryList: List<DataHistoryActivity> = gson.fromJson(dataHistoryJson, type)
            DataHistoryActivity.dataHistoryActivity.clear()
            DataHistoryActivity.dataHistoryActivity.addAll(dataHistoryList)
        }
    }

    fun clearTotalHistorySharedPreferences(context: Context) {
        val sharedPreferences = context.getSharedPreferences("datahistoryactivity_preferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }



    fun UpdateStatusIfCompleted() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -1) // Mengurangkan satu hari dari tanggal saat ini
        val kemarin = calendar.time

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val tglKemarin = dateFormat.format(kemarin).trim()

        for (data in DataDaily.dataKegiatan) {
            if(data.status == "belum"){
                if (data.progresNow >= data.lamaKegiatan) {
                    data.status = "selesai"
                    data.tanggalSelesai = tglKemarin
                }
            }
        }
    }


    fun SaveNotifikasiKegiatan(context: Context) {
        clearSharedPreferencesNotikasiKegiatan(context) // Hapus semua data sebelum menyimpan

        val sharedPreferences = context.getSharedPreferences("notifikasi_preferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val gson = Gson()
        val dataKegiatanJson = gson.toJson(DataNotification.dataNotifikasi)
        editor.putString("data_notifikasi", dataKegiatanJson)

        editor.apply()
    }

    fun LoadNotifikasiKegiatan(context: Context) {
        val sharedPreferences = context.getSharedPreferences("notifikasi_preferences", Context.MODE_PRIVATE)
        val gson = Gson()
        val dataKegiatanJson = sharedPreferences.getString("data_notifikasi", null)

        if (dataKegiatanJson != null) {
            val type = object : TypeToken<List<DataNotification>>() {}.type
            val dataKegiatanList: List<DataNotification> = gson.fromJson(dataKegiatanJson, type)
            DataNotification.dataNotifikasi.clear()
            DataNotification.dataNotifikasi.addAll(dataKegiatanList)
        }
    }

    fun clearSharedPreferencesNotikasiKegiatan(context: Context) {
        val sharedPreferences = context.getSharedPreferences("notifikasi_preferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    fun ActiveNotif(context: Context){
        val sharedPreferences = context.getSharedPreferences("notif_prefs", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean("activeNotif", true)
        NotifyChat.notify = true

        editor.apply()
    }

    fun NonActiveNotif(context: Context){
        val sharedPreferences = context.getSharedPreferences("notif_prefs", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean("activeNotif", false)
        NotifyChat.notify = false

        editor.apply()
    }

    fun LoadNotifChat(context: Context) {
        val sharedPreferences = context.getSharedPreferences("notif_prefs", Context.MODE_PRIVATE)
        val activeNotif = sharedPreferences.getBoolean("activeNotif", false)

        NotifyChat.notify = activeNotif
    }

    fun ClearNotifChat(context: Context){
        val sharedPreferences = context.getSharedPreferences("notif_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    fun SaveNotifPrefs(context: Context) {
        clearSharedPreferencesNotif(context) // Hapus semua data sebelum menyimpan

        val sharedPreferences = context.getSharedPreferences("DataNotif_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val gson = Gson()
        val dataKegiatanJson = gson.toJson(NotifyChat.notifChat)
        editor.putString("data_notif", dataKegiatanJson)

        editor.apply()
    }

    fun LoadNotifPrefs(context: Context) {
        val sharedPreferences = context.getSharedPreferences("DataNotif_prefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val dataNotif = sharedPreferences.getString("data_notif", null)

        if (dataNotif != null) {
            val type = object : TypeToken<List<NotifyChat>>() {}.type
            val dataNotifyList: List<NotifyChat> = gson.fromJson(dataNotif, type)
            NotifyChat.notifChat.clear()
            NotifyChat.notifChat.addAll(dataNotifyList)
        }
    }

    fun clearSharedPreferencesNotif(context: Context) {
        val sharedPreferences = context.getSharedPreferences("DataNotif_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    //---------------------------------------------------------Misi Aplikasi----------------------------------------------------------------------------
    fun AddFirstMissionApplication(context: Context){
        val misi1 = DataMisi(
            0,
            "Ekspedisi Pertama",
            "Selesaikan semua misinya dan rasakan manfaatnya",
            1,
            listOf("Selesaikan Screening Pertama", "Lakukan Screening kedua untuk mengetahui tingkat kecanduanmu", "Buat Jadwal Keseharianmnu", "Selesaikan 20 Kegiatan Harian", "Login selama 5 hari"),
            listOf("Lakukan Screening Pertama Saat User Pertama kali menggunakan aplikasi", "Screening 2 dilakukan untuk mengetahui tingkat kecanduan seseorang terhadap pornografi", "Atur dan tambahkan jadwal keseharianmu untuk harimu yang lebih baik", "Selesaikan semua kegiatanmu untuk masa depan yang lebih cerah", "Cek mood mu hari ini dalam dailycheck - in"),
            listOf(0, 0, 0, 0, 0),
            listOf(1, 1, 5, 10, 3),
            listOf(false, false, false, false, false),
            listOf("", "Screening2", "Schedulling", "Schedulling", "")
        )
        DataMisi.dataMisiAplikasi.add(misi1)

        val misi2 = DataMisi(
        1,
        "Ekspedisi Kedua",
        "Selesaikan semua misinya dan rasakan manfaatnya",
        3,
        listOf("Selesaikan Screening Pertama", "Lakukan Screening kedua untuk mengetahui tingkat kecanduanmu", "Buat Jadwal Keseharianmnu", "Selesaikan 20 Kegiatan Harian", "Login selama 5 hari"),
        listOf("Lakukan Screening Pertama Saat User Pertama kali menggunakan aplikasi", "Screening 2 dilakukan untuk mengetahui tingkat kecanduan seseorang terhadap pornografi", "Atur dan tambahkan jadwal keseharianmu untuk harimu yang lebih baik", "Selesaikan semua kegiatanmu untuk masa depan yang lebih cerah", "Cek mood mu hari ini dalam dailycheck - in"),
        listOf(0, 0, 0, 0, 0),
        listOf(1, 1, 5, 10, 3),
        listOf(false, false, false, false, false),
        listOf("", "Screening2", "Schedulling", "Schedulling", "")
        )
        DataMisi.dataMisiAplikasi.add(misi2)

        SaveDataMisiAplkasi(context)
    }
    fun SaveDataMisiAplkasi(context: Context) {
        clearSharedPreferencesMisiAplikasi(context) // Hapus semua data sebelum menyimpan

        val sharedPreferences = context.getSharedPreferences("datamisiaplikasi_preferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val gson = Gson()
        val dataKegiatanJson = gson.toJson(DataMisi.dataMisiAplikasi)
        editor.putString("data_misiaplikasi", dataKegiatanJson)

        editor.apply()
    }

    fun LoadDataMisiAplikasi(context: Context) {
        val sharedPreferences = context.getSharedPreferences("datamisiaplikasi_preferences", Context.MODE_PRIVATE)
        val gson = Gson()
        val dataMisiJson = sharedPreferences.getString("data_misiaplikasi", null)

        if (dataMisiJson != null) {
            val type = object : TypeToken<List<DataMisi>>() {}.type
            val dataMisiList: List<DataMisi> = gson.fromJson(dataMisiJson, type)
            DataMisi.dataMisiAplikasi.clear()
            DataMisi.dataMisiAplikasi.addAll(dataMisiList)
        }
    }

    fun clearSharedPreferencesMisiAplikasi(context: Context) {
        val sharedPreferences = context.getSharedPreferences("datamisiaplikasi_preferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}