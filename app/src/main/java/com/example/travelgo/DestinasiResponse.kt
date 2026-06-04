package com.example.travelgo

import com.google.gson.annotations.SerializedName

data class DestinasiResponse(
    @SerializedName("status") val status: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: List<Destinasi>
)

data class DestinasiDetailResponse(
    @SerializedName("status") val status: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: Destinasi
)