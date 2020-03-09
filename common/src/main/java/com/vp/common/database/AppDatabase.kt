package com.vp.common.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vp.common.model.FavoriteMovie

@Database(entities = [FavoriteMovie::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}
