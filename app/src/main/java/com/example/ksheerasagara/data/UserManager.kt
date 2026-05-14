package com.example.ksheerasagara.data

import android.content.Context

class UserManager(context: Context) {

    private val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun registerUser(email: String, password: String) {
        prefs.edit()
            .putString("email", email)
            .putString("password", password)
            .apply()
    }

    fun loginUser(email: String, password: String): Boolean {
        val savedEmail = prefs.getString("email", null)
        val savedPassword = prefs.getString("password", null)

        return email == savedEmail && password == savedPassword
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean("isLoggedIn", false)
    }

    fun setLoggedIn(value: Boolean) {
        prefs.edit().putBoolean("isLoggedIn", value).apply()
    }

    fun logout() {
        prefs.edit().putBoolean("isLoggedIn", false).apply()
    }

    fun register(email: String, password: String): Boolean {
        val savedEmail = prefs.getString("email", null)
        if (savedEmail != null && savedEmail == email) {
            return false
        }
        prefs.edit()
            .putString("email", email)
            .putString("password", password)
            .apply()
        return true
    }
}