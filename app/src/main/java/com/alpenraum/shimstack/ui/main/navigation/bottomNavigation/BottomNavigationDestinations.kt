package com.alpenraum.shimstack.ui.main.navigation.bottomNavigation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class BottomNavigationDestinations(val item: BottomNavigationItem) : Parcelable {

    @Parcelize
    object HomeScreen : BottomNavigationDestinations(BottomNavigationItem.Home)

    @Parcelize
    object Test : BottomNavigationDestinations(BottomNavigationItem.Test)

    @Parcelize
    object Settings : BottomNavigationDestinations(BottomNavigationItem.Settings)

    companion object {
        fun values() = listOf<BottomNavigationDestinations>(HomeScreen, Test, Settings)
    }
}
