package com.alpenraum.shimstack.ui.features.newBike.screens

import android.widget.Toast
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.alpenraum.shimstack.R
import com.alpenraum.shimstack.ui.compose.DecisionButtonConfig
import com.alpenraum.shimstack.ui.compose.DecisionScreen
import com.alpenraum.shimstack.ui.compose.compositionlocal.LocalWindowSizeClass
import com.alpenraum.shimstack.ui.features.destinations.EnterSetupScreenDestination
import com.alpenraum.shimstack.ui.features.newBike.NewBikeNavGraph
import com.alpenraum.shimstack.ui.theme.AppTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
@NewBikeNavGraph
fun SetupDecisionScreen(
    navigator: DestinationsNavigator? = null
) {
    val isCompactScreen =
        LocalWindowSizeClass.current.widthSizeClass == WindowWidthSizeClass.Compact

    val context = LocalContext.current
    DecisionScreen(
        imageRes = null,
        contentRes = R.string.copy_new_bike_existing_setup,
        DecisionButtonConfig(R.string.label_yes, true) {
            navigator?.navigate(EnterSetupScreenDestination, onlyIfResumed = true)
        },
        DecisionButtonConfig(R.string.label_no, false) {
            Toast.makeText(context, "TODO: Insert Setup Wizard here", Toast.LENGTH_LONG).show()
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun SetupDecisionScreenPreview() {
    AppTheme {
        SetupDecisionScreen(null)
    }
}
