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

    private var metodePembayaran: String = "BCA"
    private var totalHarga: Double = 0.0
    private var bookingId: Int = 0
    private var kodeBooking: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_detail)

        initViews()
        getDataFromIntent()
        setupPaymentInfo()
        setupListeners()
    }

    private fun initViews() {
        btnBack = findViewById(R.id.btnBack)
        btnSudahBayar = findViewById(R.id.btnSudahBayar)
        tvMetodePembayaran = findViewById(R.id.tvMetodePembayaran)
        tvDeskripsiPembayaran = findViewById(R.id.tvDeskripsiPembayaran)
        tvNamaBank = findViewById(R.id.tvNamaBank)
        tvNomorRekening = findViewById(R.id.tvNomorRekening)
        tvTotalPembayaran = findViewById(R.id.tvTotalPembayaran)
        tvCatatanBooking = findViewById(R.id.tvCatatanBooking)
    }

    private fun getDataFromIntent() {
        metodePembayaran = intent.getStringExtra("METODE_PEMBAYARAN") ?: "BCA"
        totalHarga = intent.getDoubleExtra("TOTAL_HARGA", 0.0)
        bookingId = intent.getIntExtra("BOOKING_ID", 0)
        kodeBooking = intent.getStringExtra("KODE_BOOKING") ?: "-"

        tvTotalPembayaran.text = formatRupiah(totalHarga)
        tvCatatanBooking.text = "Gunakan kode booking $kodeBooking sebagai berita saat transfer."
    }

    private fun setupPaymentInfo() {
        when (metodePembayaran) {
            "QRIS" -> {
                tvMetodePembayaran.text = "QRIS"
                tvDeskripsiPembayaran.text = "Scan QR Code berikut sebesar ${formatRupiah(totalHarga)}"
                tvNamaBank.text = "QRIS"
                tvNomorRekening.text = "Scan QR Code di kasir"
            }
            "DANA" -> {
                tvMetodePembayaran.text = "DANA"
                tvDeskripsiPembayaran.text = "Transfer ke nomor DANA TravelGo sebesar ${formatRupiah(totalHarga)}"
                tvNamaBank.text = "DANA"
                tvNomorRekening.text = "0812 3456 7890"
            }
            "OVO" -> {
                tvMetodePembayaran.text = "OVO"
                tvDeskripsiPembayaran.text = "Transfer ke nomor OVO TravelGo sebesar ${formatRupiah(totalHarga)}"
                tvNamaBank.text = "OVO"
                tvNomorRekening.text = "0812 3456 7890"
            }
            "GOPAY" -> {
                tvMetodePembayaran.text = "GoPay"
                tvDeskripsiPembayaran.text = "Transfer ke GoPay TravelGo sebesar ${formatRupiah(totalHarga)}"
                tvNamaBank.text = "GoPay"
                tvNomorRekening.text = "0812 3456 7890"
            }
            "MANDIRI" -> {
                tvMetodePembayaran.text = "Transfer Bank Mandiri"
                tvDeskripsiPembayaran.text = "Transfer ke rekening TravelGo sebesar ${formatRupiah(totalHarga)}"
                tvNamaBank.text = "Bank Mandiri"
                tvNomorRekening.text = "1234567890"
            }
            "BNI" -> {
                tvMetodePembayaran.text = "Transfer Bank BNI"
                tvDeskripsiPembayaran.text = "Transfer ke rekening TravelGo sebesar ${formatRupiah(totalHarga)}"
                tvNamaBank.text = "Bank BNI"
                tvNomorRekening.text = "1234567890"
            }
            "BRI" -> {
                tvMetodePembayaran.text = "Transfer Bank BRI"
                tvDeskripsiPembayaran.text = "Transfer ke rekening TravelGo sebesar ${formatRupiah(totalHarga)}"
                tvNamaBank.text = "Bank BRI"
                tvNomorRekening.text = "1234567890"
            }
            else -> {
                // Default BCA
                tvMetodePembayaran.text = "Transfer Bank BCA"
                tvDeskripsiPembayaran.text = "Transfer ke rekening TravelGo sebesar ${formatRupiah(totalHarga)}"
                tvNamaBank.text = "Bank BCA"
                tvNomorRekening.text = "1234567890"
            }
        }
    }

    private fun setupListeners() {
        btnBack.setOnClickListener {
            finish()
        }

        btnSudahBayar.setOnClickListener {
            val confirmationIntent = Intent(this, PaymentConfirmationActivity::class.java)
            confirmationIntent.putExtra("BOOKING_ID", bookingId)
            confirmationIntent.putExtra("METODE_PEMBAYARAN", metodePembayaran)
            confirmationIntent.putExtra("TOTAL_HARGA", totalHarga)
            confirmationIntent.putExtra("KODE_BOOKING", kodeBooking)
            startActivity(confirmationIntent)
        }
    }

    private fun formatRupiah(value: Double): String {
        val formatRupiah = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("id-ID"))
        return formatRupiah.format(value).replace(",00", "")
    }
}