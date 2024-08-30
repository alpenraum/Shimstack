package com.alpenraum.shimstack.di

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class AppModule {
    @Binds
    abstract fun provideContext(application: Application): Context
}