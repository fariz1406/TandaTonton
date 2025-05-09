package com.fariznst0075.tandatonton.util

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore("settings")

class SettingsDataStore(private val context: Context) {

    companion object {
        private val SHOW_LIST = booleanPreferencesKey("show_list")
    }

    val showListFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[SHOW_LIST] ?: true
        }

    suspend fun saveLayout(showList: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[SHOW_LIST] = showList
        }
    }
}
