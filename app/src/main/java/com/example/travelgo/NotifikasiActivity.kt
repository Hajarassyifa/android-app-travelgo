package com.example.travelgo

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class NotifikasiActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var btnBacaSemua: Button
    private lateinit var rvNotifikasi: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifikasi)

        btnBack = findViewById(R.id.btnBack)
        btnBacaSemua = findViewById(R.id.btnBacaSemua)
        rvNotifikasi = findViewById(R.id.rvNotifikasi)

        val listNotif = listOf(

            Notifikasi(
                1,
                "Booking Berhasil",
                "Booking Bali telah berhasil dibuat.",
                "booking",
                false,
                "Baru saja"
            ),

            Notifikasi(
                2,
                "Pembayaran Diterima",
                "Pembayaran Anda telah dikonfirmasi.",
                "payment",
                true,
                "2 jam lalu"
            ),

            Notifikasi(
                3,
                "Promo TravelGo",
                "Diskon 25% untuk destinasi pilihan.",
                "promo",
                false,
                "Kemarin"
            )
        )

        rvNotifikasi.layoutManager =
            LinearLayoutManager(this)

        rvNotifikasi.adapter =
            NotifikasiAdapter(listNotif)

        btnBack.setOnClickListener {
            finish()
        }

        btnBacaSemua.setOnClickListener {

            Toast.makeText(
                this,
                "Nanti connect ke endpoint read-all",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}