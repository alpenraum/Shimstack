package com.alpenraum.shimstack.ui.features.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.invisibleToUser
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alpenraum.shimstack.R
import com.alpenraum.shimstack.ui.compose.LargeButton
import com.alpenraum.shimstack.ui.theme.AppTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun OnboardingScreen(
    onSkipButtonClicked: () -> Unit,
    modifier: Modifier = Modifier,
    onAddBikeClicked: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Welcome to Shimstack!",
            style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Medium),
            textAlign = TextAlign.Center
        )
        Text(
            text = "Welcome to your personal suspension expert.",
            modifier = Modifier.padding(top = 8.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Card(
            shape = RoundedCornerShape(8.dp, 8.dp, 0.dp, 0.dp),
            modifier = Modifier.weight(1.0f)
                .fillMaxWidth(),
            elevation = CardDefaults.elevatedCardElevation(),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(8.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))
                    LargeButton(
                        onClick = onAddBikeClicked,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    ) {
                        Text(
                            "Add my first bike",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    LargeButton(
                        onClick = onSkipButtonClicked,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    ) {
                        Text(
                            "Take me to the app",
                            style = MaterialTheme.typography.titleMedium

                        )
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondaryContainer)
                    // TODO: Animation
                    Image(
                        painter = painterResource(id = R.drawable.ic_hardtail),
                        contentDescription = null,
                        colorFilter = colorFilter,
                        modifier = modifier
                            .semantics { invisibleToUser() }
                            .size(128.dp)

                    )
                    Image(
                        painter = painterResource(id = R.drawable.il_mtb_trail),
                        contentDescription = null,
                        colorFilter = colorFilter,
                        modifier = modifier
                            .semantics { invisibleToUser() }
                            .scale(3.0f)
                    )
                }
            }
        }
    }
}

@Preview(device = Devices.PIXEL_5, showSystemUi = true)
@Composable
private fun Preview() {
    AppTheme {
        OnboardingScreen(
            onSkipButtonClicked = { /*TODO*/ },
            modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
        }
    }
}

@Preview(device = Devices.PIXEL_C, showSystemUi = true)
@Composable
private fun LargePreview() {
    AppTheme {
        OnboardingScreen(
            onSkipButtonClicked = { /*TODO*/ },
            modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
        }
    }
}