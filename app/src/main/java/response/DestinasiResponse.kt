// ============================================================
// FILE: model/response/DestinasiResponse.kt
// ============================================================
package com.example.travelgo.model.response

import com.google.gson.annotations.SerializedName

data class DestinasiResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("nama") val nama: String?,
    @SerializedName("deskripsi") val deskripsi: String?,
    @SerializedName("lokasi") val lokasi: String?,
    @SerializedName("gambar") val gambar: String?,
    @SerializedName("rating") val rating: Float?,
    @SerializedName("created_at") val createdAt: String?
)