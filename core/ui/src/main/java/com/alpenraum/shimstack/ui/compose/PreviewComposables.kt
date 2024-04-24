package com.alpenraum.shimstack.ui.compose

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.alpenraum.shimstack.ui.compose.compositionlocal.LocalWindowSizeClass
import com.alpenraum.shimstack.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun PhonePreview(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalWindowSizeClass provides WindowSizeClass.calculateFromSize(DpSize.Zero)
    ) {
        AppTheme {
            content()
        }
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun TabletPreview(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalWindowSizeClass provides WindowSizeClass.calculateFromSize(DpSize(4000.dp, 2000.dp))
    ) {
        AppTheme {
            content()
        }
    }
}