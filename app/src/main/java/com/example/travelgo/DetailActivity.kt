package com.example.travelgo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailActivity : AppCompatActivity() {

    private var idDestinasi = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val imgDetail = findViewById<ImageView>(R.id.imgDetail)
        val tvNama = findViewById<TextView>(R.id.tvNamaDetail)
        val tvLokasi = findViewById<TextView>(R.id.tvLokasiDetail)
        val tvHarga = findViewById<TextView>(R.id.tvHargaDetail)
        val tvDeskripsi = findViewById<TextView>(R.id.tvDeskripsiDetail)

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val btnBooking = findViewById<Button>(R.id.btnBooking)
        val btnReview = findViewById<Button>(R.id.btnReview)

        idDestinasi = intent.getIntExtra("ID_DESTINASI", 0)

        val nama = intent.getStringExtra("NAMA_DESTINASI") ?: "-"
        val lokasi = intent.getStringExtra("LOKASI_DESTINASI") ?: "-"
        val harga = intent.getStringExtra("HARGA_DESTINASI") ?: "-"
        val gambar = intent.getIntExtra("GAMBAR_DESTINASI", R.drawable.img_onboarding1)

        tvNama.text = nama
        tvLokasi.text = lokasi
        tvHarga.text = harga
        imgDetail.setImageResource(gambar)

        tvDeskripsi.text =
            "Selamat datang di $nama! Nikmati pengalaman luar biasa di $lokasi dengan harga terbaik."

        btnBack.setOnClickListener {
            finish()
        }

        btnBooking.setOnClickListener {
            val bookingIntent = Intent(this, BookingActivity::class.java)

            bookingIntent.putExtra("ID_DESTINASI", idDestinasi)
            bookingIntent.putExtra("NAMA_DESTINASI", nama)
            bookingIntent.putExtra("LOKASI_DESTINASI", lokasi)
            bookingIntent.putExtra("HARGA_DESTINASI", harga)
            bookingIntent.putExtra("GAMBAR_DESTINASI", gambar)

            startActivity(bookingIntent)
        }

        btnReview.setOnClickListener {
            val reviewIntent = Intent(this, ReviewActivity::class.java)
            reviewIntent.putExtra("PACKAGE_ID", idDestinasi)
            startActivity(reviewIntent)
        }
    }
}