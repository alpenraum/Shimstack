package com.alpenraum.shimstack.ui.screens.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.alpenraum.shimstack.ui.base.BaseActivity
import com.alpenraum.shimstack.ui.compose.compositionlocal.LocalWindowSizeClass
import com.alpenraum.shimstack.ui.screens.main.navigation.mainNavigation.MainNavigationDestination
import com.alpenraum.shimstack.ui.screens.mainScreens.MainScreenFeature
import com.alpenraum.shimstack.ui.screens.newBike.NewBikeFeature
import com.alpenraum.shimstack.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import dev.olshevski.navigation.reimagined.AnimatedNavHost
import dev.olshevski.navigation.reimagined.NavBackHandler
import dev.olshevski.navigation.reimagined.navigate
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

            CompositionLocalProvider(LocalWindowSizeClass provides windowSizeClass) {
                AppTheme {
                    val navController =
                        rememberNavController<MainNavigationDestination>(
                            startDestination = MainNavigationDestination.MainScreen
                        )

                    NavBackHandler(controller = navController)
                    AnimatedNavHost(controller = navController) {
                        when (it) {
                            MainNavigationDestination.MainScreen -> MainScreenFeature {
                                navController.navigate(
                                    MainNavigationDestination.NewBike
                                )
                            }

                            MainNavigationDestination.NewBike -> NewBikeFeature()
                        }
                    }
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        AppTheme {}
    }
}
