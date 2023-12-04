package com.alpenraum.shimstack.ui.features.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.alpenraum.shimstack.ui.base.BaseActivity
import com.alpenraum.shimstack.ui.compose.compositionlocal.LocalWindowSizeClass
import com.alpenraum.shimstack.ui.compose.fadeIn
import com.alpenraum.shimstack.ui.compose.fadeOut
import com.alpenraum.shimstack.ui.features.NavGraphs
import com.alpenraum.shimstack.ui.theme.AppTheme
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.defaults.NestedNavGraphDefaultAnimations
import com.ramcosta.composedestinations.animations.defaults.RootNavGraphDefaultAnimations
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import dagger.hilt.android.AndroidEntryPoint

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

    @OptIn(
        ExperimentalMaterial3WindowSizeClassApi::class,
        ExperimentalMaterialNavigationApi::class,
        ExperimentalAnimationApi::class
    )
    private fun initializeContent() {
        setContent {
            val windowSizeClass = calculateWindowSizeClass(activity = this)

            CompositionLocalProvider(LocalWindowSizeClass provides windowSizeClass) {
                AppTheme {
                    CompositionLocalProvider(
                        LocalContentColor provides MaterialTheme.colorScheme.onSurface
                    ) {
                        val navHostEngine = rememberAnimatedNavHostEngine(
                            navHostContentAlignment = Alignment.TopCenter,
                            rootDefaultAnimations = RootNavGraphDefaultAnimations(
                                enterTransition = { fadeIn() },
                                exitTransition = { fadeOut() }
                            ),
                            defaultAnimationsForNestedNavGraph = mapOf(
                                NavGraphs.root to NestedNavGraphDefaultAnimations(
                                    enterTransition = { slideInHorizontally() },
                                    exitTransition = { slideOutHorizontally() },
                                    popEnterTransition = { slideInHorizontally() },
                                    popExitTransition = { slideOutHorizontally() }
                                )
                            )
                        )
                        DestinationsNavHost(
                            navGraph = NavGraphs.root,
                            engine = navHostEngine
                        )
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
