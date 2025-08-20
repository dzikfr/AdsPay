package com.example.adspay.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class SessionManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    fun saveUserSession(token: String, userId: String, username: String) {
        prefs.edit().apply {
            putString("token", token)
            putString("userId", userId)
            putString("username", username)
            apply()
        }
    }

    fun getToken(): String? {
        return prefs.getString("token", null)
    }


    fun isLoggedIn(): Boolean {
        return !prefs.getString("token", null).isNullOrEmpty()
    }

    fun clearSession() {
        prefs.edit { clear() }
    }
}
