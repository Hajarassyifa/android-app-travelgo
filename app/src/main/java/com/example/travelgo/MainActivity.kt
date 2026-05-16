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

    private lateinit var rvDestinasi: RecyclerView
    private lateinit var rvArtikel: RecyclerView
    private lateinit var bannerViewPager: ViewPager2
    private lateinit var bottomNav: BottomNavigationView

    private var listWisata = ArrayList<Destinasi>()
    private var listArtikel = ArrayList<Artikel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvDestinasi = findViewById(R.id.rvDestinasi)
        rvArtikel = findViewById(R.id.rvArtikel)
        bannerViewPager = findViewById(R.id.bannerViewPager)
        bottomNav = findViewById(R.id.bottomNavigation)

        setupBanner()
        prepareDataDestinasi()
        prepareDataArtikel()

        showDestinasiHorizontal()
        showArtikelVertical()

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
        rvArtikel.isNestedScrollingEnabled = false
    }

    private fun setupBottomNavigation() {
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {

                R.id.nav_beranda -> {
                    true
                }

                R.id.nav_destinasi -> {
                    val intent = Intent(this, SemuaDestinasi::class.java)
                    intent.putExtra("kategori", "Semua")
                    startActivity(intent)
                    true
                }

                R.id.nav_booking -> {
                    val intent = Intent(this, MyTicketActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.nav_artikel -> {
                    true
                }

                R.id.nav_akun -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }

        bottomNav.selectedItemId = R.id.nav_beranda
    }

    private fun setupCategoryButtons() {
        fun initCategory(id: Int, name: String, iconRes: Int, categoryFilter: String) {
            val layout = findViewById<LinearLayout>(id)

            layout.findViewById<TextView>(R.id.tvCategoryName).text = name
            layout.findViewById<ImageView>(R.id.imgCategory).setImageResource(iconRes)

            layout.setOnClickListener {
                val intent = Intent(this, SemuaDestinasi::class.java)
                intent.putExtra("kategori", categoryFilter)
                startActivity(intent)
            }
        }

        initCategory(R.id.catAll, "Semua", R.drawable.ic_all, "Semua")
        initCategory(R.id.catBeach, "Pantai", R.drawable.ic_beach, "Pantai")
        initCategory(R.id.catMountain, "Gunung", R.drawable.ic_mountain, "Gunung")
        initCategory(R.id.catHistory, "Sejarah", R.drawable.ic_history, "Sejarah")
        initCategory(R.id.catFood, "Kuliner", R.drawable.ic_culinary, "Kuliner")
    }
}