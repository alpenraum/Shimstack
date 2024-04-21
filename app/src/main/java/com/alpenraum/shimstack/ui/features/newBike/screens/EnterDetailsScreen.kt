package com.alpenraum.shimstack.ui.features.newBike.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alpenraum.shimstack.R
import com.alpenraum.shimstack.model.bike.BikeType
import com.alpenraum.shimstack.ui.compose.ButtonText
import com.alpenraum.shimstack.ui.compose.InfoText
import com.alpenraum.shimstack.ui.compose.LargeButton
import com.alpenraum.shimstack.ui.compose.PhonePreview
import com.alpenraum.shimstack.ui.compose.TabletPreview
import com.alpenraum.shimstack.ui.compose.TextInput
import com.alpenraum.shimstack.ui.compose.compositionlocal.LocalWindowSizeClass
import com.alpenraum.shimstack.ui.compose.number
import com.alpenraum.shimstack.ui.features.destinations.SetupDecisionScreenDestination
import com.alpenraum.shimstack.ui.features.newBike.NewBikeContract
import com.alpenraum.shimstack.ui.features.newBike.NewBikeNavGraph
import com.alpenraum.shimstack.usecases.ValidateBikeUseCase
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Destination
@NewBikeNavGraph
fun EnterDetailsScreen(
    navigator: DestinationsNavigator? = null,
    state: NewBikeContract.State,
    intent: (NewBikeContract.Intent) -> Unit,
    event: SharedFlow<NewBikeContract.Event>
) {
    LaunchedEffect(key1 = Unit) {
        event.collectLatest {
            when (it) {
                NewBikeContract.Event.NavigateToNextStep -> {
                    navigator?.navigate(SetupDecisionScreenDestination, onlyIfResumed = true)
                }

                else -> {}
            }
        }
    }

    val isCompactScreen =
        LocalWindowSizeClass.current.widthSizeClass == WindowWidthSizeClass.Compact
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        Text(
            text = stringResource(id = R.string.copy_new_bike_details),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 8.dp)
        )
        TextInput(
            value = state.detailsInput.name ?: "",
            onValueChange = {
                intent(NewBikeContract.Intent.BikeNameInput(it))
            },
            modifier =
                if (!isCompactScreen) {
                    Modifier.padding(
                        top = 16.dp
                    )
                } else {
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                },
            label = stringResource(id = R.string.label_name),
            isError = state.detailsValidationErrors?.name == false,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
        )

        Row(
            modifier = Modifier.padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            var expanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                },
                modifier =
                    if (isCompactScreen) {
                        Modifier.weight(1.0f)
                    } else {
                        Modifier.padding(
                            end = 32.dp
                        )
                    }
            ) {
                TextInput(
                    readOnly = true,
                    value = stringResource(state.bikeType.labelRes),
                    onValueChange = {},
                    label = stringResource(id = R.string.label_type),
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expanded
                        )
                    },
                    modifier = Modifier.menuAnchor(),
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                    isError = state.detailsValidationErrors?.type == false
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = {
                    expanded = false
                }) {
                    BikeType.entries.forEach { selectionOption ->
                        if (selectionOption != BikeType.UNKNOWN) {
                            DropdownMenuItem(text = {
                                Text(text = stringResource(selectionOption.labelRes))
                            }, onClick = {
                                intent(NewBikeContract.Intent.BikeTypeInput(selectionOption))
                                expanded = false
                            })
                        }
                    }
                }
            }
            Column(
                modifier = Modifier.padding(start = 8.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(text = stringResource(id = R.string.copy_new_bike_ebike))
                Switch(
                    checked = state.isEbike,
                    onCheckedChange = { intent(NewBikeContract.Intent.EbikeInput(it)) }
                )
            }
        }
        val frontSuspensionTravel = state.detailsInput.frontTravel
        SuspensionInput(
            headline = stringResource(id = R.string.label_front_suspension),
            travel = frontSuspensionTravel ?: "",
            isError = state.detailsValidationErrors?.frontSuspension == false,
            initialState = frontSuspensionTravel != null,
            hscSwitchState = state.hasHSCFork,
            hsrSwitchState = state.hasHSRFork,
            onSuspensionSwitchToggle = {
                if (!it) intent(NewBikeContract.Intent.FrontSuspensionInput(null))
            },
            onValueChange = {
                intent(NewBikeContract.Intent.FrontSuspensionInput(it))
            },
            onHSCSwitchToggle = { intent(NewBikeContract.Intent.HSCInput(it, true)) },
            onHSRSwitchToggle = { intent(NewBikeContract.Intent.HSRInput(it, true)) }
        )
        val rearSuspensionTravel = state.detailsInput.rearTravel
        SuspensionInput(
            headline = stringResource(id = R.string.label_rear_suspension),
            travel = rearSuspensionTravel ?: "",
            isError = state.detailsValidationErrors?.rearSuspension == false,
            initialState = rearSuspensionTravel != null,
            hscSwitchState = state.hasHSCShock,
            hsrSwitchState = state.hasHSRShock,
            onSuspensionSwitchToggle = {
                if (!it) intent(NewBikeContract.Intent.RearSuspensionInput(null))
            },
            onValueChange = {
                intent(NewBikeContract.Intent.RearSuspensionInput(it))
            },
            onHSCSwitchToggle = { intent(NewBikeContract.Intent.HSCInput(it, false)) },
            onHSRSwitchToggle = { intent(NewBikeContract.Intent.HSRInput(it, false)) }
        )
        TireInput(
            headline = stringResource(id = R.string.label_front_tire),
            tireWidth = state.detailsInput.frontTireWidth,
            internalRimWidth = state.detailsInput.frontInternalRimWidth,
            isError = state.detailsValidationErrors?.frontTire == false,
            lastInputImeAction = ImeAction.Next,
            {
                intent(NewBikeContract.Intent.FrontTireWidthInput(it))
            },
            {
                intent(NewBikeContract.Intent.FrontInternalRimWidthInput(it))
            }
        )
        TireInput(
            headline = stringResource(id = R.string.label_rear_tire),
            tireWidth = state.detailsInput.rearTireWidth,
            internalRimWidth = state.detailsInput.rearInternalRimWidth,
            isError = state.detailsValidationErrors?.rearTire == false,
            lastInputImeAction = ImeAction.Done,
            {
                intent(NewBikeContract.Intent.RearTireWidthInput(it))
            },
            { intent(NewBikeContract.Intent.RearInternalRimWidthInput(it)) }
        )

        LargeButton(
            enabled = state.detailsValidationErrors == null,
            onClick = {
                intent(NewBikeContract.Intent.OnNextClicked())
            },
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            ButtonText(textRes = R.string.label_next_step)
        }
    }
}

