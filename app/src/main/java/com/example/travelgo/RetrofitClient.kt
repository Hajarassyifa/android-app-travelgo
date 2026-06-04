package com.example.travelgo

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // GANTI IP INI dengan IP komputer backend kamu
    private const val BASE_URL = "http://192.168.100.30:8000/"  // ← Ganti dengan IP yang benar

    // Atau pakai localhost jika emulator dan backend di komputer sama:
    // private const val BASE_URL = "http://10.0.2.2:8000/"

    // Atau pakai 10.0.2.2 untuk emulator Android Studio:
    // private const val BASE_URL = "http://10.0.2.2:8000/"

    val instance: ApiService by lazy {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(ApiService::class.java)
    }
}