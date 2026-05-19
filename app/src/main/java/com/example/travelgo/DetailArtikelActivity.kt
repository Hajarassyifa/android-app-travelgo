package com.example.travelgo

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailArtikelActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var imgArtikel: ImageView
    private lateinit var tvJudul: TextView
    private lateinit var tvInfo: TextView
    private lateinit var tvIsi: TextView
    private lateinit var tvKategori: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_artikel)

        btnBack = findViewById(R.id.btnBack)
        imgArtikel = findViewById(R.id.imgArtikelDetail)
        tvJudul = findViewById(R.id.tvJudulDetail)
        tvInfo = findViewById(R.id.tvInfoDetail)
        tvIsi = findViewById(R.id.tvIsiArtikel)
        tvKategori = findViewById(R.id.tvKategoriDetail)

        btnBack.setOnClickListener {
            finish()
        }

        val judul = intent.getStringExtra("JUDUL") ?: "-"
        val kategori = intent.getStringExtra("KATEGORI") ?: "-"
        val waktu = intent.getStringExtra("WAKTU") ?: "-"
        val tanggal = intent.getStringExtra("TANGGAL") ?: "-"
        val gambar = intent.getIntExtra("GAMBAR", R.drawable.img_onboarding1)
        val isi = intent.getStringExtra("ISI") ?: "-"

        imgArtikel.setImageResource(gambar)
        tvJudul.text = judul
        tvKategori.text = kategori
        tvInfo.text = "$tanggal • $waktu"
        tvIsi.text = isi
    }
}