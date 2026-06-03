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
    private lateinit var imgArtikel: ImageView
    private lateinit var tvJudul: TextView
    private lateinit var tvInfo: TextView
    private lateinit var tvIsi: TextView
    private lateinit var tvKategori: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_artikel)

        // Inisialisasi komponen View XML
        btnBack = findViewById(R.id.btnBack)
        imgArtikel = findViewById(R.id.imgArtikelDetail)
        tvJudul = findViewById(R.id.tvJudulDetail)
        tvInfo = findViewById(R.id.tvInfoDetail)
        tvIsi = findViewById(R.id.tvIsiArtikel)
        tvKategori = findViewById(R.id.tvKategoriDetail)

        btnBack.setOnClickListener {
            finish()
        }

        // 1. Tangkap ID artikel yang dikirim oleh ArtikelAdapter
        val artikelId = intent.getIntExtra("ARTIKEL_ID", 0)

        if (artikelId != 0) {
            // 2. Jika ID valid, ambil data utuh dari API Laragon
            fetchDetailArtikelFromApi(artikelId)
        } else {
            Toast.makeText(this, "Gagal memuat artikel: ID Kosong", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun fetchDetailArtikelFromApi(id: Int) {
        ApiClient.apiService.getArtikelDetail(id)
            .enqueue(object : Callback<ArtikelDetailResponse> {
                override fun onResponse(
                    call: Call<ArtikelDetailResponse>,
                    response: Response<ArtikelDetailResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val artikel = response.body()!!.data

                        // 3. Pasang data dari API ke komponen teks XML kamu
                        tvJudul.text = artikel.title

                        // Gunakan fallback jika 'kategori' di model berbentuk bahasa Indonesia atau berbeda variabel
                        tvKategori.text = "Tips"
                        tvIsi.text = artikel.content

                        // PERBAIKAN MUTLAK:
                        // Menggunakan variabel 'date' (dari created_at) dan menghapus model 'author' yang tidak ada di DB
                        val tanggal = artikel.date?.take(10) ?: "Baru saja"
                        val penulis = "Admin" // Hardcode karena tabel database tidak punya kolom 'author'

                        tvInfo.text = "$tanggal • Oleh $penulis"

                        // 4. Muat gambar online/lokal menggunakan Glide
                        var imageUrl = artikel.image ?: ""
                        if (imageUrl.contains("localhost")) {
                            imageUrl = imageUrl.replace("localhost", "10.0.2.2")
                        }

                        Glide.with(this@DetailArtikelActivity)
                            .load(imageUrl)
                            .placeholder(R.drawable.placeholder_image)
                            .error(R.drawable.placeholder_image)
                            .into(imgArtikel)

                    } else {
                        Toast.makeText(this@DetailArtikelActivity, "Artikel gagal dimuat dari server", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ArtikelDetailResponse>, t: Throwable) {
                    Toast.makeText(this@DetailArtikelActivity, "Koneksi Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}