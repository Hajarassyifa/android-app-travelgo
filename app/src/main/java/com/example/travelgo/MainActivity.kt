package com.example.travelgo

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var mainNestedScrollView: NestedScrollView
    private lateinit var tvSapaan: TextView
    private lateinit var btnNotification: ImageView
    private lateinit var etSearch: EditText
    private lateinit var bottomNavigation: BottomNavigationView

    private lateinit var bannerViewPager: ViewPager2
    private lateinit var dotsLayout: LinearLayout

    private lateinit var btnKategoriSemua: LinearLayout
    private lateinit var btnKategoriPantai: LinearLayout
    private lateinit var btnKategoriGunung: LinearLayout
    private lateinit var btnKategoriSejarah: LinearLayout
    private lateinit var btnKategoriKuliner: LinearLayout

    private lateinit var rvDestinasi: RecyclerView
    private lateinit var destinasiAdapter: DestinasiAdapter
    private val destinasiList = mutableListOf<Destinasi>()
    private lateinit var tvLihatSemuaDestinasi: TextView

    private lateinit var rvArtikel: RecyclerView
    private lateinit var artikelAdapter: ArtikelAdapter
    private val artikelList = mutableListOf<Artikel>()
    private lateinit var tvLihatSemuaArtikel: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainNestedScrollView = findViewById(R.id.mainNestedScrollView)
        tvSapaan = findViewById(R.id.tvSapaan)
        btnNotification = findViewById(R.id.btnNotification)
        etSearch = findViewById(R.id.etSearch)
        bottomNavigation = findViewById(R.id.bottomNavigation)

        btnKategoriSemua = findViewById(R.id.catAll)
        btnKategoriPantai = findViewById(R.id.catBeach)
        btnKategoriGunung = findViewById(R.id.catMountain)
        btnKategoriSejarah = findViewById(R.id.catHistory)
        btnKategoriKuliner = findViewById(R.id.catFood)

        bannerViewPager = findViewById(R.id.bannerViewPager)
        dotsLayout = findViewById(R.id.dotsLayout)
        tvLihatSemuaDestinasi = findViewById(R.id.tvLihatSemuaDestinasi)
        tvLihatSemuaArtikel = findViewById(R.id.tvLihatSemuaArtikel)

        rvDestinasi = findViewById(R.id.rvDestinasi)
        rvDestinasi.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        destinasiAdapter = DestinasiAdapter(destinasiList)
        rvDestinasi.adapter = destinasiAdapter

        rvArtikel = findViewById(R.id.rvArtikel)
        rvArtikel.layoutManager = LinearLayoutManager(this)
        artikelAdapter = ArtikelAdapter(artikelList)
        rvArtikel.adapter = artikelAdapter

        setupClickListeners()

        getDestinasiData()
        getArtikelData()
    }

    private fun setupClickListeners() {
        tvLihatSemuaDestinasi.setOnClickListener {
            mainNestedScrollView.smoothScrollTo(0, rvDestinasi.top)
        }

        tvLihatSemuaArtikel.setOnClickListener {
            val intent = Intent(this, ArtikelActivity::class.java)
            startActivity(intent)
        }

        btnKategoriSemua.setOnClickListener { filterKategori("Semua") }
        btnKategoriPantai.setOnClickListener { filterKategori("Pantai") }
        btnKategoriGunung.setOnClickListener { filterKategori("Gunung") }
        btnKategoriSejarah.setOnClickListener { filterKategori("Sejarah") }
        btnKategoriKuliner.setOnClickListener { filterKategori("Kuliner") }

        // PERBAIKAN TOTAL NAVIGASI BAWAH:
        // Deteksi dinamis menggunakan posisi indeks menu atau judul item agar bebas hambatan eror XML ID
        bottomNavigation.setOnItemSelectedListener { item ->
            val title = item.title.toString().lowercase()
            val itemId = item.itemId

            // Mengambil ID item pertama dan item terakhir secara aman langsung dari runtime menu
            val firstItemId = if (bottomNavigation.menu.size() > 0) bottomNavigation.menu.getItem(0).itemId else -1
            val lastItemId = if (bottomNavigation.menu.size() > 0) bottomNavigation.menu.getItem(bottomNavigation.menu.size() - 1).itemId else -1

            when {
                // 1. Menu Beranda / Home (Bisa lewat ID ke-0, judul, atau penamaan ID umum)
                itemId == firstItemId || title.contains("beranda") || title.contains("home") -> {
                    // Berada di halaman utama, pertahankan posisi aktif menu
                    true
                }

                // 2. Menu Tiket / My Ticket
                title.contains("tiket") || title.contains("ticket") -> {
                    startActivity(Intent(this, MyTicketActivity::class.java))
                    overridePendingTransition(0, 0)
                    false // Return false agar highlight tidak tertinggal sebelum aktivitas berpindah
                }

                // 3. Menu Profil / Profile (Bisa lewat ID paling akhir atau teks judul)
                itemId == lastItemId || title.contains("profil") || title.contains("profile") -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    overridePendingTransition(0, 0)
                    false
                }

                // 4. Pengaman untuk item menu opsional lainnya (misal: History/Bookmark)
                else -> {
                    true
                }
            }
        }
    }

    private fun filterKategori(kategoriName: String) {
        Toast.makeText(this, "Menampilkan kategori: $kategoriName", Toast.LENGTH_SHORT).show()
    }

    private fun getDestinasiData() {
        ApiClient.apiService.getDestinasis(1, null, null)
            .enqueue(object : Callback<DestinasiResponse> {
                override fun onResponse(
                    call: Call<DestinasiResponse>,
                    response: Response<DestinasiResponse>
                ) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body?.status == true) {
                            destinasiList.clear()
                            destinasiList.addAll(body.data)
                            destinasiAdapter.notifyDataSetChanged()
                        } else {
                            Toast.makeText(this@MainActivity, body?.message ?: "Gagal", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@MainActivity, "Server Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<DestinasiResponse>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Koneksi Destinasi Gagal: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun getArtikelData() {
        ApiClient.apiService.getArtikels(1, null, null)
            .enqueue(object : Callback<ArtikelResponse> {
                override fun onResponse(
                    call: Call<ArtikelResponse>,
                    response: Response<ArtikelResponse>
                ) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body?.status == true) {
                            artikelList.clear()
                            artikelList.addAll(body.data)
                            artikelAdapter.notifyDataSetChanged()
                        } else {
                            Toast.makeText(this@MainActivity, body?.message ?: "Gagal", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@MainActivity, "Server Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ArtikelResponse>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Koneksi Artikel Gagal: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}