package com.alpenraum.shimstack.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DispatchersModule {
    @Provides
    fun provideAppCoroutineScope(dispatchersProvider: com.alpenraum.shimstack.common.DispatchersProvider): CoroutineScope =
        CoroutineScope(
            dispatchersProvider.main + SupervisorJob(null)
        )

    @Singleton
    @Provides
    fun provideDispatchersProvider(): com.alpenraum.shimstack.common.DispatchersProvider =
        com.alpenraum.shimstack.common.DefaultDispatchersProvider()
}