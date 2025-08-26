package com.example.adspay.services

import android.content.Context
import com.example.adspay.models.promotion.PromotionCategory
import retrofit2.http.GET

interface PromotionApi {
    @GET("promotions")
    suspend fun getPromotions(): List<PromotionCategory>
}

class PromotionService(context: Context) {
    private val api = ApiClient.create(context).create(PromotionApi::class.java)

    suspend fun getPromotions(): List<PromotionCategory> {
        return api.getPromotions()
    }
}
