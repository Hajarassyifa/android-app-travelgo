// ============================================================
// FILE: model/response/UserResponse.kt
// ============================================================
package com.example.travelgo.model.response

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("foto") val foto: String?,
    @SerializedName("created_at") val createdAt: String?
)