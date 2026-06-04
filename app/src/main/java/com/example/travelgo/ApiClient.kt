package com.example.travelgo

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    // ─── KONFIGURASI IP ADDRESS (SUDAH DITAMBAHKAN /api/) ───────────
    // Cek IP PC kamu dengan mengetik 'ipconfig' di Command Prompt (CMD)
    private const val BASE_URL_EMULATOR = "http://10.0.2.2:8000/api/"
    private const val BASE_URL_DEVICE   = "http://192.168.100.12:8000/api/" // ← Sesuai IP WiFi Laptop/PC kamu

    // ─── AKTIFKAN SESUAI UNIT PERANGKAT YANG KAMU GUNAKAN ────────────
    // private const val BASE_URL = BASE_URL_EMULATOR  // Aktifkan jika pakai Emulator bawaan
    private const val BASE_URL = BASE_URL_DEVICE       // Aktifkan jika running langsung di HP fisik (Infinix)

    // Interceptor untuk memantau lalu lintas data (request & response) di Logcat
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Mengonfigurasi batas waktu tunggu (timeout) koneksi jaringan sebesar 30 detik
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    // Inisialisasi library Retrofit Builder
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Endpoint API Service yang siap dipanggil di seluruh Activity proyekmu
    val apiService: ApiService = retrofit.create(ApiService::class.java)
}