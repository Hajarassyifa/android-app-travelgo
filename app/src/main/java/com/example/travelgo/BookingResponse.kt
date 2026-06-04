package com.example.travelgo

import com.google.gson.annotations.SerializedName

data class BookingListResponse(
    val success: Boolean,
    val message: String,
    val data: List<Booking>
)

data class Booking(
    @SerializedName("id") val id: Int,
    @SerializedName("booking_code") val booking_code: String,
    @SerializedName("destinasi") val destinasi: DestinasiBooking?,
    @SerializedName("tanggal_berangkat") val tanggal_berangkat: String,
    @SerializedName("jumlah_tiket") val jumlah_tiket: Int,
    @SerializedName("total_harga") val total_harga: Double,
    @SerializedName("status") val status: String,
    @SerializedName("payment_status") val payment_status: String,
    @SerializedName("created_at") val created_at: String?
)

data class DestinasiBooking(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("location") val location: String,
    @SerializedName("image") val image: String?,
    @SerializedName("price") val price: Double
)

data class BookingDetailResponse(
    val success: Boolean,
    val message: String,
    val data: BookingDetail
)

data class BookingDetail(
    @SerializedName("id") val id: Int,
    @SerializedName("booking_code") val booking_code: String,
    @SerializedName("destinasi") val destinasi: DestinasiBookingDetail?,
    @SerializedName("tanggal_berangkat") val tanggal_berangkat: String,
    @SerializedName("jumlah_tiket") val jumlah_tiket: Int,
    @SerializedName("total_harga") val total_harga: Double,
    @SerializedName("status") val status: String,
    @SerializedName("payment_status") val payment_status: String,
    @SerializedName("payment_method") val payment_method: String?,
    @SerializedName("customer_name") val customer_name: String,
    @SerializedName("customer_email") val customer_email: String,
    @SerializedName("customer_phone") val customer_phone: String,
    @SerializedName("special_requests") val special_requests: String?,
    @SerializedName("qr_code") val qr_code: String?,
    @SerializedName("created_at") val created_at: String?
)

data class DestinasiBookingDetail(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("location") val location: String,
    @SerializedName("description") val description: String?,
    @SerializedName("image") val image: String?,
    @SerializedName("price") val price: Double,
    @SerializedName("open_time") val open_time: String?,
    @SerializedName("close_time") val close_time: String?
)

data class BookingRequest(
    val destinasi_id: Int,
    val tanggal_berangkat: String,
    val jumlah_tiket: Int,
    val customer_name: String,
    val customer_email: String,
    val customer_phone: String,
    val special_requests: String?
)

data class BookingCreateResponse(
    val success: Boolean,
    val message: String,
    val data: BookingCreateData
)

data class BookingCreateData(
    val id: Int,
    val booking_code: String,
    val total_harga: Double,
    val status: String
)

data class BaseResponse(
    val success: Boolean,
    val message: String,
    val data: Any?
)