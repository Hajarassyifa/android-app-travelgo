package com.example.travelgo

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.imageview.ShapeableImageView
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ProfileActivity : AppCompatActivity() {

    private lateinit var ivAvatar: ShapeableImageView
    private lateinit var btnChangePhoto: FloatingActionButton
    private lateinit var tvName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvNameError: TextView   // error label untuk nama
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var btnSaveProfile: MaterialButton
    private lateinit var btnUploadPhoto: MaterialButton
    private lateinit var menuBookings: LinearLayout
    private lateinit var menuReviews: LinearLayout
    private lateinit var menuNotifications: LinearLayout
    private lateinit var btnLogout: MaterialButton

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data ?: return@registerForActivityResult
            Glide.with(this).load(uri).into(ivAvatar)
            uploadPhoto(uri)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) openGallery()
        else Toast.makeText(this, "Izin diperlukan untuk memilih foto", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        initViews()
        loadProfile()
        setupClickListeners()
    }

    private fun initViews() {
        ivAvatar          = findViewById(R.id.ivAvatar)
        btnChangePhoto    = findViewById(R.id.btnChangePhoto)
        tvName            = findViewById(R.id.tvName)
        tvEmail           = findViewById(R.id.tvEmail)
        tvNameError       = findViewById(R.id.tilName)   // tilName sekarang TextView
        etName            = findViewById(R.id.etName)
        etEmail           = findViewById(R.id.etEmail)
        btnSaveProfile    = findViewById(R.id.btnSaveProfile)
        btnUploadPhoto    = findViewById(R.id.btnUploadPhoto)
        menuBookings      = findViewById(R.id.menuBookings)
        menuReviews       = findViewById(R.id.menuReviews)
        menuNotifications = findViewById(R.id.menuNotifications)
        btnLogout         = findViewById(R.id.btnLogout)
    }

    private fun loadProfile() {
        val token = SessionManager.getToken(this) ?: return
        ApiClient.apiService.getProfile("Bearer $token")
            .enqueue(object : Callback<ProfileResponse> {
                override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                    if (response.isSuccessful) {
                        val user = response.body()?.data ?: return
                        tvName.text  = user.name
                        tvEmail.text = user.email
                        etName.setText(user.name) // UserProfile sudah map "nama" -> name via @SerializedName
                        etEmail.isEnabled = true
                        etEmail.setText(user.email)
                        etEmail.isEnabled = false
                        if (!user.photo.isNullOrEmpty()) {
                            Glide.with(this@ProfileActivity)
                                .load(user.photo)
                                .placeholder(R.drawable.ic_profile_placeholder)
                                .into(ivAvatar)
                        }
                    }
                }
                override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                    Toast.makeText(this@ProfileActivity, "Gagal memuat profil", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun setupClickListeners() {
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener { finish() }
        ivAvatar.setOnClickListener { checkAndOpenGallery() }
        btnChangePhoto.setOnClickListener { checkAndOpenGallery() }
        btnUploadPhoto.setOnClickListener { checkAndOpenGallery() }
        btnSaveProfile.setOnClickListener { saveProfile() }
        menuBookings.setOnClickListener { startActivity(Intent(this, MyTicketActivity::class.java)) }
        menuReviews.setOnClickListener { startActivity(Intent(this, ReviewsActivity::class.java)) }
        menuNotifications.setOnClickListener { startActivity(Intent(this, NotificationsActivity::class.java)) }
        btnLogout.setOnClickListener { showLogoutDialog() }
    }

    private fun saveProfile() {
        val name = etName.text.toString().trim()
        if (name.isEmpty()) {
            tvNameError.text = "Nama tidak boleh kosong"
            tvNameError.visibility = View.VISIBLE
            return
        }
        tvNameError.visibility = View.GONE

        val token = SessionManager.getToken(this) ?: return
        btnSaveProfile.isEnabled = false
        btnSaveProfile.text = "Menyimpan..."

        ApiClient.apiService.updateProfile("Bearer $token", mapOf("nama" to name))
            .enqueue(object : Callback<ProfileResponse> {
                override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                    btnSaveProfile.isEnabled = true
                    btnSaveProfile.text = "Simpan Perubahan"
                    if (response.isSuccessful) {
                        tvName.text = name
                        Toast.makeText(this@ProfileActivity, "Profil berhasil diperbarui!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@ProfileActivity, "Gagal memperbarui profil", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                    btnSaveProfile.isEnabled = true
                    btnSaveProfile.text = "Simpan Perubahan"
                    Toast.makeText(this@ProfileActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun uploadPhoto(uri: Uri) {
        val token = SessionManager.getToken(this) ?: return
        val file  = uriToFile(uri) ?: return
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("photo", file.name, requestFile)
        ApiClient.apiService.updatePhoto("Bearer $token", body)
            .enqueue(object : Callback<ProfileResponse> {
                override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                    Toast.makeText(this@ProfileActivity,
                        if (response.isSuccessful) "Foto berhasil diperbarui!" else "Gagal upload foto",
                        Toast.LENGTH_SHORT).show()
                }
                override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                    Toast.makeText(this@ProfileActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun checkAndOpenGallery() {
        val permission = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU)
            Manifest.permission.READ_MEDIA_IMAGES else Manifest.permission.READ_EXTERNAL_STORAGE
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED)
            openGallery() else requestPermissionLauncher.launch(permission)
    }

    private fun openGallery() {
        pickImageLauncher.launch(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Apakah kamu yakin ingin keluar?")
            .setPositiveButton("Ya, Keluar") { _, _ ->
                SessionManager.clearSession(this)
                startActivity(Intent(this, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                })
                finish()
            }
            .setNegativeButton("Batal", null).show()
    }

    private fun uriToFile(uri: Uri): File? {
        return try {
            val inputStream = contentResolver.openInputStream(uri) ?: return null
            val tempFile = File.createTempFile("photo_", ".jpg", cacheDir)
            tempFile.outputStream().use { inputStream.copyTo(it) }
            tempFile
        } catch (e: Exception) { null }
    }
}