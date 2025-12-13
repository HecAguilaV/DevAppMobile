package com.example.sigaapp.data.local

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("siga_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_ROLE = "user_role"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_PERMISSIONS = "permissions"
    }

    fun saveAuthSession(token: String, userId: Int, role: String, nombre: String?) {
        prefs.edit().apply {
            putString(KEY_ACCESS_TOKEN, token)
            putInt(KEY_USER_ID, userId)
            putString(KEY_USER_ROLE, role)
            putString(KEY_USER_NAME, nombre)
        }.apply()
    }

    fun savePermissions(permissions: List<String>) {
        prefs.edit().putStringSet(KEY_PERMISSIONS, permissions.toSet()).apply()
    }

    fun getAccessToken(): String? = prefs.getString(KEY_ACCESS_TOKEN, null)
    
    fun getUserId(): Int = prefs.getInt(KEY_USER_ID, -1)
    
    fun getUserRole(): String? = prefs.getString(KEY_USER_ROLE, null)

    fun getPermissions(): List<String> {
        return prefs.getStringSet(KEY_PERMISSIONS, emptySet())?.toList() ?: emptyList()
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }

    fun isLoggedIn(): Boolean {
        return getAccessToken() != null
    }
}
