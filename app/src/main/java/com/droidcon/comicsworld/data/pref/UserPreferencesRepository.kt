package com.droidcon.comicsworld.data.pref

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.droidcon.comicsworld.data.ComicCategory
import com.droidcon.comicsworld.data.SortOrder
import com.droidcon.comicsworld.data.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class UserPreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val TAG = Companion::class.java.simpleName
        private val SORT_ORDER_KEY = stringPreferencesKey("sort_order")
        private val COMIC_CATEGORY_FILTER_KEY = stringPreferencesKey("comic_category")
    }

    fun getUserPreferences(): Flow<UserPreferences> {
        return dataStore.data.catch {exeption ->
            if (exeption is IOException){
                Log.e(TAG, "getUserPreferences: Error occurred while reading")
                emit(emptyPreferences())
            }else {
                emit(emptyPreferences())
            }

        }.map {
            pref -> pref.toUserPreferences()
        }
    }

    suspend fun filterComicsByCategory(comicCategory: ComicCategory){
        dataStore.edit {
            pref ->
            val currentComicCategoryFilter = pref.getCurrentComicCategoryFilter()
            if (comicCategory == currentComicCategoryFilter)
                return@edit
            pref[COMIC_CATEGORY_FILTER_KEY] = comicCategory.name
        }
    }

    suspend fun enableSortingByRating(enabled : Boolean){
        dataStore.edit {pref ->
            val currentSortOrder = pref.getCurrentSortOrderFromUserPreferences()
            val newSortOrder = if (enabled) SortOrder.BY_RATING else if (currentSortOrder == SortOrder.BY_RATING) SortOrder.NONE else currentSortOrder
            pref[SORT_ORDER_KEY] = newSortOrder.name
        }
    }

    suspend fun enableSortingByDateAdded(enabled: Boolean){
        dataStore.edit { pref ->
            val currentSortOrder = pref.getCurrentSortOrderFromUserPreferences()
            val newSortOrder = if (enabled) SortOrder.BY_DATE_ADDED else if (currentSortOrder == SortOrder.BY_DATE_ADDED) SortOrder.NONE else currentSortOrder
            pref[SORT_ORDER_KEY] = newSortOrder.name
        }
    }
    suspend fun enableSortingByName(enabled: Boolean){
        dataStore.edit { pref ->
            val currentSortOrder = pref.getCurrentSortOrderFromUserPreferences()
            val newSortOrder = if (enabled) SortOrder.BY_NAME else if (currentSortOrder == SortOrder.BY_NAME) SortOrder.NONE else currentSortOrder
            pref[SORT_ORDER_KEY] = newSortOrder.name
        }
    }
    suspend fun disableSorting(){
        dataStore.edit {pref ->
            pref[SORT_ORDER_KEY] = SortOrder.NONE.name
        }
    }
    suspend fun removeComicCategoryFilter(){
        dataStore.edit {pref ->
            pref[COMIC_CATEGORY_FILTER_KEY] = ComicCategory.ALL.name
        }
    }

    private fun Preferences.getCurrentSortOrderFromUserPreferences() : SortOrder{
        return SortOrder.valueOf(this[SORT_ORDER_KEY] ?: SortOrder.NONE.name)
    }

    private fun Preferences.getCurrentComicCategoryFilter() : ComicCategory{
        return ComicCategory.valueOf(this[COMIC_CATEGORY_FILTER_KEY]?: ComicCategory.ALL.name)
    }

    private fun Preferences.toUserPreferences() : UserPreferences{
        val sortOrder = getCurrentSortOrderFromUserPreferences()
        val currentComicFilter = getCurrentComicCategoryFilter()
        return UserPreferences(comicCategory = currentComicFilter, sortOrder = sortOrder)
    }



}