package com.example.adspay.models.promotion

data class Promotion(
    val id: String,
    val imageUrl: String,
    val title: String? = null,
    val description: String? = null,
    val link: String? = null
)

data class PromotionCategory(
    val name: String,
    val promotions: List<Promotion>
)
