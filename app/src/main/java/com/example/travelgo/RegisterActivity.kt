package com.example.travelgo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var etNama: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etKonfirmasiPassword: EditText


    private lateinit var btnDaftar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etNama = findViewById(R.id.etNama)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etKonfirmasiPassword =
            findViewById(R.id.etKonfirmasiPassword)

        btnDaftar = findViewById(R.id.btnDaftar)

        btnDaftar.setOnClickListener {

            val nama =
                etNama.text.toString().trim()

            val email =
                etEmail.text.toString().trim()

            val password =
                etPassword.text.toString().trim()

            val konfirmasiPassword =
                etKonfirmasiPassword.text.toString().trim()

            // VALIDASI

            if (nama.isEmpty()) {

                etNama.error = "Nama wajib diisi"
                etNama.requestFocus()
                return@setOnClickListener
            }

            if (email.isEmpty()) {

                etEmail.error = "Email wajib diisi"
                etEmail.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {

                etPassword.error = "Password wajib diisi"
                etPassword.requestFocus()
                return@setOnClickListener
            }

            if (password.length < 6) {

                etPassword.error = "Password minimal 6 karakter"
                etPassword.requestFocus()
                return@setOnClickListener
            }

            if (konfirmasiPassword.isEmpty()) {

                etKonfirmasiPassword.error =
                    "Konfirmasi password wajib diisi"

                etKonfirmasiPassword.requestFocus()
                return@setOnClickListener
            }

            if (password != konfirmasiPassword) {

                etKonfirmasiPassword.error =
                    "Password tidak sama"

                etKonfirmasiPassword.requestFocus()
                return@setOnClickListener
            }

            // SIMPAN DATA USER LOKAL
            val sharedPref =
                getSharedPreferences("USER_DATA", MODE_PRIVATE)

            sharedPref.edit()
                .putString("NAMA", nama)
                .putString("EMAIL", email)
                .putString("PASSWORD", password)
                .apply()

            Toast.makeText(
                this,
                "Registrasi berhasil",
                Toast.LENGTH_SHORT
            ).show()

            // PINDAH KE LOGIN
            val intent =
                Intent(this, LoginActivity::class.java)

            startActivity(intent)
            finish()
        }
    }
}