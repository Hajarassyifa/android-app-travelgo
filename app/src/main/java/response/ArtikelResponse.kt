// ============================================================
// FILE: model/response/ArtikelResponse.kt
// ============================================================
package com.example.travelgo.model.response

import com.google.gson.annotations.SerializedName

data class ArtikelResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("judul") val judul: String?,
    @SerializedName("konten") val konten: String?,
    @SerializedName("gambar") val gambar: String?,
    @SerializedName("author") val author: UserResponse?,
    @SerializedName("created_at") val createdAt: String?
)
