package com.alpenraum.shimstack.onboarding.navigation

import com.alpenraum.shimstack.navigation.NavGraphDefinition
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.migration.DisableInstallInCheck
import dagger.multibindings.IntoSet

@Module(includes = [OnboardingWiring.Bindings::class])
@InstallIn(SingletonComponent::class)
class OnboardingWiring {
    @Provides
    @IntoSet
    fun navGraph(): NavGraphDefinition = OnboardingNavGraph

    @Module
    @DisableInstallInCheck
    interface Bindings {
        @Binds
        fun navigator(navigator: OnboardingNavigatorImpl): OnboardingNavigator
    }
}