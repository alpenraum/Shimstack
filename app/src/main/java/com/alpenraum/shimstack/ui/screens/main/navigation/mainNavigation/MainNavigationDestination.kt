package com.alpenraum.shimstack.ui.screens.main.navigation.mainNavigation

sealed class MainNavigationDestination {
    object MainScreen : MainNavigationDestination()
    object NewBike : MainNavigationDestination()
}
