// ============================================================
// FILE: model/request/TravelPackageRequest.kt
// ============================================================
package com.example.travelgo.model.request

import com.google.gson.annotations.SerializedName

data class TravelPackageRequest(
    @SerializedName("nama_paket") val namaPaket: String,
    @SerializedName("deskripsi") val deskripsi: String,
    @SerializedName("harga") val harga: Double,
    @SerializedName("destinasi_id") val destinasiId: Int,
    @SerializedName("durasi_hari") val durasiHari: Int
)
