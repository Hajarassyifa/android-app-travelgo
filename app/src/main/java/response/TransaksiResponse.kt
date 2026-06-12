// ============================================================
// FILE: model/response/TransaksiResponse.kt
// ============================================================
package com.example.travelgo.model.response

import com.google.gson.annotations.SerializedName

data class TransaksiResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("booking_id") val bookingId: Int?,
    @SerializedName("metode_pembayaran") val metodePembayaran: String?,
    @SerializedName("jumlah_bayar") val jumlahBayar: Double?,
    @SerializedName("status") val status: String?,
    @SerializedName("bukti_pembayaran") val buktiPembayaran: String?,
    @SerializedName("booking") val booking: BookingResponse?,
    @SerializedName("created_at") val createdAt: String?
)