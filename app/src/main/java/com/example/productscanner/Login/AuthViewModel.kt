package com.example.productscanner.Login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    fun updateEmail(newEmail: String) {
        email = newEmail
    }

    fun updatePassword(newPassword: String) {
        password = newPassword
    }

    init {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            email = currentUser.email ?: ""
            password = currentUser.displayName ?: ""
        }
    }

    fun getCurrentUserInfo() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            email = currentUser.email ?: ""
            password = currentUser.displayName ?: ""
        }
    }
}

fun isValidPassword(password: String): Boolean {
    val passwordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#\$%^&+=!]).{8,}$".toRegex()
    return password.matches(passwordRegex)
}
