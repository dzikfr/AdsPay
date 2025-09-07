package com.example.adspay.models.auth

import java.io.Serializable

data class PartialRegisterData(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val phoneNumber: String
) : Serializable
