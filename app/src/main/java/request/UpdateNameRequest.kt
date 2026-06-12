// ============================================================
// FILE: model/request/UpdateNameRequest.kt
// ============================================================
package com.example.travelgo.model.request

import com.google.gson.annotations.SerializedName

data class UpdateNameRequest(
    @SerializedName("name") val name: String
)