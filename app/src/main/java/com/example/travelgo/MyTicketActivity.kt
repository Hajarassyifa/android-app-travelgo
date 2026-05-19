package com.example.travelgo

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.text.NumberFormat
import java.util.Locale
import android.content.Intent
import android.view.View
import androidx.cardview.widget.CardView

class MyTicketActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var imgDestinasi: ImageView
    private lateinit var tvNamaDestinasi: TextView
    private lateinit var tvLokasi: TextView
    private lateinit var tvKodeBooking: TextView
    private lateinit var tvStatusPembayaran: TextView
    private lateinit var tvBarcode: TextView
    private lateinit var tvToken: TextView
    private lateinit var tvNamaPemesan: TextView
    private lateinit var layoutEmptyTicket: LinearLayout
    private lateinit var cardTicket: CardView

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
        tvNamaPemesan = findViewById(R.id.tvNamaPemesan)
        layoutEmptyTicket = findViewById(R.id.layoutEmptyTicket)
        cardTicket = findViewById(R.id.cardTicket)

        btnBack.setOnClickListener {

            val intent =
                Intent(this, MainActivity::class.java)

            intent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_NEW_TASK

            startActivity(intent)
            finish()
        }
        onBackPressedDispatcher.addCallback(this, object : androidx.activity.OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@MyTicketActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        })

        val pref =
            getSharedPreferences("LAST_TICKET", MODE_PRIVATE)
        val namaPemesan =
            pref.getString("NAMA_PEMESAN", "Traveler") ?: "Traveler"

        tvNamaPemesan.text = namaPemesan

        val adaTiket =
            pref.getBoolean("ADA_TIKET", false)

        if (!adaTiket) {

            layoutEmptyTicket.visibility = View.VISIBLE
            cardTicket.visibility = View.GONE

            return
        }
        layoutEmptyTicket.visibility = View.GONE
        cardTicket.visibility = View.VISIBLE


        val nama =
            pref.getString("NAMA_DESTINASI", "-") ?: "-"

        val lokasi =
            pref.getString("LOKASI_DESTINASI", "-") ?: "-"

        val gambar =
            pref.getInt(
                "GAMBAR_DESTINASI",
                R.drawable.img_onboarding1
            )

        val tanggalBerangkat =
            pref.getString("TANGGAL_BERANGKAT", "-") ?: "-"

        val tanggalKembali =
            pref.getString("TANGGAL_KEMBALI", "-") ?: "-"

        val jumlahOrang =
            pref.getInt("JUMLAH_ORANG", 1)

        val totalHarga =
            pref.getInt("TOTAL_HARGA", 0)

        val status =
            pref.getString("PAYMENT_STATUS", "-") ?: "-"

        kodeBooking =
            pref.getString("KODE_BOOKING", "-") ?: "-"

        tokenTiket =
            pref.getString("TOKEN_TIKET", "-") ?: "-"
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