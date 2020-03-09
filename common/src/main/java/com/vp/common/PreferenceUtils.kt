package com.vp.common

import android.content.Context

/**
 * Object not used
 */
object PreferenceUtils {

    private const val PREFS_FILENAME = "com.vp.prefs"
    private const val FAVORITES_PREFERENCE_KEY = "$PREFS_FILENAME.favorite_movies"


    private fun getStringPreference(context: Context?, key: String, defaultValue: String?): String? {
        val sharedPref = context?.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
                ?: return null
        return sharedPref.getString(key, defaultValue)
    }


    private fun setStringPreference(context: Context?, key: String, value: String) {
        val sharedPref = context?.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
                ?: return
        with(sharedPref.edit()) {
            putString(key, value)
            commit()
        }
    }

    fun resetFavorites(context: Context?) {
        val sharedPref = context?.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
                ?: return
        with(sharedPref.edit()) {
            clear()
            commit()
        }
    }


    fun addOrRemoveFavorite(context: Context?, favoriteImdbId: String?) {
        if (favoriteImdbId.isNullOrBlank()) {
            return
        }
        val currentFavorites = getFavorites(context) as ArrayList
        if (currentFavorites.contains(favoriteImdbId)) {
            currentFavorites.remove(favoriteImdbId!!)
        } else {
            currentFavorites.add(favoriteImdbId!!)
        }
        val newFavoritesString = currentFavorites.joinToString(",")
        setStringPreference(context, FAVORITES_PREFERENCE_KEY, newFavoritesString)
    }


    fun addFavorite(context: Context?, favoriteImdbId: String?) {
        val currentFavorites = getFavorites(context) as ArrayList
        if (!favoriteImdbId.isNullOrBlank() && !currentFavorites.contains(favoriteImdbId)) {
            currentFavorites.add(favoriteImdbId!!)
        }
        val newFavoritesString = currentFavorites.joinToString(",")
        setStringPreference(context, FAVORITES_PREFERENCE_KEY, newFavoritesString)
    }


    fun removeFavorite(context: Context?, favoriteImdbId: String?) {
        val currentFavorites = getFavorites(context) as ArrayList
        if (!favoriteImdbId.isNullOrBlank() && currentFavorites.contains(favoriteImdbId)) {
            currentFavorites.remove(favoriteImdbId!!)
        }
        val newFavoritesString = currentFavorites.joinToString(",")
        setStringPreference(context, FAVORITES_PREFERENCE_KEY, newFavoritesString)
    }


    fun isFavorite(context: Context?, favoriteImdbId: String?): Boolean {
        val currentFavorites = getFavorites(context) as ArrayList
        return if (!favoriteImdbId.isNullOrBlank()) {
            currentFavorites.contains(favoriteImdbId!!)
        } else {
            false
        }
    }


    fun getFavorites(context: Context?): List<String> {
        val currentFavorites = getStringPreference(context, FAVORITES_PREFERENCE_KEY, "") ?: ""
        return currentFavorites.split(",").map { it.trim() }.filter { !it.isNullOrBlank() }
    }


}