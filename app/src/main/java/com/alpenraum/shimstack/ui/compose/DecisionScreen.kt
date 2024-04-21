package com.alpenraum.shimstack.ui.compose

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alpenraum.shimstack.R
import com.alpenraum.shimstack.ui.theme.AppTheme

class DecisionButtonConfig(
    @StringRes val label: Int,
    val isPrimaryButton: Boolean,
    val onClick: () -> Unit
)

@Composable
fun DecisionScreen(
    @DrawableRes imageRes: Int?,
    @StringRes contentRes: Int,
    buttons: List<DecisionButtonConfig>,
    modifier: Modifier = Modifier
) {
    DecisionScreen(imageContent = {
        imageRes?.let { Image(painter = painterResource(id = it), contentDescription = null) }
    }, contentRes = contentRes, modifier = modifier, buttons = buttons)
}

@Composable
fun DecisionScreen(
    imageContent: @Composable () -> Unit,
    @StringRes contentRes: Int,
    modifier: Modifier = Modifier,
    buttons: List<DecisionButtonConfig>
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxHeight()
    ) {
        imageContent()
        Text(
            text = stringResource(contentRes),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        buttons.forEach {
            val label = @Composable { ButtonText(it.label) }
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
            listOf(
                DecisionButtonConfig(R.string.mm, true) {},
                DecisionButtonConfig(R.string.mm, false) {},
                DecisionButtonConfig(R.string.mm, false) {},
                DecisionButtonConfig(R.string.mm, true) {}
            )
        )
    }
}