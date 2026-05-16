package com.example.travelgo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailActivity : AppCompatActivity() {

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

        val nama = intent.getStringExtra("NAMA_DESTINASI")
        val lokasi = intent.getStringExtra("LOKASI_DESTINASI")
        val harga = intent.getStringExtra("HARGA_DESTINASI")
        val gambar = intent.getIntExtra("GAMBAR_DESTINASI", 0)

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

            val intent = Intent(this, BookingActivity::class.java)

            intent.putExtra("NAMA_DESTINASI", nama)
            intent.putExtra("LOKASI_DESTINASI", lokasi)
            intent.putExtra("HARGA_DESTINASI", harga)
            intent.putExtra("GAMBAR_DESTINASI", gambar)

            startActivity(intent)
        }
    }
}