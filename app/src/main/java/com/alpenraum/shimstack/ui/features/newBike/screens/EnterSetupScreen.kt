package com.alpenraum.shimstack.ui.features.newBike.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alpenraum.shimstack.R
import com.alpenraum.shimstack.ui.compose.InfoText
import com.alpenraum.shimstack.ui.compose.LargeButton
import com.alpenraum.shimstack.ui.compose.TextInput
import com.alpenraum.shimstack.ui.compose.compositionlocal.LocalWindowSizeClass
import com.alpenraum.shimstack.ui.compose.number
import com.alpenraum.shimstack.ui.features.destinations.SetupDecisionScreenDestination
import com.alpenraum.shimstack.ui.features.newBike.DetailsInputData
import com.alpenraum.shimstack.ui.features.newBike.NewBikeContract
import com.alpenraum.shimstack.ui.features.newBike.NewBikeNavGraph
import com.alpenraum.shimstack.ui.theme.AppTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
@Destination
@NewBikeNavGraph
fun EnterSetupScreen(
    state: NewBikeContract.State,
    intent: (NewBikeContract.Intent) -> Unit,
    event: SharedFlow<NewBikeContract.Event>,
    navigator: DestinationsNavigator?
) {
    LaunchedEffect(key1 = Unit) {
        event.collectLatest {
            when (it) {
                NewBikeContract.Event.NavigateToNextStep -> {
                    navigator?.navigate(SetupDecisionScreenDestination, onlyIfResumed = true)
                }

                NewBikeContract.Event.NavigateToPreviousStep -> { /*empty */
                }
            }
        }
    }

    val isCompactScreen =
        LocalWindowSizeClass.current.widthSizeClass == WindowWidthSizeClass.Compact
    val context = LocalContext.current

    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        Text(
            text = stringResource(id = R.string.label_entersetup_intro),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 8.dp)
        )

        Text(
            text = stringResource(id = R.string.label_tire_pressure),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(top = 16.dp)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)) {
            TextInput(
                value = "${state.setupInput.frontTirePressure}",
                onValueChange = {
                    intent(NewBikeContract.Intent.FrontTirePressureInput(it))
                },
                suffix = stringResource(id = R.string.bar),
                modifier = Modifier
                    .weight(1.0f)
                    .padding(top = 8.dp),
                label = "${stringResource(id = R.string.front)} ${
                    stringResource(
                        id = R.string.label_tire_pressure
                    )
                }",
                keyboardOptions = KeyboardOptions.number(ImeAction.Next)

            )
            TextInput(
                value = "${state.setupInput.rearSuspensionTokens}",
                onValueChange = {
                    intent(NewBikeContract.Intent.RearTirePressureInput(it))
                },
                suffix = stringResource(id = R.string.bar),
                modifier = Modifier
                    .weight(1.0f)
                    .padding(top = 8.dp),
                label = "${stringResource(id = R.string.rear)} ${
                    stringResource(
                        id = R.string.label_tire_pressure
                    )
                }",
                keyboardOptions = KeyboardOptions.number(ImeAction.Next)

            )
        }

        if (state.detailsInput.frontTravel?.isNotEmpty() == true) {
            SuspensionInput(
                title = stringResource(id = R.string.label_front_suspension),
                pressureInput = "${state.setupInput.frontSuspensionPressure}",
                tokensInput = "${state.setupInput.frontSuspensionTokens}",
                lscInput = "${state.setupInput.frontSuspensionLSC}",
                hscInput = "${state.setupInput.frontSuspensionHSC}",
                lsrInput = "${state.setupInput.frontSuspensionLSR}",
                hsrInput = "${state.setupInput.frontSuspensionHSR}",
                showHSC = state.hasHSCFork,
                showHSR = state.hasHSRFork,
                onPressureChanged = { intent(NewBikeContract.Intent.FrontSuspensionPressure(it)) },
                onTokensChanged = { intent(NewBikeContract.Intent.FrontSuspensionTokens(it)) },
                onLSCChanged = { intent(NewBikeContract.Intent.FrontSuspensionLSC(it)) },
                onHSCChanged = { intent(NewBikeContract.Intent.FrontSuspensionHSC(it)) },
                onLSRChanged = { intent(NewBikeContract.Intent.FrontSuspensionLSR(it)) },
                onHSRChanged = { intent(NewBikeContract.Intent.FrontSuspensionHSR(it)) }
            )
        }

        if (state.detailsInput.rearTravel?.isNotEmpty() == true) {
            SuspensionInput(
                title = stringResource(id = R.string.label_rear_suspension),
                pressureInput = "${state.setupInput.rearSuspensionPressure}",
                tokensInput = "${state.setupInput.rearSuspensionTokens}",
                lscInput = "${state.setupInput.rearSuspensionLSC}",
                hscInput = "${state.setupInput.rearSuspensionHSC}",
                lsrInput = "${state.setupInput.rearSuspensionLSR}",
                hsrInput = "${state.setupInput.rearSuspensionHSR}",
                showHSC = state.hasHSCShock,
                showHSR = state.hasHSRShock,
                onPressureChanged = { intent(NewBikeContract.Intent.RearSuspensionPressure(it)) },
                onTokensChanged = { intent(NewBikeContract.Intent.RearSuspensionTokens(it)) },
                onLSCChanged = { intent(NewBikeContract.Intent.RearSuspensionLSC(it)) },
                onHSCChanged = { intent(NewBikeContract.Intent.RearSuspensionHSC(it)) },
                onLSRChanged = { intent(NewBikeContract.Intent.RearSuspensionLSR(it)) },
                onHSRChanged = { intent(NewBikeContract.Intent.RearSuspensionHSR(it)) }
            )
        }

        AnimatedVisibility(visible = state.showSetupOutlierHint) {
            InfoText(textRes = R.string.copy_setup_outlier_hint)
        }
        LargeButton(
            enabled = state.setupValidationErrors == null,
            onClick = {
                intent(NewBikeContract.Intent.OnNextClicked)
            },
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            Text(text = stringResource(id = R.string.label_next_step))
        }
    }
}

