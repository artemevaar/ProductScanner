package com.example.productscanner.Firebase

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

fun fetchProductsFromFavorites(
    userId: String,
    onSuccess: (List<FavProduct>) -> Unit,
    onFailure: (Exception) -> Unit
) {
    val db = Firebase.firestore
    db.collection("users")
        .document(userId)
        .collection("favorites")
        .get()
        .addOnSuccessListener { result ->
            val products = result.mapNotNull { document ->
                Log.d("MyTagScan", "1 DOCUMENT ${document}")
                val remote = document.toObject(FavProduct::class.java)
                FavProduct(
                    productId = remote.productId,
                    productName = remote.productName,
                    ingredientsText = remote.ingredientsText,
                    imageUrl = remote.imageUrl,
                    isFavorite = true
                )

            }
            onSuccess(products)
            Log.d("MyTagScan", " 2 FAV ${products}")
        }

        .addOnFailureListener { exception ->
            onFailure(exception)
        }
}
