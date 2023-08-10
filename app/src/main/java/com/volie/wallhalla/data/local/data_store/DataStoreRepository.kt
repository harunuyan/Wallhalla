package com.volie.wallhalla.data.local.data_store

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.volie.wallhalla.data.model.Quality
import com.volie.wallhalla.data.model.Theme
import com.volie.wallhalla.util.Constant.PREFERENCES_NAME
import com.volie.wallhalla.util.Constant.PREFERENCES_QUALITY
import com.volie.wallhalla.util.Constant.PREFERENCES_THEME
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_NAME)

@Singleton
class DataStoreRepository
@Inject constructor(@ApplicationContext private val context: Context) {

    private object PreferenceKeys {
        val SAVED_THEME = stringPreferencesKey(PREFERENCES_THEME)
        val SAVED_QUALITY = stringPreferencesKey(PREFERENCES_QUALITY)
    }

    private val dataStore: DataStore<Preferences> = context.dataStore

    suspend fun saveThemeOption(theme: Theme) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.SAVED_THEME] = theme.name
        }
    }

    val selectedThemeFlow: Flow<Theme> = dataStore.data.map { preferences ->
        val themeName = preferences[PreferenceKeys.SAVED_THEME] ?: Theme.SYSTEM.name
        try {
            Theme.valueOf(themeName)
        } catch (ex: IllegalArgumentException) {
            Log.e("Failed theme flow", "$ex")
            Theme.SYSTEM
        }
    }

    suspend fun saveQualityOption(quality: Quality) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.SAVED_QUALITY] = quality.name
        }
    }

    val selectedQualityFlow: Flow<Quality> = dataStore.data.map { preferences ->
        val qualityName = preferences[PreferenceKeys.SAVED_QUALITY] ?: Quality.ORIGINAL.name
        try {
            Quality.valueOf(qualityName)
        } catch (ex: IllegalArgumentException) {
            Log.e("Failed quality flow", "$ex")
            Quality.ORIGINAL
        }
    }
}