package com.example.productscanner.Firebase

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object FavoritesRepository {
    private val db = FirebaseFirestore.getInstance()

    fun toggleFavorite(
        userId: String,
        productId: String,
        productName: String,
        ingredientsText: String,
        imageUrl: String?,
        isCurrentlyFavorite: Boolean,
        onResult: (Boolean) -> Unit
    ) {
        val favoritesRef =
            db.collection("users").document(userId).collection("favorites").document(productId)

        if (isCurrentlyFavorite) {
            favoritesRef.delete().addOnSuccessListener {
                onResult(false)
            }.addOnFailureListener {
                onResult(true)
            }
        } else {
            val data = hashMapOf(
                "productId" to productId,
                "productName" to productName,
                "ingredientsText" to ingredientsText,
                "imageUrl" to imageUrl,
                "timestamp" to FieldValue.serverTimestamp()
            )
            favoritesRef.set(data).addOnSuccessListener {
                onResult(true)
            }.addOnFailureListener {
                onResult(false)
            }
        }
    }

    fun clearAllFavorites(userId: String, onComplete: () -> Unit) {
        val db = Firebase.firestore
        db.collection("users")
            .document(userId)
            .collection("favorites")
            .get()
            .addOnSuccessListener { snapshot ->
                val batch = db.batch()
                for (doc in snapshot.documents) {
                    batch.delete(doc.reference)
                }
                batch.commit().addOnSuccessListener {
                    onComplete()
                }
            }
    }


    fun checkIfFavorite(
        userId: String,
        productId: String,
        onResult: (Boolean) -> Unit
    ) {
        val docRef =
            db.collection("users").document(userId).collection("favorites").document(productId)
        docRef.get().addOnSuccessListener { document ->
            onResult(document.exists())
        }.addOnFailureListener {
            onResult(false)
        }
    }
}
