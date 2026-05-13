package com.example.travelgo

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout

class SemuaDestinasi : AppCompatActivity() {

    private lateinit var rvPantai: RecyclerView
    private lateinit var rvGunung: RecyclerView
    private lateinit var rvSejarah: RecyclerView
    private lateinit var rvKuliner: RecyclerView
    private lateinit var tabLayout: TabLayout
    // REVISI: Tambahkan inisialisasi NestedScrollView untuk poin BIRU
    private lateinit var mainScrollView: NestedScrollView

    private var listWisata = ArrayList<Destinasi>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_semua_destinasi)

        // 1. Inisialisasi View
        tabLayout = findViewById(R.id.tabLayout)
        rvPantai = findViewById(R.id.rvPantai)
        rvGunung = findViewById(R.id.rvGunung)
        rvSejarah = findViewById(R.id.rvSejarah)
        rvKuliner = findViewById(R.id.rvKuliner)
        // REVISI: Hubungkan ke ID mainScrollView yang ada di XML
        mainScrollView = findViewById(R.id.mainScrollView)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener { finish() }

        // 2. Setup TabLayout (Poin Kuning)
        setupTabs()

        // 3. Siapkan Data
        prepareData()

        // 4. Setup Setiap RecyclerView secara Horizontal
        setupHorizontalRV(rvPantai, "Pantai")
        setupHorizontalRV(rvGunung, "Gunung")
        setupHorizontalRV(rvSejarah, "Sejarah")
        setupHorizontalRV(rvKuliner, "Kuliner")
    }

    private fun setupTabs() {
        val categories = listOf("Semua", "Pantai", "Gunung", "Sejarah", "Kuliner")
        for (category in categories) {
            tabLayout.addTab(tabLayout.newTab().setText(category))
        }

        // REVISI POIN KUNING & BIRU: Tambahkan logic scroll otomatis
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> mainScrollView.smoothScrollTo(0, 0) // Kembali ke atas (Semua)
                    1 -> scrollToSection(R.id.sectionPantai) // Ke Section Pantai
                    2 -> scrollToSection(R.id.sectionGunung) // Ke Section Gunung
                    3 -> scrollToSection(R.id.sectionSejarah) // Ke Section Sejarah
                    4 -> scrollToSection(R.id.sectionKuliner) // Ke Section Kuliner
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    // REVISI: Fungsi baru untuk memudahkan scroll otomatis ke ID tertentu
    private fun scrollToSection(viewId: Int) {
        val view = findViewById<android.view.View>(viewId)
        mainScrollView.post {
            // Kita ambil posisi 'top' dari layout kategori tersebut
            mainScrollView.smoothScrollTo(0, view.top)
        }
    }

    private fun setupHorizontalRV(recyclerView: RecyclerView, kategori: String) {
        val filteredList = ArrayList(listWisata.filter { it.kategori == kategori })

        // REVISI: Tetap pakai LinearLayoutManager.HORIZONTAL sesuai desain card menyamping
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val adapter = DestinasiAdapter(filteredList)
        recyclerView.adapter = adapter

        // Tetap false agar tidak konflik dengan NestedScrollView
        recyclerView.isNestedScrollingEnabled = false
    }

    private fun prepareData() {
        listWisata.clear()
        // Data dummy sesuai permintaan (pastikan resource drawable tersedia)
        listWisata.add(Destinasi(1, "Bali", "Indonesia", "Keindahan alam.", "Rp 1.250.000", "Pantai", R.drawable.img_onboarding1))
        listWisata.add(Destinasi(2, "Labuan Bajo", "NTT", "Komodo island.", "Rp 2.150.000", "Pantai", R.drawable.img_onboarding2))
        listWisata.add(Destinasi(3, "Bromo", "Jawa Timur", "Sunrise View.", "Rp 950.000", "Gunung", R.drawable.img_onboarding3))
        listWisata.add(Destinasi(4, "Rinjani", "Lombok", "Pendakian indah.", "Rp 1.850.000", "Gunung", R.drawable.img_onboarding1))
        listWisata.add(Destinasi(5, "Borobudur", "Jawa Tengah", "Candi megah.", "Rp 350.000", "Sejarah", R.drawable.img_onboarding2))
        listWisata.add(Destinasi(6, "Kota Tua", "Jakarta", "Wisata sejarah.", "Rp 200.000", "Sejarah", R.drawable.img_onboarding3))
        listWisata.add(Destinasi(7, "Gudeg Jogja", "Yogyakarta", "Manis gurih.", "Rp 60.000", "Kuliner", R.drawable.img_onboarding1))
        listWisata.add(Destinasi(8, "Sate Madura", "Madura", "Bumbu kacang.", "Rp 35.000", "Kuliner", R.drawable.img_onboarding2))
    }
}
