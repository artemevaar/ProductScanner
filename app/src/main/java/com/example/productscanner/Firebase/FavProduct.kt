package com.example.productscanner.Firebase

data class FavProduct(
    val productId: String = "",
    val productName: String = "",
    val ingredientsText: String = "",
    val imageUrl: String = "",
    val isFavorite: Boolean = false
)