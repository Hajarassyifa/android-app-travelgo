package com.example.travelgo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText

    private lateinit var btnMasuk: Button
    private lateinit var tvBelumPunyaAkun: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // CEK SESSION LOGIN
        val sessionPref =
            getSharedPreferences("USER_SESSION", MODE_PRIVATE)

        val isLogin =
            sessionPref.getBoolean("IS_LOGIN", false)

        if (isLogin) {

            startActivity(
                Intent(this, MainActivity::class.java)
            )

            finish()
            return
        }

        setContentView(R.layout.activity_login)

        // INIT VIEW
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)

        btnMasuk = findViewById(R.id.btnMasuk)
        tvBelumPunyaAkun = findViewById(R.id.tvToRegister)

        // LOGIN
        btnMasuk.setOnClickListener {

            val email =
                etEmail.text.toString().trim()

            val password =
                etPassword.text.toString().trim()

            // VALIDASI EMAIL
            if (email.isEmpty()) {

                etEmail.error = "Email wajib diisi"
                etEmail.requestFocus()

                return@setOnClickListener
            }

            // VALIDASI PASSWORD
            if (password.isEmpty()) {

                etPassword.error = "Password wajib diisi"
                etPassword.requestFocus()

                return@setOnClickListener
            }

            // AMBIL DATA USER
            val sharedPref =
                getSharedPreferences("USER_DATA", MODE_PRIVATE)

            val savedEmail =
                sharedPref.getString("EMAIL", "")

            val savedPassword =
                sharedPref.getString("PASSWORD", "")

            // CEK LOGIN
            if (email == savedEmail &&
                password == savedPassword
            ) {

                // SIMPAN SESSION LOGIN
                sessionPref.edit()
                    .putBoolean("IS_LOGIN", true)
                    .apply()

                Toast.makeText(
                    this,
                    "Login berhasil",
                    Toast.LENGTH_SHORT
                ).show()

                startActivity(
                    Intent(this, MainActivity::class.java)
                )

                finish()

            } else {

                Toast.makeText(
                    this,
                    "Email atau password salah",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // KE REGISTER
        tvBelumPunyaAkun.setOnClickListener {

            val intent =
                Intent(this, RegisterActivity::class.java)

            startActivity(intent)
        }
    }
}