package com.alpenraum.shimstack.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.alpenraum.shimstack.common.DispatchersProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatastoreModule {
    const val DATASTORE_NAME = "shimstack_datastore"

    @Singleton
    @Provides
    fun providePreferencesDataStore(
        @ApplicationContext appContext: Context,
        dispatchersProvider: DispatchersProvider
    ): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler =
                ReplaceFileCorruptionHandler(
                    produceNewData = { emptyPreferences() }
                ),
            scope = CoroutineScope(SupervisorJob() + dispatchersProvider.io),
            migrations =
                listOf()
        ) {
            appContext.preferencesDataStoreFile(DATASTORE_NAME)
        }
    }

    @Singleton
    @Provides
    fun provideShimstackDatastore(dataStore: DataStore<Preferences>): ShimstackDatastore = ShimstackDatastore(dataStore)
}