package com.alpenraum.shimstack.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.alpenraum.shimstack.home.bottomNavigation.BottomNavigationGraph
import com.alpenraum.shimstack.home.bottomNavigation.BottomNavigationItem
import com.alpenraum.shimstack.ui.compose.compositionlocal.LocalWindowSizeClass

@Composable
fun MainScreenFeature(navController: NavController) {
    val useNavRail = LocalWindowSizeClass.current.widthSizeClass >= WindowWidthSizeClass.Medium

    val bottomNavController = rememberNavController()
    val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Row(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(visible = useNavRail) {
            NavigationRail(
                modifier = Modifier.fillMaxHeight(),
                containerColor = MaterialTheme.colorScheme.inverseOnSurface
            ) {
                BottomNavigationItem.asList().forEach { destination ->
                    NavigationRailItem(
                        label = {
                            Text(
                                stringResource(id = destination.title)
                            )
                        },
                        icon = {
                            Icon(
                                painter = painterResource(destination.icon),
                                contentDescription = null
                            )
                        },
                        selected = currentRoute == destination.route,
                        onClick = {
                            bottomNavController.navigate(destination.route) {
                                bottomNavController.graph.startDestinationRoute?.let { route ->
                                    popUpTo(route) {
                                        saveState = true
                                    }
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
        Column(
            modifier = Modifier
        ) {
            BottomNavigationGraph(bottomNavController, navController, Modifier.weight(1.0f))

            AnimatedVisibility(visible = !useNavRail) {
                NavigationBar {
                    BottomNavigationItem.asList().forEach { destination ->
                        NavigationBarItem(
                            label = {
                                Text(
                                    stringResource(id = destination.title)
                                )
                            },
                            icon = {
                                Icon(
                                    painter = painterResource(destination.icon),
                                    contentDescription = null
                                )
                            },
                            selected = destination.route == currentRoute,
                            onClick = {
                                bottomNavController.navigate(destination.route) {
                                    bottomNavController.graph.startDestinationRoute?.let { route ->
                                        popUpTo(route) {
                                            saveState = true
                                        }
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}