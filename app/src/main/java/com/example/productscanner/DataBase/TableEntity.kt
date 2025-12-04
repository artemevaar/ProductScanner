package com.example.productscanner.DataBase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scanned_products")
data class ScannedProduct(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: String,
    val productName: String,
    val ingredients: String,
    val ingredientsExplanation: String,
    val scannedCode: String,
    val scannedTime: String,
    val productImageUrl: String
)

@Entity(tableName = "user_restrictions")
data class UserRestrictions(
    @PrimaryKey val userId: String,
    val restrictions: String
)

@Entity(tableName = "user_product_rating", primaryKeys = ["userId", "productId"])
data class UserProductRating(
    val userId: String,
    val productId: String,
    val ingredients: String,
    val rating: Int
)