package com.example.travelgo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArtikelActivity : AppCompatActivity() {

    private lateinit var rvArtikel: RecyclerView
    private lateinit var artikelAdapter: ArtikelAdapter
    private val artikelList = mutableListOf<Artikel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artikel)

        rvArtikel = findViewById(R.id.rvArtikel)
        rvArtikel.layoutManager = LinearLayoutManager(this)

        artikelAdapter = ArtikelAdapter(artikelList)
        rvArtikel.adapter = artikelAdapter

        getArtikel()
    }

    private fun getArtikel() {
        ApiClient.apiService.getArtikels(1, null, null)
            .enqueue(object : Callback<ArtikelResponse> {
                override fun onResponse(
                    call: Call<ArtikelResponse>,
                    response: Response<ArtikelResponse>
                ) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body?.status == true) { // Menggunakan validasi status backend
                            artikelList.clear()
                            artikelList.addAll(body.data)
                            artikelAdapter.notifyDataSetChanged()
                        } else {
                            Toast.makeText(this@ArtikelActivity, body?.message ?: "Gagal", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@ArtikelActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ArtikelResponse>, t: Throwable) {
                    Toast.makeText(this@ArtikelActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}