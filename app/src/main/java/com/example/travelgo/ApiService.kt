package com.example.travelgo

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    // ─── DESTINASI ────────────────────────────────────────────────

    @GET("api/destinasi")
    fun getDestinasiList(): Call<DestinasiResponse>

    @GET("api/destinasi/{id}")
    fun getDestinasiById(
        @Path("id") id: Int
    ): Call<DestinasiDetailResponse>

    // ─── ARTIKEL ──────────────────────────────────────────────────

    @GET("api/artikel")
    fun getArtikels(
        @Query("page") page: Int,
        @Query("category") category: String?,
        @Query("search") search: String?
    ): Call<ArtikelResponse>

    @GET("api/artikel/{id}")
    fun getArtikelDetail(
        @Path("id") id: Int
    ): Call<ArtikelDetailResponse>

    // ─── BOOKING ──────────────────────────────────────────────────

    @GET("api/booking")
    fun getBookings(
        @Header("Authorization") token: String
    ): Call<BookingListResponse>

    @GET("api/booking/{id}")
    fun getBookingDetail(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Call<BookingDetailResponse>

    @POST("api/booking")
    fun createBooking(
        @Header("Authorization") token: String,
        @Body request: BookingRequest
    ): Call<BookingCreateResponse>

    @DELETE("api/booking/{id}")
    fun cancelBooking(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Call<BaseResponse>

    // ─── AUTH ─────────────────────────────────────────────────────

    @POST("api/login")
    fun login(
        @Body request: LoginRequest
    ): Call<AuthResponse>

    @POST("api/register")
    fun register(
        @Body request: RegisterRequest
    ): Call<AuthResponse>

    @POST("api/logout")
    fun logout(
        @Header("Authorization") token: String
    ): Call<BaseResponse>

    // ─── PROFILE ──────────────────────────────────────────────────

    @GET("api/profile")
    fun getProfile(
        @Header("Authorization") token: String
    ): Call<ProfileResponse>

    @PUT("api/profile")
    fun updateProfile(
        @Header("Authorization") token: String,
        @Body body: Map<String, String>
    ): Call<ProfileResponse>

    @Multipart
    @POST("api/profile/photo")
    fun updatePhoto(
        @Header("Authorization") token: String,
        @Part photo: MultipartBody.Part
    ): Call<ProfileResponse>

    @PUT("api/change-password")
    fun changePassword(
        @Header("Authorization") token: String,
        @Body body: Map<String, String>
    ): Call<BaseResponse>

    // ─── TRANSAKSI ────────────────────────────────────────────────

    @GET("api/transaksi")
    fun getTransaksiList(
        @Header("Authorization") token: String
    ): Call<TransaksiListResponse>

    @GET("api/transaksi/{id}")
    fun getTransaksiDetail(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Call<TransaksiDetailResponse>

    @POST("api/transaksi")
    fun createTransaksi(
        @Header("Authorization") token: String,
        @Body request: TransaksiRequest
    ): Call<BaseResponse>

    // ─── NOTIFICATIONS ────────────────────────────────────────────

    @GET("api/notifications")
    fun getNotifications(
        @Header("Authorization") token: String
    ): Call<NotificationListResponse>

    @PUT("api/notifications/read-all")
    fun markAllNotificationsRead(
        @Header("Authorization") token: String
    ): Call<BaseResponse>

    @PUT("api/notifications/{id}/read")
    fun markNotificationRead(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Call<BaseResponse>

    // ─── REVIEWS ──────────────────────────────────────────────────

    @GET("api/packages/{packageId}/reviews")
    fun getReviews(
        @Path("packageId") packageId: Int
    ): Call<ReviewListResponse>

    @POST("api/packages/{packageId}/reviews")
    fun createReview(
        @Header("Authorization") token: String,
        @Path("packageId") packageId: Int,
        @Body body: Map<String, String>
    ): Call<ReviewResponse>

    @PUT("api/reviews/{id}")
    fun updateReview(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body body: Map<String, String>
    ): Call<ReviewResponse>

    @DELETE("api/reviews/{id}")
    fun deleteReview(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Call<BaseResponse>

    @GET("api/reviews/my")
    fun getMyReviews(
        @Header("Authorization") token: String
    ): Call<ReviewListResponse>
}