package com.alpenraum.shimstack.ui.features.main.navigation.mainNavigation

sealed class MainNavigationDestination {
    object MainScreen : MainNavigationDestination()
    object NewBike : MainNavigationDestination()
}
