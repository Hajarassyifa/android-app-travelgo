package com.example.travelgo

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.NumberFormat
import java.util.Calendar
import java.util.Locale

class BookingActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView

    private lateinit var tvTanggalBerangkat: TextView
    private lateinit var tvTanggalKembali: TextView

    private lateinit var tvJumlahOrang: TextView
    private lateinit var tvJumlahStepper: TextView

    private lateinit var btnMinus: ImageView
    private lateinit var btnPlus: ImageView

    private lateinit var tvTotalHarga: TextView
    private lateinit var btnLanjutPembayaran: Button

    private var namaDestinasi = ""
    private var lokasiDestinasi = ""
    private var hargaText = ""
    private var gambarDestinasi = 0

    private var hargaPerOrang = 0
    private var jumlahOrang = 1
    private var totalHarga = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)

        initViews()
        getIntentData()

        hargaPerOrang = ambilAngkaHarga(hargaText)

        btnBack.setOnClickListener {
            finish()
        }

        setupTanggal()
        setupStepper()

        updateJumlahOrangUI()
        updateTotalHarga()

        btnLanjutPembayaran.setOnClickListener {

            if (tvTanggalBerangkat.text.toString() == "Pilih tanggal") {

                Toast.makeText(
                    this,
                    "Pilih tanggal berangkat dulu",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            if (tvTanggalKembali.text.toString() == "Pilih tanggal") {

                Toast.makeText(
                    this,
                    "Pilih tanggal kembali dulu",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            val kodeBooking = generateKodeBooking()
            val tokenTiket = generateTicketToken()

            val detailIntent =
                Intent(this, DetailBookingActivity::class.java)

            detailIntent.putExtra(
                "NAMA_DESTINASI",
                namaDestinasi
            )

            detailIntent.putExtra(
                "LOKASI_DESTINASI",
                lokasiDestinasi
            )

            detailIntent.putExtra(
                "GAMBAR_DESTINASI",
                gambarDestinasi
            )

            detailIntent.putExtra(
                "TANGGAL_BERANGKAT",
                tvTanggalBerangkat.text.toString()
            )

            detailIntent.putExtra(
                "TANGGAL_KEMBALI",
                tvTanggalKembali.text.toString()
            )

            detailIntent.putExtra(
                "JUMLAH_ORANG",
                jumlahOrang
            )

            detailIntent.putExtra(
                "HARGA_PER_ORANG",
                hargaPerOrang
            )

            detailIntent.putExtra(
                "TOTAL_HARGA",
                totalHarga
            )

            detailIntent.putExtra(
                "PAYMENT_STATUS",
                "Menunggu Pembayaran"
            )

            detailIntent.putExtra(
                "KODE_BOOKING",
                kodeBooking
            )

            detailIntent.putExtra(
                "TOKEN_TIKET",
                tokenTiket
            )

            startActivity(detailIntent)
        }
    }

    private fun initViews() {

        btnBack = findViewById(R.id.btnBack)

        tvTanggalBerangkat =
            findViewById(R.id.tvTanggalBerangkat)

        tvTanggalKembali =
            findViewById(R.id.tvTanggalKembali)

        tvJumlahOrang =
            findViewById(R.id.tvJumlahOrang)

        tvJumlahStepper =
            findViewById(R.id.tvJumlahStepper)

        btnMinus =
            findViewById(R.id.btnMinus)

        btnPlus =
            findViewById(R.id.btnPlus)

        tvTotalHarga =
            findViewById(R.id.tvTotalHarga)

        btnLanjutPembayaran =
            findViewById(R.id.btnLanjutPembayaran)
    }

    private fun getIntentData() {

        namaDestinasi =
            intent.getStringExtra("NAMA_DESTINASI")
                ?: "Destinasi"

        lokasiDestinasi =
            intent.getStringExtra("LOKASI_DESTINASI")
                ?: "Indonesia"

        hargaText =
            intent.getStringExtra("HARGA_DESTINASI")
                ?: "Rp 0"

        gambarDestinasi =
            intent.getIntExtra(
                "GAMBAR_DESTINASI",
                R.drawable.img_onboarding1
            )
    }

    private fun setupTanggal() {

        tvTanggalBerangkat.setOnClickListener {
            showDatePicker(tvTanggalBerangkat)
        }

        tvTanggalKembali.setOnClickListener {
            showDatePicker(tvTanggalKembali)
        }
    }

    private fun setupStepper() {

        btnMinus.setOnClickListener {

            if (jumlahOrang > 1) {

                jumlahOrang--

                updateJumlahOrangUI()
                updateTotalHarga()
            }
        }

        btnPlus.setOnClickListener {

            if (jumlahOrang < 10) {

                jumlahOrang++

                updateJumlahOrangUI()
                updateTotalHarga()

            } else {

                Toast.makeText(
                    this,
                    "Maksimal 10 orang",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun updateJumlahOrangUI() {

        tvJumlahOrang.text =
            "$jumlahOrang Orang"

        tvJumlahStepper.text =
            jumlahOrang.toString()
    }

    private fun showDatePicker(targetTextView: TextView) {

        val calendar = Calendar.getInstance()

        val datePicker = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->

                val tanggal =
                    "$dayOfMonth ${getNamaBulan(month)} $year"

                targetTextView.text = tanggal
            },

            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePicker.show()
    }

    private fun getNamaBulan(month: Int): String {

        val bulan = arrayOf(
            "Januari",
            "Februari",
            "Maret",
            "April",
            "Mei",
            "Juni",
            "Juli",
            "Agustus",
            "September",
            "Oktober",
            "November",
            "Desember"
        )

        return bulan[month]
    }

    private fun updateTotalHarga() {

        totalHarga =
            hargaPerOrang * jumlahOrang

        tvTotalHarga.text =
            formatRupiah(totalHarga)
    }

    private fun ambilAngkaHarga(harga: String): Int {

        val angkaOnly =
            harga.replace("[^0-9]".toRegex(), "")

        return angkaOnly.toIntOrNull() ?: 0
    }

    private fun formatRupiah(value: Int): String {

        val formatRupiah =
            NumberFormat.getCurrencyInstance(
                Locale("in", "ID")
            )

        return formatRupiah.format(value)
            .replace(",00", "")
    }

    private fun generateKodeBooking(): String {

        val time =
            System.currentTimeMillis()
                .toString()
                .takeLast(6)

        val random =
            (100..999).random()

        return "TRV$time$random"
    }

    private fun generateTicketToken(): String {

        val random =
            (100000..999999).random()

        return "TKT-$random"
    }
}