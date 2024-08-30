package com.alpenraum.shimstack.newbike.navigation

import com.alpenraum.shimstack.navigation.NavGraphDefinition
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.migration.DisableInstallInCheck
import dagger.multibindings.IntoSet

@Module(includes = [NewBikeWiring.Bindings::class])
@InstallIn(SingletonComponent::class)
class NewBikeWiring {
    @Provides
    @IntoSet
    fun navGraph(): NavGraphDefinition = NewBikeNavGraph

    @Module
    @DisableInstallInCheck
    interface Bindings {
        @Binds
        fun navigator(navigator: NewBikeNavigatorImpl): NewBikeNavigator
    }
}