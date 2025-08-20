package com.example.adspay.models.promotion

//noinspection SuspiciousImport
import android.R

data class Promotion (
    val id: String,
    val promotionName: String,
    val durationLeft: String,
    val totalPrice: R.integer? = null,
    val discountRate: UByte? = null
)
