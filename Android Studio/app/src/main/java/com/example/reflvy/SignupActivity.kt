package com.example.reflvy

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.reflvy.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignupActivity : AppCompatActivity() {

    private lateinit var binding:ActivitySignupBinding
    private lateinit var firebaseAuth:FirebaseAuth
    private val db = Firebase.firestore

    private var isVerificationLayoutVisible = false

    private lateinit var email:String
    private lateinit var password:String
    private lateinit var confirmPass:String
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var otpCode:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        sharedPreferences = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)

        binding.haveAccount.setOnClickListener {
            val intent = Intent(this, SigninActivity::class.java)
            startActivity(intent)
        }

        binding.button.setOnClickListener {
            email = binding.emailInp.text.toString()
            password = binding.passwordInp.text.toString()
            confirmPass = binding.confirmInp.text.toString()

            fun isPasswordValid(password: String): Boolean {
                val pattern = "^(?=.*[A-Z]).{8,}$".toRegex()
                return pattern.matches(password)
            }

            firebaseAuth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val signInMethods = task.result?.signInMethods
                        if (signInMethods != null && signInMethods.isNotEmpty()) {
                            Toast.makeText(this, "Email Sudah Terdaftar", Toast.LENGTH_SHORT).show()
                        } else {

                            if (email.isNotEmpty() && password.isNotEmpty() && confirmPass.isNotEmpty()) {
                                if (password == confirmPass) {
                                    if (isPasswordValid(password)) {
                                        showVerificationLayout()

                                        binding.emailInp.visibility = View.GONE
                                        binding.emailInp.visibility = View.GONE
                                        binding.confirmInp.visibility = View.GONE
                                        binding.button.visibility = View.GONE


                                        AddOTP()
                                    } else {
                                        Toast.makeText(
                                            this,
                                            "Password memuat minimal satu kapital dan angka serta 8 karakter",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } else {
                                    Toast.makeText(
                                        this,
                                        "Password Tidak Sesuai",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                Toast.makeText(this, "Field Harus Terisi", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }

                    } else {
                        val exception = task.exception
                        if (exception is FirebaseAuthInvalidUserException) {
                            if (email.isNotEmpty() && password.isNotEmpty() && confirmPass.isNotEmpty()) {
                                if (password == confirmPass) {
                                    if (isPasswordValid(password)) {
                                        showVerificationLayout()

                                        binding.emailInp.visibility = View.GONE
                                        binding.emailInp.visibility = View.GONE
                                        binding.confirmInp.visibility = View.GONE
                                        binding.button.visibility = View.GONE


                                        AddOTP()
                                    } else {
                                        Toast.makeText(
                                            this,
                                            "Password memuat minimal satu kapital dan angka serta 8 karakter",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } else {
                                    Toast.makeText(
                                        this,
                                        "Password Tidak Sesuai",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                Toast.makeText(this, "Field Harus Terisi", Toast.LENGTH_SHORT)
                                    .show()
                            }

                        } else {
                            Toast.makeText(this, "Terjadi Kesalahan", Toast.LENGTH_SHORT)
                        }
                    }
                }
        }
    }

    fun CreateAccount(){

        val name : String

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if(it.isSuccessful){
                val currentUser = firebaseAuth.currentUser
                val userID = currentUser?.uid ?: ""

                val editor = sharedPreferences.edit()
                editor.putString("userId", userID)
                editor.putString("userEmail", email)
                editor.apply()

                CheckOrCreateUserDocument(userID, email)

                val intent = Intent(this, SigninActivity::class.java)
                startActivity(intent)
            }else{
                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
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
                        "name" to email
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

    private fun showVerificationLayout() {
        if (!isVerificationLayoutVisible) {
            // Menampilkan layout verifikasi

            // Inisialisasi layout verifikasi
            val verificationLayout = layoutInflater.inflate(R.layout.verification_signup, binding.root, true)
            isVerificationLayoutVisible = true

            val value1 : EditText = findViewById(R.id.verif_box1)
            val value2 : EditText = findViewById(R.id.verif_box2)
            val value3 : EditText = findViewById(R.id.verif_box3)
            val value4 : EditText = findViewById(R.id.verif_box4)

            value1.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    // Implementasikan logika setelah pengguna memasukkan teks pada editText1
                    if (s?.length == 1) {
                        value2.requestFocus()
                    }
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    // Kosongkan implementasi
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // Kosongkan implementasi
                }
            })

            value2.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    // Implementasikan logika setelah pengguna memasukkan teks pada editText2
                    if (s?.length == 1) {
                        value3.requestFocus()
                    }
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    // Kosongkan implementasi
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // Kosongkan implementasi
                }
            })

            value3.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    // Implementasikan logika setelah pengguna memasukkan teks pada editText2
                    if (s?.length == 1) {
                        value4.requestFocus()
                    }
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    // Kosongkan implementasi
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // Kosongkan implementasi
                }
            })

            value4.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    // Implementasikan logika setelah pengguna memasukkan teks pada editText2
                    if (s?.length == 1) {
                        value4.clearFocus() // Menghilangkan fokus dari EditText
                        hideKeyboard() // Menghilangkan keyboard
                    }
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    // Kosongkan implementasi
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // Kosongkan implementasi
                }
            })

            val btnVerif: Button = findViewById(R.id.verif_email)
            val btnResend: TextView = findViewById(R.id.send_ulang)

            btnVerif.setOnClickListener {
                val enteredOtp =
                    value1.text.toString() +
                            value2.text.toString() +
                            value3.text.toString() +
                            value4.text.toString()

                if (TextUtils.isEmpty(enteredOtp)) {
                    Toast.makeText(this, "Masukkan kode OTP", Toast.LENGTH_SHORT).show()
                } else if (enteredOtp == otpCode) {
                    // Kode OTP sesuai, akun berhasil dibuat
                    Toast.makeText(this, "Pembuatan akun berhasil", Toast.LENGTH_SHORT).show()
                    // TODO: Lakukan tindakan setelah verifikasi berhasil

                    CreateAccount()
                } else {
                    Toast.makeText(this, "Kode OTP salah", Toast.LENGTH_SHORT).show()
                }
            }

            // Mengirim kode OTP ke email pengguna jika tombol resendOtpButton ditekan
            btnResend.setOnClickListener {
                AddOTP()
            }
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(window.decorView.rootView.windowToken, 0)
    }

    private fun generateOtpCode(): String {
        // Menghasilkan kode OTP acak dengan 4 digit
        val otpCode = (1000..9999).random()
        return otpCode.toString()
    }

    private fun AddOTP(){
        otpCode = generateOtpCode()
        GenerateEmailOTP()
    }

    private fun GenerateEmailOTP(){
        val userEmail : String = email.trim()

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Mohon masukkan alamat email", Toast.LENGTH_SHORT).show()
            return
        }
        EmailSender.SendEmail(userEmail, otpCode)
    }
}