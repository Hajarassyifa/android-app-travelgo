package com.example.travelgo

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {

    private lateinit var tvNamaUser: TextView
    private lateinit var tvEmailUser: TextView
    private lateinit var tvTotalPerjalanan: TextView
    private lateinit var btnLogout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        tvNamaUser = findViewById(R.id.tvNamaUser)
        tvEmailUser = findViewById(R.id.tvEmailUser)
        tvTotalPerjalanan = findViewById(R.id.tvTotalPerjalanan)
        btnLogout = findViewById(R.id.btnLogout)

        setupMenu(R.id.menuBooking, R.drawable.ic_booking, "Booking Saya")
        setupMenu(R.id.menuFavorit, R.drawable.ic_favorite, "Favorit")
        setupMenu(R.id.menuNotif, R.drawable.ic_notification, "Notifikasi")
        setupMenu(R.id.menuPengaturan, R.drawable.ic_setting, "Pengaturan")

        tampilkanDataUser()
        setupLogout()
    }

    private fun tampilkanDataUser() {
        val userPref = getSharedPreferences("USER_DATA", MODE_PRIVATE)
        val ticketPref = getSharedPreferences("LAST_TICKET", MODE_PRIVATE)

        val nama = userPref.getString("NAMA", "Traveler")
        val email = userPref.getString("EMAIL", "traveler@gmail.com")
        val totalPerjalanan = ticketPref.getInt("TOTAL_PERJALANAN", 0)

        tvNamaUser.text = nama
        tvEmailUser.text = email
        tvTotalPerjalanan.text = totalPerjalanan.toString()
    }

    private fun setupMenu(id: Int, icon: Int, title: String) {

        val menu = findViewById<LinearLayout>(id)

        menu.findViewById<ImageView>(R.id.imgMenuIcon)
            .setImageResource(icon)

        menu.findViewById<TextView>(R.id.tvMenuTitle)
            .text = title

        menu.setOnClickListener {

            when (title) {

                "Pengaturan" -> {

                    startActivity(
                        Intent(this, EditProfileActivity::class.java)
                    )
                }

                "Booking Saya" -> {

                    startActivity(
                        Intent(this, MyTicketActivity::class.java)
                    )
                }
                "Notifikasi" -> {
                    startActivity(
                        Intent(this, NotifikasiActivity::class.java)
                    )
                }

                else -> {

                    Toast.makeText(
                        this,
                        title,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun setupLogout() {
        btnLogout.setOnClickListener {
            val sessionPref = getSharedPreferences("USER_SESSION", MODE_PRIVATE)

            sessionPref.edit()
                .clear()
                .apply()

            Toast.makeText(this, "Logout berhasil", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
            finish()
        }
    }
}