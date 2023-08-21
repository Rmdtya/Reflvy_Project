package com.example.reflvy

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.example.reflvy.data.User
import com.example.reflvy.databinding.ActivitySigninBinding
import com.example.reflvy.utils.ApplicationManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson

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

                        val sharedPreferences = getSharedPreferences("login_status", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putBoolean("isLoggedIn", true)
                        editor.apply()

                        val intent = Intent(this, MenuActivity::class.java)
                        startActivity(intent)

                        UpdateInfoUser()

                        progressDialog.dismiss()
                        SetEmailUser()
                        finishAffinity()
                    }else{
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
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


                val userDocRef = db.collection("users").document(userID)
                userDocRef.get()
                    .addOnSuccessListener { documentSnapshot ->
                        if (documentSnapshot.exists()) {
                            val screening = documentSnapshot.get("screeningHistory") as? ArrayList<String>
                            val userName = documentSnapshot.getString("name") ?: ""
                            val gender = documentSnapshot.getString("gender") ?: ""
                            val tglLahir = documentSnapshot.getString("tanggalLahir") ?: ""
                            val telepon = documentSnapshot.getString("telepon") ?: ""

                            val gson = Gson()
                            val screeningJson = gson.toJson(screening)

                            val editor = sharedPreferences.edit()
                            editor.putString("userId", userID)
                            editor.putString("userEmail", email)
                            editor.putString("userName", userName)
                            editor.putString("userGender", gender)
                            editor.putString("tanggalLahir", tglLahir)
                            editor.putString("telepon", telepon)
                            editor.putString("userScreening", screeningJson) // Simpan JSON string
                            editor.apply()
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Gagal Menload", LENGTH_SHORT).show()
                    }


                CheckOrCreateUserDocument(userID, email)

                startActivity(Intent(this, MenuActivity::class.java))
                val sharedPreferencesLogin = getSharedPreferences("login_status", Context.MODE_PRIVATE)
                val editorLogin = sharedPreferencesLogin.edit()
                editorLogin.putBoolean("isLoggedIn", true)
                editorLogin.apply()

                UpdateInfoUser()

                finishAffinity()
            }
            .addOnFailureListener{
                error -> Toast.makeText(this, error.localizedMessage, LENGTH_SHORT).show()
            }
            .addOnCompleteListener {
                progressDialog.dismiss()
            }
    }

    fun CheckOrCreateUserDocument(userID: String, email: String) {
        val userDocument = db.collection("users").document(userID)

        userDocument.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document != null && document.exists()) {
                    // Dokumen dengan userID sebagai document ID sudah ada, lewati proses.
                    // Anda dapat menambahkan logika atau tindakan lain di sini jika diperlukan.
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
                        "linkHistory" to null // Field screeningHistory dengan tipe data array int yang masih null
                    )
                    userDocument.set(userData)
                        .addOnSuccessListener {
                            // Berhasil membuat dokumen baru.
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