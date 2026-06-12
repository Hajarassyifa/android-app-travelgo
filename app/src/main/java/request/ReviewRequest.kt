// ============================================================
// FILE: model/request/ReviewRequest.kt
// ============================================================
package com.example.travelgo.model.request

import com.google.gson.annotations.SerializedName

data class ReviewRequest(
    @SerializedName("rating") val rating: Int,
    @SerializedName("komentar") val komentar: String
)