@Composable
private fun ColumnScope.SuspensionInput(
    title: String,
    pressureInput: String,
    tokensInput: String,
    lscInput: String,
    hscInput: String?,
    lsrInput: String,
    hsrInput: String?,
    showHSC: Boolean,
    showHSR: Boolean,

    onPressureChanged: (String) -> Unit,
    onTokensChanged: (String) -> Unit,
    onLSCChanged: (String) -> Unit,
    onHSCChanged: (String) -> Unit,
    onLSRChanged: (String) -> Unit,
    onHSRChanged: (String) -> Unit
) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier.padding(top = 16.dp)
    )
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)) {
        TextInput(
            value = pressureInput,
            onValueChange = onPressureChanged,
            suffix = stringResource(id = R.string.bar),
            label = stringResource(id = R.string.pressure),
            keyboardOptions = KeyboardOptions.number(ImeAction.Next),
            modifier = Modifier.weight(1.0f)
        )
        TextInput(
            value = tokensInput,
            onValueChange = onTokensChanged,
            suffix = stringResource(id = R.string.bar),
            label = stringResource(id = R.string.tokens),
            keyboardOptions = KeyboardOptions.number(ImeAction.Next),
            modifier = Modifier.weight(1.0f)
        )
    }
    Text(
        text = stringResource(id = R.string.comp),
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.padding(top = 16.dp)
    )
    InfoText(textRes = androidx.appcompat.R.string.abc_search_hint)
    DampingInput(
        lowSpeed = lscInput,
        highSpeed = hscInput,
        showHighSpeed = showHSC,
        onLowSpeedChanged = onLSCChanged,
        onHighSpeedChanged = onHSCChanged,
        modifier = Modifier.padding(top = 8.dp)
    )
    Text(
        text = stringResource(id = R.string.rebound),
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.padding(top = 16.dp)
    )
    InfoText(textRes = androidx.appcompat.R.string.abc_search_hint)
    DampingInput(
        lowSpeed = lsrInput,
        highSpeed = hsrInput,
        showHighSpeed = showHSR,
        onLowSpeedChanged = onLSRChanged,
        onHighSpeedChanged = onHSRChanged,
        modifier = Modifier.padding(top = 8.dp)
    )
}

@Composable
private fun ColumnScope.DampingInput(
    lowSpeed: String?,
    highSpeed: String?,
    showHighSpeed: Boolean,
    onHighSpeedChanged: (String) -> Unit,
    onLowSpeedChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
    ) {
        TextInput(
            value = lowSpeed ?: "",
            onValueChange = { onLowSpeedChanged(it) },
            modifier = Modifier.weight(1.0f),
            label = stringResource(id = R.string.label_entersetup_low_speed_clicks)
        )
        AnimatedVisibility(visible = showHighSpeed, modifier = Modifier.weight(1.0f)) {
            TextInput(
                value = highSpeed ?: "",
                onValueChange = { onHighSpeedChanged(it) },
                label = stringResource(id = R.string.label_entersetup_high_speed_clicks)
            )
        }
    }
}

@Preview(device = Devices.PIXEL_4, showBackground = true)
@Composable
private fun Preview() {
    AppTheme {
        EnterSetupScreen(
            state = NewBikeContract.State(
                detailsInput = DetailsInputData(
                    frontTravel = "1",
                    rearTravel = "1"
                ),
                hasHSCShock = true,
                hasHSRShock = true,
                hasHSCFork = true,
                hasHSRFork = true
            ),
            intent = {},
            event = MutableSharedFlow(),
            null
        )
    }
}
