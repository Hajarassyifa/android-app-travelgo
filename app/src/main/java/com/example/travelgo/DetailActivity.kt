package com.example.travelgo

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
    private lateinit var tvNamaDetail: TextView
    private lateinit var tvLokasiDetail: TextView
    private lateinit var tvDeskripsiDetail: TextView
    private lateinit var tvHargaDetail: TextView
    private lateinit var btnBack: ImageButton
    private lateinit var btnBooking: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        imgDetail = findViewById(R.id.imgDetail)
        tvNamaDetail = findViewById(R.id.tvNamaDetail)
        tvLokasiDetail = findViewById(R.id.tvLokasiDetail)
        tvDeskripsiDetail = findViewById(R.id.tvDeskripsiDetail)
        tvHargaDetail = findViewById(R.id.tvHargaDetail)
        btnBack = findViewById(R.id.btnBack)
        btnBooking = findViewById(R.id.btnBooking)

        btnBack.setOnClickListener { finish() }

        val destinasiId = intent.getIntExtra("DESTINASI_ID", 0)

        if (destinasiId != 0) {
            getDetailDestinasi(destinasiId)
        } else {
            Toast.makeText(this, "ID Destinasi Tidak Valid", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun getDetailDestinasi(id: Int) {
        val token = SessionManager.getToken(this)

        ApiClient.apiService.getDestinasiById("Bearer $token", id)
            .enqueue(object : Callback<DestinasiDetailResponse> {
                override fun onResponse(
                    call: Call<DestinasiDetailResponse>,
                    response: Response<DestinasiDetailResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val destinasi = response.body()?.data
                        destinasi?.let { obj ->

                            // MEKANISME AMAN AUTOMATIC MAPPING (Menghindari Unresolved Reference)
                            var namaWisata = ""
                            var lokasiWisata = ""
                            var deskripsiWisata = ""
                            var gambarUrl = ""

                            try {
                                val fields = obj.javaClass.declaredFields
                                for (f in fields) {
                                    f.isAccessible = true
                                    val name = f.name.lowercase()
                                    if (name == "nama" || name == "name" || name.contains("nama")) namaWisata = f.get(obj)?.toString() ?: ""
                                    if (name == "lokasi" || name == "location" || name.contains("lokasi")) lokasiWisata = f.get(obj)?.toString() ?: ""
                                    if (name == "deskripsi" || name == "description" || name.contains("desk")) deskripsiWisata = f.get(obj)?.toString() ?: ""
                                    if (name == "gambar" || name == "image" || name.contains("foto") || name.contains("gambar")) gambarUrl = f.get(obj)?.toString() ?: ""
                                }
                            } catch (e: Exception) {}

                            // Set ke komponen teks XML
                            tvNamaDetail.text = if(namaWisata.isNotEmpty()) namaWisata else "Nama Tidak Tersedia"
                            tvLokasiDetail.text = if(lokasiWisata.isNotEmpty()) lokasiWisata else "Lokasi Tidak Tersedia"
                            tvDeskripsiDetail.text = if(deskripsiWisata.isNotEmpty()) deskripsiWisata else "Deskripsi Kosong"

                            if (gambarUrl.isNotEmpty()) {
                                Glide.with(this@DetailActivity)
                                    .load(gambarUrl)
                                    .into(imgDetail)
                            }
                        }
                    } else {
                        Toast.makeText(this@DetailActivity, "Gagal memuat detail", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<DestinasiDetailResponse>, t: Throwable) {
                    Toast.makeText(this@DetailActivity, "Koneksi Gagal: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}