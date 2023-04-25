package com.alpenraum.shimstack.ui.main

import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.alpenraum.shimstack.common.moveLastEntryToStart
import com.alpenraum.shimstack.ui.base.BaseActivity
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

    @OptIn(ExperimentalMaterial3Api::class)
    private fun initializeContent() {
        setContent {
            AppTheme {
                val navController = rememberNavController<BottomNavigationDestinations>(
                    startDestination = BottomNavigationDestinations.HomeScreen
                )

                BottomNavigationBackHandler(navController)

                Scaffold(bottomBar = {
                    val lastDestination = navController.backstack.entries.last().destination
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
                }, content = { paddingValues ->
                        Content(navController, paddingValues)
                    })
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun Content(
    navController: NavController<BottomNavigationDestinations>,
    paddingValues: PaddingValues
) {
    AnimatedNavHost(controller = navController) { destination ->
        when (destination) {
            BottomNavigationDestinations.HomeScreen -> {
                val viewModel = hiltViewModel<HomeScreenViewModel>()
                HomeScreen(
                    modifier = Modifier.padding(paddingValues).fillMaxSize(),
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