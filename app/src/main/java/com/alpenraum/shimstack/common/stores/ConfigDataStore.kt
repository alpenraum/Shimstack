package com.alpenraum.shimstack.common.stores

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.alpenraum.shimstack.ShimstackApplication
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object ConfigDataStore {
    private val dataStore = ShimstackApplication.appContext?.dataStore

    const val SETTINGS_PREFERENCES_NAME = "shimstack_settings"

    private val PREF_USE_DYNAMIC_THEME = booleanPreferencesKey("PREF_USE_DYNAMIC_THEME")

    val useDynamicTheme: Flow<Boolean>?
        get() = dataStore?.data?.map { it[PREF_USE_DYNAMIC_THEME] ?: false }

    suspend fun setUseDynamicTheme(value: Boolean) {
        dataStore?.edit { it[PREF_USE_DYNAMIC_THEME] = value }
    }
}

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = ConfigDataStore.SETTINGS_PREFERENCES_NAME
)