package com.example.productscanner.DataBase

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ScannedProduct::class, UserRestrictions::class, UserProductRating::class],
    version = 6
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scannedProductDao(): ScannedProductDao
    abstract fun userRestrictionsDao(): UserRestrictionsDao
    abstract fun userProductRating(): UserProductRatingDao
}

