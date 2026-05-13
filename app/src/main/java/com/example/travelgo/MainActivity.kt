package com.example.travelgo

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    // 1. Inisialisasi variabel View
    private lateinit var rvDestinasi: RecyclerView
    private lateinit var rvArtikel: RecyclerView
    private lateinit var bannerViewPager: ViewPager2
    private lateinit var bottomNav: BottomNavigationView

    // 2. Inisialisasi Data List
    private var listWisata = ArrayList<Destinasi>()
    private var listArtikel = ArrayList<Artikel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // --- HUBUNGKAN ID DARI XML ---
        rvDestinasi = findViewById(R.id.rvDestinasi)
        rvArtikel = findViewById(R.id.rvArtikel)
        bannerViewPager = findViewById(R.id.bannerViewPager)
        bottomNav = findViewById(R.id.bottomNavigation)

        // --- SETUP DATA & TAMPILAN ---
        setupBanner()
        prepareDataDestinasi()
        prepareDataArtikel()

        showDestinasiHorizontal()
        showArtikelVertical()

        // --- SETUP NAVIGASI & KATEGORI ---
        setupCategoryButtons()
        setupBottomNavigation()
    }

    private fun setupBanner() {
        val images = listOf(
            R.drawable.img_onboarding1,
            R.drawable.img_onboarding2,
            R.drawable.img_onboarding3
        )
        bannerViewPager.adapter = OnboardingAdapter(images)
    }

    private fun prepareDataDestinasi() {
        listWisata.clear()
        listWisata.add(Destinasi(1, "Bali", "Bali", "Sunrise terbaik.", "Rp 1.250.000", "Pantai", R.drawable.img_onboarding1))
        listWisata.add(Destinasi(2, "Raja Ampat", "Papua Barat", "Surga dunia.", "Rp 3.250.000", "Pantai", R.drawable.img_onboarding2))
        listWisata.add(Destinasi(3, "Labuan Bajo", "NTT", "Komodo island.", "Rp 2.150.000", "Pantai", R.drawable.img_onboarding3))
    }

    private fun prepareDataArtikel() {
        listArtikel.clear()
        listArtikel.add(Artikel(1, "5 Destinasi Pantai Terindah di Indonesia", "Pantai", "5 menit baca", R.drawable.img_onboarding1))
        listArtikel.add(Artikel(2, "Tips Packing Hemat untuk Traveling", "Tips", "4 menit baca", R.drawable.img_onboarding2))
        listArtikel.add(Artikel(3, "Mengunjungi Candi Borobudur", "Sejarah", "6 menit baca", R.drawable.img_onboarding3))
    }

    private fun showDestinasiHorizontal() {
        rvDestinasi.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvDestinasi.adapter = DestinasiAdapter(listWisata)
    }

    private fun showArtikelVertical() {
        rvArtikel.layoutManager = LinearLayoutManager(this)
        rvArtikel.adapter = ArtikelAdapter(listArtikel)
        rvArtikel.isNestedScrollingEnabled = false // Menghindari lag saat scroll di NestedScrollView
    }

    private fun setupBottomNavigation() {
        // Logika klik Bottom Navigation Bar
        bottomNav.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.nav_beranda -> {
                    // Sudah di Beranda, tidak perlu pindah
                    true
                }
                R.id.nav_destinasi -> {
                    startActivity(Intent(this, SemuaDestinasi::class.java))
                    true
                }
                R.id.nav_artikel -> {
                    // Opsional: pindah ke activity artikel jika ada
                    true
                }
                R.id.nav_akun -> {
                    // Opsional: pindah ke ProfileActivity
                    true
                }
                else -> false
            }
        }

        // Mengatur agar icon Beranda terpilih secara default
        bottomNav.selectedItemId = R.id.nav_beranda
    }

    private fun setupCategoryButtons() {
        fun initCategory(id: Int, name: String, iconRes: Int, categoryFilter: String) {
            val layout = findViewById<LinearLayout>(id)
            layout?.let {
                it.findViewById<TextView>(R.id.tvCategoryName).text = name
                it.findViewById<ImageView>(R.id.imgCategory).setImageResource(iconRes)

                it.setOnClickListener {
                    if (categoryFilter == "Semua") {
                        startActivity(Intent(this, SemuaDestinasi::class.java))
                    } else {
                        // Logika filter data listWisata berdasarkan kategori
                    }
                }
            }
        }

        initCategory(R.id.catAll, "Semua", R.drawable.ic_all, "Semua")
        initCategory(R.id.catBeach, "Pantai", R.drawable.ic_beach, "Pantai")
        initCategory(R.id.catMountain, "Gunung", R.drawable.ic_mountain, "Gunung")
        initCategory(R.id.catHistory, "Sejarah", R.drawable.ic_history, "Sejarah")
        initCategory(R.id.catFood, "Kuliner", R.drawable.ic_culinary, "Kuliner")
    }
}
