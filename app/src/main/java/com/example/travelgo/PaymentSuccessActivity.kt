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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_success)

        simpanTiketTerakhir()

        btnLihatDetailBooking = findViewById(R.id.btnLihatDetailBooking)
        btnKembaliBeranda = findViewById(R.id.btnKembaliBeranda)
        tvKodeBooking = findViewById(R.id.tvKodeBooking)
        tvTotalPembayaran = findViewById(R.id.tvTotalPembayaran)
        tvStatusBooking = findViewById(R.id.tvStatusBooking)

        val kodeBooking = intent.getStringExtra("KODE_BOOKING") ?: "-"
        val totalHarga = intent.getIntExtra("TOTAL_HARGA", 0)

        tvKodeBooking.text = kodeBooking
        tvTotalPembayaran.text = formatRupiah(totalHarga)
        tvStatusBooking.text = "Sudah Dibayar"

        btnLihatDetailBooking.setOnClickListener {
            startActivity(Intent(this, MyTicketActivity::class.java))
        }

        btnKembaliBeranda.setOnClickListener {
            val homeIntent = Intent(this, MainActivity::class.java)
            homeIntent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(homeIntent)
            finish()
        }
    }

    private fun simpanTiketTerakhir() {
        val pref = getSharedPreferences("LAST_TICKET", MODE_PRIVATE)

        val userPref = getSharedPreferences("USER_DATA", MODE_PRIVATE)
        val namaPemesan = userPref.getString("NAMA", "Traveler")
        val totalPerjalanan = pref.getInt("TOTAL_PERJALANAN", 0)

        pref.edit()
            .putString("NAMA_DESTINASI", intent.getStringExtra("NAMA_DESTINASI"))
            .putString("LOKASI_DESTINASI", intent.getStringExtra("LOKASI_DESTINASI"))
            .putInt(
                "GAMBAR_DESTINASI",
                intent.getIntExtra("GAMBAR_DESTINASI", R.drawable.img_onboarding1)
            )
            .putString("TANGGAL_BERANGKAT", intent.getStringExtra("TANGGAL_BERANGKAT"))
            .putString("TANGGAL_KEMBALI", intent.getStringExtra("TANGGAL_KEMBALI"))
            .putInt("JUMLAH_ORANG", intent.getIntExtra("JUMLAH_ORANG", 1))
            .putInt("TOTAL_HARGA", intent.getIntExtra("TOTAL_HARGA", 0))
            .putString("KODE_BOOKING", intent.getStringExtra("KODE_BOOKING"))
            .putString("TOKEN_TIKET", intent.getStringExtra("TOKEN_TIKET"))
            .putString("PAYMENT_STATUS", "Sudah Dibayar")
            .putString("METODE_PEMBAYARAN", intent.getStringExtra("METODE_PEMBAYARAN"))
            .putString("NAMA_PEMESAN", intent.getStringExtra("NAMA_PEMESAN") ?: "Traveler")
            .putBoolean("ADA_TIKET", true)
            .putInt("TOTAL_PERJALANAN", totalPerjalanan + 1)
            .apply()
    }

    private fun formatRupiah(value: Int): String {
        val formatRupiah =
            NumberFormat.getCurrencyInstance(Locale("in", "ID"))

        return formatRupiah.format(value).replace(",00", "")
    }
}