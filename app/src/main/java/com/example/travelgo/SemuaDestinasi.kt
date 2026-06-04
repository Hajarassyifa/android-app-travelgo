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
                    val body = response.body()
                    if (body?.success == true) {
                        val data = body.data
                        if (data.isNotEmpty()) {
                            listWisata.clear()
                            listWisata.addAll(data)

                            setupHorizontalRV(rvPantai, "Pantai")
                            setupHorizontalRV(rvGunung, "Gunung")
                            setupHorizontalRV(rvSejarah, "Sejarah")
                            setupHorizontalRV(rvKuliner, "Kuliner")

                            setDefaultTab()
                        } else {
                            prepareDataDummy()
                        }
                    } else {
                        prepareDataDummy()
                    }
                } else {
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
            override fun onTabReselected(tab: TabLayout.Tab?) {}
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
        val filteredList = ArrayList(listWisata.filter {
            it.description?.contains(kategori, ignoreCase = true) == true ||
                    it.name.contains(kategori, ignoreCase = true)
        })

        val finalList = if (filteredList.isEmpty()) listWisata else filteredList

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = DestinasiAdapter(finalList)
        recyclerView.isNestedScrollingEnabled = false
    }

    private fun prepareDataDummy() {
        listWisata.clear()
        listWisata.add(Destinasi(1, "Pantai Kuta", "Bali", 1250000.0, null, "Pantai indah di Bali", 4.5))
        listWisata.add(Destinasi(2, "Labuan Bajo", "NTT", 2150000.0, null, "Pantai eksotis dengan pulau Komodo", 4.7))
        listWisata.add(Destinasi(3, "Gunung Bromo", "Jawa Timur", 950000.0, null, "Gunung dengan sunrise terbaik", 4.8))
        listWisata.add(Destinasi(4, "Gunung Rinjani", "Lombok", 1850000.0, null, "Gunung dengan pemandangan indah", 4.6))
        listWisata.add(Destinasi(5, "Candi Borobudur", "Jawa Tengah", 350000.0, null, "Candi Buddha terbesar di dunia", 4.9))
        listWisata.add(Destinasi(6, "Kota Tua Jakarta", "Jakarta", 200000.0, null, "Wisata sejarah dengan bangunan lawas", 4.3))
        listWisata.add(Destinasi(7, "Gudeg Jogja", "Yogyakarta", 60000.0, null, "Kuliner khas Yogyakarta", 4.5))
        listWisata.add(Destinasi(8, "Sate Madura", "Madura", 35000.0, null, "Kuliner sate dengan bumbu kacang", 4.4))

        setupHorizontalRV(rvPantai, "Pantai")
        setupHorizontalRV(rvGunung, "Gunung")
        setupHorizontalRV(rvSejarah, "Sejarah")
        setupHorizontalRV(rvKuliner, "Kuliner")
        setDefaultTab()
    }
}