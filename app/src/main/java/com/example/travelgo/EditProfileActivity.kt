package com.example.travelgo

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EditProfileActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var imgProfile: ImageView
    private lateinit var tvNamaProfile: TextView
    private lateinit var tvEmailProfile: TextView

    private lateinit var etNama: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPasswordLama: EditText
    private lateinit var etPasswordBaru: EditText
    private lateinit var etKonfirmasiPassword: EditText

    private lateinit var btnGantiFoto: Button
    private lateinit var btnSimpanNama: Button
    private lateinit var btnUbahPassword: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        initViews()
        loadUserData()
        setupButton()
    }

    private fun initViews() {
        btnBack = findViewById(R.id.btnBack)
        imgProfile = findViewById(R.id.imgProfile)
        tvNamaProfile = findViewById(R.id.tvNamaProfile)
        tvEmailProfile = findViewById(R.id.tvEmailProfile)

        etNama = findViewById(R.id.etNama)
        etEmail = findViewById(R.id.etEmail)
        etPasswordLama = findViewById(R.id.etPasswordLama)
        etPasswordBaru = findViewById(R.id.etPasswordBaru)
        etKonfirmasiPassword = findViewById(R.id.etKonfirmasiPassword)

        btnGantiFoto = findViewById(R.id.btnGantiFoto)
        btnSimpanNama = findViewById(R.id.btnSimpanNama)
        btnUbahPassword = findViewById(R.id.btnUbahPassword)
    }

    private fun loadUserData() {
        val userPref = getSharedPreferences("USER_DATA", MODE_PRIVATE)

        val nama = userPref.getString("NAMA", "Traveler") ?: "Traveler"
        val email = userPref.getString("EMAIL", "traveler@gmail.com") ?: "traveler@gmail.com"

        tvNamaProfile.text = nama
        tvEmailProfile.text = email

        etNama.setText(nama)
        etEmail.setText(email)
    }

    private fun setupButton() {
        btnBack.setOnClickListener {
            finish()
        }

        btnGantiFoto.setOnClickListener {
            Toast.makeText(this, "Fitur upload foto akan disambungkan ke API", Toast.LENGTH_SHORT).show()
        }

        btnSimpanNama.setOnClickListener {
            val namaBaru = etNama.text.toString().trim()

            if (namaBaru.isEmpty()) {
                etNama.error = "Nama tidak boleh kosong"
                etNama.requestFocus()
                return@setOnClickListener
            }

            val userPref = getSharedPreferences("USER_DATA", MODE_PRIVATE)

            userPref.edit()
                .putString("NAMA", namaBaru)
                .apply()

            tvNamaProfile.text = namaBaru

            Toast.makeText(this, "Nama berhasil diperbarui", Toast.LENGTH_SHORT).show()
        }

        btnUbahPassword.setOnClickListener {
            val passwordLama = etPasswordLama.text.toString().trim()
            val passwordBaru = etPasswordBaru.text.toString().trim()
            val konfirmasi = etKonfirmasiPassword.text.toString().trim()

            if (passwordLama.isEmpty()) {
                etPasswordLama.error = "Password lama wajib diisi"
                etPasswordLama.requestFocus()
                return@setOnClickListener
            }

            if (passwordBaru.length < 8) {
                etPasswordBaru.error = "Password minimal 8 karakter"
                etPasswordBaru.requestFocus()
                return@setOnClickListener
            }

            if (passwordBaru != konfirmasi) {
                etKonfirmasiPassword.error = "Konfirmasi password tidak sama"
                etKonfirmasiPassword.requestFocus()
                return@setOnClickListener
            }

            Toast.makeText(this, "Ubah password siap disambungkan ke API", Toast.LENGTH_SHORT).show()
        }
    }
}