package com.example.travelgo

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        setupMenu(R.id.menuBooking, R.drawable.ic_booking, "Booking Saya")
        setupMenu(R.id.menuFavorit, R.drawable.ic_favorite, "Favorit")
        setupMenu(R.id.menuNotif, R.drawable.ic_notification, "Notifikasi")
        setupMenu(R.id.menuPengaturan, R.drawable.ic_setting, "Pengaturan")

        findViewById<LinearLayout>(R.id.btnLogout).setOnClickListener {
            Toast.makeText(this, "Logout berhasil", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupMenu(id: Int, icon: Int, title: String) {
        val menu = findViewById<LinearLayout>(id)
        menu.findViewById<ImageView>(R.id.imgMenuIcon).setImageResource(icon)
        menu.findViewById<TextView>(R.id.tvMenuTitle).text = title

        menu.setOnClickListener {
            Toast.makeText(this, title, Toast.LENGTH_SHORT).show()
        }
    }
}