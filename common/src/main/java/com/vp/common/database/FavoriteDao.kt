package com.vp.common.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.vp.common.model.FavoriteMovie

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM favoritemovie")
    fun getAll(): List<FavoriteMovie>

    @Query("SELECT * FROM favoritemovie WHERE imdbId == :movieId")
    fun getById(movieId: String): List<FavoriteMovie>

    @Insert
    fun insertAll(vararg movies: FavoriteMovie)

    @Delete
    fun delete(movies: FavoriteMovie)

}