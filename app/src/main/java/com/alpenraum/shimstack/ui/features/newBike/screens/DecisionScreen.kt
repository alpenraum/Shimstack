package com.alpenraum.shimstack.ui.features.newBike.screens

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alpenraum.shimstack.R
import com.alpenraum.shimstack.ui.compose.LargeButton
import com.alpenraum.shimstack.ui.compose.LargeSecondaryButton
import com.alpenraum.shimstack.ui.compose.compositionlocal.LocalWindowSizeClass
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

    DecisionScreen(
        imageRes = null,
        contentRes = R.string.copy_new_bike_existing_setup,
        ButtonConfig(R.string.label_yes, true) { TODO() },
        ButtonConfig(R.string.label_no, false) { TODO() }
    )
}

private class ButtonConfig(
    @StringRes val label: Int,
    val isPrimaryButton: Boolean,
    val onClick: () -> Unit
)

@Composable
private fun DecisionScreen(
    @DrawableRes imageRes: Int?,
    @StringRes contentRes: Int,
    vararg buttons: ButtonConfig
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxHeight()
    ) {
        imageRes?.let { Image(painter = painterResource(id = it), contentDescription = null) }
        Text(
            text = stringResource(contentRes),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        buttons.forEach {
            val label = @Composable { Text(text = stringResource(id = it.label))}
            if (it.isPrimaryButton) {
                LargeButton(onClick = it.onClick, modifier = Modifier.padding(bottom = 8.dp)) {
                    label()
                }
            } else {
                LargeSecondaryButton(
                    onClick = it.onClick,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    label()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DecisionScreenPreview() {
    AppTheme {
        DecisionScreen(
            imageRes = R.drawable.ic_launcher_foreground,
            contentRes = R.string.copy_no_shock,
            ButtonConfig(R.string.mm, true) {},
            ButtonConfig(R.string.mm, false) {},
            ButtonConfig(R.string.mm, false) {},
            ButtonConfig(R.string.mm, true) {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SetupDecisionScreenPreview() {
    AppTheme {
        SetupDecisionScreen()
    }
}
