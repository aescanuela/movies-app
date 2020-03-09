package com.vp.common.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteMovie(@PrimaryKey val imdbId: String,
                         @ColumnInfo(name = "Title") val title: String,
                         @ColumnInfo(name = "Year") val year: String,
                         @ColumnInfo(name = "Runtime") val runtime: String,
                         @ColumnInfo(name = "Director") val director: String,
                         @ColumnInfo(name = "Plot") val plot: String,
                         @ColumnInfo(name = "Poster") val poster: String)