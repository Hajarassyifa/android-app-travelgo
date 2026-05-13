package com.example.travelgo

// REVISI: Langsung gunakan nama 'image' agar sinkron dengan kodingan Adapter
data class Destinasi(
    val id: Int,
    val nama: String,
    val lokasi: String,
    val deskripsi: String,
    val harga: String,
    val kategori: String,
    val image: Int // Variabel ini yang akan menyimpan R.drawable.nama_gambar
)
