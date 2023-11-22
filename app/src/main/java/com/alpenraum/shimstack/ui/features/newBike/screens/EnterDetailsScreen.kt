package com.alpenraum.shimstack.ui.features.newBike.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
import com.alpenraum.shimstack.ui.compose.PhonePreview
import com.alpenraum.shimstack.ui.compose.ShimstackRoundedCornerShape
import com.alpenraum.shimstack.ui.compose.TabletPreview
import com.alpenraum.shimstack.ui.compose.compositionlocal.LocalWindowSizeClass
import com.alpenraum.shimstack.ui.features.newBike.NewBikeContract

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnterDetailsScreen(
    state: NewBikeContract.State.Details,
    intent: (NewBikeContract.Intent) -> Unit
) {
    val isCompactScreen =
        LocalWindowSizeClass.current.widthSizeClass == WindowWidthSizeClass.Compact
    Column {
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
                // TODO
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
            }
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
                    colors = ExposedDropdownMenuDefaults.textFieldColors()
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = {
                    expanded = false
                }) {
                    Bike.Type.values().forEach { selectionOption ->
                        DropdownMenuItem(text = {
                            Text(text = stringResource(selectionOption.labelRes))
                        }, onClick = {
                            // todo
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
                Switch(checked = state.bike.isEBike, onCheckedChange = { /*todo*/ })
            }
        }
        SuspensionInput(
            headline = stringResource(id = R.string.label_front_suspension),
            data = state.bike.frontSuspension,
            initialState = state.bike.frontSuspension != null,
            onValueChange = {
                // todo
            }
        )
        SuspensionInput(
            headline = stringResource(id = R.string.label_rear_suspension),
            data = state.bike.rearSuspension,
            initialState = state.bike.rearSuspension != null,
            onValueChange = {
                // todo
            }
        )
        TireInput(
            headline = stringResource(id = R.string.label_front_tire),
            data = state.bike.frontTire,
            {
            },
            {}
        ) // todo
        TireInput(
            headline = stringResource(id = R.string.label_rear_tire),
            data = state.bike.rearTire,
            {
            },
            {}
        ) // todo
    }
}

@Composable
private fun ColumnScope.SuspensionInput(
    headline: String,
    data: Suspension?,
    initialState: Boolean,
    onValueChange: (String) -> Unit
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
            onCheckedChange = { showSuspensionInput = !showSuspensionInput },
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
                    onValueChange(value)
                }
            },
            suffix = { Text(text = stringResource(id = R.string.mm)) },
            modifier = Modifier.padding(top = 8.dp),
            label = {
                Text(text = stringResource(id = R.string.label_travel))
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)

        )
    }
}

@Composable
private fun ColumnScope.TireInput(
    headline: String,
    data: Tire,
    onTireWidthChanged: (Double) -> Unit,
    onRimWidthChanged: (Double) -> Unit
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
            value = data.widthInMM.toString(),
            onValueChange = { value ->
                value.toDoubleOrNull()?.let {
                    onTireWidthChanged(it)
                }
            },
            suffix = { stringResource(id = R.string.mm) },
            modifier = Modifier.weight(1.0f).padding(end = 16.dp),
            label = {
                Text(text = stringResource(id = R.string.label_tire_width))
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal)
        )
        Column(
            modifier = Modifier.weight(1.0f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                shape = ShimstackRoundedCornerShape(),
                singleLine = true,
                value = data.internalRimWidthInMM.toString(),
                onValueChange = { value ->
                    value.toDoubleOrNull()?.let {
                        onRimWidthChanged(it)
                    }
                },
                suffix = { Text(text = stringResource(id = R.string.mm)) },
                label = {
                    Text(text = stringResource(id = R.string.label_internal_rim_width))
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal)
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
            state = NewBikeContract.State.Details(BikeTemplate.testData().toBikeDTO())
        ) {}
    }
}

@Preview(name = "PIXEL_5", device = Devices.PIXEL_4, showBackground = true)
@Composable
private fun Preview1() {
    PhonePreview {
        EnterDetailsScreen(
            state = NewBikeContract.State.Details(BikeTemplate.testData().toBikeDTO())
        ) {}
    }
}
