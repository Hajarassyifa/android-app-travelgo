package com.example.travelgo

import com.google.gson.annotations.SerializedName

data class DestinasiResponse(
    val success: Boolean,
    val data: List<Destinasi>
)

data class Destinasi(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("location") val location: String,
    @SerializedName("price") val price: Double,
    @SerializedName("image") val image: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("rating") val rating: Double
)

data class DestinasiDetailResponse(
    val success: Boolean,
    val data: DestinasiDetail
)

data class DestinasiDetail(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("location") val location: String,
    @SerializedName("price") val price: Double,
    @SerializedName("image") val image: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("open_time") val openTime: String?,
    @SerializedName("close_time") val closeTime: String?,
    @SerializedName("rating") val rating: Double
)