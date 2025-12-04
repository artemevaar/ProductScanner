package com.example.productscanner.DataBase

import android.content.Context
import com.example.productscanner.Firebase.getUserIdFromPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun deleteRestrictions(context: Context) {
    val userId = getUserIdFromPreferences(context) ?: return
    val database = DatabaseProvider.getDatabase(context)

    CoroutineScope(Dispatchers.IO).launch {
        database.userRestrictionsDao().deleteRestrictionsByUserId(userId)
    }
}

fun saveRestrictions(context: Context, restrictions: List<String>) {
    val userId = getUserIdFromPreferences(context) ?: return
    val database = DatabaseProvider.getDatabase(context)

    val userRestrictions = UserRestrictions(
        userId = userId,
        restrictions = restrictions.joinToString(",")
    )

    CoroutineScope(Dispatchers.IO).launch {
        database.userRestrictionsDao().insertOrUpdateRestrictions(userRestrictions)
    }
}

suspend fun getRestrictions(context: Context): String? {
    val userId = getUserIdFromPreferences(context) ?: return null
    val database = DatabaseProvider.getDatabase(context)
    return database.userRestrictionsDao().getRestrictionsByUserId(userId)
}

