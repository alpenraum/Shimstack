package com.alpenraum.shimstack.di

import com.alpenraum.shimstack.datastore.ShimstackDatastore
import com.alpenraum.shimstack.home.navigation.HomeNavGraph
import com.alpenraum.shimstack.navigation.StartDestinationRoute
import com.alpenraum.shimstack.onboarding.navigation.OnboardingNavGraph
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking

@Module
@InstallIn(SingletonComponent::class)
object NavigationModule {
    @Provides
    @StartDestinationRoute
    fun startDestination(dataStore: ShimstackDatastore): String =
        runBlocking {
            return@runBlocking if (dataStore.isOnboardingCompleted.firstOrNull() == true) {
                HomeNavGraph.route
            } else {
                OnboardingNavGraph.route
            }
        }
}