package com.example.adspay.models.auth

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("resp_code")
    val respCode: String,

    @SerializedName("resp_message")
    val respMessage: String,

    @SerializedName("data")
    val data: UserData? = null
)

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