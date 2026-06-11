package com.example.travelgo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ReviewActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var btnTulisUlasan: Button
    private lateinit var rvReview: RecyclerView
    private lateinit var tvAverageRating: TextView
    private lateinit var tvTotalReview: TextView

    private val listReview = ArrayList<Review>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)

        btnBack = findViewById(R.id.btnBack)
        btnTulisUlasan = findViewById(R.id.btnTulisUlasan)
        rvReview = findViewById(R.id.rvReview)
        tvAverageRating = findViewById(R.id.tvAverageRating)
        tvTotalReview = findViewById(R.id.tvTotalReview)

        prepareReview()
        setupReview()

        btnBack.setOnClickListener {
            finish()
        }

        btnTulisUlasan.setOnClickListener {
            startActivity(
                Intent(this, TulisReviewActivity::class.java)
            )
        }
    }

    private fun prepareReview() {
        listReview.clear()

        listReview.add(
            Review(
                1,
                "Syifa Ramadhani",
                5,
                "Paket wisatanya seru banget! Pemandangannya indah dan proses booking mudah.",
                "2 hari lalu"
            )
        )

        listReview.add(
            Review(
                2,
                "Fajar Maulana",
                4,
                "Pengalaman menyenangkan, destinasi sesuai dengan deskripsi.",
                "5 hari lalu"
            )
        )

        listReview.add(
            Review(
                3,
                "Nadya Putri",
                5,
                "Sangat recommended! Tiket digitalnya juga mudah digunakan.",
                "1 minggu lalu"
            )
        )
    }

    private fun setupReview() {
        rvReview.layoutManager = LinearLayoutManager(this)
        rvReview.adapter = ReviewAdapter(listReview)

        val average =
            if (listReview.isNotEmpty()) {
                listReview.map { it.rating }.average()
            } else {
                0.0
            }

        tvAverageRating.text =
            String.format("%.1f", average)

        tvTotalReview.text =
            "${listReview.size} ulasan"
    }
}