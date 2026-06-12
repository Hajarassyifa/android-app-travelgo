package com.example.travelgo.api

import com.example.travelgo.model.request.*
import com.example.travelgo.model.response.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    companion object {
        val api: ApiService by lazy {
            ApiClient.instance.create(ApiService::class.java)
        }
    }

    // ==================== AUTH ====================
    @POST("register")
    fun register(@Body body: RegisterRequest): Call<AuthResponse>

    @POST("login")
    fun login(@Body body: LoginRequest): Call<AuthResponse>

    @POST("logout")
    fun logout(@Header("Authorization") token: String): Call<MessageResponse>

    @GET("user")
    fun getUser(@Header("Authorization") token: String): Call<UserResponse>

    @PUT("change-password")
    fun changePassword(
        @Header("Authorization") token: String,
        @Body body: ChangePasswordRequest
    ): Call<MessageResponse>

    // ==================== DESTINASI ====================
    @GET("destinasi")
    fun getAllDestinasi(): Call<List<DestinasiResponse>>

    @GET("destinasi/{id}")
    fun getDestinasiById(@Path("id") id: Int): Call<DestinasiResponse>

    // ==================== TRAVEL PACKAGES ====================
    @GET("travel-packages")
    fun getAllTravelPackages(): Call<List<TravelPackageResponse>>

    @GET("travel-packages/{id}")
    fun getTravelPackageById(@Path("id") id: Int): Call<TravelPackageResponse>

    @POST("travel-packages")
    fun createTravelPackage(
        @Header("Authorization") token: String,
        @Body body: TravelPackageRequest
    ): Call<TravelPackageResponse>

    @PUT("travel-packages/{id}")
    fun updateTravelPackage(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body body: TravelPackageRequest
    ): Call<TravelPackageResponse>

    @DELETE("travel-packages/{id}")
    fun deleteTravelPackage(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Call<MessageResponse>

    // ==================== BOOKING ====================
    @GET("booking")
    fun getAllBooking(@Header("Authorization") token: String): Call<List<BookingResponse>>

    @GET("booking/{id}")
    fun getBookingById(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Call<BookingResponse>

    @POST("booking")
    fun createBooking(
        @Header("Authorization") token: String,
        @Body body: BookingRequest
    ): Call<BookingResponse>

    @PUT("booking/{id}")
    fun updateBooking(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body body: BookingRequest
    ): Call<BookingResponse>

    @DELETE("booking/{id}")
    fun deleteBooking(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Call<MessageResponse>

    // ==================== TRANSAKSI ====================
    @GET("transaksi")
    fun getAllTransaksi(@Header("Authorization") token: String): Call<List<TransaksiResponse>>

    @GET("transaksi/{id}")
    fun getTransaksiById(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Call<TransaksiResponse>

    @POST("transaksi")
    fun createTransaksi(
        @Header("Authorization") token: String,
        @Body body: TransaksiRequest
    ): Call<TransaksiResponse>

    @PUT("transaksi/{id}")
    fun updateTransaksi(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body body: TransaksiRequest
    ): Call<TransaksiResponse>

    @DELETE("transaksi/{id}")
    fun deleteTransaksi(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Call<MessageResponse>

    // ==================== ARTIKEL ====================
    @GET("artikel")
    fun getAllArtikel(): Call<List<ArtikelResponse>>

    @GET("artikel/{id}")
    fun getArtikelById(@Path("id") id: Int): Call<ArtikelResponse>

    @POST("artikel")
    fun createArtikel(
        @Header("Authorization") token: String,
        @Body body: ArtikelResponse
    ): Call<ArtikelResponse>

    @PUT("artikel/{id}")
    fun updateArtikel(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body body: ArtikelResponse
    ): Call<ArtikelResponse>

    @DELETE("artikel/{id}")
    fun deleteArtikel(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Call<MessageResponse>

    // ==================== PROFILE ====================
    @GET("profile")
    fun getProfile(@Header("Authorization") token: String): Call<ProfileResponse>

    @PUT("profile")
    fun updateName(
        @Header("Authorization") token: String,
        @Body body: UpdateNameRequest
    ): Call<ProfileResponse>

    @Multipart
    @POST("profile/photo")
    fun updatePhoto(
        @Header("Authorization") token: String,
        @Part photo: MultipartBody.Part
    ): Call<ProfileResponse>

    // ==================== NOTIFICATIONS ====================
    @GET("notifications")
    fun getNotifications(@Header("Authorization") token: String): Call<List<NotificationResponse>>

    @PUT("notifications/read-all")
    fun markAllRead(@Header("Authorization") token: String): Call<MessageResponse>

    @PUT("notifications/{id}/read")
    fun markRead(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Call<MessageResponse>

    // ==================== REVIEWS ====================
    @GET("packages/{packageId}/reviews")
    fun getReviews(@Path("packageId") packageId: Int): Call<List<ReviewResponse>>

    @POST("packages/{packageId}/reviews")
    fun createReview(
        @Header("Authorization") token: String,
        @Path("packageId") packageId: Int,
        @Body body: ReviewRequest
    ): Call<ReviewResponse>

    @PUT("reviews/{id}")
    fun updateReview(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body body: ReviewRequest
    ): Call<ReviewResponse>

    @DELETE("reviews/{id}")
    fun deleteReview(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Call<MessageResponse>

    @GET("reviews/my")
    fun getMyReviews(@Header("Authorization") token: String): Call<List<ReviewResponse>>
}