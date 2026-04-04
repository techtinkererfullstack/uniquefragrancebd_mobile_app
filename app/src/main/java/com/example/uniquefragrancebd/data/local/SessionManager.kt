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

    fun saveUser(
        userId: String,
        email: String,
        firstName: String,
        lastName: String,
        phone: String?,
        address: String?
    ) {
        prefs.edit {
            putString("user_id", userId)
            putString("user_email", email)
            putString("first_name", firstName)
            putString("last_name", lastName)
            putString("phone", phone)
            putString("address", address)
        }
    }

    fun getUserId(): String? = prefs.getString("user_id", null)
    fun getUserEmail(): String? = prefs.getString("user_email", null)
    fun getFirstName(): String? = prefs.getString("first_name", null)
    fun getLastName(): String? = prefs.getString("last_name", null)
    fun getPhone(): String? = prefs.getString("phone", null)
    fun getAddress(): String? = prefs.getString("address", null)

    fun clearSession() {
        prefs.edit { clear() }
    }
}