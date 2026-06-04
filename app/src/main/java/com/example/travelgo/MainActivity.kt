package com.example.travelgo

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var rvDestinasi: RecyclerView
    private lateinit var rvArtikel: RecyclerView
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var tvSapaan: TextView
    private lateinit var etSearch: EditText
    private lateinit var tvLihatSemuaDestinasi: TextView
    private lateinit var tvLihatSemuaArtikel: TextView
    private lateinit var bannerViewPager: ViewPager2
    private lateinit var dotsLayout: LinearLayout

    private lateinit var destinasiAdapter: DestinasiAdapter
    private var listWisata = ArrayList<Destinasi>()
    private var listArtikel = ArrayList<Artikel>()
    private var filteredWisata = ArrayList<Destinasi>()

    private val bannerHandler = Handler(Looper.getMainLooper())
    private var bannerRunnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvDestinasi           = findViewById(R.id.rvDestinasi)
        rvArtikel             = findViewById(R.id.rvArtikel)
        bottomNav             = findViewById(R.id.bottomNavigation)
        tvSapaan              = findViewById(R.id.tvSapaan)
        etSearch              = findViewById(R.id.etSearch)
        tvLihatSemuaDestinasi = findViewById(R.id.tvLihatSemuaDestinasi)
        tvLihatSemuaArtikel   = findViewById(R.id.tvLihatSemuaArtikel)
        bannerViewPager       = findViewById(R.id.bannerViewPager)
        dotsLayout            = findViewById(R.id.dotsLayout)

        setupSapaanUser()
        setupBanner()
        loadDestinasiFromApi()
        loadArtikelFromApi()
        setupBottomNavigation()
        setupSearch()
        setupClickListeners()
    }

    // ─── SAPAAN ──────────────────────────────────────────────────

    private fun setupSapaanUser() {
        val token = SessionManager.getToken(this)
        if (token != null) {
            ApiClient.apiService.getProfile("Bearer $token")
                .enqueue(object : Callback<ProfileResponse> {
                    override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                        val nama = if (response.isSuccessful) response.body()?.data?.name ?: "Traveler"
                        else SessionManager.getName(this@MainActivity)
                        tvSapaan.text = "Halo, $nama 👋"
                    }
                    override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                        tvSapaan.text = "Halo, ${SessionManager.getName(this@MainActivity)} 👋"
                    }
                })
        } else {
            tvSapaan.text = "Halo, ${SessionManager.getName(this)} 👋"
        }
    }

    // ─── BANNER ──────────────────────────────────────────────────

    data class BannerItem(val title: String, val subtitle: String, val bgColor: String, val emoji: String)

    inner class BannerAdapter(private val items: List<BannerItem>) :
        RecyclerView.Adapter<BannerAdapter.BannerVH>() {

        inner class BannerVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvTitle: TextView    = itemView.findViewById(R.id.tvBannerTitle)
            val tvSubtitle: TextView = itemView.findViewById(R.id.tvBannerSubtitle)
            val tvEmoji: TextView    = itemView.findViewById(R.id.tvBannerEmoji)
            val root: View           = itemView.findViewById(R.id.bannerRoot)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerVH {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_banner, parent, false)
            return BannerVH(view)
        }

        override fun onBindViewHolder(holder: BannerVH, position: Int) {
            val item = items[position]
            holder.tvTitle.text    = item.title
            holder.tvSubtitle.text = item.subtitle
            holder.tvEmoji.text    = item.emoji
            holder.root.setBackgroundColor(Color.parseColor(item.bgColor))
        }

        override fun getItemCount() = items.size
    }

    private fun setupBanner() {
        val bannerItems = listOf(
            BannerItem("Jelajahi Bali 🌺",   "Paket wisata mulai Rp 800.000", "#1E40AF", "🏖️"),
            BannerItem("Bromo Adventure",    "Liburan tak terlupakan",        "#065F46", "⛰️"),
            BannerItem("Promo Akhir Tahun!", "Diskon hingga 40% semua paket", "#7C3AED", "🎉"),
            BannerItem("Kuliner Nusantara",  "Temukan cita rasa terbaik",     "#B45309", "🍜")
        )
        bannerViewPager.adapter = BannerAdapter(bannerItems)
        setupDots(bannerItems.size)

        bannerRunnable = object : Runnable {
            override fun run() {
                val next = (bannerViewPager.currentItem + 1) % bannerItems.size
                bannerViewPager.setCurrentItem(next, true)
                bannerHandler.postDelayed(this, 3000)
            }
        }
        bannerHandler.postDelayed(bannerRunnable!!, 3000)

        bannerViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) { updateDots(position, bannerItems.size) }
        })
    }

    private fun setupDots(count: Int) {
        dotsLayout.removeAllViews()
        for (i in 0 until count) {
            val dot = View(this)
            val params = LinearLayout.LayoutParams(if (i == 0) dpToPx(24) else dpToPx(8), dpToPx(8))
            params.marginEnd = dpToPx(4)
            dot.layoutParams = params
            dot.setBackgroundColor(if (i == 0) Color.WHITE else Color.parseColor("#80FFFFFF"))
            dotsLayout.addView(dot)
        }
    }

    private fun updateDots(selected: Int, count: Int) {
        for (i in 0 until count) {
            val dot = dotsLayout.getChildAt(i) ?: continue
            val params = dot.layoutParams as LinearLayout.LayoutParams
            params.width = if (i == selected) dpToPx(24) else dpToPx(8)
            dot.layoutParams = params
            dot.setBackgroundColor(if (i == selected) Color.WHITE else Color.parseColor("#80FFFFFF"))
        }
    }

    private fun dpToPx(dp: Int): Int = (dp * resources.displayMetrics.density).toInt()

    // ─── API ─────────────────────────────────────────────────────

    private fun loadDestinasiFromApi() {
        ApiClient.apiService.getDestinasiList()
            .enqueue(object : Callback<DestinasiResponse> {
                override fun onResponse(call: Call<DestinasiResponse>, response: Response<DestinasiResponse>) {
                    val data = if (response.isSuccessful) response.body()?.data ?: emptyList() else emptyList()
                    listWisata.clear()
                    if (data.isEmpty()) prepareDataDestinasiDummy() else listWisata.addAll(data)
                    filteredWisata.clear()
                    filteredWisata.addAll(listWisata)
                    showDestinasiHorizontal()
                }
                override fun onFailure(call: Call<DestinasiResponse>, t: Throwable) {
                    prepareDataDestinasiDummy()
                    filteredWisata.clear()
                    filteredWisata.addAll(listWisata)
                    showDestinasiHorizontal()
                }
            })
    }

    private fun loadArtikelFromApi() {
        ApiClient.apiService.getArtikels(1, null, null)
            .enqueue(object : Callback<ArtikelResponse> {
                override fun onResponse(call: Call<ArtikelResponse>, response: Response<ArtikelResponse>) {
                    val data = if (response.isSuccessful) response.body()?.data ?: emptyList() else emptyList()
                    listArtikel.clear()
                    if (data.isEmpty()) prepareDataArtikelDummy() else listArtikel.addAll(data)
                    showArtikelVertical()
                }
                override fun onFailure(call: Call<ArtikelResponse>, t: Throwable) {
                    prepareDataArtikelDummy()
                    showArtikelVertical()
                }
            })
    }

    // ─── DUMMY FALLBACK ──────────────────────────────────────────

    private fun prepareDataDestinasiDummy() {
        listWisata.clear()
        listWisata.add(Destinasi(1, "Pantai Kuta",     "Bali",        1250000.0, null, "Pantai indah",    4.5))
        listWisata.add(Destinasi(2, "Gunung Bromo",    "Jawa Timur",  950000.0,  null, "Gunung terkenal", 4.8))
        listWisata.add(Destinasi(3, "Candi Borobudur", "Jawa Tengah", 350000.0,  null, "Candi Buddha",    4.9))
        listWisata.add(Destinasi(4, "Gudeg Jogja",     "Yogyakarta",  60000.0,   null, "Kuliner khas",    4.5))
    }

    private fun prepareDataArtikelDummy() {
        listArtikel.clear()
        listArtikel.add(Artikel(1, "Tips Traveling ke Bali", "tips-bali", "Tips seru",       null, "Tips",   "Admin", 100, "2024-05-19"))
        listArtikel.add(Artikel(2, "5 Pantai Terbaik",       "pantai",    "Pantai indah",    null, "Pantai", "Admin", 85,  "2024-05-18"))
        listArtikel.add(Artikel(3, "Packing Hemat",          "packing",   "Packing praktis", null, "Tips",   "Admin", 70,  "2024-05-17"))
    }

    // ─── RECYCLERVIEWS ───────────────────────────────────────────

    private fun showDestinasiHorizontal() {
        rvDestinasi.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        destinasiAdapter = DestinasiAdapter(filteredWisata)
        rvDestinasi.adapter = destinasiAdapter
    }

    private fun showArtikelVertical() {
        rvArtikel.layoutManager = LinearLayoutManager(this)
        rvArtikel.adapter = ArtikelAdapter(listArtikel)
    }

    // ─── SEARCH ──────────────────────────────────────────────────

    private fun setupSearch() {
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { filterDestinasi(s.toString()) }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun filterDestinasi(keyword: String) {
        filteredWisata.clear()
        filteredWisata.addAll(
            if (keyword.isEmpty()) listWisata
            else listWisata.filter {
                it.name.lowercase().contains(keyword.lowercase()) ||
                        it.location.lowercase().contains(keyword.lowercase())
            }
        )
        destinasiAdapter.notifyDataSetChanged()
    }

    // ─── BOTTOM NAV ──────────────────────────────────────────────

    private fun setupBottomNavigation() {
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_beranda   -> true
                R.id.nav_destinasi -> {
                    Toast.makeText(this, "Fitur Semua Destinasi segera hadir", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_booking   -> {
                    startActivity(Intent(this, MyTicketActivity::class.java))
                    true
                }
                R.id.nav_artikel   -> {
                    startActivity(Intent(this, ArtikelActivity::class.java))
                    true
                }
                R.id.nav_akun      -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    // ─── CLICK LISTENERS ─────────────────────────────────────────

    private fun setupClickListeners() {
        // Icon notifikasi — sekarang ada di XML
        findViewById<android.widget.ImageView>(R.id.btnNotification).setOnClickListener {
            startActivity(Intent(this, NotificationsActivity::class.java))
        }

        tvLihatSemuaDestinasi.setOnClickListener {
            Toast.makeText(this, "Fitur Semua Destinasi segera hadir", Toast.LENGTH_SHORT).show()
        }

        tvLihatSemuaArtikel.setOnClickListener {
            startActivity(Intent(this, ArtikelActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        bannerRunnable?.let { bannerHandler.removeCallbacks(it) }
    }
}