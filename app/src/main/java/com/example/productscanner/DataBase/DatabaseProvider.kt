package com.example.productscanner.DataBase

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    private var instance: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        if (instance == null) {
            instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "app_database"

            ).fallbackToDestructiveMigration().build()
        }
        return instance!!
    }
}