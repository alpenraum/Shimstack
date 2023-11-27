package com.alpenraum.shimstack.ui.features.main.navigation.bottomNavigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.alpenraum.shimstack.ui.features.mainScreens.home.HomeScreen
import com.alpenraum.shimstack.ui.features.mainScreens.settings.SettingsScreen
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun BottomNavigationGraph(
    navController: NavHostController,
    rootNavigator: DestinationsNavigator,
    modifier: Modifier
) {
    NavHost(
        navController,
        startDestination = BottomNavigationItem.Home.route,
        modifier = modifier
    ) {
        composable(BottomNavigationItem.Home.route) {
            HomeScreen(rootNavigator)
        }
        composable(BottomNavigationItem.Test.route) {
            SettingsScreen()
        }
        composable(BottomNavigationItem.Settings.route) {
            SettingsScreen()
        }
    }
}
