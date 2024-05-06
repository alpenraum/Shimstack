package com.alpenraum.shimstack.home.bottomNavigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.alpenraum.shimstack.home.R

sealed class BottomNavigationItem(
    val route: String,
    @StringRes var title: Int,
    @DrawableRes var icon: Int
) {
    data object Home : BottomNavigationItem(
        "home",
        R.string.title_home,
        R.drawable.home_24px
    )

    data object Settings :
        BottomNavigationItem("settings", R.string.settings, R.drawable.home_24px)

    companion object {
        fun asList() = listOf(Home, Settings)
    }
}