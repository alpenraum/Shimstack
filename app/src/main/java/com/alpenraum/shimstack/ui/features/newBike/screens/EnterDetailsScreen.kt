package com.alpenraum.shimstack.ui.features.newBike.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import com.alpenraum.shimstack.data.bike.Bike
import com.alpenraum.shimstack.data.bike.Suspension
import com.alpenraum.shimstack.data.bike.Tire
import com.alpenraum.shimstack.data.bikeTemplates.BikeTemplate
import com.alpenraum.shimstack.ui.compose.InfoText
import com.alpenraum.shimstack.ui.compose.LargeButton
import com.alpenraum.shimstack.ui.compose.PhonePreview
import com.alpenraum.shimstack.ui.compose.ShimstackRoundedCornerShape
import com.alpenraum.shimstack.ui.compose.TabletPreview
import com.alpenraum.shimstack.ui.compose.compositionlocal.LocalWindowSizeClass
import com.alpenraum.shimstack.ui.features.destinations.SetupDecisionScreenDestination
import com.alpenraum.shimstack.ui.features.newBike.NewBikeContract
import com.alpenraum.shimstack.ui.features.newBike.NewBikeNavGraph
import com.alpenraum.shimstack.usecases.biometrics.ValidateBikeDTOUseCase
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

                NewBikeContract.Event.NavigateToPreviousStep -> { /*empty */
                }
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
        OutlinedTextField(
            shape = ShimstackRoundedCornerShape(),
            singleLine = true,
            value = state.bike.name,
            onValueChange = {
                intent(NewBikeContract.Intent.BikeNameInput(it))
            },
            modifier = if (!isCompactScreen) {
                Modifier.padding(
                    top = 16.dp
                )
            } else {
                Modifier.fillMaxWidth().padding(top = 16.dp)
            },
            label = {
                Text(text = stringResource(id = R.string.label_name))
            },
            isError = state.validationErrors?.name == false,
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
                modifier = if (isCompactScreen) {
                    Modifier.weight(1.0f)
                } else {
                    Modifier.padding(
                        end = 32.dp
                    )
                }
            ) {
                OutlinedTextField(
                    shape = ShimstackRoundedCornerShape(),
                    readOnly = true,
                    value = stringResource(state.bike.type.labelRes),
                    onValueChange = {},
                    label = { Text(text = stringResource(id = R.string.label_type)) },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expanded
                        )
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                    isError = state.validationErrors?.type == false
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = {
                    expanded = false
                }) {
                    Bike.Type.values().forEach { selectionOption ->
                        DropdownMenuItem(text = {
                            Text(text = stringResource(selectionOption.labelRes))
                        }, onClick = {
                            intent(NewBikeContract.Intent.BikeTypeInput(selectionOption))
                            expanded = false
                        })
                    }
                }
            }
            Column(
                modifier = Modifier.padding(start = 8.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(text = stringResource(id = R.string.copy_new_bike_ebike))
                Switch(
                    checked = state.bike.isEBike,
                    onCheckedChange = { intent(NewBikeContract.Intent.EbikeInput(it)) }
                )
            }
        }
        SuspensionInput(
            headline = stringResource(id = R.string.label_front_suspension),
            data = state.bike.frontSuspension,
            isError = state.validationErrors?.frontSuspension == false,
            initialState = state.bike.frontSuspension != null,
            onSwitchToggle = {
                if (!it) intent(NewBikeContract.Intent.FrontSuspensionInput(null))
            },
            onValueChange = {
                intent(NewBikeContract.Intent.FrontSuspensionInput(it))
            }
        )
        SuspensionInput(
            headline = stringResource(id = R.string.label_rear_suspension),
            data = state.bike.rearSuspension,
            isError = state.validationErrors?.rearSuspension == false,
            initialState = state.bike.rearSuspension != null,
            onSwitchToggle = {
                if (!it) intent(NewBikeContract.Intent.RearSuspensionInput(null))
            },
            onValueChange = {
                intent(NewBikeContract.Intent.RearSuspensionInput(it))
            }
        )
        TireInput(
            headline = stringResource(id = R.string.label_front_tire),
            data = state.bike.frontTire,
            isError = state.validationErrors?.frontTire == false,
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
            data = state.bike.rearTire,
            isError = state.validationErrors?.rearTire == false,
            lastInputImeAction = ImeAction.Done,
            {
                intent(NewBikeContract.Intent.RearTireWidthInput(it))
            },
            { intent(NewBikeContract.Intent.RearInternalRimWidthInput(it)) }
        )

        LargeButton(
            enabled = state.validationErrors == null && state.bike.isPopulated(),
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
    headline: String,
    data: Suspension?,
    isError: Boolean,
    initialState: Boolean,
    onSwitchToggle: (Boolean) -> Unit,
    onValueChange: (Int?) -> Unit
) {
    var showSuspensionInput by remember {
        mutableStateOf(initialState)
    }

    Row(
        modifier = Modifier.padding(top = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = headline, style = MaterialTheme.typography.headlineSmall)
        Switch(
            checked = showSuspensionInput,
            onCheckedChange = {
                showSuspensionInput = it
                onSwitchToggle(it)
            },
            modifier = Modifier.padding(start = 16.dp)
        )
    }
    AnimatedVisibility(visible = showSuspensionInput) {
        OutlinedTextField(
            shape = ShimstackRoundedCornerShape(),
            singleLine = true,
            value = data?.travel?.toString() ?: "0",
            onValueChange = { value ->
                value.toIntOrNull()?.let {
                    onValueChange(it)
                } ?: onValueChange(null)
            },
            suffix = { Text(text = stringResource(id = R.string.mm)) },
            modifier = Modifier.padding(top = 8.dp),
            label = {
                Text(text = stringResource(id = R.string.label_travel))
            },
            isError = isError,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            )

        )
    }
}

