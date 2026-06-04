package com.example.travelgo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.NumberFormat
import java.util.Locale

class PaymentDetailActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var btnSudahBayar: Button

    private lateinit var tvMetodePembayaran: TextView
    private lateinit var tvDeskripsiPembayaran: TextView
    private lateinit var tvNamaBank: TextView
    private lateinit var tvNomorRekening: TextView

    private lateinit var tvTotalPembayaran: TextView

    private lateinit var tvCatatanBooking: TextView

    private var metodePembayaran = "BCA"
    private var totalHarga = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_detail)

        btnBack = findViewById(R.id.btnBack)
        btnSudahBayar = findViewById(R.id.btnSudahBayar)
        tvMetodePembayaran = findViewById(R.id.tvMetodePembayaran)
        tvDeskripsiPembayaran = findViewById(R.id.tvDeskripsiPembayaran)
        tvNamaBank = findViewById(R.id.tvNamaBank)
        tvNomorRekening = findViewById(R.id.tvNomorRekening)
        tvTotalPembayaran = findViewById(R.id.tvTotalPembayaran)
        tvTotalPembayaran.text = formatRupiah(totalHarga)

        tvCatatanBooking = findViewById(R.id.tvCatatanBooking)


        metodePembayaran =
            intent.getStringExtra("METODE_PEMBAYARAN") ?: "BCA"

        totalHarga =
            intent.getIntExtra("TOTAL_HARGA", 0)
        val kodeBooking =
            intent.getStringExtra("KODE_BOOKING")
                ?: "-"
        tvCatatanBooking.text =
            "Gunakan kode booking $kodeBooking sebagai berita saat transfer."

        setupPaymentInfo()

        btnBack.setOnClickListener {
            finish()
        }

        btnSudahBayar.setOnClickListener {
            val confirmationIntent =
                Intent(this, PaymentConfirmationActivity::class.java)

            confirmationIntent.putExtras(intent)

            confirmationIntent.putExtra(
                "METODE_PEMBAYARAN",
                metodePembayaran
            )

            confirmationIntent.putExtra(
                "TOTAL_HARGA",
                totalHarga
            )

            startActivity(confirmationIntent)
        }
    }

    private fun setupPaymentInfo() {
        when (metodePembayaran) {
            "DANA" -> {
                tvMetodePembayaran.text = "DANA"
                tvDeskripsiPembayaran.text =
                    "Transfer ke nomor DANA TravelGo sebesar ${formatRupiah(totalHarga)}"
                tvNamaBank.text = "DANA"
                tvNomorRekening.text = "0812 3456 7890"
            }

            else -> {
                tvMetodePembayaran.text = "Transfer Bank BCA"
                tvDeskripsiPembayaran.text =
                    "Transfer ke rekening TravelGo sebesar ${formatRupiah(totalHarga)}"
                tvNamaBank.text = "Bank BCA"
                tvNomorRekening.text = "1234567890"
            }
        }
    }

    private fun formatRupiah(value: Int): String {
        val formatRupiah =
            NumberFormat.getCurrencyInstance(Locale("in", "ID"))

        return formatRupiah.format(value).replace(",00", "")
    }
}