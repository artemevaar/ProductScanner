package com.example.productscanner.DataBase

import android.content.Context
import com.example.productscanner.Firebase.getUserIdFromPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun saveProductRating(
    productId: String,
    ingredients: String,
    rating: Int,
    context: Context
) {
    val userId = getUserIdFromPreferences(context) ?: return

    val database = DatabaseProvider.getDatabase(context)

    val userRatingData = UserProductRating(
        userId = userId,
        productId = productId,
        ingredients = ingredients,
        rating = rating
    )

    CoroutineScope(Dispatchers.IO).launch {
        database.userProductRating().insertOrUpdateRating(userRatingData)
    }
}
