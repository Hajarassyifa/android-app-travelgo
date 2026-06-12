// ============================================================
// FILE: model/response/BookingResponse.kt
// ============================================================
package com.example.travelgo.model.response

import com.google.gson.annotations.SerializedName

data class BookingResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("user_id") val userId: Int?,
    @SerializedName("travel_package_id") val travelPackageId: Int?,
    @SerializedName("tanggal_berangkat") val tanggalBerangkat: String?,
    @SerializedName("jumlah_orang") val jumlahOrang: Int?,
    @SerializedName("total_harga") val totalHarga: Double?,
    @SerializedName("status") val status: String?,
    @SerializedName("catatan") val catatan: String?,
    @SerializedName("travel_package") val travelPackage: TravelPackageResponse?,
    @SerializedName("created_at") val createdAt: String?
)