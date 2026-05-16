package com.example.travelgo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PaymentConfirmationActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var layoutUploadBukti: LinearLayout
    private lateinit var tvUploadTitle: TextView
    private lateinit var tvUploadDesc: TextView
    private lateinit var tvMetodePembayaran: TextView
    private lateinit var tvNomorTujuan: TextView
    private lateinit var btnKonfirmasi: Button

    private var metodePembayaran = "BCA"
    private var buktiTerupload = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_confirmation)

        btnBack = findViewById(R.id.btnBack)
        layoutUploadBukti = findViewById(R.id.layoutUploadBukti)
        tvUploadTitle = findViewById(R.id.tvUploadTitle)
        tvUploadDesc = findViewById(R.id.tvUploadDesc)
        tvMetodePembayaran = findViewById(R.id.tvMetodePembayaran)
        tvNomorTujuan = findViewById(R.id.tvNomorTujuan)
        btnKonfirmasi = findViewById(R.id.btnKonfirmasi)

        metodePembayaran =
            intent.getStringExtra("METODE_PEMBAYARAN") ?: "BCA"

        setupPaymentInfo()

        btnBack.setOnClickListener {
            finish()
        }

        layoutUploadBukti.setOnClickListener {
            buktiTerupload = true
            tvUploadTitle.text = "bukti_transfer.jpg"
            tvUploadDesc.text = "1.2 MB • berhasil diupload"

            Toast.makeText(
                this,
                "Bukti pembayaran berhasil dipilih",
                Toast.LENGTH_SHORT
            ).show()
        }

        btnKonfirmasi.setOnClickListener {
            if (!buktiTerupload) {
                Toast.makeText(
                    this,
                    "Upload bukti pembayaran dulu ya",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val successIntent =
                    Intent(this, PaymentSuccessActivity::class.java)

                successIntent.putExtras(intent)

                successIntent.putExtra(
                    "METODE_PEMBAYARAN",
                    getNamaMetodePembayaran()
                )

                successIntent.putExtra(
                    "PAYMENT_STATUS",
                    "Sudah Dibayar"
                )

                startActivity(successIntent)
            }
        }
    }

    private fun setupPaymentInfo() {
        when (metodePembayaran) {
            "DANA" -> {
                tvMetodePembayaran.text = "DANA"
                tvNomorTujuan.text = "0812 3456 7890 a.n TravelGo Indonesia"
            }

            else -> {
                tvMetodePembayaran.text = "Transfer Bank BCA"
                tvNomorTujuan.text = "1234567890 a.n TravelGo Indonesia"
            }
        }
    }

    private fun getNamaMetodePembayaran(): String {
        return when (metodePembayaran) {
            "DANA" -> "DANA"
            else -> "Transfer Bank BCA"
        }
    }
}