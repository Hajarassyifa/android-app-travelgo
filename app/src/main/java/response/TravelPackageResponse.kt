// ============================================================
// FILE: model/response/TravelPackageResponse.kt
// ============================================================
package com.example.travelgo.model.response

import com.google.gson.annotations.SerializedName

data class TravelPackageResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("nama_paket") val namaPaket: String?,
    @SerializedName("deskripsi") val deskripsi: String?,
    @SerializedName("harga") val harga: Double?,
    @SerializedName("durasi_hari") val durasiHari: Int?,
    @SerializedName("gambar") val gambar: String?,
    @SerializedName("destinasi") val destinasi: DestinasiResponse?,
    @SerializedName("created_at") val createdAt: String?
)