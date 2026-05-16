package com.example.travelgo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class PaymentSuccessActivity : AppCompatActivity() {

    private lateinit var btnLihatDetailBooking: Button
    private lateinit var btnKembaliBeranda: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_success)

        btnLihatDetailBooking =
            findViewById(R.id.btnLihatDetailBooking)

        btnKembaliBeranda =
            findViewById(R.id.btnKembaliBeranda)

        btnLihatDetailBooking.setOnClickListener {

            val ticketIntent =
                Intent(this, MyTicketActivity::class.java)

            ticketIntent.putExtra(
                "NAMA_DESTINASI",
                intent.getStringExtra("NAMA_DESTINASI")
            )

            ticketIntent.putExtra(
                "LOKASI_DESTINASI",
                intent.getStringExtra("LOKASI_DESTINASI")
            )

            ticketIntent.putExtra(
                "GAMBAR_DESTINASI",
                intent.getIntExtra(
                    "GAMBAR_DESTINASI",
                    R.drawable.img_onboarding1
                )
            )

            ticketIntent.putExtra(
                "TANGGAL_BERANGKAT",
                intent.getStringExtra("TANGGAL_BERANGKAT")
            )

            ticketIntent.putExtra(
                "TANGGAL_KEMBALI",
                intent.getStringExtra("TANGGAL_KEMBALI")
            )

            ticketIntent.putExtra(
                "JUMLAH_ORANG",
                intent.getIntExtra("JUMLAH_ORANG", 1)
            )

            ticketIntent.putExtra(
                "TOTAL_HARGA",
                intent.getIntExtra("TOTAL_HARGA", 0)
            )

            ticketIntent.putExtra(
                "PAYMENT_STATUS",
                "Sudah Dibayar"
            )

            ticketIntent.putExtra(
                "METODE_PEMBAYARAN",
                intent.getStringExtra("METODE_PEMBAYARAN")
            )

            ticketIntent.putExtra(
                "KODE_BOOKING",
                intent.getStringExtra("KODE_BOOKING")
                    ?: "TRV123456789"
            )

            ticketIntent.putExtra(
                "TOKEN_TIKET",
                intent.getStringExtra("TOKEN_TIKET")
                    ?: "TKT-000000"
            )

            startActivity(ticketIntent)
        }

        btnKembaliBeranda.setOnClickListener {

            val homeIntent =
                Intent(this, MainActivity::class.java)

            homeIntent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_NEW_TASK

            startActivity(homeIntent)
            finish()
        }
    }
}