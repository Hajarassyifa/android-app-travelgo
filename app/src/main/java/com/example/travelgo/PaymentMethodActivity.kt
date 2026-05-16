package com.example.travelgo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PaymentMethodActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var btnLanjutkan: Button

    private lateinit var radioBca: RadioButton
    private lateinit var radioDana: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_method)

        btnBack = findViewById(R.id.btnBack)
        btnLanjutkan = findViewById(R.id.btnLanjutkan)

        radioBca = findViewById(R.id.radioBca)
        radioDana = findViewById(R.id.radioDana)

        btnBack.setOnClickListener {
            finish()
        }

        btnLanjutkan.setOnClickListener {

            var metodePembayaran = ""

            when {
                radioBca.isChecked -> {
                    metodePembayaran = "BCA"
                }

                radioDana.isChecked -> {
                    metodePembayaran = "DANA"
                }
            }

            if (metodePembayaran.isEmpty()) {

                Toast.makeText(
                    this,
                    "Pilih metode pembayaran dulu",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            val paymentDetailIntent =
                Intent(this, PaymentDetailActivity::class.java)

            paymentDetailIntent.putExtras(intent)

            paymentDetailIntent.putExtra(
                "METODE_PEMBAYARAN",
                metodePembayaran
            )

            startActivity(paymentDetailIntent)
        }
    }
}