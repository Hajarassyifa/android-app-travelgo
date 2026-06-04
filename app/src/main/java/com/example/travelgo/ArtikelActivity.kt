package com.example.travelgo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArtikelActivity : AppCompatActivity() {

    private lateinit var rvArtikel: RecyclerView
    private lateinit var artikelAdapter: ArtikelAdapter
    private val artikelList = mutableListOf<Artikel>()
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artikel)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        rvArtikel = findViewById(R.id.rvArtikel)
        rvArtikel.layoutManager = LinearLayoutManager(this)
        artikelAdapter = ArtikelAdapter(artikelList)
        rvArtikel.adapter = artikelAdapter

        loadSemuaArtikel()
    }

    private fun loadSemuaArtikel() {
        val token = SessionManager.getToken(this)

        ApiClient.apiService.getArtikels("Bearer $token", 1, null, null)
            .enqueue(object : Callback<ArtikelResponse> {
                override fun onResponse(call: Call<ArtikelResponse>, response: Response<ArtikelResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        val body = response.body()
                        if (body?.data != null) {
                            artikelList.clear()
                            artikelList.addAll(body.data)
                            artikelAdapter.notifyDataSetChanged()
                        }
                    } else {
                        Toast.makeText(this@ArtikelActivity, "Gagal mengambil artikel", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ArtikelResponse>, t: Throwable) {
                    Toast.makeText(this@ArtikelActivity, "Eror: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}