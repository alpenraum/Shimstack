package com.alpenraum.shimstack.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ShimstackDatastore(private val dataStore: DataStore<Preferences>) {
    companion object {
        private val PREF_USE_DYNAMIC_THEME = booleanPreferencesKey("PREF_USE_DYNAMIC_THEME")
        private val PREF_ALLOW_ANALYTICS = booleanPreferencesKey("PREF_ALLOW_ANALYTICS")
        private val PREF_IS_ONBOARDING_COMPLETED = booleanPreferencesKey("PREF_IS_ONBOARDING_COMPLETED")
    }

    val useDynamicTheme: Flow<Boolean> = PREF_USE_DYNAMIC_THEME.get(defaultValue = false)

    suspend fun setUseDynamicTheme(value: Boolean) {
        dataStore.edit { it[PREF_USE_DYNAMIC_THEME] = value }
    }

    val allowAnalytics: Flow<Boolean> = PREF_ALLOW_ANALYTICS.get(defaultValue = false)

    suspend fun setAllowAnalytics(value: Boolean) {
        dataStore.edit { it[PREF_ALLOW_ANALYTICS] = value }
    }

    val isOnboardingCompleted: Flow<Boolean> =
        PREF_IS_ONBOARDING_COMPLETED.get(defaultValue = false)

    suspend fun setIsOnboardingCompleted(value: Boolean) = PREF_IS_ONBOARDING_COMPLETED.set(value)

    private fun <T> Preferences.Key<T>.get(defaultValue: T) = dataStore.data.map { it[this] ?: defaultValue }

    private suspend fun <T> Preferences.Key<T>.set(value: T) {
        dataStore.edit { it[this] = value }
    }
}