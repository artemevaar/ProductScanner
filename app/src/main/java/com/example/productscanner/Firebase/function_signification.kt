package com.example.productscanner.Firebase

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

fun signUp(
    auth: FirebaseAuth,
    email: String,
    password: String,
    context: Context,
    onResult: (Boolean, String?) -> Unit
) {
    auth.fetchSignInMethodsForEmail(email)
        .addOnCompleteListener { fetchTask ->
            if (fetchTask.isSuccessful) {
                val signInMethods = fetchTask.result?.signInMethods
                if (signInMethods.isNullOrEmpty()) {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { signUpTask ->
                            if (signUpTask.isSuccessful) {
                                val userId = auth.currentUser?.uid ?: ""
                                Toast.makeText(context, "Регистрация успешна", Toast.LENGTH_SHORT)
                                    .show()
                                onResult(true, userId)

                            } else {
                                val exception = signUpTask.exception
                                val errorMessage = "Ошибка при регистрации: ${exception?.message}"
                                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                                onResult(false, errorMessage)
                            }
                        }
                } else {
                    val errorMessage = "Пользователь с таким email уже существует"
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    onResult(false, errorMessage)
                }
            } else {
                val exception = fetchTask.exception
                val errorMessage = "Произошла ошибка: ${exception?.message}"
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                onResult(false, errorMessage)
            }
        }
}

fun signIn(
    auth: FirebaseAuth,
    email: String,
    password: String,
    context: Context,
    onResult: (Boolean, String?) -> Unit
) {
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userId = auth.currentUser?.uid
                Toast.makeText(context, "Авторизация успешна", Toast.LENGTH_SHORT).show()
                onResult(true, userId)
            } else {
                val exception = task.exception
                val errorMessage = when (exception) {
                    is FirebaseAuthInvalidUserException -> "Пользователь с таким email не найден"
                    is FirebaseAuthInvalidCredentialsException -> "Неверный пароль"
                    else -> "Произошла ошибка: ${exception?.message}"
                }
                Toast.makeText(
                    context,
                    "Проверьте корректность введенных даннных",
                    Toast.LENGTH_SHORT
                ).show()
                onResult(false, errorMessage)
            }
        }
}


fun signOut(auth: FirebaseAuth) {
    auth.signOut()
}

fun deleteAccount(auth: FirebaseAuth, email: String, password: String) {
    val credential = EmailAuthProvider.getCredential(email, password)
    auth.currentUser?.reauthenticate(credential)?.addOnCompleteListener {
        if (it.isSuccessful) {
            auth.currentUser?.delete()?.addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("MyTagScan", "Аккаунт удален")
                } else {
                    Log.d("MyTagScan", "Аккаунт не удален")
                }
            }
        }
    }
}

fun resetPassword(
    auth: FirebaseAuth,
    email: String,
    context: Context,
    onResult: (Boolean, String?) -> Unit
) {
    auth.sendPasswordResetEmail(email)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    context,
                    "Инструкция по сбросу пароля отправлена на $email",
                    Toast.LENGTH_SHORT
                ).show()
                onResult(true, null)
            } else {
                val exception = task.exception
                val errorMessage = when (exception) {
                    is FirebaseAuthInvalidUserException -> "Пользователь с таким email не найден"
                    else -> "Произошла ошибка: ${exception?.message}"
                }
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                onResult(false, errorMessage)
            }
        }
}

fun saveUserIdToPreferences(context: Context, userId: String) {
    val sharedPreferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
    sharedPreferences.edit().putString("USER_ID", userId).apply()
}

fun getUserIdFromPreferences(context: Context): String? {
    val sharedPreferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
    return sharedPreferences.getString("USER_ID", null)
}
