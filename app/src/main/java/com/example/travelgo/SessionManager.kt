package com.example.travelgo

import android.content.Context

object SessionManager {

    private const val PREF_NAME = "USER_SESSION"
    private const val KEY_TOKEN = "TOKEN"
    private const val KEY_NAME  = "NAMA"
    private const val KEY_EMAIL = "EMAIL"
    private const val KEY_ID    = "USER_ID"

    fun saveSession(context: Context, token: String, name: String, email: String, id: Int = 0) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
            .putString(KEY_TOKEN, token)
            .putString(KEY_NAME,  name)
            .putString(KEY_EMAIL, email)
            .putInt(KEY_ID,       id)
            .apply()
    }

    fun getToken(context: Context): String? =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_TOKEN, null)

    fun getName(context: Context): String =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_NAME, "Traveler") ?: "Traveler"

    fun getEmail(context: Context): String =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_EMAIL, "") ?: ""

    fun getUserId(context: Context): Int =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getInt(KEY_ID, 0)

    fun isLoggedIn(context: Context): Boolean = getToken(context) != null

    fun clearSession(context: Context) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
            .clear()
            .apply()
    }
}