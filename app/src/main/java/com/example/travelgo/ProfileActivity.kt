package com.example.travelgo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ProfileActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageButton
    private lateinit var tvName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var btnSaveProfile: MaterialButton
    private lateinit var btnLogout: MaterialButton
    private lateinit var ivAvatar: ShapeableImageView
    private lateinit var btnUploadPhoto: MaterialButton

    private lateinit var menuBookings: LinearLayout
    private lateinit var menuReviews: LinearLayout
    private lateinit var menuNotifications: LinearLayout

    private var selectedImageUri: Uri? = null

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            Glide.with(this).load(it).into(ivAvatar)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // 1. Inisialisasi ID Komponen
        btnBack = findViewById(R.id.btnBack)
        tvName = findViewById(R.id.tvName)
        tvEmail = findViewById(R.id.tvEmail)
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        btnSaveProfile = findViewById(R.id.btnSaveProfile)
        btnLogout = findViewById(R.id.btnLogout)
        ivAvatar = findViewById(R.id.ivAvatar)
        btnUploadPhoto = findViewById(R.id.btnUploadPhoto)

        menuBookings = findViewById(R.id.menuBookings)
        menuReviews = findViewById(R.id.menuReviews)
        menuNotifications = findViewById(R.id.menuNotifications)

        // 2. Mengambil Data Sesi
        val token = SessionManager.getToken(this)
        val namaUser = SessionManager.getName(this)
        val emailUser = SessionManager.getEmail(this)

        tvName.text = namaUser
        tvEmail.text = emailUser
        etName.setText(namaUser)
        etEmail.setText(emailUser)

        // ========================================================
        // PERBAIKAN NAVIGASI MENU (KLIK)
        // ========================================================

        menuBookings.setOnClickListener {
            // Dipastikan mengarah ke halaman list/daftar riwayat booking milik user
            val intent = Intent(this, BookingActivity::class.java)
            startActivity(intent)
        }

        menuReviews.setOnClickListener {
            val intent = Intent(this, ReviewsActivity::class.java)
            startActivity(intent)
        }

        menuNotifications.setOnClickListener {
            val intent = Intent(this, NotificationsActivity::class.java)
            startActivity(intent)
        }

        // ========================================================
        // TOMBOL AKSI PROFIL
        // ========================================================

        btnUploadPhoto.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        btnSaveProfile.setOnClickListener {
            val updatedName = etName.text.toString().trim()
            if (updatedName.isEmpty()) {
                Toast.makeText(this, "Nama tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (token == null) {
                Toast.makeText(this, "Sesi habis, silakan login kembali", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (selectedImageUri != null) {
                uploadFotoKeServer("Bearer $token", selectedImageUri!!)
            } else {
                updateDataProfil("Bearer $token", updatedName)
            }
        }

        btnBack.setOnClickListener { finish() }

        btnLogout.setOnClickListener {
            SessionManager.clearSession(this)
            Toast.makeText(this, "Berhasil keluar dari akun", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            finish()
        }
    }

    private fun uploadFotoKeServer(authToken: String, uri: Uri) {
        val filePath = getRealPathFromURI(uri)
        if (filePath == null) {
            Toast.makeText(this, "Gagal memproses file gambar", Toast.LENGTH_SHORT).show()
            return
        }

        val file = File(filePath)
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("photo", file.name, requestFile)

        btnSaveProfile.isEnabled = false
        btnSaveProfile.text = "Mengunggah..."

        ApiClient.apiService.updatePhoto(authToken, body).enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                btnSaveProfile.isEnabled = true
                btnSaveProfile.text = "Simpan Perubahan"

                if (response.isSuccessful) {
                    Toast.makeText(this@ProfileActivity, "Foto profil berhasil diperbarui!", Toast.LENGTH_SHORT).show()
                    val updatedName = etName.text.toString().trim()
                    updateDataProfil(authToken, updatedName)
                } else {
                    Toast.makeText(this@ProfileActivity, "Gagal upload foto: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                btnSaveProfile.isEnabled = true
                btnSaveProfile.text = "Simpan Perubahan"
                Toast.makeText(this@ProfileActivity, "Koneksi gagal: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateDataProfil(authToken: String, name: String) {
        btnSaveProfile.isEnabled = false
        btnSaveProfile.text = "Menyimpan..."

        val request = UpdateProfileRequest(name, "")

        ApiClient.apiService.updateProfile(authToken, request).enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                btnSaveProfile.isEnabled = true
                btnSaveProfile.text = "Simpan Perubahan"

                if (response.isSuccessful) {
                    SessionManager.saveSession(
                        context = this@ProfileActivity,
                        token   = SessionManager.getToken(this@ProfileActivity) ?: "",
                        name    = name,
                        email   = SessionManager.getEmail(this@ProfileActivity)
                    )
                    Toast.makeText(this@ProfileActivity, "Profil berhasil diperbarui!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@ProfileActivity, "Server menolak: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                btnSaveProfile.isEnabled = true
                btnSaveProfile.text = "Simpan Perubahan"
                Toast.makeText(this@ProfileActivity, "Gagal: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun getRealPathFromURI(contentUri: Uri): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(contentUri, proj, null, null, null)
        val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        val path = columnIndex?.let { cursor.getString(it) }
        cursor?.close()
        return path
    }
}