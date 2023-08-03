package com.example.reflvy

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import com.example.reflvy.data.Music
import com.example.reflvy.data.News
import com.example.reflvy.data.News.Companion.newsList
import com.example.reflvy.data.Screening
import com.example.reflvy.data.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

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

//        if (Music.isMusicAvailable(0)) {
//            // Music sudah tersedia, lakukan tindakan yang sesuai
//            // Misalnya, tidak perlu menambahkan data Music dengan playlistIDToCheck ke playList lagi.
//            Toast.makeText(this, "Sudah Tersedia", Toast.LENGTH_SHORT).show()
//        } else {
//            Toast.makeText(this, "Belum Tesedia", Toast.LENGTH_SHORT).show()
//        }

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

    fun ExitApplication(){
        newsList.clear()
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
                    val eventRespon = documentSnapshot.get("eventrespon") as? List<String> ?: emptyList()

                    val data = Screening(pertanyaan, soal, jawaban, nilai, chatUser, event , eventRespon )
                    Screening.screenData.add(data)

                    index = index + 1
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, exception.toString(), Toast.LENGTH_SHORT).show()
            }
    }

}