package com.alpenraum.shimstack.ui.compose

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
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
import com.alpenraum.shimstack.data.models.bike.Bike
import com.alpenraum.shimstack.data.models.bike.BikeType
import com.alpenraum.shimstack.data.models.pressure.Pressure
import com.alpenraum.shimstack.data.models.suspension.Damping
import com.alpenraum.shimstack.data.models.suspension.Suspension
import com.alpenraum.shimstack.data.models.tire.Tire
import com.alpenraum.shimstack.ui.features.mainScreens.home.UIDataLabel
import com.alpenraum.shimstack.ui.theme.AppTheme
import kotlinx.collections.immutable.ImmutableList

@Composable
fun TireDetails(
    bigCard: Boolean,
    bike: Bike,
    modifier: Modifier = Modifier
) {
    DetailsCard(title = R.string.tire, bigCard, modifier = modifier) {
        Row(
            modifier =
                Modifier
                    .padding(horizontal = 8.dp)
                    .padding(top = 16.dp)
                    .weight(1.0f),
            horizontalArrangement = Arrangement.Center
        ) {
            val data = bike.getTireUIData(LocalContext.current)
            SimpleTextPair(
                heading = stringResource(R.string.front),
                content = data.first.content,
                bigCard = bigCard
            )
            VerticalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            SimpleTextPair(
                heading = stringResource(R.string.rear),
                content = data.second.content,
                bigCard = bigCard
            )
        }
    }
}

@Composable
private fun SimpleTextPair(
    heading: String,
    content: String,
    bigCard: Boolean,
    modifier: Modifier = Modifier
) = Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
    AdaptiveSizeText(
        text = heading,
        style =
            MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.outline
            ),
        modifier = Modifier.padding(bottom = 4.dp),
        textAlign = TextAlign.Center
    )
    if (bigCard) {
        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    } else {
        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ForkDetails(
    bigCard: Boolean,
    bike: Bike
) {
    SuspensionDetails(
        bigCard = bigCard,
        suspensionData = bike.getFrontSuspensionUIData(LocalContext.current),
        titleRes = R.string.fork,
        errorTextRes = R.string.copy_no_fork
    )
}

@Composable
fun ShockDetails(
    bigCard: Boolean,
    bike: Bike
) {
    SuspensionDetails(
        bigCard = bigCard,
        suspensionData = bike.getRearSuspensionUIData(LocalContext.current),
        titleRes = R.string.shock,
        errorTextRes = R.string.copy_no_shock
    )
}

@Composable
private fun SuspensionDetails(
    bigCard: Boolean,
    @StringRes titleRes: Int,
    @StringRes errorTextRes: Int,
    suspensionData: ImmutableList<UIDataLabel>?
) {
    DetailsCard(title = titleRes, bigCard = bigCard) {
        suspensionData?.let {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(top = 16.dp)
                        .weight(1.0f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                            .weight(1.0f)
                ) {
                    SuspensionQuarter(data = it[0]) // ,modifier = Modifier.weight(1.0f)
                    // modifier = Modifier.weight(1.0f,fill = false)
                    SuspensionQuarter(data = it[1])
                }
                HorizontalDivider()
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .weight(1.0f)
                ) {
                    SuspensionQuarter(data = it[2])
                    SuspensionQuarter(data = it[3])
                }
            }
        } ?: run {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                        .weight(1.0f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    stringResource(errorTextRes),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun SuspensionQuarter(
    data: UIDataLabel,
    modifier: Modifier = Modifier
) {
    when (data) {
        is UIDataLabel.Simple -> {
            SimpleTextPair(
                heading = data.heading,
                content = data.content,
                bigCard = false,
                modifier = modifier
            )
        }

        is UIDataLabel.Complex -> {
            Column(modifier = Modifier) {
                data.data.forEach {
                    Row(modifier = Modifier.padding(bottom = 4.dp)) {
                        Text(
                            text = it.key,
                            style =
                                MaterialTheme.typography.bodyMedium.copy(
                                    color = MaterialTheme.colorScheme.outline
                                ),
                            modifier = Modifier.padding(end = 8.dp),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = it.value,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailsCard(
    @StringRes title: Int,
    bigCard: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable (ColumnScope.() -> Unit)
) {
    Card(
        modifier =
            modifier
                .height(CARD_DIMENSION)
                .width(if (bigCard) CARD_DIMENSION * 2.0f + CARD_MARGIN else CARD_DIMENSION * 1.0f)
                .padding(vertical = CARD_MARGIN / 2.0f)
    ) {
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
private fun PreviewTireData() {
    AppTheme {
        TireDetails(bigCard = false, Bike.empty())
    }
}

@Preview()
@Composable
private fun PreviewTireDataBig() {
    AppTheme {
        TireDetails(bigCard = true, Bike.empty())
    }
}

private val testBike =
    Bike(
        name = "1",
        type = BikeType.UNKNOWN,
        frontSuspension = Suspension(Pressure(60.0), Damping(1), Damping(1), 3, 140),
        frontTire =
            Tire(
                Pressure(20.0),
                0.0,
                0.0
            ),
        rearTire = Tire(Pressure(20.0), 0.0, 0.0),
        isEBike = false,
        id = 0
    )
private val testBikeMax =
    Bike(
        name = "1",
        type = BikeType.UNKNOWN,
        frontSuspension = Suspension(Pressure(60.0), Damping(1, 2), Damping(3, 4), 5, 420),
        frontTire =
            Tire(
                Pressure(20.0),
                0.0,
                0.0
            ),
        rearTire = Tire(Pressure(20.0), 0.0, 0.0),
        isEBike = false,
        id = 0
    )

@Preview
@Composable
private fun PreviewForkData() = ForkDetails(bigCard = false, bike = testBike)

@Preview
@Composable
private fun PreviewForkDataBig() = ForkDetails(bigCard = true, bike = testBike)

@Preview
@Composable
private fun PreviewForkDataMax() = ForkDetails(bigCard = false, bike = testBikeMax)

@Preview
@Composable
private fun PreviewForkDataMaxBig() = ForkDetails(bigCard = true, bike = testBikeMax)