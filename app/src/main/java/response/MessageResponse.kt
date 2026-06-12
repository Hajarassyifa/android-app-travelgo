// ============================================================
// FILE: model/response/MessageResponse.kt
// ============================================================
package com.example.travelgo.model.response

import com.google.gson.annotations.SerializedName

data class MessageResponse(
    @SerializedName("message") val message: String?
)