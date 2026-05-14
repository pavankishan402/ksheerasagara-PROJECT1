package com.example.ksheerasagara.data

object SessionManager {

    var isLoggedIn = false
    var currentUser: String? = null

    fun login(email: String) {
        isLoggedIn = true
        currentUser = email
    }

    fun logout() {
        isLoggedIn = false
        currentUser = null
    }
}