package com.example.travelgo

import com.google.gson.annotations.SerializedName

data class ArtikelResponse(
    val success: Boolean,
    val message: String,
    val data: List<Artikel>,
    val pagination: Pagination?
)

data class Artikel(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("slug") val slug: String,
    @SerializedName("excerpt") val excerpt: String?,
    @SerializedName("image") val image: String?,
    @SerializedName("category") val category: String,
    @SerializedName("author") val author: String,
    @SerializedName("views") val views: Int,
    @SerializedName("published_at") val published_at: String?
)

data class ArtikelDetailResponse(
    val success: Boolean,
    val message: String,
    val data: ArtikelDetail
)

data class ArtikelDetail(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("slug") val slug: String,
    @SerializedName("content") val content: String,
    @SerializedName("excerpt") val excerpt: String?,
    @SerializedName("image") val image: String?,
    @SerializedName("category") val category: String,
    @SerializedName("author") val author: String,
    @SerializedName("views") val views: Int,
    @SerializedName("published_at") val published_at: String?,
    @SerializedName("created_at") val created_at: String?
)

data class Pagination(
    val current_page: Int,
    val last_page: Int,
    val per_page: Int,
    val total: Int
)