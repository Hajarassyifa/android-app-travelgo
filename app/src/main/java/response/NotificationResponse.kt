// ============================================================
// FILE: model/response/NotificationResponse.kt
// ============================================================
package com.example.travelgo.model.response

import com.google.gson.annotations.SerializedName

data class NotificationResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("judul") val judul: String?,
    @SerializedName("pesan") val pesan: String?,
    @SerializedName("is_read") val isRead: Boolean?,
    @SerializedName("created_at") val createdAt: String?
)