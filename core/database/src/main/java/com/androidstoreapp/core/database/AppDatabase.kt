package com.androidstoreapp.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.androidstoreapp.core.database.dao.FavoriteDao
import com.androidstoreapp.core.database.entity.FavoriteEntity

@Database(
    entities = [FavoriteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}
