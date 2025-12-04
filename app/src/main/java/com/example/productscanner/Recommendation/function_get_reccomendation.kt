package com.example.productscanner.Recommendation

import android.content.Context
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

fun fetchProductsFromFirestore(
    userId: String,
    context: Context,
    onDataUpdated: (List<Product>) -> Unit,
    onError: (Exception) -> Unit
) {
    val db = Firebase.firestore
    val productList = mutableListOf<Product>()
    var loadedCount = 0

    db.collection("рекомендации").document(userId).get()
        .addOnSuccessListener { document ->
            if (document.exists()) {
                val barcodes = (document.get("products") as? List<String>).orEmpty()

                if (barcodes.isEmpty()) {
                    onDataUpdated(emptyList())
                    return@addOnSuccessListener
                }

                val randomBarcodes = barcodes.shuffled().take(100)

                for (barcode in randomBarcodes) {
                    db.collection("products")
                        .document(barcode)
                        .get()
                        .addOnSuccessListener { productDoc ->
                            productDoc?.toObject(Product::class.java)?.let { product ->
                                productList.add(product)
                            }
                            loadedCount++
                            if (loadedCount == randomBarcodes.size) {
                                onDataUpdated(productList)
                            }
                        }
                        .addOnFailureListener { exception ->
                            loadedCount++
                            if (loadedCount == randomBarcodes.size) {
                                onDataUpdated(productList)
                            }
                        }
                }
            } else {
                onDataUpdated(emptyList())
            }
        }
        .addOnFailureListener { exception ->
            onError(exception)
        }
}
