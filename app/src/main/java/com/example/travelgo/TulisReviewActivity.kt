package com.example.travelgo

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class TulisReviewActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var ratingBar: RatingBar
    private lateinit var tvRatingValue: TextView
    private lateinit var etKomentar: EditText
    private lateinit var btnKirimReview: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tulis_review)

        btnBack = findViewById(R.id.btnBack)
        ratingBar = findViewById(R.id.ratingBar)
        tvRatingValue = findViewById(R.id.tvRatingValue)
        etKomentar = findViewById(R.id.etKomentar)
        btnKirimReview = findViewById(R.id.btnKirimReview)

        btnBack.setOnClickListener {
            finish()
        }

        ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            tvRatingValue.text = "${rating.toInt()} Bintang"
        }

        btnKirimReview.setOnClickListener {

            val rating = ratingBar.rating.toInt()
            val komentar = etKomentar.text.toString().trim()

            if (rating == 0) {
                Toast.makeText(
                    this,
                    "Pilih rating terlebih dahulu",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (komentar.isEmpty()) {
                etKomentar.error = "Komentar wajib diisi"
                return@setOnClickListener
            }

            Toast.makeText(
                this,
                "Review siap dikirim ke API",
                Toast.LENGTH_SHORT
            ).show()

            finish()
        }
    }
}