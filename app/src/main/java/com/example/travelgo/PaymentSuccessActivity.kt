package com.example.travelgo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.NumberFormat
import java.util.Locale

class PaymentSuccessActivity : AppCompatActivity() {

    private lateinit var btnLihatDetailBooking: Button
    private lateinit var btnKembaliBeranda: Button
    private lateinit var tvKodeBooking: TextView
    private lateinit var tvTotalPembayaran: TextView
    private lateinit var tvStatusBooking: TextView

    private var bookingId: Int = 0
    private var kodeBooking: String = ""
    private var totalHarga: Double = 0.0
    private var metodePembayaran: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_success)

        initViews()
        getDataFromIntent()
        saveToLocalStorage()
        setupListeners()
    }

    private fun initViews() {
        btnLihatDetailBooking = findViewById(R.id.btnLihatDetailBooking)
        btnKembaliBeranda = findViewById(R.id.btnKembaliBeranda)
        tvKodeBooking = findViewById(R.id.tvKodeBooking)
        tvTotalPembayaran = findViewById(R.id.tvTotalPembayaran)
        tvStatusBooking = findViewById(R.id.tvStatusBooking)
    }

    private fun getDataFromIntent() {
        bookingId = intent.getIntExtra("BOOKING_ID", 0)
        kodeBooking = intent.getStringExtra("KODE_BOOKING") ?: "-"
        totalHarga = intent.getDoubleExtra("TOTAL_HARGA", 0.0)
        metodePembayaran = intent.getStringExtra("METODE_PEMBAYARAN") ?: "BCA"

        tvKodeBooking.text = kodeBooking
        tvTotalPembayaran.text = formatRupiah(totalHarga)
        tvStatusBooking.text = "Sudah Dibayar"
    }

    private fun saveToLocalStorage() {
        val pref = getSharedPreferences("LAST_TICKET", MODE_PRIVATE)
        val userPref = getSharedPreferences("USER_DATA", MODE_PRIVATE)
        val namaPemesan = userPref.getString("NAMA", "Traveler") ?: "Traveler"
        val totalPerjalanan = pref.getInt("TOTAL_PERJALANAN", 0)

        pref.edit()
            .putString("NAMA_DESTINASI", intent.getStringExtra("NAMA_DESTINASI"))
            .putString("LOKASI_DESTINASI", intent.getStringExtra("LOKASI_DESTINASI"))
            .putInt("GAMBAR_DESTINASI", intent.getIntExtra("GAMBAR_DESTINASI", R.drawable.img_onboarding1))
            .putString("TANGGAL_BERANGKAT", intent.getStringExtra("TANGGAL_BERANGKAT"))
            .putInt("JUMLAH_ORANG", intent.getIntExtra("JUMLAH_ORANG", 1))
            .putInt("JUMLAH_TIKET", intent.getIntExtra("JUMLAH_TIKET", 1))
            // PERBAIKAN: pakai putString untuk Double
            .putString("TOTAL_HARGA", totalHarga.toString())
            .putString("KODE_BOOKING", kodeBooking)
            .putString("PAYMENT_STATUS", "paid")
            .putString("METODE_PEMBAYARAN", metodePembayaran)
            .putString("NAMA_PEMESAN", namaPemesan)
            .putBoolean("ADA_TIKET", true)
            .putInt("TOTAL_PERJALANAN", totalPerjalanan + 1)
            .apply()
    }

    private fun setupListeners() {
        btnLihatDetailBooking.setOnClickListener {
            val intent = Intent(this, MyTicketActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnKembaliBeranda.setOnClickListener {
            val homeIntent = Intent(this, MainActivity::class.java)
            homeIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(homeIntent)
            finish()
        }
    }

    private fun formatRupiah(value: Double): String {
        val formatRupiah = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("id-ID"))
        return formatRupiah.format(value).replace(",00", "")
    }
}