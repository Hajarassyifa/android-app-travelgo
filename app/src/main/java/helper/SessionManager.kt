package com.example.travelgo.helper

import android.content.Context

class SessionManager(context: Context) {

    private val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor = pref.edit()

    companion object {
        private const val PREF_NAME = "TravelGoSession"
        private const val KEY_TOKEN = "token"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_FOTO = "user_foto"
    }

    fun saveSession(token: String, userId: Int, name: String, email: String, foto: String?) {
        editor.putString(KEY_TOKEN, "Bearer $token")
        editor.putInt(KEY_USER_ID, userId)
        editor.putString(KEY_USER_NAME, name)
        editor.putString(KEY_USER_EMAIL, email)
        editor.putString(KEY_USER_FOTO, foto ?: "")
        editor.apply()
    }

    fun getToken(): String = pref.getString(KEY_TOKEN, "") ?: ""
    fun getUserId(): Int = pref.getInt(KEY_USER_ID, -1)
    fun getUserName(): String = pref.getString(KEY_USER_NAME, "") ?: ""
    fun getUserEmail(): String = pref.getString(KEY_USER_EMAIL, "") ?: ""
    fun getUserFoto(): String = pref.getString(KEY_USER_FOTO, "") ?: ""
    fun isLoggedIn(): Boolean = getToken().isNotEmpty()

    fun clearSession() {
        editor.clear()
        editor.apply()
    }
}