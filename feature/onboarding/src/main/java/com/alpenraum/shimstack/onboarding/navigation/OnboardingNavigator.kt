package com.alpenraum.shimstack.onboarding.navigation

import android.os.Bundle
import androidx.navigation.NavController
import com.alpenraum.shimstack.navigation.navigateWithArgs
import javax.inject.Inject

interface OnboardingNavigator {
    fun navigateToOnboarding(navController: NavController)
}

class OnboardingNavigatorImpl
    @Inject
    constructor() : OnboardingNavigator {
        override fun navigateToOnboarding(navController: NavController) {
            navController.navigateWithArgs(OnboardingNavGraph.route, Bundle())
        }
    }