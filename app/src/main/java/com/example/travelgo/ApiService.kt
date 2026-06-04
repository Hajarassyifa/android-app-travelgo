package com.example.travelgo

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    // ======================
    // AUTHENTICATION
    // ======================
    @POST("login")
    fun login(@Body request: LoginRequest): Call<AuthResponse>

    @POST("register")
    fun register(@Body request: RegisterRequest): Call<AuthResponse>


    // ======================
    // DESTINASI (Ditambahkan Token Pengaman)
    // ======================
    @GET("destinasi")
    fun getDestinasis(
        @Header("Authorization") token: String?,
        @Query("page") page: Int,
        @Query("kategori") kategori: String?,
        @Query("search") search: String?
    ): Call<DestinasiResponse>

    @GET("destinasi/{id}")
    fun getDestinasiById(
        @Header("Authorization") token: String?,
        @Path("id") id: Int
    ): Call<DestinasiDetailResponse>


    // ======================
    // ARTIKEL (Ditambahkan Token Pengaman)
    // ======================
    @GET("artikel")
    fun getArtikels(
        @Header("Authorization") token: String?,
        @Query("page") page: Int,
        @Query("kategori") kategori: String?,
        @Query("search") search: String?
    ): Call<ArtikelResponse>

    @GET("artikel/{id}")
    fun getArtikelDetail(
        @Header("Authorization") token: String?,
        @Path("id") id: Int
    ): Call<ArtikelDetailResponse>


    // ======================
    // BOOKING
    // ======================
    @GET("booking")
    fun getBookings(
        @Header("Authorization") token: String?
    ): Call<BookingListResponse>

    @GET("booking/{id}")
    fun getBookingDetail(
        @Header("Authorization") token: String?,
        @Path("id") id: Int
    ): Call<BookingDetailResponse>

    @POST("booking")
    fun createBooking(
        @Header("Authorization") token: String?,
        @Body request: BookingRequest
    ): Call<BookingCreateResponse>


    // ======================
    // TRANSAKSI
    // ======================
    @POST("transaksi")
    fun createTransaksi(
        @Header("Authorization") token: String?,
        @Body request: TransaksiRequest
    ): Call<BaseResponse>

    @GET("transaksi")
    fun getTransaksiList(
        @Header("Authorization") token: String?
    ): Call<TransaksiListResponse>

    @GET("transaksi/{id}")
    fun getTransaksiDetail(
        @Header("Authorization") token: String?,
        @Path("id") id: Int
    ): Call<TransaksiDetailResponse>


    // ======================
    // REVIEWS
    // ======================
    @GET("review")
    fun getReviews(
        @Header("Authorization") token: String?
    ): Call<ReviewListResponse>

    @GET("my-review")
    fun getMyReviews(
        @Header("Authorization") token: String?
    ): Call<ReviewListResponse>

    @POST("review")
    fun createReview(
        @Header("Authorization") token: String?,
        @Body request: ReviewRequest
    ): Call<ReviewResponse>

    @PUT("review/{id}")
    fun updateReview(
        @Header("Authorization") token: String?,
        @Path("id") id: Int,
        @Body body: Map<String, String>
    ): Call<ReviewResponse>

    @DELETE("review/{id}")
    fun deleteReview(
        @Header("Authorization") token: String?,
        @Path("id") id: Int
    ): Call<BaseResponse>


    // ======================
    // PROFILE
    // ======================
    @GET("profile")
    fun getProfile(
        @Header("Authorization") token: String?
    ): Call<ProfileResponse>

    @PUT("profile")
    fun updateProfile(
        @Header("Authorization") token: String?,
        @Body request: UpdateProfileRequest
    ): Call<ProfileResponse>

    @Multipart
    @POST("profile/photo")
    fun updatePhoto(
        @Header("Authorization") token: String?,
        @Part photo: MultipartBody.Part
    ): Call<ProfileResponse>


    // ======================
    // NOTIFICATIONS
    // ======================
    @GET("notifications")
    fun getNotifications(
        @Header("Authorization") token: String?
    ): Call<NotificationListResponse>

    @POST("notifications/{id}/read")
    fun markNotificationRead(
        @Header("Authorization") token: String?,
        @Path("id") id: Int
    ): Call<BaseResponse>

    @POST("notifications/read-all")
    fun markAllNotificationsRead(
        @Header("Authorization") token: String?
    ): Call<BaseResponse>
}