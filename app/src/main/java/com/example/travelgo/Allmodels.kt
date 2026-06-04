package com.example.travelgo

import com.google.gson.annotations.SerializedName

// ======================
// AUTH
// ======================
data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val nama: String,           // backend pakai "nama" bukan "name"
    val email: String,
    val password: String,
    val password_confirmation: String
)

data class AuthResponse(
    val status: Boolean,        // backend pakai "status" bukan "success"
    val message: String,
    val token: String?,
    val data: UserData?
)

data class UserData(
    val id: Int,
    val nama: String,           // backend pakai "nama"
    val email: String,
    val role: String?
)

// ======================
// BASE
// ======================
data class BaseResponse(
    val status: Boolean,        // backend pakai "status"
    val message: String,
    val data: Any? = null
)

// ======================
// BOOKING
// ======================
data class BookingListResponse(
    val status: Boolean,
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
    val status: Boolean,
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
    val status: Boolean,
    val message: String,
    val data: BookingCreateData
)

data class BookingCreateData(
    val id: Int,
    val booking_code: String,
    val total_harga: Double,
    val status: String
)

// ======================
// TRANSAKSI
// ======================
data class TransaksiRequest(
    val booking_id: Int,
    val jumlah: Double,
    val metode_pembayaran: String,
    val status: String? = null
)

data class TransaksiListResponse(
    val status: Boolean,
    val data: List<Transaksi>
)

data class TransaksiDetailResponse(
    val status: Boolean,
    val data: Transaksi
)

data class Transaksi(
    val id: Int,
    val booking_id: Int,
    val jumlah: Double,
    val metode_pembayaran: String,
    val status: String,
    val created_at: String?
)

// ======================
// PROFILE
// ======================
data class ProfileResponse(
    val status: Boolean,
    val message: String = "",
    val data: UserProfile
)

data class UserProfile(
    @SerializedName("id") val id: Int,
    @SerializedName("nama") val name: String,   // backend pakai "nama"
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: String?,
    @SerializedName("photo") val photo: String?,
    @SerializedName("role") val role: String?
)

data class UpdateNameRequest(
    val nama: String    // backend pakai "nama"
)

// ======================
// NOTIFICATIONS
// ======================
data class NotificationListResponse(
    val status: Boolean,
    val message: String = "",
    val data: List<Notification>
)

data class Notification(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("message") val message: String,
    @SerializedName("type") val type: String?,
    @SerializedName("is_read") val is_read: Boolean,
    @SerializedName("created_at") val created_at: String?
)

// ======================
// REVIEWS
// ======================
data class ReviewListResponse(
    val status: Boolean,
    val message: String = "",
    val data: List<Review>
)

data class ReviewResponse(
    val status: Boolean,
    val message: String,
    val data: Review
)

data class ReviewRequest(
    val rating: Int,
    val comment: String
)

data class Review(
    @SerializedName("id") val id: Int,
    @SerializedName("rating") val rating: Int,
    @SerializedName("comment") val comment: String?,
    @SerializedName("user") val user: ReviewUser?,
    @SerializedName("destinasi") val destinasi: ReviewDestinasi?,
    @SerializedName("created_at") val created_at: String?
)

data class ReviewUser(
    @SerializedName("id") val id: Int,
    @SerializedName("nama") val name: String,   // backend pakai "nama"
    @SerializedName("photo") val photo: String?
)

data class ReviewDestinasi(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)