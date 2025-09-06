package com.example.adspay.models.auth

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("access_token")
    val accessToken: String,

    @SerializedName("expires_in")
    val expiresIn: Int,

    @SerializedName("refresh_expires_in")
    val refreshExpiresIn: Int,

    @SerializedName("refresh_token")
    val refreshToken: String,

    @SerializedName("token_type")
    val tokenType: String,

    @SerializedName("session_state")
    val sessionState: String,

    val scope: String
)
