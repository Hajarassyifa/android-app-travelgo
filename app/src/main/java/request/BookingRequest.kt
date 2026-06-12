// ============================================================
// FILE: model/request/BookingRequest.kt
// ============================================================
package com.example.travelgo.model.request

import com.google.gson.annotations.SerializedName

data class BookingRequest(
    @SerializedName("travel_package_id") val travelPackageId: Int,
    @SerializedName("tanggal_berangkat") val tanggalBerangkat: String,
    @SerializedName("jumlah_orang") val jumlahOrang: Int,
    @SerializedName("catatan") val catatan: String? = null
)