package com.example.adspay.models.auth

data class RegisterResponse(
    val success: Boolean,
    val message: String,
    val userId: String? = null
)
