package com.alpenraum.shimstack.data.di

import com.alpenraum.shimstack.data.bike.BikeRepository
import com.alpenraum.shimstack.data.bike.LocalBikeRepository
import com.alpenraum.shimstack.data.bikeTemplates.BikeTemplateRepository
import com.alpenraum.shimstack.data.bikeTemplates.LocalBikeTemplateRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    abstract fun provideBikeRepository(repository: LocalBikeRepository): BikeRepository

    @Binds
    abstract fun provideBikeTemplateRepository(repository: LocalBikeTemplateRepository): BikeTemplateRepository
}