package com.example.adspay.models.notification

data class FcmTokenRequest(
    val userId: String,
    val token: String
)

data class FcmTokenDataResponse(
    val status: String?,
)
