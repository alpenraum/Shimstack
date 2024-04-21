package com.alpenraum.shimstack.di

import com.alpenraum.shimstack.common.DefaultDispatchersProvider
import com.alpenraum.shimstack.common.DispatchersProvider
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
    fun provideAppCoroutineScope(dispatchersProvider: DispatchersProvider): CoroutineScope =
        CoroutineScope(
            dispatchersProvider.main + SupervisorJob(null)
        )

    @Singleton
    @Provides
    fun provideDispatchersProvider(): DispatchersProvider = DefaultDispatchersProvider()
}