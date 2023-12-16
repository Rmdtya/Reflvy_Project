package com.crysure.reflvy

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import com.crysure.reflvy.data.DataMisi
import com.crysure.reflvy.data.User
import com.crysure.reflvy.databinding.ActivitySigninBinding
import com.crysure.reflvy.utils.ApplicationManager
import com.crysure.reflvy.utils.GameEventManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlin.random.Random

class SigninActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySigninBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private val db = Firebase.firestore

    private lateinit var googleSigninClient : GoogleSignInClient

    private lateinit var progressDialog : ProgressDialog

    lateinit var applicationManager : ApplicationManager
    private lateinit var sharedPreferences: SharedPreferences

    companion object{
        private const val requestCode_Signin = 1000;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Sign In")
        progressDialog.setMessage("Loading, Silangkan Menunggu")

        sharedPreferences = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)

        binding.register.setOnClickListener{
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        binding.button.setOnClickListener{
            val email = binding.emailInp.text.toString()
            val password = binding.passwordInp.text.toString()

            if(email.isNotEmpty() && password.isNotEmpty()){
                progressDialog.show()
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if(it.isSuccessful){

                        val currentUser = firebaseAuth.currentUser
                        val userID = currentUser?.uid ?: ""
                        val email = currentUser?.email ?: "" // Dapatkan email dari hasil sign in

                        ApplicationManager.instance.AddFirstMissionApplication(this)
                        ApplicationManager.instance.SaveDataMisiAplkasi(this)
                        ApplicationManager.instance.LoadDataMisiAplikasi(this)

                        GameEventManager.instance.SaveEventStatus(this)

                        CheckOrCreateUserDocument(userID, email)

                    }else{
                        //Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(this, "Field Harus Terisi", Toast.LENGTH_SHORT).show()
            }
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSigninClient = GoogleSignIn.getClient(this, gso)

        binding.btngoogle.setOnClickListener{
            val signInIntent = googleSigninClient.signInIntent
            startActivityForResult(signInIntent, requestCode_Signin)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == requestCode_Signin){
            //Menangani Proses Login Google
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                //Jika Berhasil
                val account = task.getResult(ApiException::class.java)!!
                FirebaseAuthWithGoogle(account.idToken!!)
            }catch (e:ApiException){
                e.printStackTrace()
                Toast.makeText(applicationContext, e.localizedMessage, LENGTH_SHORT).show()
            }
        }
    }

    fun FirebaseAuthWithGoogle(idtoken: String) {
        progressDialog.show()
        val credential = GoogleAuthProvider.getCredential(idtoken, null)

        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener {

                val currentUser = firebaseAuth.currentUser
                val userID = currentUser?.uid ?: ""
                val email = currentUser?.email ?: "" // Dapatkan email dari hasil sign in

                ApplicationManager.instance.AddFirstMissionApplication(this)
                ApplicationManager.instance.SaveDataMisiAplkasi(this)
                ApplicationManager.instance.LoadDataMisiAplikasi(this)

                GameEventManager.instance.SaveEventStatus(this)

                CheckOrCreateUserDocument(userID, email)
            }
            .addOnFailureListener{
                error -> Toast.makeText(this, error.localizedMessage, LENGTH_SHORT).show()
            }
            .addOnCompleteListener {
                progressDialog.dismiss()
            }
    }

    fun LoadDataFromFirestore(id : String, email : String){
        val userDocRef = db.collection("users").document(id)
        userDocRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val screening = documentSnapshot.get("screeningHistory") as? ArrayList<String>
                    val userName = documentSnapshot.getString("name") ?: ""
                    val gender = documentSnapshot.getString("gender") ?: ""
                    val tglLahir = documentSnapshot.getString("tanggalLahir") ?: ""
                    val telepon = documentSnapshot.getString("telepon") ?: ""
                    val screening1 = documentSnapshot.getBoolean("screeningSatu") ?: false
                    val screening2 = documentSnapshot.getBoolean("screeningDua") ?: false
                    val screening3 = documentSnapshot.getBoolean("screeningTiga") ?: false
                    val screening4 = documentSnapshot.getBoolean("screeningEmpat") ?: false
                    val nilaiScreening1 = documentSnapshot.getLong("nilaiScreening1")?.toInt() ?: 0
                    val nilaiScreening2 = documentSnapshot.getLong("nilaiScreening2")?.toInt() ?: 0
                    val nilaiScreening3 = documentSnapshot.getLong("nilaiScreening3")?.toInt() ?: 0
                    val nilaiScreening4 = documentSnapshot.getLong("nilaiScreening4")?.toInt() ?: 0
                    val reveralCode = documentSnapshot.getString("reveralKode") ?: ""

                    val gson = Gson()
                    val screeningJson = gson.toJson(screening)

                    val editor = sharedPreferences.edit()
                    editor.putString("userId", id)
                    editor.putString("userEmail", email)
                    editor.putString("userName", userName)
                    editor.putString("userGender", gender)
                    editor.putString("tanggalLahir", tglLahir)
                    editor.putString("telepon", telepon)
                    editor.putString("userScreening", screeningJson) // Simpan JSON string
                    editor.putBoolean("screening1", screening1)
                    editor.putBoolean("screening2", screening2)
                    editor.putBoolean("screening3", screening3)
                    editor.putBoolean("screening4", screening4)
                    editor.putInt("nilaiScreening1", nilaiScreening1)
                    editor.putInt("nilaiScreening2", nilaiScreening2)
                    editor.putInt("nilaiScreening3", nilaiScreening3)
                    editor.putInt("nilaiScreening4", nilaiScreening4)
                    editor.putString("userReveral", reveralCode)
                    editor.apply()

                    val sharedPreferencesUser = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
                    User.userData.loadFromSharedPreferences(sharedPreferencesUser)

                    if(User.userData.screeningSatu){
                        startActivity(Intent(this, MenuActivity::class.java))
                        val sharedPreferencesLogin = getSharedPreferences("login_status", Context.MODE_PRIVATE)
                        val editorLogin = sharedPreferencesLogin.edit()
                        editorLogin.putBoolean("isLoggedIn", true)
                        editorLogin.apply()

                        DataMisi.dataMisiAplikasi[0].statusMisi = DataMisi.dataMisiAplikasi[0].statusMisi.toMutableList().apply {
                            set(0, true) // Mengganti nilai pada indeks 0 dengan `true`
                        }

                        DataMisi.dataMisiAplikasi[0].progresNow = DataMisi.dataMisiAplikasi[0].progresNow.toMutableList().apply {
                            set(0, 1) // Mengganti nilai pada indeks 0 dengan `true`
                        }

                        ApplicationManager.instance.SaveDataMisiAplkasi(this)

                        UpdateInfoUser()

                        finishAffinity()
                    }else{
                        startActivity(Intent(this, ScreeningSatuActivity::class.java))
                        val sharedPreferencesLogin = getSharedPreferences("login_status", Context.MODE_PRIVATE)
                        val editorLogin = sharedPreferencesLogin.edit()
                        editorLogin.putBoolean("isLoggedIn", true)
                        editorLogin.apply()

                        UpdateInfoUser()

                        finishAffinity()
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal Menload", LENGTH_SHORT).show()
            }
    }

    fun generateRandomCode(): String {
        val characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        val codeLength = 6 // 3 huruf + 3 angka

        val random = Random.Default
        return (1..codeLength)
            .map { characters[random.nextInt(0, characters.length)] }
            .joinToString("")
    }

    fun CreateReveralCode(userID: String, email: String) {
        val code = generateRandomCode()

        try {
            val userDocument = db.collection("ReveralCode").document(code)

            userDocument.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document != null && document.exists()) {
                        CreateReveralCode(userID, email)
                    } else {
                        // Dokumen dengan userID sebagai document ID belum ada, buat dokumen baru.
                        val userData = hashMapOf(
                            "userId" to userID
                        )
                        userDocument.set(userData)
                            .addOnSuccessListener {

                                val documentReference = db.collection("users").document(userID)

                                val updateData = hashMapOf(
                                    "reveralKode" to code
                                )

                                documentReference.set(updateData, SetOptions.merge())
                                    .addOnSuccessListener {

                                    }
                                    .addOnFailureListener {
                                        // Gagal mengganti data
                                    }

                                LoadDataFromFirestore(userID, email)
                            }
                            .addOnFailureListener { error ->
                                // Gagal membuat dokumen baru, tangani error jika diperlukan.
                                Toast.makeText(this, error.localizedMessage, Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    // Gagal mendapatkan dokumen, tangani error jika diperlukan.
                    Toast.makeText(this, task.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }catch (e:Exception){
            Toast.makeText(this, e.toString(), LENGTH_SHORT).show()
        }
    }

    fun CheckOrCreateUserDocument(userID: String, email: String) {

        try {
            val userDocument = db.collection("users").document(userID)

            userDocument.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document != null && document.exists()) {
                        // Dokumen dengan userID sebagai document ID sudah ada, lewati proses.
                        // Anda dapat menambahkan logika atau tindakan lain di sini jika diperlukan.
                        LoadDataFromFirestore(userID, email)
                    } else {
                        // Dokumen dengan userID sebagai document ID belum ada, buat dokumen baru.
                        val userData = hashMapOf(
                            "name" to email,
                            "email" to email,
                            "gender" to null, // Field gender dengan tipe data string yang masih null
                            "telepon" to null, // Field telepon dengan tipe data string yang masih null
                            "tanggalLahir" to null, // Field telepon dengan tipe data string yang masih null
                            "historyvpn" to null, // Field historyvpn dengan tipe data array string yang masih null
                            "activityHistory" to null, // Field activityHistory dengan tipe data array string yang masih null
                            "deteksijarak" to null, // Field deteksijarak dengan tipe data array string yang masih null
                            "screeningHistory" to null, // Field screeningHistory dengan tipe data array int yang masih null
                            "dailyPoint" to null, // Field screeningHistory dengan tipe data array int yang masih null
                            "linkHistory" to null, // Field screeningHistory dengan tipe data array int yang masih null
                            "screeningSatu" to false,
                            "screeningDua" to false,
                            "screeningTiga" to false,
                            "screeningEmpat" to false,
                            "nilaiScreening1" to 0,
                            "nilaiScreening2" to 0,
                            "nilaiScreening3" to 0,
                            "nilaiScreening4" to 0,
                            "reveralKode" to null,
                            "activePhone" to null,
                            "historyAplikasi" to null,
                            "durasiAplikasi" to null,
                            "aplikasiAktif" to null,
                            "moodHariIni" to null,
                            "terakhirAktif" to null
                        )
                        userDocument.set(userData)
                            .addOnSuccessListener {
                                // Berhasil membuat dokumen baru.
                                CreateReveralCode(userID, email)
                            }
                            .addOnFailureListener { error ->
                                // Gagal membuat dokumen baru, tangani error jika diperlukan.
                                Toast.makeText(this, error.localizedMessage, Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    // Gagal mendapatkan dokumen, tangani error jika diperlukan.
                    Toast.makeText(this, task.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }catch (e:Exception){
            Toast.makeText(this, e.toString(), LENGTH_SHORT).show()
        }

    }

    fun SetEmailUser(){
        val currentUser = FirebaseAuth.getInstance().currentUser
        val email = currentUser?.email

        val userEmail : String = email.toString()

        ApplicationManager.instance.setEmail(userEmail)
    }

    private fun UpdateInfoUser(){
        var sharedPreferences: SharedPreferences
        sharedPreferences = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
        User.userData.loadFromSharedPreferences(sharedPreferences)
    }

}