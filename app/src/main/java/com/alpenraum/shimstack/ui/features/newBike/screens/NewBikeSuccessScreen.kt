package com.alpenraum.shimstack.ui.features.newBike.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.alpenraum.shimstack.R
import com.alpenraum.shimstack.ui.compose.DecisionButtonConfig
import com.alpenraum.shimstack.ui.compose.DecisionScreen
import com.alpenraum.shimstack.ui.features.destinations.EntryScreenDestination
import com.alpenraum.shimstack.ui.features.newBike.NewBikeContract
import com.alpenraum.shimstack.ui.features.newBike.NewBikeNavGraph
import com.alpenraum.shimstack.ui.theme.AppTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo

@Composable
@Destination
@NewBikeNavGraph
fun NewBikeSuccessScreen(
    navigator: DestinationsNavigator? = null,
    intent: (NewBikeContract.Intent) -> Unit
) {
    BackHandler {
        intent(NewBikeContract.Intent.OnFlowFinished)
    }
    DecisionScreen(
        imageContent = {
            Image(
                painter = painterResource(id = R.drawable.il_mountain_biker),
                contentDescription = null,
                modifier = Modifier.scale(0.7f)
            )
        },
        contentRes = R.string.copy_new_bike_success,
        modifier = Modifier,
        buttons =
            listOf(
                DecisionButtonConfig(R.string.label_done, true) {
                    intent(NewBikeContract.Intent.OnFlowFinished)
                },
                DecisionButtonConfig(R.string.label_add_another_bike, false) {
                    navigator?.navigate(EntryScreenDestination, onlyIfResumed = true) {
                        popUpTo(EntryScreenDestination)
                    }
                }
            )
    )
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    AppTheme {
        NewBikeSuccessScreen(null) {}
    }
}