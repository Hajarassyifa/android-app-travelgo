package com.example.travelgo

import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    // DESTINASI
    @GET("api/destinasi")
    fun getDestinasiList(): Call<DestinasiResponse>

    @GET("api/destinasi/{id}")
    fun getDestinasiById(@Path("id") id: Int): Call<DestinasiDetailResponse>

    // ARTIKEL
    @GET("api/artikel")
    fun getArtikels(
        @Query("page") page: Int,
        @Query("category") category: String?,
        @Query("search") search: String?
    ): Call<ArtikelResponse>

    @GET("api/artikel/{id}")
    fun getArtikelDetail(@Path("id") id: Int): Call<ArtikelDetailResponse>

    // BOOKING
    @GET("api/booking")
    fun getBookings(): Call<BookingListResponse>

    @GET("api/booking/{id}")
    fun getBookingDetail(@Path("id") id: Int): Call<BookingDetailResponse>

    @POST("api/booking")
    fun createBooking(@Body request: BookingRequest): Call<BookingCreateResponse>

    @DELETE("api/booking/{id}")
    fun cancelBooking(@Path("id") id: Int): Call<BaseResponse>
}