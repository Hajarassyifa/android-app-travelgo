package com.example.travelgo

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ArtikelActivity : AppCompatActivity() {

    private lateinit var rvArtikel: RecyclerView
    private lateinit var btnBack: ImageView
    private lateinit var etSearchArtikel: EditText

    private lateinit var chipSemua: TextView
    private lateinit var chipTips: TextView
    private lateinit var chipPantai: TextView
    private lateinit var chipWisata: TextView

    private val listArtikel = ArrayList<Artikel>()
    private val filteredArtikel = ArrayList<Artikel>()

    private lateinit var artikelAdapter: ArtikelAdapter
    private var selectedKategori = "Semua"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artikel)

        btnBack = findViewById(R.id.btnBack)
        rvArtikel = findViewById(R.id.rvArtikel)
        etSearchArtikel = findViewById(R.id.etSearchArtikel)

        chipSemua = findViewById(R.id.chipSemua)
        chipTips = findViewById(R.id.chipTips)
        chipPantai = findViewById(R.id.chipPantai)
        chipWisata = findViewById(R.id.chipWisata)

        btnBack.setOnClickListener {
            finish()
        }

        prepareArtikel()
        setupRecyclerView()
        setupSearch()
        setupKategori()
        filterArtikel()
    }

    private fun prepareArtikel() {
        listArtikel.clear()

        listArtikel.add(
            Artikel(
                1,
                "Tips Traveling ke Bali",
                "Tips",
                "5 menit baca",
                "19 Mei 2026",
                R.drawable.img_onboarding1,
                "Bali selalu menjadi destinasi favorit para traveler. Dengan keindahan alam, budaya yang kaya, dan kuliner yang menggoda, Bali cocok untuk liburan santai maupun petualangan."
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
                "Indonesia punya banyak pantai indah yang wajib dikunjungi. Mulai dari Bali, Lombok, Labuan Bajo, hingga Raja Ampat."
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
                "Packing yang rapi bisa membuat perjalanan lebih nyaman. Bawa barang seperlunya dan gunakan tas yang ringan."
            )
        )

        listArtikel.add(
            Artikel(
                4,
                "Wisata Alam yang Wajib Dikunjungi",
                "Wisata",
                "6 menit baca",
                "16 Mei 2026",
                R.drawable.img_onboarding1,
                "Indonesia memiliki banyak wisata alam indah, dari gunung, pantai, air terjun, sampai desa wisata yang masih asri."
            )
        )
    }

    private fun setupRecyclerView() {
        artikelAdapter = ArtikelAdapter(filteredArtikel) { artikel ->
            val intent = Intent(this, DetailArtikelActivity::class.java)
            intent.putExtra("JUDUL", artikel.judul)
            intent.putExtra("KATEGORI", artikel.kategori)
            intent.putExtra("WAKTU", artikel.waktuBaca)
            intent.putExtra("TANGGAL", artikel.tanggal)
            intent.putExtra("GAMBAR", artikel.gambar)
            intent.putExtra("ISI", artikel.isi)
            startActivity(intent)
        }

        rvArtikel.layoutManager = LinearLayoutManager(this)
        rvArtikel.adapter = artikelAdapter
    }

    private fun setupSearch() {
        etSearchArtikel.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterArtikel()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupKategori() {
        chipSemua.setOnClickListener {
            selectedKategori = "Semua"
            filterArtikel()
        }

        chipTips.setOnClickListener {
            selectedKategori = "Tips"
            filterArtikel()
        }

        chipPantai.setOnClickListener {
            selectedKategori = "Pantai"
            filterArtikel()
        }

        chipWisata.setOnClickListener {
            selectedKategori = "Wisata"
            filterArtikel()
        }
    }

    private fun filterArtikel() {
        val keyword = etSearchArtikel.text.toString().trim().lowercase()

        filteredArtikel.clear()

        filteredArtikel.addAll(
            listArtikel.filter { artikel ->

                val cocokKategori =
                    selectedKategori == "Semua" ||
                            artikel.kategori.equals(selectedKategori, ignoreCase = true)

                val cocokSearch =
                    artikel.judul.lowercase().contains(keyword) ||
                            artikel.kategori.lowercase().contains(keyword) ||
                            artikel.isi.lowercase().contains(keyword)

                cocokKategori && cocokSearch
            }
        )

        artikelAdapter.notifyDataSetChanged()
    }
}