package com.example.travelgo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvRegister: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etEmail    = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin   = findViewById(R.id.btnLogin)
        tvRegister = findViewById(R.id.tvRegister)

        if (SessionManager.isLoggedIn(this)) {
            goToMain()
            return
        }

        btnLogin.setOnClickListener {
            val email    = etEmail.text.toString().trim()
            val password = etPassword.text.toString()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Isi email dan password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            doLogin(email, password)
        }

        tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun doLogin(email: String, password: String) {
        btnLogin.isEnabled = false
        btnLogin.text = "Masuk..."

        ApiClient.apiService.login(LoginRequest(email, password))
            .enqueue(object : Callback<AuthResponse> {
                override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                    btnLogin.isEnabled = true
                    btnLogin.text = "MASUK"

                    if (response.isSuccessful) {
                        val body = response.body()
                        // backend pakai "status" bukan "success"
                        if (body?.status == true && body.token != null) {
                            SessionManager.saveSession(
                                context = this@LoginActivity,
                                token   = body.token,
                                name    = body.data?.nama ?: "",  // backend pakai "nama"
                                email   = body.data?.email ?: "",
                                id      = body.data?.id ?: 0
                            )
                            Toast.makeText(this@LoginActivity, "Login berhasil!", Toast.LENGTH_SHORT).show()
                            goToMain()
                        } else {
                            Toast.makeText(this@LoginActivity, body?.message ?: "Login gagal", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        val msg = when (response.code()) {
                            401  -> "Email atau password salah"
                            422  -> "Data tidak valid"
                            else -> "Error: ${response.code()}"
                        }
                        Toast.makeText(this@LoginActivity, msg, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                    btnLogin.isEnabled = true
                    btnLogin.text = "MASUK"
                    Toast.makeText(this@LoginActivity, "Tidak dapat terhubung ke server", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
        finish()
    }
}