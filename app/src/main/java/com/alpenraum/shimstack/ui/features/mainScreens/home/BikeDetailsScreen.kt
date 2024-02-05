package com.alpenraum.shimstack.ui.features.mainScreens.home

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alpenraum.shimstack.R
import com.alpenraum.shimstack.data.bike.Bike
import com.alpenraum.shimstack.data.bike.BikeDTO
import com.alpenraum.shimstack.data.bike.Suspension
import com.alpenraum.shimstack.ui.compose.ButtonText
import com.alpenraum.shimstack.ui.compose.CrossfadeTransition
import com.alpenraum.shimstack.ui.compose.InfoText
import com.alpenraum.shimstack.ui.theme.AppTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination(style = CrossfadeTransition::class)
@Composable
fun BikeDetailsScreen(
    bike: BikeDTO,
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator? = null
) {
    Box(modifier.padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)) {
        IconButton(
            onClick = { navigator?.popBackStack() },
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(Icons.Default.Close, contentDescription = "close")
        }
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .align(Alignment.TopCenter).padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BikeCard(showPlaceholder = false, modifier = Modifier.size(200.dp)) {
                BikeCardContent(bike, modifier = Modifier.padding(8.dp))
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier =
                    Modifier
                        .padding(top = 8.dp)
                        .width(IntrinsicSize.Min)
            ) {
                // TODO: Button click
                Button(
                    {},
                    modifier = Modifier.weight(1.0f)
                ) {
                    ButtonText(textRes = R.string.label_edit)
                }
                Spacer(modifier = Modifier.width(16.dp))
                // TODO: Button Click
                Button(
                    {},
                    colors =
                        ButtonDefaults.outlinedButtonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        ),
                    modifier = Modifier.weight(1.0f)
                ) {
                    ButtonText(textRes = R.string.label_delete)
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            BikeInfo(bike = bike, Modifier)
        }
    }
}

@Composable
fun BikeInfo(
    bike: BikeDTO,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top),
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
    ) {
        Text(bike.name, style = MaterialTheme.typography.headlineSmall)
        InfoText(bike.type.labelRes)

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
        ) {
            Card(
                Modifier
                    .height(IntrinsicSize.Min)
                    .padding(top = 16.dp)
            ) {
                Column(modifier.padding(8.dp)) {
                    InfoText(R.string.label_front_tire)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(modifier = Modifier.weight(1.0f)) {
                        // front tire
                        TextPair(
                            R.string.label_tire_pressure,
                            bike.frontTire.getFormattedPressure(context),
                            modifier = Modifier.weight(1.0f)
                        )
                        TextPair(
                            R.string.label_tire_width,
                            bike.frontTire.getFormattedTireWidth(context),
                            modifier = Modifier.weight(1.0f)
                        )
                        TextPair(
                            R.string.label_internal_rim_width,
                            bike.frontTire.getFormattedInternalRimWidth(context),
                            modifier = Modifier.weight(1.0f)
                        )
                    }
                }
            }

            Card(
                Modifier
                    .height(IntrinsicSize.Min)
                    .padding(top = 16.dp)
            ) {
                Column(modifier.padding(8.dp)) {
                    InfoText(R.string.label_rear_tire)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(modifier = Modifier.weight(1.0f)) {
                        // rear tire
                        TextPair(
                            R.string.label_tire_pressure,
                            bike.rearTire.getFormattedPressure(context),
                            modifier = Modifier.weight(1.0f)
                        )
                        TextPair(
                            R.string.label_tire_width,
                            bike.rearTire.getFormattedTireWidth(context),
                            modifier = Modifier.weight(1.0f)
                        )
                        TextPair(
                            R.string.label_internal_rim_width,
                            bike.rearTire.getFormattedInternalRimWidth(context),
                            modifier = Modifier.weight(1.0f)
                        )
                    }
                }
            }

            bike.frontSuspension?.let { suspension ->
                Card(
                    Modifier
                        .height(IntrinsicSize.Min)
                        .padding(top = 16.dp)
                ) {
                    Column(modifier.padding(8.dp)) {
                        SuspensionBlock(
                            label = R.string.label_front_suspension,
                            context = context,
                            suspension = suspension
                        )
                    }
                }
            }
            bike.rearSuspension?.let { suspension ->
                Card(
                    Modifier
                        .height(IntrinsicSize.Min)
                        .padding(top = 16.dp)
                ) {
                    Column(modifier.padding(8.dp)) {
                        SuspensionBlock(
                            label = R.string.label_rear_suspension,
                            context = context,
                            suspension = suspension
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TextPair(
    @StringRes label: Int,
    text: String,
    modifier: Modifier = Modifier
) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text)
        InfoText(textRes = label)
    }
}

@Composable
private fun ColumnScope.SuspensionBlock(
    @StringRes label: Int,
    context: Context,
    suspension: Suspension,
    modifier: Modifier = Modifier
) {
    InfoText(label)
    Spacer(modifier = Modifier.height(4.dp))
    Row(modifier = Modifier.weight(1.0f)) {
        TextPair(
            label = R.string.label_travel,
            text = suspension.getFormattedTravel(context),
            modifier = Modifier.weight(1.0f)
        )
        TextPair(
            label = R.string.pressure,
            text = suspension.getFormattedPressure(context),
            modifier = Modifier.weight(1.0f)
        )
        TextPair(
            label = R.string.tokens,
            text = suspension.tokens.toString(),
            modifier = Modifier.weight(1.0f)
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
    Row(modifier = Modifier.weight(1.0f), horizontalArrangement = Arrangement.Center) {
        TextPair(
            label = R.string.rebound,
            text = suspension.getFormattedRebound(context),
            modifier = Modifier.weight(1.0f)
        )
        TextPair(
            label = R.string.comp,
            text = suspension.getFormattedCompression(context),
            modifier = Modifier.weight(1.0f)
        )
        Spacer(modifier.weight(1.0f))
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    AppTheme {
        BikeDetailsScreen(
            bike =
                BikeDTO.empty()
                    .copy(
                        name = "Specialized Stumpjumper",
                        type = Bike.Type.ALL_MTN,
                        frontSuspension = Suspension(150),
                        rearSuspension = Suspension(150)
                    )
        )
    }
}