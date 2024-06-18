package com.alpenraum.shimstack.ui.features.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.alpenraum.shimstack.datastore.ShimstackDatastore
import com.alpenraum.shimstack.navigation.ShimstackNavHost
import com.alpenraum.shimstack.ui.base.BaseActivity
import com.alpenraum.shimstack.ui.compose.compositionlocal.LocalWindowSizeClass
import com.alpenraum.shimstack.ui.theme.AppTheme
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<MainViewModel>() {
    override val viewModelClass: Class<MainViewModel> = MainViewModel::class.java

    @Inject
    lateinit var datastore: ShimstackDatastore

//    private val isBiometricAuthenticationAvailableUseCase = IsBiometricAuthenticationAvailableUseCase()
//    private val triggerBiometricsPromptUseCase = TriggerBiometricsPromptUseCase()

    override fun onViewModelBound() {
        viewModel.onBound(this)
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

    // private val navGraph = NavGraphs.root

    @OptIn(
        ExperimentalMaterial3WindowSizeClassApi::class,
        ExperimentalMaterialNavigationApi::class,
        ExperimentalAnimationApi::class
    )
    private fun initializeContent() {
        setContent {
            val windowSizeClass = calculateWindowSizeClass(activity = this)
            val useDynamicTheme = datastore.useDynamicTheme.collectAsState(false)

            CompositionLocalProvider(LocalWindowSizeClass provides windowSizeClass) {
                AppTheme(useDynamicTheme = useDynamicTheme.value) {
                    CompositionLocalProvider(
                        LocalContentColor provides MaterialTheme.colorScheme.onSurface
                    ) {
                        val navController = rememberNavController()
                        ShimstackNavHost(navController)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    AppTheme(useDynamicTheme = false) {}
}