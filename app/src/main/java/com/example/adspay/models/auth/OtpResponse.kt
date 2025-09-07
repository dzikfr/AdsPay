package com.example.adspay.models.auth

data class OtpResponse(
    val resp_code: String,
    val resp_message: String,
    val data: Data
)

data class Data(
    val phoneNumber: String,
)
