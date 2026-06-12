// ============================================================
// FILE: model/request/TransaksiRequest.kt
// ============================================================
package com.example.travelgo.model.request

import com.google.gson.annotations.SerializedName

data class TransaksiRequest(
    @SerializedName("booking_id") val bookingId: Int,
    @SerializedName("metode_pembayaran") val metodePembayaran: String,
    @SerializedName("jumlah_bayar") val jumlahBayar: Double
)