// ============================================================
// FILE: model/response/AuthResponse.kt
// ============================================================
package com.example.travelgo.model.response

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("message") val message: String?,
    @SerializedName("token") val token: String?,
    @SerializedName("user") val user: UserResponse?
)