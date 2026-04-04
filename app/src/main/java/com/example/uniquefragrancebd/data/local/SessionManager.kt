package com.example.uniquefragrancebd.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(@ApplicationContext context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    fun saveAuthToken(token: String) {
        prefs.edit { putString("auth_token", token) }
    }

    fun getAuthToken(): String? = prefs.getString("auth_token", null)

    fun saveUser(userId: String, email: String, name: String) {
        prefs.edit {
            putString("user_id", userId)
            putString("user_email", email)
            putString("user_name", name)
        }
    }

    fun getUserId(): String? = prefs.getString("user_id", null)
    fun getUserEmail(): String? = prefs.getString("user_email", null)
    fun getUserName(): String? = prefs.getString("user_name", null)

    fun clearSession() {
        prefs.edit { clear() }
    }
}