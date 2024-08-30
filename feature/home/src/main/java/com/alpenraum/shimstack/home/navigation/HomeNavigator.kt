package com.alpenraum.shimstack.home.navigation

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.alpenraum.shimstack.navigation.navigateWithArgs
import javax.inject.Inject

interface HomeNavigator {
    fun navigateToHome(
        navController: NavController,
        navOptions: NavOptions.Builder? = null
    )

    fun navigateToBikeDetails(
        navController: NavController,
        id: Int
    )
}

class HomeNavigatorImpl
    @Inject
    constructor() : HomeNavigator {
        override fun navigateToHome(
            navController: NavController,
            navOptions: NavOptions.Builder?
        ) {
            navController.navigateWithArgs(
                HomeNavGraph.route,
                Bundle(),
                navOptions = navOptions?.apply { setLaunchSingleTop(true) }?.build()
            )
        }

        override fun navigateToBikeDetails(
            navController: NavController,
            id: Int
        ) {
            navController.navigateWithArgs(
                HomeNavGraph.BikeDetails.route,
                args = bundleOf(HomeNavGraph.BikeDetails.ARGUMENT_ID to id)
            )
        }
    }