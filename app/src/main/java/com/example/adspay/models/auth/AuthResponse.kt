package com.example.adspay.models.auth

import com.google.gson.annotations.SerializedName

data class AuthData(
    @SerializedName("access_token")
    val accessToken: String,

    @SerializedName("refresh_token")
    val refreshToken: String,

    @SerializedName("expires_in")
    val expiresIn: Int,

    @SerializedName("refresh_expires_in")
    val refreshExpiresIn: Int,

    @SerializedName("token_type")
    val tokenType: String,

    @SerializedName("id_token")
    val idToken: String?,

    val scope: String
)
