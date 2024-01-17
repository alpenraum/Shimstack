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

object ShimstackDataStore {
    private val dataStore = ShimstackApplication.appContext?.dataStore

    const val SETTINGS_PREFERENCES_NAME = "shimstack_settings"

    private val PREF_USE_DYNAMIC_THEME = booleanPreferencesKey("PREF_USE_DYNAMIC_THEME")
    private val PREF_IS_ONBOARDING_COMPLETED = booleanPreferencesKey("PREF_IS_ONBOARDING_COMPLETED")

    val useDynamicTheme: Flow<Boolean>? = PREF_USE_DYNAMIC_THEME.get(defaultValue = false)

    suspend fun setUseDynamicTheme(value: Boolean) {
        dataStore?.edit { it[PREF_USE_DYNAMIC_THEME] = value }
    }

    val isOnboardingCompleted: Flow<Boolean>? =
        PREF_IS_ONBOARDING_COMPLETED.get(defaultValue = false)

    suspend fun setIsOnboardingCompleted(value: Boolean) = PREF_IS_ONBOARDING_COMPLETED.set(value)

    private fun <T> Preferences.Key<T>.get(defaultValue: T) =
        dataStore?.data?.map { it[this] ?: defaultValue }

    private suspend fun <T> Preferences.Key<T>.set(value: T) {
        dataStore?.edit { it[this] = value }
    }
}

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = ShimstackDataStore.SETTINGS_PREFERENCES_NAME
)