@Composable
private fun ColumnScope.SuspensionInput(
    headline: String,
    travel: String,
    isError: Boolean,
    initialState: Boolean,
    hscSwitchState: Boolean,
    hsrSwitchState: Boolean,
    onSuspensionSwitchToggle: (Boolean) -> Unit,
    onValueChange: (String?) -> Unit,
    onHSCSwitchToggle: (Boolean) -> Unit,
    onHSRSwitchToggle: (Boolean) -> Unit
) {
    var showSuspensionInput by remember {
        mutableStateOf(initialState)
    }

    Row(
        modifier = Modifier.padding(top = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = headline, style = MaterialTheme.typography.titleLarge)
        Switch(
            checked = showSuspensionInput,
            onCheckedChange = {
                showSuspensionInput = it
                onSuspensionSwitchToggle(it)
            },
            modifier = Modifier.padding(start = 16.dp)
        )
    }
    AnimatedVisibility(visible = showSuspensionInput) {
        Column {
            TextInput(
                value = travel,
                onValueChange = { value ->
                    onValueChange(value)
                },
                suffix = stringResource(id = R.string.mm),
                modifier = Modifier.padding(top = 8.dp),
                label = stringResource(id = R.string.label_travel),
                isError = isError,
                keyboardOptions = KeyboardOptions.number(ImeAction.Next)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Separate high- and low-speed Compression? ", modifier = Modifier.weight(1.0f))
                Switch(
                    checked = hscSwitchState,
                    onCheckedChange = onHSCSwitchToggle,
                    modifier = Modifier.weight(1.0f)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Separate high- and low-speed Rebound?", modifier = Modifier.weight(1.0f))
                Switch(
                    checked = hsrSwitchState,
                    onCheckedChange = onHSRSwitchToggle,
                    modifier = Modifier.weight(1.0f)
                )
            }
        }
    }
}

@Composable
private fun ColumnScope.TireInput(
    headline: String,
    tireWidth: String?,
    internalRimWidth: String?,
    isError: Boolean,
    lastInputImeAction: ImeAction,
    onTireWidthChanged: (String) -> Unit,
    onRimWidthChanged: (String?) -> Unit
) {
    Text(
        text = headline,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(top = 16.dp)
    )
    Row(modifier = Modifier.padding(top = 8.dp)) {
        TextInput(
            value = tireWidth ?: "",
            onValueChange = { value ->
                onTireWidthChanged(value)
            },
            suffix = stringResource(id = R.string.mm),
            modifier =
                Modifier
                    .weight(1.0f)
                    .padding(end = 16.dp),
            label = stringResource(id = R.string.label_tire_width),
            isError = isError,
            keyboardOptions = KeyboardOptions.number(ImeAction.Next)
        )
        Column(
            modifier = Modifier.weight(1.0f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextInput(
                value = internalRimWidth ?: "",
                onValueChange = { value ->
                    onRimWidthChanged(value)
                },
                suffix = stringResource(id = R.string.mm),
                label = stringResource(id = R.string.label_internal_rim_width),
                isError = isError,
                keyboardOptions =
                    KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = lastInputImeAction
                    )
            )
            InfoText(R.string.copy_new_bike_internal_width_inf)
        }
    }
}

@Preview(name = "PIXEL_C", device = Devices.PIXEL_C, showBackground = true)
@Composable
private fun Preview() {
    TabletPreview {
        EnterDetailsScreen(
            state = NewBikeContract.State(),
            intent = {},
            event = MutableSharedFlow()
        )
    }
}

@Preview(name = "PIXEL_5", device = Devices.PIXEL_4, showBackground = true)
@Composable
private fun Preview1() {
    PhonePreview {
        EnterDetailsScreen(
            state = NewBikeContract.State(),
            intent = {},
            event = MutableSharedFlow()
        )
    }
}

@Preview(name = "ERROR", device = Devices.PIXEL_4, showBackground = true)
@Composable
private fun Error() {
    PhonePreview {
        EnterDetailsScreen(
            state =
                NewBikeContract.State(
                    detailsValidationErrors =
                        ValidateBikeUseCase.DetailsFailure(
                            name = false,
                            type = false,
                            frontTire = false,
                            rearTire = false,
                            frontSuspension = false,
                            rearSuspension = false
                        )
                ),
            intent = {},
            event = MutableSharedFlow()
        )
    }
}