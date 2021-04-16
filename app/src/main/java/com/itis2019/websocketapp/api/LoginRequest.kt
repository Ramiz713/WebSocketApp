package com.itis2019.websocketapp.api

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    val username: String,
    @SerializedName("device_id")
    val deviceId: String
)
