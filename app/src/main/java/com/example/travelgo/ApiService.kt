package com.example.travelgo

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("api/destinasi")
    fun getDestinasiList(): Call<DestinasiResponse>

    @GET("api/destinasi/{id}")
    fun getDestinasiById(@Path("id") id: Int): Call<DestinasiDetailResponse>
}