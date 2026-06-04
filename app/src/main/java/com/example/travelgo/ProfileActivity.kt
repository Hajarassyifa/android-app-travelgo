package com.example.travelgo

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {

    private lateinit var tvName: TextView
    private lateinit var etName: EditText
    private lateinit var btnSaveProfile: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        tvName = findViewById(R.id.tvName)
        etName = findViewById(R.id.etName)
        btnSaveProfile = findViewById(R.id.btnSaveProfile)

        loadProfile()

        btnSaveProfile.setOnClickListener {
            saveProfile()
        }
    }

    private fun loadProfile() {
        val token = SessionManager.getToken(this) ?: return
        ApiClient.apiService.getProfile("Bearer $token")
            .enqueue(object : Callback<ProfileResponse> {
                override fun onResponse(
                    call: Call<ProfileResponse>,
                    response: Response<ProfileResponse>
                ) {
                    if (response.isSuccessful && response.body()?.status == true) {
                        val user = response.body()?.data
                        tvName.text = user?.name
                        etName.setText(user?.name)
                    }
                }

                override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {}
            })
    }

    private fun saveProfile() {
        val name = etName.text.toString().trim()
        if (name.isEmpty()) return

        val token = SessionManager.getToken(this) ?: return
        btnSaveProfile.isEnabled = false
        btnSaveProfile.text = "Menyimpan..."

        // Tambahkan parameter phone (misal diisi string kosong "" atau nomor default jika ada etPhone)
        val requestBody = UpdateProfileRequest(nama = name, phone = "")

        ApiClient.apiService.updateProfile("Bearer $token", requestBody)
            .enqueue(object : Callback<ProfileResponse> {
                override fun onResponse(
                    call: Call<ProfileResponse>,
                    response: Response<ProfileResponse>
                ) {
                    btnSaveProfile.isEnabled = true
                    btnSaveProfile.text = "Simpan Perubahan"
                    if (response.isSuccessful) {
                        tvName.text = name
                        Toast.makeText(
                            this@ProfileActivity,
                            "Profil diperbarui!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                    btnSaveProfile.isEnabled = true
                    btnSaveProfile.text = "Simpan Perubahan"
                }
            })
    }
}