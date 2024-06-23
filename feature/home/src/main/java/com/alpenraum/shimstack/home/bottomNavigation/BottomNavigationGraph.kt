package com.alpenraum.shimstack.home.bottomNavigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.alpenraum.shimstack.home.overview.HomeScreen
import com.alpenraum.shimstack.home.settings.SettingsScreen

@Composable
fun BottomNavigationGraph(
    bottomNavController: NavHostController,
    featureNavController: NavController,
    modifier: Modifier = Modifier
) {
    NavHost(
        bottomNavController,
        startDestination = BottomNavigationItem.Home.route,
        modifier = modifier,
        enterTransition = {
            com.alpenraum.shimstack.ui.compose
                .fadeIn()
        },
        exitTransition = {
            com.alpenraum.shimstack.ui.compose
                .fadeOut()
        },
        popExitTransition = {
            com.alpenraum.shimstack.ui.compose
                .fadeOut()
        },
        popEnterTransition = {
            com.alpenraum.shimstack.ui.compose
                .fadeIn()
        }
    ) {
        composable(BottomNavigationItem.Home.route) {
            HomeScreen(navController = featureNavController)
        }
        composable(BottomNavigationItem.Settings.route) {
            SettingsScreen(navController = featureNavController)
        }
    }
}