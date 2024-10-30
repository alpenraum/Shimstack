package com.alpenraum.shimstack.newbike.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.alpenraum.shimstack.model.bikesetup.DetailsInputData
import com.alpenraum.shimstack.newbike.NewBikeContract
import com.alpenraum.shimstack.newbike.NewBikeDestinations
import com.alpenraum.shimstack.newbike.R
import com.alpenraum.shimstack.ui.compose.ButtonText
import com.alpenraum.shimstack.ui.compose.InfoText
import com.alpenraum.shimstack.ui.compose.LargeButton
import com.alpenraum.shimstack.ui.compose.TextInput
import com.alpenraum.shimstack.ui.compose.number
import com.alpenraum.shimstack.ui.theme.AppTheme
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import com.alpenraum.shimstack.ui.R as CommonR

@Composable
fun EnterSetupScreen(
    state: NewBikeContract.State,
    intent: (NewBikeContract.Intent) -> Unit,
    event: SharedFlow<NewBikeContract.Event>,
    navigator: NavController?
) {
    LaunchedEffect(key1 = Unit) {
        event.collectLatest {
            when (it) {
                NewBikeContract.Event.NavigateToNextStep -> {
                    navigator?.navigate(NewBikeDestinations.SUCCESS.route)
                }

                else -> {}
            }
        }
    }

    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        Text(
            text = stringResource(id = R.string.label_entersetup_intro),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 8.dp)
        )

        Text(
            text = stringResource(id = CommonR.string.label_tire_pressure),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(top = 16.dp)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)) {
            TextInput(
                value = state.setupInput.frontTirePressure ?: "",
                onValueChange = {
                    intent(NewBikeContract.Intent.FrontTirePressureInput(it))
                },
                suffix = stringResource(id = CommonR.string.bar),
                modifier =
                    Modifier
                        .weight(1.0f)
                        .padding(top = 8.dp),
                label = "${stringResource(id = CommonR.string.front)} ${
                    stringResource(
                        id = CommonR.string.label_tire_pressure
                    )
                }",
                keyboardOptions = KeyboardOptions.number(ImeAction.Next)
            )
            TextInput(
                value = state.setupInput.rearTirePressure ?: "",
                onValueChange = {
                    intent(NewBikeContract.Intent.RearTirePressureInput(it))
                },
                suffix = stringResource(id = CommonR.string.bar),
                modifier =
                    Modifier
                        .weight(1.0f)
                        .padding(top = 8.dp),
                label = "${stringResource(id = CommonR.string.rear)} ${
                    stringResource(
                        id = CommonR.string.label_tire_pressure
                    )
                }",
                keyboardOptions = KeyboardOptions.number(ImeAction.Next)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (state.hasFrontSuspension()) {
            SuspensionInput(
                title = stringResource(id = CommonR.string.label_front_suspension),
                pressureInput = state.setupInput.frontSuspensionPressure ?: "",
                tokensInput = state.setupInput.frontSuspensionTokens ?: "",
                lscInput = state.setupInput.frontSuspensionLSC ?: "",
                hscInput = state.setupInput.frontSuspensionHSC ?: "",
                lsrInput = state.setupInput.frontSuspensionLSR ?: "",
                hsrInput = state.setupInput.frontSuspensionHSR ?: "",
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
        Spacer(modifier = Modifier.height(16.dp))
        if (state.hasRearSuspension()) {
            SuspensionInput(
                title = stringResource(id = CommonR.string.label_rear_suspension),
                pressureInput = state.setupInput.rearSuspensionPressure ?: "",
                tokensInput = state.setupInput.rearSuspensionTokens ?: "",
                lscInput = state.setupInput.rearSuspensionLSC ?: "",
                hscInput = state.setupInput.rearSuspensionHSC ?: "",
                lsrInput = state.setupInput.rearSuspensionLSR ?: "",
                hsrInput = state.setupInput.rearSuspensionHSR ?: "",
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
                intent(NewBikeContract.Intent.OnNextClicked(flowFinished = true))
            },
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            ButtonText(CommonR.string.label_next_step)
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
            suffix = stringResource(id = CommonR.string.bar),
            label = stringResource(id = CommonR.string.pressure),
            keyboardOptions = KeyboardOptions.number(ImeAction.Next),
            modifier = Modifier.weight(1.0f)
        )
        TextInput(
            value = tokensInput,
            onValueChange = onTokensChanged,
            label = stringResource(id = CommonR.string.tokens),
            keyboardOptions = KeyboardOptions.number(ImeAction.Next),
            modifier = Modifier.weight(1.0f)
        )
    }
    Text(
        text = stringResource(id = CommonR.string.comp),
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.padding(top = 16.dp)
    )
    InfoText(textRes = R.string.info_compression)
    DampingInput(
        lowSpeed = lscInput,
        highSpeed = hscInput,
        showHighSpeed = showHSC,
        onLowSpeedChanged = onLSCChanged,
        onHighSpeedChanged = onHSCChanged,
        modifier = Modifier.padding(top = 8.dp),
        isLastInput = false
    )
    Text(
        text = stringResource(id = CommonR.string.rebound),
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.padding(top = 16.dp)
    )
    InfoText(textRes = R.string.info_rebound)
    DampingInput(
        lowSpeed = lsrInput,
        highSpeed = hsrInput,
        showHighSpeed = showHSR,
        onLowSpeedChanged = onLSRChanged,
        onHighSpeedChanged = onHSRChanged,
        modifier = Modifier.padding(top = 8.dp),
        isLastInput = true
    )
}

@Composable
private fun ColumnScope.DampingInput(
    lowSpeed: String?,
    highSpeed: String?,
    showHighSpeed: Boolean,
    isLastInput: Boolean,
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
            keyboardOptions =
                KeyboardOptions.number(
                    if (isLastInput) ImeAction.Done else ImeAction.Next
                ),
            label = stringResource(id = R.string.label_entersetup_low_speed_clicks)
        )
        AnimatedVisibility(visible = showHighSpeed, modifier = Modifier.weight(1.0f)) {
            TextInput(
                value = highSpeed ?: "",
                onValueChange = { onHighSpeedChanged(it) },
                keyboardOptions = KeyboardOptions.number(ImeAction.Next),
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
            state =
                NewBikeContract.State(
                    detailsInput =
                        DetailsInputData(
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