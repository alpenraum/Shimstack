package com.alpenraum.shimstack.ui.features.main.navigation.bottomNavigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.alpenraum.shimstack.R

sealed class BottomNavigationItem(
    val route: String,
    @StringRes var title: Int,
    @DrawableRes var icon: Int
) {
    object Home : BottomNavigationItem(
        "home",
        R.string.title_home,
        R.drawable.home_24px
    )

    object Test :
        BottomNavigationItem("test", R.string.app_name, R.drawable.home_24px)

    object Settings :
        BottomNavigationItem("settings", R.string.settings, R.drawable.home_24px)

    companion object {
        fun asList() = listOf(Home, Test, Settings)
    }
}