package com.vp.common.database

import android.content.Context
import androidx.room.Room
import com.vp.common.model.FavoriteMovie

object DatabaseHelper {

    /**
     * Singleton db instance
     */
    private var db: AppDatabase? = null


    fun getDatabase(applicationContext: Context): AppDatabase? {
        if (db == null) {
            db = Room.databaseBuilder(
                    applicationContext,
                    AppDatabase::class.java, "database-name"
            ).build()
        }
        return db
    }


    fun addOrRemoveFavorite(context: Context, movieDetail: FavoriteMovie) {
        val found = getDatabase(context)?.favoriteDao()?.getById(movieDetail.imdbId)
        if (found == null || found.isEmpty()) {
            // Add
            getDatabase(context)?.favoriteDao()?.insertAll(movieDetail)
        } else {
            // Remove
            getDatabase(context)?.favoriteDao()?.delete(movieDetail)
        }
    }


    fun isFavorite(context: Context, favoriteImdbId: String): Boolean {
        val found = getDatabase(context)?.favoriteDao()?.getById(favoriteImdbId)
        return (found != null && found.isNotEmpty())
    }


    fun getAllFavorites(context: Context): List<FavoriteMovie>? {
        return getDatabase(context)?.favoriteDao()?.getAll()
    }


}