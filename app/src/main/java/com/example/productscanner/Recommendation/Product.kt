package com.example.productscanner.Recommendation

data class Product(
    val barcode: String = "",
    val name: String = "",
    val ingredients: String = "",
    val imageUrl: String = "",
    val categories: List<String> = listOf(),
    val isFavorite: Boolean = false
)