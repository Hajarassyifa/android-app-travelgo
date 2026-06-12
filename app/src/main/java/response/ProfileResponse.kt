// ============================================================
// FILE: model/response/ProfileResponse.kt
// ============================================================
package com.example.travelgo.model.response

import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("foto") val foto: String?,
    @SerializedName("phone") val phone: String?,
    @SerializedName("created_at") val createdAt: String?
)