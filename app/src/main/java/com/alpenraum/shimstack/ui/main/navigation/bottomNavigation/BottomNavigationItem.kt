package com.alpenraum.shimstack.ui.main.navigation.bottomNavigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.alpenraum.shimstack.R

sealed class BottomNavigationItem(
    @StringRes var title: Int,
    @DrawableRes var icon: Int
) {
    object Home : BottomNavigationItem(R.string.title_home, R.drawable.home_24px)
    object Test : BottomNavigationItem(R.string.app_name,R.drawable.home_24px)
}
