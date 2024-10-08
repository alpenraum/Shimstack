package com.alpenraum.shimstack.common.di

import com.alpenraum.shimstack.common.logger.ShimstackLogger
import com.alpenraum.shimstack.common.logger.TimberShimstackLogger
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object CommonModule {
    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @Provides
    fun provideLogger(): ShimstackLogger = TimberShimstackLogger()
}