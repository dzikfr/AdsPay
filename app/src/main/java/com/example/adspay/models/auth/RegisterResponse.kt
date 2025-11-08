package com.example.adspay.models.auth

import com.google.gson.annotations.SerializedName

data class UserData(
    @SerializedName("userId")
    val userId: String,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("status")
    val status: String
)