package com.example.travelgo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {

    private lateinit var imgDetail: ImageView
    private lateinit var tvNama: TextView
    private lateinit var tvLokasi: TextView
    private lateinit var tvHarga: TextView
    private lateinit var tvDeskripsi: TextView
    private lateinit var btnBack: ImageButton
    private lateinit var btnBooking: Button

    private var destinasiId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        initViews()
        setupClickListeners()
        loadDataFromIntent()
    }

    private fun initViews() {
        imgDetail   = findViewById(R.id.imgDetail)
        tvNama      = findViewById(R.id.tvNamaDetail)
        tvLokasi    = findViewById(R.id.tvLokasiDetail)
        tvHarga     = findViewById(R.id.tvHargaDetail)
        tvDeskripsi = findViewById(R.id.tvDeskripsiDetail)
        btnBack     = findViewById(R.id.btnBack)
        btnBooking  = findViewById(R.id.btnBooking)
    }

    private fun loadDataFromIntent() {
        destinasiId      = intent.getIntExtra("DESTINASI_ID", -1)
        val nama         = intent.getStringExtra("NAMA_DESTINASI")
        val lokasi       = intent.getStringExtra("LOKASI_DESTINASI")
        val harga        = intent.getStringExtra("HARGA_DESTINASI")
        val gambar       = intent.getIntExtra("GAMBAR_DESTINASI", 0)
        val imageUrl     = intent.getStringExtra("IMAGE_URL")

        tvNama.text      = nama ?: "-"
        tvLokasi.text    = lokasi ?: "-"
        tvHarga.text     = harga ?: "-"
        tvDeskripsi.text = "Selamat datang di $nama! Nikmati pengalaman luar biasa di $lokasi."

        when {
            !imageUrl.isNullOrEmpty() ->
                Glide.with(this).load(imageUrl).placeholder(R.drawable.img_onboarding1).into(imgDetail)
            gambar != 0 ->
                imgDetail.setImageResource(gambar)
        }

        // Load detail dari API kalau ada ID
        if (destinasiId != -1) loadDetailFromApi(destinasiId)
    }

    private fun setupClickListeners() {
        btnBack.setOnClickListener { finish() }

        btnBooking.setOnClickListener {
            val token = SessionManager.getToken(this)
            if (token == null) {
                Toast.makeText(this, "Silakan login terlebih dahulu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            Intent(this, BookingActivity::class.java).also {
                it.putExtra("DESTINASI_ID",    destinasiId)
                it.putExtra("NAMA_DESTINASI",  tvNama.text.toString())
                it.putExtra("LOKASI_DESTINASI", tvLokasi.text.toString())
                it.putExtra("HARGA_DESTINASI", tvHarga.text.toString())
                startActivity(it)
            }
        }
    }

    private fun loadDetailFromApi(id: Int) {
        ApiClient.apiService.getDestinasiById(id)
            .enqueue(object : Callback<DestinasiDetailResponse> {
                override fun onResponse(call: Call<DestinasiDetailResponse>, response: Response<DestinasiDetailResponse>) {
                    if (response.isSuccessful) {
                        val d = response.body()?.data ?: return
                        tvNama.text    = d.name
                        tvLokasi.text  = d.location
                        tvHarga.text   = "Rp ${String.format("%,.0f", d.price)}"
                        tvDeskripsi.text = d.description ?: "Tidak ada deskripsi."

                        if (!d.image.isNullOrEmpty()) {
                            Glide.with(this@DetailActivity)
                                .load(d.image)
                                .placeholder(R.drawable.img_onboarding1)
                                .into(imgDetail)
                        }
                    }
                }
                override fun onFailure(call: Call<DestinasiDetailResponse>, t: Throwable) {
                    // Data dari intent tetap tampil
                }
            })
    }
}