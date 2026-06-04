package com.example.travelgo

import com.google.gson.annotations.SerializedName

data class Destinasi(
    @SerializedName("id") val id: Int,
    @SerializedName("nama_destinasi") val nama: String,
    @SerializedName("lokasi") val lokasi: String,
    @SerializedName("deskripsi") val deskripsi: String,
    @SerializedName("harga_tiket") val harga: String,
    val kategori: String = "",   // ← tambah ini
    val image: Int = 0
)