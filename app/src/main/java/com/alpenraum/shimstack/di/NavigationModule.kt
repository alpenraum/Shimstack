package com.alpenraum.shimstack.di

import com.alpenraum.shimstack.common.navigation.StartDestinationRoute
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object NavigationModule {
    @Provides
    @StartDestinationRoute
    fun startDestination(): String = "x" // TODO
}