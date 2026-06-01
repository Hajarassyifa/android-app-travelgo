package com.example.travelgo

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
    private lateinit var tvJumlahTiket: TextView
    private lateinit var tvHargaPerTiket: TextView
    private lateinit var tvTotalHarga: TextView
    private lateinit var tvMetodePembayaran: TextView
    private lateinit var tvPaymentNote: TextView
    private lateinit var tvKodeBooking: TextView
    private lateinit var tvNamaPemesan: TextView
    private lateinit var tvEmailPemesan: TextView
    private lateinit var tvNoHpPemesan: TextView

    private var bookingId: Int = 0
    private var totalHarga: Double = 0.0
    private var paymentStatus: String = "unpaid"
    private var kodeBooking: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_booking)

        initViews()
        setupButton()
        getDataFromApi()
    }

    private fun initViews() {
        btnBack            = findViewById(R.id.btnBack)
        btnBayarSekarang   = findViewById(R.id.btnBayarSekarang)
        imgDestinasi       = findViewById(R.id.imgDestinasi)
        imgStatusIcon      = findViewById(R.id.imgStatusIcon)
        tvStatusBooking    = findViewById(R.id.tvStatusBooking)
        tvNamaDestinasi    = findViewById(R.id.tvNamaDestinasi)
        tvTanggalBerangkat = findViewById(R.id.tvTanggalBerangkat)
        tvJumlahTiket      = findViewById(R.id.tvJumlahOrang)
        tvHargaPerTiket    = findViewById(R.id.tvHargaPerOrang)
        tvTotalHarga       = findViewById(R.id.tvTotalHarga)
        tvMetodePembayaran = findViewById(R.id.tvMetodePembayaran)
        tvPaymentNote      = findViewById(R.id.tvPaymentNote)
        tvKodeBooking      = findViewById(R.id.tvKodeBooking)
        tvNamaPemesan      = findViewById(R.id.tvNamaPemesan)
        tvEmailPemesan     = findViewById(R.id.tvEmailPemesan)
        tvNoHpPemesan      = findViewById(R.id.tvNoHpPemesan)
    }

    private fun getDataFromApi() {
        bookingId = intent.getIntExtra("BOOKING_ID", 0)

        if (bookingId == 0) {
            Toast.makeText(this, "Data booking tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val token = SessionManager.getToken(this)
        if (token == null) {
            Toast.makeText(this, "Silakan login terlebih dahulu", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        ApiClient.apiService.getBookingDetail("Bearer $token", bookingId)
            .enqueue(object : Callback<BookingDetailResponse> {
                override fun onResponse(call: Call<BookingDetailResponse>, response: Response<BookingDetailResponse>) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body?.status == true) {
                            displayBookingData(body.data)
                        } else {
                            Toast.makeText(this@DetailBookingActivity, body?.message ?: "Gagal", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    } else {
                        Toast.makeText(this@DetailBookingActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }

                override fun onFailure(call: Call<BookingDetailResponse>, t: Throwable) {
                    Toast.makeText(this@DetailBookingActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    finish()
                }
            })
    }

    private fun displayBookingData(booking: BookingDetail) {
        totalHarga    = booking.total_harga
        paymentStatus = booking.payment_status
        kodeBooking   = booking.booking_code

        tvNamaPemesan.text      = booking.customer_name
        tvEmailPemesan.text     = booking.customer_email
        tvNoHpPemesan.text      = booking.customer_phone
        tvNamaDestinasi.text    = booking.destinasi?.name ?: "-"
        tvTanggalBerangkat.text = booking.tanggal_berangkat
        tvJumlahTiket.text      = "${booking.jumlah_tiket} Tiket"
        tvHargaPerTiket.text    = formatRupiah(booking.total_harga / booking.jumlah_tiket)
        tvTotalHarga.text       = formatRupiah(booking.total_harga)
        tvKodeBooking.text      = booking.booking_code

        if (!booking.destinasi?.image.isNullOrEmpty()) {
            Glide.with(this)
                .load(booking.destinasi?.image)
                .placeholder(android.R.color.darker_gray)
                .into(imgDestinasi)
        }

        updateStatusUI()
    }

    private fun updateStatusUI() {
        when (paymentStatus) {
            "paid" -> {
                tvStatusBooking.text = "Sudah Dibayar"
                tvStatusBooking.setTextColor(Color.parseColor("#10B981"))
                imgStatusIcon.setColorFilter(Color.parseColor("#10B981"))
                tvMetodePembayaran.text = "Transfer Bank / QRIS"
                tvPaymentNote.text = "Pembayaran berhasil diverifikasi"
                btnBayarSekarang.text = "Pembayaran Selesai"
                btnBayarSekarang.isEnabled = false
            }
            "pending_verification" -> {
                tvStatusBooking.text = "Verifikasi Pembayaran"
                tvStatusBooking.setTextColor(Color.parseColor("#F59E0B"))
                imgStatusIcon.setColorFilter(Color.parseColor("#F59E0B"))
                tvMetodePembayaran.text = "Menunggu Verifikasi"
                tvPaymentNote.text = "Bukti pembayaran sedang diverifikasi"
                btnBayarSekarang.text = "Menunggu Verifikasi"
                btnBayarSekarang.isEnabled = false
            }
            else -> {
                tvStatusBooking.text = "Menunggu Pembayaran"
                tvStatusBooking.setTextColor(Color.parseColor("#F59E0B"))
                imgStatusIcon.setColorFilter(Color.parseColor("#F59E0B"))
                tvMetodePembayaran.text = "Belum Dibayar"
                tvPaymentNote.text = "Selesaikan pembayaran untuk mengonfirmasi booking"
                btnBayarSekarang.text = "Bayar Sekarang"
                btnBayarSekarang.isEnabled = true
            }
        }
    }

    private fun setupButton() {
        btnBack.setOnClickListener { finish() }

        btnBayarSekarang.setOnClickListener {
            if (paymentStatus != "unpaid") return@setOnClickListener
            val intent = Intent(this, PaymentMethodActivity::class.java).apply {
                putExtra("BOOKING_ID",   bookingId)
                putExtra("TOTAL_HARGA",  totalHarga)
                putExtra("KODE_BOOKING", kodeBooking)
            }
            startActivity(intent)
        }
    }

    private fun formatRupiah(value: Double): String {
        val format = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("id-ID"))
        return format.format(value).replace(",00", "")
    }
}