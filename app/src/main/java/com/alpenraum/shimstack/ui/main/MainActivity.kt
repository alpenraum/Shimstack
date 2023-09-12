package com.alpenraum.shimstack.ui.main

import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.alpenraum.shimstack.common.moveLastEntryToStart
import com.alpenraum.shimstack.ui.base.BaseActivity
import com.alpenraum.shimstack.ui.compose.compositionlocal.LocalWindowSizeClass
import com.alpenraum.shimstack.ui.main.navigation.bottomNavigation.BottomNavigationDestinations
import com.alpenraum.shimstack.ui.main.screens.HomeScreen
import com.alpenraum.shimstack.ui.main.screens.HomeScreenViewModel
import com.alpenraum.shimstack.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import dev.olshevski.navigation.reimagined.AnimatedNavHost
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.hilt.hiltViewModel
import dev.olshevski.navigation.reimagined.moveToTop
import dev.olshevski.navigation.reimagined.navigate
import dev.olshevski.navigation.reimagined.pop
import dev.olshevski.navigation.reimagined.rememberNavController

@AndroidEntryPoint
class MainActivity : BaseActivity<MainViewModel>() {

    override val viewModelClass: Class<MainViewModel> = MainViewModel::class.java

//    private val isBiometricAuthenticationAvailableUseCase = IsBiometricAuthenticationAvailableUseCase()
//    private val triggerBiometricsPromptUseCase = TriggerBiometricsPromptUseCase()

    override fun onViewModelBound() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        initializeContent()

//        when (isBiometricAuthenticationAvailableUseCase(this)) {
//            IsBiometricAuthenticationAvailableUseCase.Result.Failure -> TODO()
//            IsBiometricAuthenticationAvailableUseCase.Result.NoneEnrolled -> TODO()
//            IsBiometricAuthenticationAvailableUseCase.Result.Success -> {
//                triggerBiometricsPromptUseCase(
//                    this,
//                    object : AuthenticationCallback() {
//                        override fun onAuthenticationSucceeded(
//                            result: BiometricPrompt.AuthenticationResult
//                        ) {
//                            super.onAuthenticationSucceeded(result)
//                            viewModel.x()
//                        }
//                    }
//                )
//            }
//        }
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    private fun initializeContent() {
        setContent {
            val windowSizeClass = calculateWindowSizeClass(activity = this)
            val useNavRail = windowSizeClass.widthSizeClass >= WindowWidthSizeClass.Medium
            CompositionLocalProvider(LocalWindowSizeClass provides windowSizeClass) {
                AppTheme {
                    val navController = rememberNavController<BottomNavigationDestinations>(
                        startDestination = BottomNavigationDestinations.HomeScreen
                    )

                    BottomNavigationBackHandler(navController)
                    val lastDestination = navController.backstack.entries.last().destination
                    Row(modifier = Modifier.fillMaxSize()) {
                        AnimatedVisibility(visible = useNavRail) {
                            NavigationRail(
                                modifier = Modifier.fillMaxHeight()
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
                                .fillMaxSize()
                        ) {
                            Content(
                                navController = navController,
                                modifier = Modifier.weight(1f)
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
                                                if (!navController.moveToTop { it == destination }) {
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
            }
        }
    }

    @Composable
    private fun Content(
        navController: NavController<BottomNavigationDestinations>,
        modifier: Modifier = Modifier
    ) {
        AnimatedNavHost(controller = navController, modifier) { destination ->
            when (destination) {
                BottomNavigationDestinations.HomeScreen -> {
                    val viewModel = hiltViewModel<HomeScreenViewModel>()
                    HomeScreen(
                        modifier = Modifier,
                        viewModel = viewModel
                    )
                }

                BottomNavigationDestinations.Test -> {
                    Text("Test screen")
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

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        AppTheme {}
    }
}
