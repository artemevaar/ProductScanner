package com.example.productscanner.Firebase

import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log
import com.google.firebase.firestore.SetOptions

fun saveProductRating_firestore(
    userId: String,
    productId: String,
    ingredients: String,
    rating: Int
) {
    val firestore = FirebaseFirestore.getInstance()

    val documentRef = firestore.collection("user_product_rating")
        .document("${userId}_$productId") // Создаём уникальный ID

    val ratingData = hashMapOf(
        "userId" to userId,
        "productId" to productId,
        "ingredients" to ingredients,
        "rating" to rating
    )

    documentRef.set(ratingData, SetOptions.merge())
        .addOnSuccessListener {
            Log.d("Firestore", "Рейтинг успешно сохранён/обновлён")
        }
        .addOnFailureListener { e ->
            Log.w("Firestore", "Ошибка при сохранении/обновлении данных", e)
        }
}
