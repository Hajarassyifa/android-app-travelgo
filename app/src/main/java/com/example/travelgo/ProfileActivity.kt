package com.example.travelgo

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class ProfileActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageButton
    private lateinit var tvName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var btnSaveProfile: MaterialButton
    private lateinit var btnLogout: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile) // Menghubungkan ke XML profilmu

        // 1. Inisialisasi ID Komponen (100% Akurat Sesuai XML Aslimu)
        btnBack = findViewById(R.id.btnBack)
        tvName = findViewById(R.id.tvName)
        tvEmail = findViewById(R.id.tvEmail)
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        btnSaveProfile = findViewById(R.id.btnSaveProfile)
        btnLogout = findViewById(R.id.btnLogout)

        // 2. Ambil Data Pengguna dari SessionManager Asli Kamu
        val namaUser = SessionManager.getName(this)
        val emailUser = SessionManager.getEmail(this)

        // Tampilkan data ke komponen teks atas dan kolom input
        tvName.text = namaUser
        tvEmail.text = emailUser
        etName.setText(namaUser)
        etEmail.setText(emailUser)

        // 3. Logika Tombol Kembali (Back)
        btnBack.setOnClickListener {
            finish() // Menutup halaman profil dan kembali ke halaman sebelumnya
        }

        // 4. Logika Tombol Simpan Perubahan
        btnSaveProfile.setOnClickListener {
            Toast.makeText(this, "Perubahan berhasil disimpan!", Toast.LENGTH_SHORT).show()
            // Setelah simpan, arahkan kembali ke halaman utama (Beranda)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // 5. Logika Tombol Keluar (Logout) -> MENGATASI STUCK BISA PINDAH KE LOGIN/REGISTER
        btnLogout.setOnClickListener {
            // Jalankan fungsi penghapus sesi dari SessionManager bawaanmu
            SessionManager.clearSession(this)

            Toast.makeText(this, "Berhasil keluar dari akun", Toast.LENGTH_SHORT).show()

            // Alihkan paksa pengguna ke halaman Login dan bersihkan tumpukan halaman belakang
            val intent = Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            finish()
        }
    }
}