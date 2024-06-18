package com.alpenraum.shimstack.newbike.navigation

import android.os.Bundle
import androidx.navigation.NavController
import com.alpenraum.shimstack.navigation.navigateWithArgs
import javax.inject.Inject

interface NewBikeNavigator {
    fun navigateToNewBike(navController: NavController)
}

class NewBikeNavigatorImpl
    @Inject
    constructor() : NewBikeNavigator {
        override fun navigateToNewBike(navController: NavController) {
            navController.navigateWithArgs(NewBikeNavGraph.route, Bundle())
        }
    }