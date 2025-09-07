package com.example.adspay.models.auth

data class OtpValidateResponse(
    val resp_code: String,
    val resp_message: String,
    val data: OtpValidateData?
)

data class OtpValidateData(
    val registrationToken: String
)
