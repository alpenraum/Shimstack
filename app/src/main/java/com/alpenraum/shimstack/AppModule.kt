package com.alpenraum.shimstack

import android.app.Application
import android.content.Context
import com.alpenraum.shimstack.data.bike.BikeRepository
import com.alpenraum.shimstack.data.bike.LocalBikeRepository
import com.alpenraum.shimstack.data.bikeTemplates.BikeTemplateRepository
import com.alpenraum.shimstack.data.bikeTemplates.LocalBikeTemplateRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class AppModule {
    @Binds
    abstract fun provideContext(application: Application): Context

    @Binds
    abstract fun provideBikeRepository(repository: LocalBikeRepository): BikeRepository

    @Binds
    abstract fun provideBikeTemplateRepository(repository: LocalBikeTemplateRepository): BikeTemplateRepository
}

@InstallIn(SingletonComponent::class)
@Module
class AppUtilsModule {
    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
}