package com.alpenraum.shimstack.ui.screens.mainScreens

import androidx.activity.compose.BackHandler
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.alpenraum.shimstack.common.moveLastEntryToStart
import com.alpenraum.shimstack.ui.compose.compositionlocal.LocalWindowSizeClass
import com.alpenraum.shimstack.ui.screens.main.navigation.bottomNavigation.BottomNavigationDestinations
import com.alpenraum.shimstack.ui.screens.mainScreens.home.HomeScreen
import com.alpenraum.shimstack.ui.screens.mainScreens.settings.SettingsScreen
import dev.olshevski.navigation.reimagined.AnimatedNavHost
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.moveToTop
import dev.olshevski.navigation.reimagined.navigate
import dev.olshevski.navigation.reimagined.pop
import dev.olshevski.navigation.reimagined.rememberNavController

@Composable
fun MainScreenFeature(onNewBikeClicked: () -> Unit) {
    val navController = rememberNavController<BottomNavigationDestinations>(
        startDestination = BottomNavigationDestinations.HomeScreen
    )

    val useNavRail = LocalWindowSizeClass.current.widthSizeClass >= WindowWidthSizeClass.Medium

    BottomNavigationBackHandler(navController)
    val lastDestination = navController.backstack.entries.last().destination
    Row(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(visible = useNavRail) {
            NavigationRail(
                modifier = Modifier.fillMaxHeight(),
                containerColor = MaterialTheme.colorScheme.inverseOnSurface
            ) {
                BottomNavigationDestinations.values().forEach { destination ->
                    NavigationRailItem(label = {
                        Text(
                            stringResource(id = destination.item.title)
                        )
                    }, icon = {
                        Icon(
                            painter = painterResource(destination.item.icon),
                            contentDescription = null
                        )
                    }, selected = destination == lastDestination, onClick = {
                        // keep only one instance of a destination in the backstack
                        if (!navController.moveToTop { it == destination }) {
                            // if there is no existing instance, add it
                            navController.navigate(destination)
                        }
                    })
                }
            }
        }
        Column(
            modifier = Modifier
        ) {
            Content(
                navController = navController,
                modifier = Modifier.weight(1f),
                onNewBikeClicked = onNewBikeClicked
            )
            AnimatedVisibility(visible = !useNavRail) {
                NavigationBar {
                    BottomNavigationDestinations.values().forEach { destination ->
                        NavigationBarItem(label = {
                            Text(
                                stringResource(id = destination.item.title)
                            )
                        }, icon = {
                            Icon(
                                painter = painterResource(destination.item.icon),
                                contentDescription = null
                            )
                        }, selected = destination == lastDestination, onClick = {
                            // keep only one instance of a destination in the backstack
                            if (!navController.moveToTop {
                                    it == destination
                                }
                            ) {
                                // if there is no existing instance, add it
                                navController.navigate(destination)
                            }
                        })
                    }
                }
            }
        }
    }
}

@Composable
private fun Content(
    navController: NavController<BottomNavigationDestinations>,
    modifier: Modifier = Modifier,
    onNewBikeClicked: () -> Unit
) {
    AnimatedNavHost(controller = navController, modifier) { destination ->
        when (destination) {
            BottomNavigationDestinations.HomeScreen -> {
                HomeScreen(
                    modifier = Modifier,
                    onNewBikeClicked = onNewBikeClicked
                )
            }

            BottomNavigationDestinations.Test -> {
                Text("Test screen")
            }

            BottomNavigationDestinations.Settings -> {
                SettingsScreen()
            }
        }
    }
}

@Composable
private fun BottomNavigationBackHandler(
    navController: NavController<BottomNavigationDestinations>
) {
    BackHandler(enabled = navController.backstack.entries.size > 1) {
        val lastEntry = navController.backstack.entries.last()
        if (lastEntry.destination == BottomNavigationDestinations.HomeScreen) {
            // The start destination should always be the last to pop. We move it to the start
            // to preserve its saved state and view models.
            navController.moveLastEntryToStart()
        } else {
            navController.pop()
        }
    }
}
