package com.alpenraum.shimstack.onboarding.navigation

import com.alpenraum.shimstack.home.navigation.HomeNavGraph
import com.alpenraum.shimstack.home.navigation.HomeNavigator
import com.alpenraum.shimstack.home.navigation.HomeNavigatorImpl
import com.alpenraum.shimstack.navigation.NavGraphDefinition
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.migration.DisableInstallInCheck
import dagger.multibindings.IntoSet

@Module(includes = [HomeWiring.Bindings::class])
@InstallIn(SingletonComponent::class)
class HomeWiring {
    @Provides
    @IntoSet
    fun navGraph(): NavGraphDefinition = HomeNavGraph

    @Module
    @DisableInstallInCheck
    interface Bindings {
        @Binds
        fun navigator(navigator: HomeNavigatorImpl): HomeNavigator
    }
}