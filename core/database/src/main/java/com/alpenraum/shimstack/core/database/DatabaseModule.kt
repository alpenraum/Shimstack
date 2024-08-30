package com.alpenraum.shimstack.core.database

import android.content.Context
import androidx.room.Room
import com.alpenraum.shimstack.core.database.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext appContext: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "Shimstack"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideBikeDao(appDatabase: AppDatabase) = appDatabase.bikeDao()

    @Provides
    fun provideBikeTemplateDao(appDatabase: AppDatabase) = appDatabase.bikeTemplateDao()
}