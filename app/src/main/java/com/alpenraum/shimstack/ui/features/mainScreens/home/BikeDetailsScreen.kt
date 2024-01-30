package com.alpenraum.shimstack.ui.features.mainScreens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alpenraum.shimstack.R
import com.alpenraum.shimstack.data.bike.BikeDTO
import com.alpenraum.shimstack.ui.compose.ButtonText
import com.alpenraum.shimstack.ui.compose.CrossfadeTransition
import com.alpenraum.shimstack.ui.theme.AppTheme
import com.ramcosta.composedestinations.annotation.Destination

@Destination(style = CrossfadeTransition::class)
@Composable
fun BikeDetailsScreen(bike: BikeDTO, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(top = 32.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BikeCard(showPlaceholder = false, modifier = Modifier.size(200.dp)) {
            BikeCardContent(bike)
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(top = 8.dp).width(IntrinsicSize.Min)
        ) {
            Button(
                {}, // TODO
                modifier = Modifier.weight(1.0f)
            ) {
                ButtonText(textRes = R.string.label_edit)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                {}, // TODO
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                ),
                modifier = Modifier.weight(1.0f)
            ) {
                ButtonText(textRes = R.string.label_delete)
            }
        }
        BikeInfo(bike = bike, Modifier)
    }
}

@Composable
fun BikeInfo(bike: BikeDTO, modifier: Modifier = Modifier) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top)) {
        // TODO: MAKE it nice
        Text(bike.name)
        Text(stringResource(bike.type.labelRes))
        Text(bike.frontSuspension.toString())
        Text(bike.rearSuspension.toString())
        Text(bike.frontTire.toString())
        Text(bike.rearTire.toString())
    }
}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    AppTheme {
        BikeDetailsScreen(bike = BikeDTO.empty())
    }
}