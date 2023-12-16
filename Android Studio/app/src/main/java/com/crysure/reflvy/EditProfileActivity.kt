package com.crysure.reflvy

import UserViewModel
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.crysure.reflvy.data.NotifyChat
import com.crysure.reflvy.data.User
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditProfileActivity : AppCompatActivity() {

    private lateinit var editTextDateOfBirth: EditText
    private lateinit var calendar : Calendar
    private lateinit var userName : EditText
    private lateinit var emailUser : EditText
    private lateinit var noTelepon : EditText
    private lateinit var editButton : Button
    private lateinit var userViewModel: UserViewModel
    private val db = Firebase.firestore
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var selectedGender: String
    private lateinit var radioGroupGender : RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        Footer()

        editTextDateOfBirth = findViewById(R.id.editTextDateOfBirth);
        calendar = Calendar.getInstance();

        emailUser = findViewById(R.id.email_inp)
        userName = findViewById(R.id.nama_inp)
        noTelepon = findViewById(R.id.telp_inp)
        editButton = findViewById(R.id.edit_btn)

        radioGroupGender = findViewById(R.id.radioGroupGender)

        sharedPreferences = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        // Observasi LiveData dan perbarui UI saat data berubah
        userViewModel.userLiveData.observe(this) { updatedUser ->
            emailUser.text = Editable.Factory.getInstance().newEditable(updatedUser.email)
            userName.text = Editable.Factory.getInstance().newEditable(updatedUser.userName)
            noTelepon.text = Editable.Factory.getInstance().newEditable(updatedUser.telepon)
            editTextDateOfBirth.text =
                Editable.Factory.getInstance().newEditable(updatedUser.tanggalLahir)
            selectedGender = updatedUser.gender

            if (selectedGender != null || selectedGender.isNotEmpty() || selectedGender != "") {
                if (selectedGender == "pria") {
                    radioGroupGender.check(R.id.radioButtonMale)
                } else if (selectedGender == "wanita") {
                    radioGroupGender.check(R.id.radioButtonFemale)
                }
            } else {
                radioGroupGender.clearCheck()
            }
        }

        radioGroupGender.setOnCheckedChangeListener { group, checkedId ->
            val selectedRadioButton: RadioButton = findViewById(checkedId)

            selectedGender = when (selectedRadioButton.id) {
                R.id.radioButtonMale -> "pria"
                R.id.radioButtonFemale -> "wanita"
                else -> ""
            }
        }

        // Load data pertama kali
        val sharedPreferences = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
        userViewModel.updateUserData(sharedPreferences)

        editButton.setOnClickListener{
            try {
                val documentReference = db.collection("users").document(User.userData.userID)

                val uname : String = userName.text.toString()
                val tgl : String = editTextDateOfBirth.text.toString()
                val tlp : String = noTelepon.text.toString()


                val updateData = hashMapOf(
                    "name" to uname,
                    "tanggalLahir" to tgl,
                    "telepon" to tlp,
                    "gender" to selectedGender
                )

                documentReference.set(updateData, SetOptions.merge())
                    .addOnSuccessListener {
                        UpdateUserInfo(uname, tlp, tgl, selectedGender)
                        UpdateLayout()
                    }
                    .addOnFailureListener {
                        // Gagal mengganti data
                    }
            }catch (e:Exception){
                //Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
            }

        }
    }

    fun showDatePickerDialog(view: View) {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this,
            { _: DatePicker, year: Int, month: Int, day: Int ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, day)

                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                editTextDateOfBirth.setText(sdf.format(calendar.time))
            }, year, month, dayOfMonth)

        datePickerDialog.show()
    }

    private fun UpdateUserInfo(username : String, tlp : String, tgl : String, gender : String){
        val editor = sharedPreferences.edit()
        editor.putString("userName", username)
        editor.putString("userGender", gender)
        editor.putString("tanggalLahir", tgl)
        editor.putString("telepon", tlp)
        editor.apply()

        updateUserData()
    }

    private fun UpdateLayout(){
        sharedPreferences = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        // Observasi LiveData dan perbarui UI saat data berubah
        userViewModel.userLiveData.observe(this) { updatedUser ->
            emailUser.text = Editable.Factory.getInstance().newEditable(updatedUser.email)
            userName.text = Editable.Factory.getInstance().newEditable(updatedUser.userName)
            noTelepon.text = Editable.Factory.getInstance().newEditable(updatedUser.telepon)
            editTextDateOfBirth.text =
                Editable.Factory.getInstance().newEditable(updatedUser.tanggalLahir)
            selectedGender = updatedUser.gender

            if (selectedGender != null || selectedGender.isNotEmpty() || selectedGender != "") {
                if (selectedGender == "pria") {
                    radioGroupGender.check(R.id.radioButtonMale)
                } else if (selectedGender == "wanita") {
                    radioGroupGender.check(R.id.radioButtonFemale)
                }
            } else {
                radioGroupGender.clearCheck()
            }
        }
    }

    fun updateUserData() {
        val sharedPreferences = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
        userViewModel.updateUserData(sharedPreferences)
    }

    override fun onBackPressed() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
        finish() // Optional: Menutup activity saat ini agar tidak kembali lagi dengan tombol kembali
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