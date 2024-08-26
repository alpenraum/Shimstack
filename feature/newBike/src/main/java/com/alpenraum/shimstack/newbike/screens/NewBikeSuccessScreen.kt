package com.alpenraum.shimstack.newbike.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.alpenraum.shimstack.newbike.NewBikeContract
import com.alpenraum.shimstack.newbike.NewBikeDestinations
import com.alpenraum.shimstack.newbike.R
import com.alpenraum.shimstack.ui.compose.DecisionButtonConfig
import com.alpenraum.shimstack.ui.compose.DecisionScreen
import com.alpenraum.shimstack.ui.theme.AppTheme
import com.alpenraum.shimstack.ui.R as CommonR

@Composable
fun NewBikeSuccessScreen(
    navigator: NavController? = null,
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
                DecisionButtonConfig(CommonR.string.label_done, true) {
                    intent(NewBikeContract.Intent.OnFlowFinished)
                },
                DecisionButtonConfig(R.string.label_add_another_bike, false) {
                    navigator?.navigate(NewBikeDestinations.ENTRY.route) {
                        popUpTo(NewBikeDestinations.ENTRY.route)
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