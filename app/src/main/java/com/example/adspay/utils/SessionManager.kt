package com.example.adspay.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class SessionManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    fun saveAuthSession(accessToken: String, refreshToken: String, expiresIn: Int) {
        prefs.edit {
            putString("access_token", accessToken)
            putString("refresh_token", refreshToken)
            putLong("expires_at", System.currentTimeMillis() + (expiresIn * 1000))
        }
    }

    fun getExpiresInSeconds(): Long {
        val expiresAt = prefs.getLong("expires_at", 0L)
        val remainingMillis = expiresAt - System.currentTimeMillis()
        return remainingMillis / 1000
    }

    fun getAccessToken(): String? = prefs.getString("access_token", null)

    fun getRefreshToken(): String? = prefs.getString("refresh_token", null)

    fun isAccessTokenExpired(): Boolean {
        val expiresAt = prefs.getLong("expires_at", 0L)
        return System.currentTimeMillis() >= expiresAt
    }

    fun clearSession() {
        prefs.edit { clear() }
    }

    fun isLoggedIn(): Boolean {
        return !getAccessToken().isNullOrEmpty() && !isAccessTokenExpired()
    }
}
