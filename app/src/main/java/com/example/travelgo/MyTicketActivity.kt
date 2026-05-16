package com.example.travelgo

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.text.NumberFormat
import java.util.Locale

class MyTicketActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var imgDestinasi: ImageView
    private lateinit var tvNamaDestinasi: TextView
    private lateinit var tvLokasi: TextView
    private lateinit var tvKodeBooking: TextView
    private lateinit var tvStatusPembayaran: TextView
    private lateinit var tvBarcode: TextView
    private lateinit var tvToken: TextView

    private var kodeBooking = ""
    private var tokenTiket = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_ticket)

        btnBack = findViewById(R.id.btnBack)
        imgDestinasi = findViewById(R.id.imgDestinasi)
        tvNamaDestinasi = findViewById(R.id.tvNamaDestinasi)
        tvLokasi = findViewById(R.id.tvLokasi)
        tvKodeBooking = findViewById(R.id.tvKodeBooking)
        tvStatusPembayaran = findViewById(R.id.tvStatusPembayaran)
        tvBarcode = findViewById(R.id.tvBarcode)
        tvToken = findViewById(R.id.tvToken)

        btnBack.setOnClickListener {
            finish()
        }

        val nama = intent.getStringExtra("NAMA_DESTINASI") ?: "Destinasi"
        val lokasi = intent.getStringExtra("LOKASI_DESTINASI") ?: "Indonesia"
        val gambar = intent.getIntExtra("GAMBAR_DESTINASI", R.drawable.img_onboarding1)
        val tanggalBerangkat = intent.getStringExtra("TANGGAL_BERANGKAT") ?: "-"
        val tanggalKembali = intent.getStringExtra("TANGGAL_KEMBALI") ?: "-"
        val jumlahOrang = intent.getIntExtra("JUMLAH_ORANG", 1)
        val totalHarga = intent.getIntExtra("TOTAL_HARGA", 0)
        val status = intent.getStringExtra("PAYMENT_STATUS") ?: "Sudah Dibayar"

        kodeBooking = intent.getStringExtra("KODE_BOOKING") ?: buatKodeBooking()
        tokenTiket = intent.getStringExtra("TOKEN_TIKET") ?: buatTokenTiket()

        tvNamaDestinasi.text = nama
        tvLokasi.text = lokasi
        imgDestinasi.setImageResource(gambar)
        tvKodeBooking.text = kodeBooking
        tvStatusPembayaran.text = status

        tvBarcode.text = generateBarcodeText(kodeBooking)
        tvToken.text = tokenTiket

        setupInfoBox(
            R.id.boxBerangkat,
            "Tanggal Berangkat",
            tanggalBerangkat,
            R.drawable.ic_calendar,
            R.drawable.bg_icon_blue
        )

        setupInfoBox(
            R.id.boxKembali,
            "Tanggal Kembali",
            tanggalKembali,
            R.drawable.ic_calendar,
            R.drawable.bg_icon_orange
        )

        setupInfoBox(
            R.id.boxOrang,
            "Jumlah Orang",
            "$jumlahOrang Orang",
            R.drawable.ic_people,
            R.drawable.bg_icon_green
        )

        setupInfoBox(
            R.id.boxTotal,
            "Total Harga",
            formatRupiah(totalHarga),
            R.drawable.ic_payment,
            R.drawable.bg_icon_purple
        )
    }

    private fun setupInfoBox(
        includeId: Int,
        label: String,
        value: String,
        icon: Int,
        background: Int
    ) {
        val box = findViewById<LinearLayout>(includeId)

        val iconBox = box.findViewById<LinearLayout>(R.id.iconBox)
        val imgIcon = box.findViewById<ImageView>(R.id.imgIcon)
        val tvLabel = box.findViewById<TextView>(R.id.tvLabel)
        val tvValue = box.findViewById<TextView>(R.id.tvValue)

        iconBox.background = ContextCompat.getDrawable(this, background)
        imgIcon.setImageResource(icon)
        tvLabel.text = label
        tvValue.text = value
    }

    private fun buatKodeBooking(): String {
        val random = (100000000..999999999).random()
        return "TRV$random"
    }

    private fun buatTokenTiket(): String {
        val random = (100000..999999).random()
        return "TKT-$random"
    }

    private fun generateBarcodeText(kode: String): String {
        val builder = StringBuilder()

        kode.forEachIndexed { index, char ->
            val repeat = if ((char.code + index) % 2 == 0) 2 else 4
            builder.append("|".repeat(repeat)).append(" ")
        }

        return builder.toString()
    }

    private fun formatRupiah(value: Int): String {
        val formatRupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
        return formatRupiah.format(value).replace(",00", "")
    }
}