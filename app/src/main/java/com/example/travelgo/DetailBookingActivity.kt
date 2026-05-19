package com.example.travelgo

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.NumberFormat
import java.util.Locale

class DetailBookingActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var btnBayarSekarang: Button

    private lateinit var imgDestinasi: ImageView
    private lateinit var imgStatusIcon: ImageView

    private lateinit var tvStatusBooking: TextView
    private lateinit var tvNamaDestinasi: TextView
    private lateinit var tvTanggalBerangkat: TextView
    private lateinit var tvTanggalKembali: TextView
    private lateinit var tvJumlahOrang: TextView
    private lateinit var tvHargaPerOrang: TextView
    private lateinit var tvTotalHarga: TextView
    private lateinit var tvMetodePembayaran: TextView
    private lateinit var tvPaymentNote: TextView
    private lateinit var tvKodeBooking: TextView

    private lateinit var tvNamaPemesan: TextView

    private var totalHarga = 0
    private var paymentStatus = "Menunggu Pembayaran"
    private var kodeBooking = ""
    private var tokenTiket = ""
    private var metodePembayaran = "Belum Dibayar"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_booking)

        initViews()
        ambilDataIntent()
        setupButton()
    }

    private fun initViews() {
        btnBack = findViewById(R.id.btnBack)
        btnBayarSekarang = findViewById(R.id.btnBayarSekarang)

        imgDestinasi = findViewById(R.id.imgDestinasi)
        imgStatusIcon = findViewById(R.id.imgStatusIcon)

        tvStatusBooking = findViewById(R.id.tvStatusBooking)
        tvNamaDestinasi = findViewById(R.id.tvNamaDestinasi)
        tvTanggalBerangkat = findViewById(R.id.tvTanggalBerangkat)
        tvTanggalKembali = findViewById(R.id.tvTanggalKembali)
        tvJumlahOrang = findViewById(R.id.tvJumlahOrang)
        tvHargaPerOrang = findViewById(R.id.tvHargaPerOrang)
        tvTotalHarga = findViewById(R.id.tvTotalHarga)
        tvMetodePembayaran = findViewById(R.id.tvMetodePembayaran)
        tvPaymentNote = findViewById(R.id.tvPaymentNote)
        tvKodeBooking = findViewById(R.id.tvKodeBooking)
        tvNamaPemesan = findViewById(R.id.tvNamaPemesan)
    }

    private fun ambilDataIntent() {
        val namaPemesan =
            intent.getStringExtra("NAMA_PEMESAN") ?: "Traveler"

        tvNamaPemesan.text = namaPemesan
        val namaDestinasi =
            intent.getStringExtra("NAMA_DESTINASI") ?: "Destinasi"

        val gambarDestinasi =
            intent.getIntExtra("GAMBAR_DESTINASI", R.drawable.img_onboarding1)

        val tanggalBerangkat =
            intent.getStringExtra("TANGGAL_BERANGKAT") ?: "-"

        val tanggalKembali =
            intent.getStringExtra("TANGGAL_KEMBALI") ?: "-"

        val jumlahOrang =
            intent.getIntExtra("JUMLAH_ORANG", 1)

        val hargaPerOrang =
            intent.getIntExtra("HARGA_PER_ORANG", 0)

        totalHarga =
            intent.getIntExtra("TOTAL_HARGA", 0)

        paymentStatus =
            intent.getStringExtra("PAYMENT_STATUS") ?: "Menunggu Pembayaran"

        kodeBooking =
            intent.getStringExtra("KODE_BOOKING") ?: "-"

        tokenTiket =
            intent.getStringExtra("TOKEN_TIKET") ?: "-"

        metodePembayaran =
            intent.getStringExtra("METODE_PEMBAYARAN") ?: "Belum Dibayar"

        tvNamaDestinasi.text = namaDestinasi
        imgDestinasi.setImageResource(gambarDestinasi)
        tvTanggalBerangkat.text = tanggalBerangkat
        tvTanggalKembali.text = tanggalKembali
        tvJumlahOrang.text = "$jumlahOrang Orang"
        tvHargaPerOrang.text = formatRupiah(hargaPerOrang)
        tvTotalHarga.text = formatRupiah(totalHarga)
        tvKodeBooking.text = kodeBooking

        updateStatusUI()
    }

    private fun updateStatusUI() {
        if (paymentStatus == "Sudah Dibayar") {
            tvStatusBooking.text = "Sudah Dibayar"
            tvStatusBooking.setTextColor(Color.parseColor("#10B981"))
            imgStatusIcon.setColorFilter(Color.parseColor("#10B981"))

            tvMetodePembayaran.text = metodePembayaran
            tvPaymentNote.text = "Pembayaran berhasil diverifikasi"

            btnBayarSekarang.text = "Pembayaran Selesai"
            btnBayarSekarang.isEnabled = false
        } else {
            tvStatusBooking.text = "Menunggu Pembayaran"
            tvStatusBooking.setTextColor(Color.parseColor("#F59E0B"))
            imgStatusIcon.setColorFilter(Color.parseColor("#F59E0B"))

            tvMetodePembayaran.text = "Belum Dibayar"
            tvPaymentNote.text = "Selesaikan pembayaran untuk mengonfirmasi booking"

            btnBayarSekarang.text = "Bayar Sekarang"
            btnBayarSekarang.isEnabled = true
        }
    }

    private fun setupButton() {
        btnBack.setOnClickListener {
            finish()
        }

        btnBayarSekarang.setOnClickListener {
            if (paymentStatus == "Sudah Dibayar") return@setOnClickListener

            val paymentIntent = Intent(this, PaymentMethodActivity::class.java)
            paymentIntent.putExtras(intent)
            paymentIntent.putExtra("TOTAL_HARGA", totalHarga)
            paymentIntent.putExtra("KODE_BOOKING", kodeBooking)
            paymentIntent.putExtra("TOKEN_TIKET", tokenTiket)



            startActivity(paymentIntent)
        }
    }

    private fun formatRupiah(value: Int): String {
        val formatRupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
        return formatRupiah.format(value).replace(",00", "")
    }
}