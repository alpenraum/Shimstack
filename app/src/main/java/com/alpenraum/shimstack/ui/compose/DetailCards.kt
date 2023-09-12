package com.alpenraum.shimstack.ui.compose

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alpenraum.shimstack.R
import com.alpenraum.shimstack.data.bike.Bike
import com.alpenraum.shimstack.data.bike.Pressure
import com.alpenraum.shimstack.data.bike.Suspension
import com.alpenraum.shimstack.data.bike.Tire
import com.alpenraum.shimstack.ui.theme.AppTheme

@Composable
fun TireDetails(bigCard: Boolean, bike: Bike) {
    DetailsCard(title = R.string.tire, bigCard) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp).padding(top = 16.dp).weight(1.0f),
            horizontalArrangement = Arrangement.Center
        ) {
            SimpleTextPair(heading = R.string.front, data = bike.frontTire, bigCard = bigCard)
            VerticalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            SimpleTextPair(heading = R.string.rear, data = bike.rearTire, bigCard = bigCard)
        }
    }
}

@Composable
private fun SimpleTextPair(@StringRes heading: Int, data: Tire, bigCard: Boolean) =
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(id = heading),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.outline
            ),
            modifier = Modifier.padding(bottom = 8.dp),
            textAlign = TextAlign.Center
        )
        if (bigCard) {
            // TODO
        } else {
            Text(
                text = data.getFormattedPressure(LocalContext.current),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }

@Composable
fun ForkDetails(bigCard: Boolean, bike: Bike) {
    DetailsCard(title = R.string.fork, bigCard = bigCard) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp).padding(top = 16.dp).weight(1.0f),
            horizontalArrangement = Arrangement.Center
        ) {
            SimpleTextPair(heading = R.string.front, data = bike.frontTire, bigCard = bigCard)
            VerticalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            SimpleTextPair(heading = R.string.rear, data = bike.rearTire, bigCard = bigCard)
        }
    }
}

private sealed class SuspensionDataLabel {
    class Simple(val heading: String, val content: String) : SuspensionDataLabel()
    class Complex(val data: Map<String, String>) : SuspensionDataLabel()
}

@Composable
private fun SuspensionQuarter(data: SuspensionDataLabel, bigCard: Boolean) {
    when (data) {
        is SuspensionDataLabel.Simple -> {
            // SimpleTextPair(heading = a, data = a, bigCard = a)
        }
        is SuspensionDataLabel.Complex -> {}
    }
}

@Composable
private fun DetailsCard(
    @StringRes title: Int,
    bigCard: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable() (ColumnScope.() -> Unit)
) {
    Card(modifier = modifier.height(CARD_DIMENSION).aspectRatio(if (bigCard) 2.0f else 1.0f)) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier.fillMaxWidth()
        ) {
            content()
            Text(
                text = stringResource(id = title),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp, top = 4.dp)
            )
        }
    }
}

@Preview()
@Composable
fun PreviewTireData() {
    AppTheme {
        TireDetails(bigCard = false, Bike.empty())
    }
}

@Preview()
@Composable
fun PreviewTireDataBig() {
    AppTheme {
        TireDetails(bigCard = true, Bike.empty())
    }
}

private val testBike = Bike(
    name = "1",
    type = Bike.Type.UNKNOWN,
    frontSuspension = Suspension(60.0, 1, 2, 3, 4, 5),
    frontTire = Tire(
        Pressure(20.0),
        0.0,
        0.0
    ),
    rearTire = Tire(Pressure(20.0), 0.0, 0.0),
    isEBike = false
)
private val testBikeMin = Bike(
    name = "1",
    type = Bike.Type.UNKNOWN,
    frontSuspension = Suspension(60.0, 1, null, 3, null, 5),
    frontTire = Tire(
        Pressure(20.0),
        0.0,
        0.0
    ),
    rearTire = Tire(Pressure(20.0), 0.0, 0.0),
    isEBike = false
)

@Preview
@Composable
fun PreviewForkData() = ForkDetails(bigCard = false, bike = testBike)

@Preview
@Composable
fun PreviewForkDataBig() = ForkDetails(bigCard = true, bike = testBike)

@Preview
@Composable
fun PreviewForkDataMin() = ForkDetails(bigCard = false, bike = testBikeMin)

@Preview
@Composable
fun PreviewForkDataMinBig() = ForkDetails(bigCard = true, bike = testBikeMin)
