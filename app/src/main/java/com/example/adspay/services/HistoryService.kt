package com.example.adspay.services

import android.content.Context
import com.example.adspay.models.history.History
import retrofit2.http.GET

interface HistoryApi {
    @GET("histories")
    suspend fun getHistories(): List<History>
}

class HistoryService(context: Context) {
    private val api = ApiClient.create(context).create(HistoryApi::class.java)

    suspend fun getHistories(): List<History> {
        return api.getHistories()
    }
}
