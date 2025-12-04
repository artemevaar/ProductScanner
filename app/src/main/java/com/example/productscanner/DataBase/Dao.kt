package com.example.productscanner.DataBase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ScannedProductDao {
    @Insert
    suspend fun insertProduct(product: ScannedProduct)

    @Query("SELECT * FROM scanned_products WHERE userId = :userId")
    suspend fun getProductsByUserId(userId: String): List<ScannedProduct>

    @Query("DELETE FROM scanned_products WHERE userId = :userId")
    suspend fun deleteProductsByUserId(userId: String)
}

@Dao
interface UserRestrictionsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateRestrictions(restrictions: UserRestrictions)

    @Query("SELECT restrictions FROM user_restrictions WHERE userId = :userId")
    suspend fun getRestrictionsByUserId(userId: String): String?

    @Query("DELETE FROM user_restrictions WHERE userId = :userId")
    suspend fun deleteRestrictionsByUserId(userId: String)
}

@Dao
interface UserProductRatingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateRating(rating: UserProductRating)

    @Query("SELECT * FROM user_product_rating WHERE userId = :userId AND productId = :productId")
    suspend fun getRating(userId: String, productId: String): UserProductRating?

    @Delete
    suspend fun deleteRating(rating: UserProductRating)
}
