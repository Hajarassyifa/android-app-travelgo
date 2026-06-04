package com.example.travelgo

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailArtikelActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var imgArtikelDetail: ImageView
    private lateinit var tvKategoriDetail: TextView
    private lateinit var tvJudulDetail: TextView
    private lateinit var tvInfoDetail: TextView
    private lateinit var tvIsiArtikel: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_artikel)

        btnBack = findViewById(R.id.btnBack)
        imgArtikelDetail = findViewById(R.id.imgArtikelDetail)
        tvKategoriDetail = findViewById(R.id.tvKategoriDetail)
        tvJudulDetail = findViewById(R.id.tvJudulDetail)
        tvInfoDetail = findViewById(R.id.tvInfoDetail)
        tvIsiArtikel = findViewById(R.id.tvIsiArtikel)

        btnBack.setOnClickListener { finish() }

        val artikelId = intent.getIntExtra("ARTIKEL_ID", 0)
        if (artikelId != 0) {
            getDetailArtikel(artikelId)
        } else {
            Toast.makeText(this, "ID Artikel Tidak Valid", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun getDetailArtikel(id: Int) {
        val token = SessionManager.getToken(this)

        ApiClient.apiService.getArtikelDetail("Bearer $token", id)
            .enqueue(object : Callback<ArtikelDetailResponse> {
                override fun onResponse(
                    call: Call<ArtikelDetailResponse>,
                    response: Response<ArtikelDetailResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val artikel = response.body()?.data
                        artikel?.let { obj ->

                            // MEKANISME AMAN AUTOMATIC MAPPING
                            var judulArtikel = ""
                            var isiArtikel = ""
                            var gambarUrl = ""

                            try {
                                val fields = obj.javaClass.declaredFields
                                for (f in fields) {
                                    f.isAccessible = true
                                    val name = f.name.lowercase()
                                    if (name == "judul" || name == "title" || name.contains("judul")) judulArtikel = f.get(obj)?.toString() ?: ""
                                    if (name == "konten" || name == "content" || name.contains("konten") || name.contains("isi")) isiArtikel = f.get(obj)?.toString() ?: ""
                                    if (name == "gambar" || name == "image" || name.contains("gambar") || name.contains("foto")) gambarUrl = f.get(obj)?.toString() ?: ""
                                }
                            } catch (e: Exception) {}

                            tvJudulDetail.text = if(judulArtikel.isNotEmpty()) judulArtikel else "Judul Kosong"
                            tvIsiArtikel.text = if(isiArtikel.isNotEmpty()) isiArtikel else "Isi Kosong"

                            if (gambarUrl.isNotEmpty()) {
                                Glide.with(this@DetailArtikelActivity)
                                    .load(gambarUrl)
                                    .into(imgArtikelDetail)
                            }
                        }
                    } else {
                        Toast.makeText(this@DetailArtikelActivity, "Gagal memuat artikel", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ArtikelDetailResponse>, t: Throwable) {
                    Toast.makeText(this@DetailArtikelActivity, "Koneksi Gagal: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}