@Composable
private fun ColumnScope.TireInput(
    headline: String,
    data: Tire,
    isError: Boolean,
    lastInputImeAction: ImeAction,
    onTireWidthChanged: (Double) -> Unit,
    onRimWidthChanged: (Double?) -> Unit

) {
    Text(
        text = headline,
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier.padding(top = 16.dp)
    )
    Row(modifier = Modifier.padding(top = 8.dp)) {
        OutlinedTextField(
            shape = ShimstackRoundedCornerShape(),
            singleLine = true,
            value = if (data.widthInMM == 0.0) "" else data.widthInMM.toString(),
            onValueChange = { value ->
                value.toDoubleOrNull()?.let {
                    onTireWidthChanged(it)
                }
            },
            suffix = { Text(text = stringResource(id = R.string.mm)) },
            modifier = Modifier.weight(1.0f).padding(end = 16.dp),
            label = {
                Text(text = stringResource(id = R.string.label_tire_width))
            },
            isError = isError,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions()
        )
        Column(
            modifier = Modifier.weight(1.0f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                shape = ShimstackRoundedCornerShape(),
                singleLine = true,
                value = if (data.internalRimWidthInMM == 0.0 || data.internalRimWidthInMM == null) "" else data.internalRimWidthInMM.toString(),
                onValueChange = { value ->
                    value.toDoubleOrNull()?.let {
                        onRimWidthChanged(it)
                    } ?: onRimWidthChanged(null)
                },
                suffix = { Text(text = stringResource(id = R.string.mm)) },
                label = {
                    Text(text = stringResource(id = R.string.label_internal_rim_width))
                },
                isError = isError,
                keyboardOptions = KeyboardOptions.Default.copy(
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
            state = NewBikeContract.State(bike = BikeTemplate.testData().toBikeDTO()),
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
            state = NewBikeContract.State(bike = BikeTemplate.testData().toBikeDTO()),
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
            state = NewBikeContract.State(
                bike = BikeTemplate.testData().toBikeDTO(),
                validationErrors = ValidateBikeDTOUseCase.Result.Failure(
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
