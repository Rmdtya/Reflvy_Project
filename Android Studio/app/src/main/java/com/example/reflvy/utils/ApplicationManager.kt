package com.example.reflvy.utils

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import com.example.reflvy.data.EventScreening
import com.example.reflvy.data.Music
import com.example.reflvy.data.News
import com.example.reflvy.data.News.Companion.newsList
import com.example.reflvy.data.SaveDataScreening
import com.example.reflvy.data.Screening
import com.example.reflvy.data.YoutubeVideo
import com.google.common.reflect.TypeToken
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson

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
        Toast.makeText(this, "Help Me", Toast.LENGTH_SHORT).show()
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
                    val description = documentSnapshot.getString("deskripsi") ?: ""
                    val img = documentSnapshot.getString("image") ?: ""
                    val title = documentSnapshot.getString("judul") ?: ""
                    val paragraphs = documentSnapshot.get("paragraf") as? List<String> ?: emptyList()
                    val date = documentSnapshot.getString("tanggal") ?: ""


                    val news = News(index, description, img, title, paragraphs, date)
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
                val playlistPod = mutableListOf<com.example.reflvy.data.Music>()

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
                Log.d("MenuInfoActivity", "Error getting document: ", exception)
                Toast.makeText(this, exception.toString(), Toast.LENGTH_SHORT).show()
            }
    }


    fun ExitApplication(){
        newsList.clear()

        YoutubeVideo.videoList.clear()
    }

    fun LoadUserData() {

    }

    fun loadScreeningData(ind : Int) {
        val collectionNames = arrayOf("screening", "screening2", "screening3")

        val collectionSelect = collectionNames[ind]

        var index : Int
        index = 0;

        db.collection(collectionSelect)
            .get()
            .addOnSuccessListener { querySnapshot ->

                for (documentSnapshot in querySnapshot) {
                    val pertanyaan = index + 1
                    val soal = documentSnapshot.get("chatbot") as? List<String> ?: emptyList()
                    val jawaban = documentSnapshot.get("jawaban") as? List<String> ?: emptyList()
                    val nilai = documentSnapshot.get("nilai") as? List<Int> ?: emptyList()
                    val chatUser = documentSnapshot.get("chatuser") as? List<String> ?: emptyList()
                    val event = documentSnapshot.get("event") as? List<Int> ?: emptyList()

                    val data = Screening(pertanyaan, soal, jawaban, nilai, chatUser, event)
                    Screening.screenData.add(data)

                    index = index + 1
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, exception.toString(), Toast.LENGTH_SHORT).show()
            }
    }

    fun loadEventScreeningData(ind : Int) {
        val collectionNames = arrayOf("eventrespon", "screening2", "screening3")

        val collectionSelect = collectionNames[ind]

        var index : Int
        index = 0;

        db.collection(collectionSelect)
            .get()
            .addOnSuccessListener { querySnapshot ->

                for (documentSnapshot in querySnapshot) {
                    val pertanyaan = index + 1
                    val eventRespon1 = documentSnapshot.get("respon0") as? List<String> ?: emptyList()
                    val eventRespon2 = documentSnapshot.get("respon1") as? List<String> ?: emptyList()
                    val eventRespon3 = documentSnapshot.get("respon2") as? List<String> ?: emptyList()
                    val opsiRespon = documentSnapshot.get("jawaban") as? List<String> ?: emptyList()
                    val tampilanRespon = documentSnapshot.get("responbalik") as? List<String> ?: emptyList()

                    val data = EventScreening(pertanyaan, eventRespon1, eventRespon2, eventRespon3, opsiRespon, tampilanRespon)
                    EventScreening.eventScreenData.add(data)

                    index = index + 1
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, exception.toString(), Toast.LENGTH_SHORT).show()
            }

    }

//    fun LoadScreeningData(context: Context){
//
//        SharedPrefsManager.loadScreeningDataFromSharedPreferences(context)
//        SharedPrefsManager.loadEventScreeningDataFromSharedPreferences(context)
//    }
//
//    fun SaveDataScreening(context: Context){
//        SharedPrefsManager.clearScreeningDataFromSharedPreferences(context)
//
//        SharedPrefsManager.saveScreeningDataToSharedPreferences(context)
//        SharedPrefsManager.saveEventScreeningDataToSharedPreferences(context)
//    }

    object SharedPrefsManager {

        private const val PREFS_NAME = "newsFrefs"
        private const val SCREENING_KEY = "musicData"

        private fun getSharedPreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        }

        fun SaveScreenData(context: Context, dataList: List<Screening>) {
            val sharedPreferences = getSharedPreferences(context)
            val editor = sharedPreferences.edit()
            val gson = Gson()
            val dataListJson = gson.toJson(dataList)
            editor.putString(SCREENING_KEY, dataListJson)
            editor.apply()
        }

        fun LoadScreenData(context: Context): List<Screening> {
            val sharedPreferences = getSharedPreferences(context)
            val dataListJson = sharedPreferences.getString(SCREENING_KEY, null)
            val gson = Gson()
            val itemType = object : TypeToken<List<SaveDataScreening>>() {}.type
            return gson.fromJson(dataListJson, itemType) ?: emptyList()
        }

        fun RemoveDataByKey(context: Context) {
            SaveDataScreening.lastData.clear()
            val sharedPreferences = getSharedPreferences(context)
            val editor = sharedPreferences.edit()
            editor.remove(SCREENING_KEY)
            editor.apply()
        }
    }
}