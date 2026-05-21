package com.example.travelgo

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SemuaDestinasi : AppCompatActivity() {

    private lateinit var rvPantai: RecyclerView
    private lateinit var rvGunung: RecyclerView
    private lateinit var rvSejarah: RecyclerView
    private lateinit var rvKuliner: RecyclerView
    private lateinit var tabLayout: TabLayout
    private lateinit var mainScrollView: NestedScrollView

    private var listWisata = ArrayList<Destinasi>()
    private var selectedKategori = "Semua"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_semua_destinasi)

        tabLayout = findViewById(R.id.tabLayout)
        rvPantai = findViewById(R.id.rvPantai)
        rvGunung = findViewById(R.id.rvGunung)
        rvSejarah = findViewById(R.id.rvSejarah)
        rvKuliner = findViewById(R.id.rvKuliner)
        mainScrollView = findViewById(R.id.mainScrollView)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener { finish() }

        selectedKategori = intent.getStringExtra("kategori") ?: "Semua"

        setupTabs()
        loadDestinasiFromApi()
    }

    private fun loadDestinasiFromApi() {
        RetrofitClient.instance.getDestinasiList().enqueue(object : Callback<DestinasiResponse> {
            override fun onResponse(call: Call<DestinasiResponse>, response: Response<DestinasiResponse>) {
                if (response.isSuccessful) {
                    val data = response.body()?.data
                    if (data != null) {
                        listWisata.clear()
                        listWisata.addAll(data)

                        setupHorizontalRV(rvPantai, "Pantai")
                        setupHorizontalRV(rvGunung, "Gunung")
                        setupHorizontalRV(rvSejarah, "Sejarah")
                        setupHorizontalRV(rvKuliner, "Kuliner")

                        setDefaultTab()
                    }
                } else {
                    Toast.makeText(this@SemuaDestinasi, "Gagal memuat data", Toast.LENGTH_SHORT).show()
                    prepareDataDummy()
                }
            }

            override fun onFailure(call: Call<DestinasiResponse>, t: Throwable) {
                Toast.makeText(this@SemuaDestinasi, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                prepareDataDummy()
            }
        })
    }

    private fun setupTabs() {
        val categories = listOf("Semua", "Pantai", "Gunung", "Sejarah", "Kuliner")
        tabLayout.removeAllTabs()
        for (category in categories) {
            tabLayout.addTab(tabLayout.newTab().setText(category))
        }
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> mainScrollView.smoothScrollTo(0, 0)
                    1 -> scrollToSection(R.id.sectionPantai)
                    2 -> scrollToSection(R.id.sectionGunung)
                    3 -> scrollToSection(R.id.sectionSejarah)
                    4 -> scrollToSection(R.id.sectionKuliner)
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> mainScrollView.smoothScrollTo(0, 0)
                    1 -> scrollToSection(R.id.sectionPantai)
                    2 -> scrollToSection(R.id.sectionGunung)
                    3 -> scrollToSection(R.id.sectionSejarah)
                    4 -> scrollToSection(R.id.sectionKuliner)
                }
            }
        })
    }

    private fun setDefaultTab() {
        mainScrollView.post {
            when (selectedKategori) {
                "Pantai" -> tabLayout.getTabAt(1)?.select()
                "Gunung" -> tabLayout.getTabAt(2)?.select()
                "Sejarah" -> tabLayout.getTabAt(3)?.select()
                "Kuliner" -> tabLayout.getTabAt(4)?.select()
                else -> tabLayout.getTabAt(0)?.select()
            }
        }
    }

    private fun scrollToSection(viewId: Int) {
        val view = findViewById<View>(viewId)
        mainScrollView.post {
            mainScrollView.smoothScrollTo(0, view.top)
        }
    }

    private fun setupHorizontalRV(recyclerView: RecyclerView, kategori: String) {
        val filteredList = ArrayList(listWisata.filter { it.kategori == kategori })
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = DestinasiAdapter(filteredList)
        recyclerView.isNestedScrollingEnabled = false
    }

    private fun prepareDataDummy() {
        listWisata.clear()
        listWisata.add(Destinasi(1, "Bali", "Indonesia", "Keindahan alam.", "Rp 1.250.000", "Pantai", R.drawable.img_onboarding1))
        listWisata.add(Destinasi(2, "Labuan Bajo", "NTT", "Komodo island.", "Rp 2.150.000", "Pantai", R.drawable.img_onboarding2))
        listWisata.add(Destinasi(3, "Bromo", "Jawa Timur", "Sunrise View.", "Rp 950.000", "Gunung", R.drawable.img_onboarding3))
        listWisata.add(Destinasi(4, "Rinjani", "Lombok", "Pendakian indah.", "Rp 1.850.000", "Gunung", R.drawable.img_onboarding1))
        listWisata.add(Destinasi(5, "Borobudur", "Jawa Tengah", "Candi megah.", "Rp 350.000", "Sejarah", R.drawable.img_onboarding2))
        listWisata.add(Destinasi(6, "Kota Tua", "Jakarta", "Wisata sejarah.", "Rp 200.000", "Sejarah", R.drawable.img_onboarding3))
        listWisata.add(Destinasi(7, "Gudeg Jogja", "Yogyakarta", "Manis gurih.", "Rp 60.000", "Kuliner", R.drawable.img_onboarding1))
        listWisata.add(Destinasi(8, "Sate Madura", "Madura", "Bumbu kacang.", "Rp 35.000", "Kuliner", R.drawable.img_onboarding2))

        setupHorizontalRV(rvPantai, "Pantai")
        setupHorizontalRV(rvGunung, "Gunung")
        setupHorizontalRV(rvSejarah, "Sejarah")
        setupHorizontalRV(rvKuliner, "Kuliner")
        setDefaultTab()
    }
}