// ============================================================
// FILE: model/response/ReviewResponse.kt
// ============================================================
package com.example.travelgo.model.response

import com.google.gson.annotations.SerializedName

data class ReviewResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("rating") val rating: Int?,
    @SerializedName("komentar") val komentar: String?,
    @SerializedName("user") val user: UserResponse?,
    @SerializedName("travel_package_id") val travelPackageId: Int?,
    @SerializedName("created_at") val createdAt: String?
)