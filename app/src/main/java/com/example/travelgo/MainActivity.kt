package com.example.travelgo

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
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
    private lateinit var tvSapaan: TextView

    private lateinit var etSearch: EditText
    private lateinit var destinasiAdapter: DestinasiAdapter
    private var listWisata = ArrayList<Destinasi>()
    private var listArtikel = ArrayList<Artikel>()

    private var filteredWisata = ArrayList<Destinasi>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvDestinasi = findViewById(R.id.rvDestinasi)
        rvArtikel = findViewById(R.id.rvArtikel)
        bannerViewPager = findViewById(R.id.bannerViewPager)
        bottomNav = findViewById(R.id.bottomNavigation)
        tvSapaan = findViewById(R.id.tvSapaan)
        etSearch = findViewById(R.id.etSearch)

        setupSapaanUser()
        setupBanner()
        prepareDataDestinasi()
        prepareDataArtikel()

        showDestinasiHorizontal()
        showArtikelVertical()

        setupCategoryButtons()
        setupBottomNavigation()
        setupSearch()
    }

    private fun setupBanner() {
        val images = listOf(
            R.drawable.img_onboarding1,
            R.drawable.img_onboarding2,
            R.drawable.img_onboarding3
        )
        bannerViewPager.adapter = OnboardingAdapter(images)
    }
    private fun setupSapaanUser() {

        val sharedPref =
            getSharedPreferences("USER_DATA", MODE_PRIVATE)

        val namaUser =
            sharedPref.getString("NAMA", "Traveler")

        tvSapaan.text =
            "Halo, $namaUser 👋"
    }


    private fun prepareDataDestinasi() {
        listWisata.clear()
        listWisata.add(Destinasi(1, "Bali", "Bali", "Sunrise terbaik.", "Rp 1.250.000", "Pantai", R.drawable.img_onboarding1))
        listWisata.add(Destinasi(2, "Raja Ampat", "Papua Barat", "Surga dunia.", "Rp 3.250.000", "Pantai", R.drawable.img_onboarding2))
        listWisata.add(Destinasi(3, "Labuan Bajo", "NTT", "Komodo island.", "Rp 2.150.000", "Pantai", R.drawable.img_onboarding3))
    }

    private fun prepareDataArtikel() {
        listArtikel.clear()

        listArtikel.add(
            Artikel(
                1,
                "Tips Traveling ke Bali",
                "Tips",
                "5 menit baca",
                "19 Mei 2026",
                R.drawable.img_onboarding1,
                "Bali selalu menjadi destinasi favorit para traveler."
            )
        )

        listArtikel.add(
            Artikel(
                2,
                "5 Pantai Terbaik di Indonesia",
                "Pantai",
                "4 menit baca",
                "18 Mei 2026",
                R.drawable.img_onboarding2,
                "Indonesia memiliki banyak pantai indah yang wajib dikunjungi."
            )
        )

        listArtikel.add(
            Artikel(
                3,
                "Packing Hemat untuk Traveler",
                "Tips",
                "3 menit baca",
                "17 Mei 2026",
                R.drawable.img_onboarding3,
                "Packing hemat membuat perjalanan lebih nyaman dan praktis."
            )
        )
    }

    private fun showDestinasiHorizontal() {
        filteredWisata.clear()
        filteredWisata.addAll(listWisata)

        rvDestinasi.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        destinasiAdapter = DestinasiAdapter(filteredWisata)
        rvDestinasi.adapter = destinasiAdapter
    }
    private fun setupSearch() {
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterDestinasi(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun filterDestinasi(keyword: String) {
        filteredWisata.clear()

        if (keyword.isEmpty()) {
            filteredWisata.addAll(listWisata)
        } else {
            val query = keyword.lowercase()

            filteredWisata.addAll(
                listWisata.filter {
                    it.nama.lowercase().contains(query) ||
                            it.lokasi.lowercase().contains(query) ||
                            it.kategori.lowercase().contains(query)
                }
            )
        }

        destinasiAdapter.notifyDataSetChanged()
    }

    private fun showArtikelVertical() {
        rvArtikel.layoutManager =
            LinearLayoutManager(this)

        rvArtikel.adapter =
            ArtikelAdapter(listArtikel) { artikel ->

                val intent =
                    Intent(this, DetailArtikelActivity::class.java)

                intent.putExtra("JUDUL", artikel.judul)
                intent.putExtra("KATEGORI", artikel.kategori)
                intent.putExtra("WAKTU", artikel.waktuBaca)
                intent.putExtra("TANGGAL", artikel.tanggal)
                intent.putExtra("GAMBAR", artikel.gambar)
                intent.putExtra("ISI", artikel.isi)

                startActivity(intent)
            }

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
                    startActivity(Intent(this, ArtikelActivity::class.java))
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