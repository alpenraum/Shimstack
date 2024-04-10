@file:OptIn(ExperimentalMaterial3Api::class)

package com.alpenraum.shimstack.ui.features.mainScreens.home

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alpenraum.shimstack.R
import com.alpenraum.shimstack.data.models.bike.Bike
import com.alpenraum.shimstack.data.models.bike.BikeType
import com.alpenraum.shimstack.data.models.pressure.Pressure
import com.alpenraum.shimstack.data.models.suspension.Damping
import com.alpenraum.shimstack.data.models.suspension.Suspension
import com.alpenraum.shimstack.data.models.tire.Tire
import com.alpenraum.shimstack.ui.base.use
import com.alpenraum.shimstack.ui.compose.AttachToLifeCycle
import com.alpenraum.shimstack.ui.compose.ButtonText
import com.alpenraum.shimstack.ui.compose.CrossfadeTransition
import com.alpenraum.shimstack.ui.compose.InfoText
import com.alpenraum.shimstack.ui.compose.TextInput
import com.alpenraum.shimstack.ui.compose.number
import com.alpenraum.shimstack.ui.theme.AppTheme
import com.alpenraum.shimstack.usecases.ValidateBikeUseCase
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest

@Destination(style = CrossfadeTransition::class, navArgsDelegate = BikeDetailsNavArgs::class)
@Composable
fun BikeDetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: BikeDetailsViewModel = hiltViewModel(),
    navigator: DestinationsNavigator? = null
) {
    AttachToLifeCycle(viewModel = viewModel)
    val (state, intent, event) = use(viewModel)

    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        event.collectLatest {
            when (it) {
                BikeDetailsContract.Event.NavigateBack -> navigator?.popBackStack()
                is BikeDetailsContract.Event.ShowSnackbar ->
                    Toast.makeText(
                        context,
                        it.message,
                        Toast.LENGTH_LONG
                    ).show()
            }
        }
    }
    Content(state = state, intents = intent, modifier = modifier)
}

@Composable
private fun Content(
    state: BikeDetailsContract.State,
    modifier: Modifier = Modifier,
    intents: (BikeDetailsContract.Intent) -> Unit
) {
    Box(modifier.padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)) {
        IconButton(
            onClick = { intents(BikeDetailsContract.Intent.OnBackPressed) },
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(Icons.Default.Close, contentDescription = "close")
        }
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BikeCard(showPlaceholder = false, modifier = Modifier.size(200.dp)) {
                BikeCardContent(state.bike, modifier = Modifier.padding(8.dp))
            }

            Spacer(modifier = Modifier.height(32.dp))
            BikeInfo(state = state, intents, Modifier)
        }
        AnimatedVisibility(
            visible = state.editMode,
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            ExtendedFloatingActionButton(
                onClick = {
                    if (state.validationFailure == null) {
                        intents(BikeDetailsContract.Intent.OnSaveClicked)
                    }
                },
                containerColor =
                    if (state.validationFailure == null) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_save),
                        contentDescription = null
                    )
                },
                text = {
                    ButtonText(textRes = R.string.label_save)
                }
            )
        }
    }
}

@Composable
fun BikeInfo(
    state: BikeDetailsContract.State,
    intents: (BikeDetailsContract.Intent) -> Unit,
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
        AnimatedContent(targetState = state.editMode, label = "") {
            if (it) {
                EditBikeHeading(state = state, intents = intents)
            } else {
                BikeHeading(state, intents)
            }
        }

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
        ) {
            FrontTireBlock(state = state, intents = intents, context = context)
            RearTireBlock(state = state, intents = intents, context = context)
            FrontSuspensionBlock(state = state, context = context, intents)
            RearSuspensionBlock(state = state, context, intents)
            if (state.editMode) {
                Spacer(Modifier.height(64.dp))
            }
        }
    }
}

@Composable
private fun BikeHeading(
    state: BikeDetailsContract.State,
    intents: (BikeDetailsContract.Intent) -> Unit
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                state.bike.name,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.weight(1.0f)
            )
            IconButton(onClick = { intents(BikeDetailsContract.Intent.OnEditClicked) }) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = stringResource(id = R.string.label_edit)
                )
            }
        }
        InfoText(state.bike.type.labelRes)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditBikeHeading(
    state: BikeDetailsContract.State,
    intents: (BikeDetailsContract.Intent) -> Unit
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextInput(
                value = state.bike.name,
                onValueChange = {
                    intents(
                        BikeDetailsContract.Intent.Input.BikeName(it)
                    )
                },
                modifier = Modifier.weight(1.0f),
                label = stringResource(id = R.string.label_name)
            )
        }
        Spacer(Modifier.height(8.dp))

        var expanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            },
            modifier = Modifier
        ) {
            TextInput(
                readOnly = true,
                value = stringResource(state.bike.type.labelRes),
                onValueChange = {},
                label = stringResource(id = R.string.label_type),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                },
                modifier = Modifier.menuAnchor(),
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                isError = state.validationFailure?.type == false
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = {
                expanded = false
            }) {
                BikeType.entries.forEach { selectionOption ->
                    if (selectionOption != BikeType.UNKNOWN) {
                        DropdownMenuItem(text = {
                            Text(text = stringResource(selectionOption.labelRes))
                        }, onClick = {
                            intents(BikeDetailsContract.Intent.Input.BikeType(selectionOption))
                            expanded = false
                        })
                    }
                }
            }
        }
    }
}

@Composable
private fun FrontTireBlock(
    state: BikeDetailsContract.State,
    intents: (BikeDetailsContract.Intent) -> Unit,
    context: Context
) {
    TireBlock(
        tire = state.bike.frontTire,
        label = R.string.label_front_tire,
        intents = intents,
        context = context,
        editMode = state.editMode,
        isFront = true,
        showError = state.validationFailure?.frontTire == false
    )
}

@Composable
private fun RearTireBlock(
    state: BikeDetailsContract.State,
    intents: (BikeDetailsContract.Intent) -> Unit,
    context: Context
) {
    TireBlock(
        tire = state.bike.rearTire,
        label = R.string.label_rear_tire,
        intents = intents,
        context = context,
        editMode = state.editMode,
        isFront = false,
        showError = state.validationFailure?.rearTire == false
    )
}

@Composable
private fun TireBlock(
    tire: Tire,
    @StringRes label: Int,
    intents: (BikeDetailsContract.Intent) -> Unit,
    context: Context,
    editMode: Boolean,
    isFront: Boolean,
    showError: Boolean
) {
    AnimatedContent(targetState = editMode, label = "") { edit ->
        val content: @Composable () -> Unit =
            if (edit) {
                {
                    Column(Modifier.padding(8.dp)) {
                        InfoText(label)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.weight(1.0f),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            TextInput(
                                value = tire.pressure.pressureInBar.toPlainString(),
                                onValueChange = {
                                    intents(
                                        if (isFront) {
                                            BikeDetailsContract.Intent.Input.FrontTirePressure(
                                                it
                                            )
                                        } else {
                                            BikeDetailsContract.Intent.Input.RearTirePressure(it)
                                        }
                                    )
                                },
                                label =
                                    stringResource(
                                        id = R.string.label_tire_pressure
                                    ),
                                suffix = stringResource(id = R.string.bar),
                                modifier = Modifier.weight(1.0f),
                                keyboardOptions = KeyboardOptions.number(ImeAction.Next),
                                isError = showError
                            )

                            TextInput(
                                value = tire.widthInMM.toString(),
                                onValueChange = {
                                    intents(
                                        if (isFront) {
                                            BikeDetailsContract.Intent.Input.FrontTireWidth(
                                                it
                                            )
                                        } else {
                                            BikeDetailsContract.Intent.Input.RearTireWidth(it)
                                        }
                                    )
                                },
                                label =
                                    stringResource(
                                        id = R.string.label_tire_width
                                    ),
                                suffix = stringResource(id = R.string.mm),
                                modifier = Modifier.weight(1.0f),
                                keyboardOptions = KeyboardOptions.number(ImeAction.Next),
                                isError = showError
                            )

                            TextInput(
                                value = tire.internalRimWidthInMM.toString(),
                                onValueChange = {
                                    intents(
                                        if (isFront) {
                                            BikeDetailsContract.Intent.Input.FrontTireInternalRimWidth(
                                                it
                                            )
                                        } else {
                                            BikeDetailsContract.Intent.Input.RearTireInternalRimWidth(
                                                it
                                            )
                                        }
                                    )
                                },
                                label =
                                    stringResource(
                                        id = R.string.label_internal_rim_width
                                    ),
                                suffix = stringResource(id = R.string.mm),
                                modifier = Modifier.weight(1.0f),
                                keyboardOptions = KeyboardOptions.number(ImeAction.Done),
                                isError = showError
                            )
                        }
                    }
                }
            } else {
                {
                    Column(Modifier.padding(8.dp)) {
                        InfoText(label)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(modifier = Modifier.weight(1.0f)) {
                            // rear tire
                            TextPair(
                                R.string.label_tire_pressure,
                                tire.getFormattedPressure(context),
                                modifier = Modifier.weight(1.0f)
                            )
                            TextPair(
                                R.string.label_tire_width,
                                tire.getFormattedTireWidth(context),
                                modifier = Modifier.weight(1.0f)
                            )
                            TextPair(
                                R.string.label_internal_rim_width,
                                tire.getFormattedInternalRimWidth(context),
                                modifier = Modifier.weight(1.0f)
                            )
                        }
                    }
                }
            }

        Card(
            Modifier
                .height(IntrinsicSize.Min)
                .padding(top = 16.dp)
        ) {
            content()
        }
    }
}

@Composable
private fun FrontSuspensionBlock(
    state: BikeDetailsContract.State,
    context: Context,
    intents: (BikeDetailsContract.Intent) -> Unit
) {
    state.bike.frontSuspension?.let { suspension ->
        Card(
            Modifier
                .height(IntrinsicSize.Min)
                .padding(top = 16.dp)
        ) {
            Column(Modifier.padding(8.dp)) {
                SuspensionBlock(
                    label = R.string.label_front_suspension,
                    context = context,
                    suspension = suspension,
                    editMode = state.editMode,
                    intents = intents,
                    isFront = true,
                    showError = state.validationFailure?.frontSuspension == false
                )
            }
        }
    }
}

@Composable
private fun RearSuspensionBlock(
    state: BikeDetailsContract.State,
    context: Context,
    intents: (BikeDetailsContract.Intent) -> Unit
) {
    state.bike.rearSuspension?.let { suspension ->
        Card(
            Modifier
                .height(IntrinsicSize.Min)
                .padding(top = 16.dp)
        ) {
            Column(Modifier.padding(8.dp)) {
                SuspensionBlock(
                    label = R.string.label_rear_suspension,
                    context = context,
                    suspension = suspension,
                    editMode = state.editMode,
                    intents = intents,
                    isFront = false,
                    showError = state.validationFailure?.rearSuspension == false
                )
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
private fun SuspensionBlock(
    @StringRes label: Int,
    context: Context,
    suspension: Suspension,
    editMode: Boolean,
    isFront: Boolean,
    showError: Boolean,
    modifier: Modifier = Modifier,
    intents: (BikeDetailsContract.Intent) -> Unit
) {
    AnimatedContent(targetState = editMode, modifier = modifier) {
        Column {
            InfoText(label)
            Spacer(modifier = Modifier.height(4.dp))
            if (it) {
                Row(
                    modifier = Modifier.weight(1.0f),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextInput(
                        value = suspension.travel.toString(),
                        onValueChange = {
                            intents(
                                if (isFront) {
                                    BikeDetailsContract.Intent.Input.FrontSuspensionTravel(
                                        it
                                    )
                                } else {
                                    BikeDetailsContract.Intent.Input.RearSuspensionTravel(it)
                                }
                            )
                        },
                        label =
                            stringResource(
                                id = R.string.label_travel
                            ),
                        suffix = stringResource(id = R.string.mm),
                        modifier = Modifier.weight(1.0f),
                        keyboardOptions = KeyboardOptions.number(ImeAction.Next),
                        isError = showError
                    )

                    TextInput(
                        value = suspension.pressure.pressureInBar.toPlainString(),
                        onValueChange = {
                            intents(
                                if (isFront) {
                                    BikeDetailsContract.Intent.Input.FrontSuspensionPressure(
                                        it
                                    )
                                } else {
                                    BikeDetailsContract.Intent.Input.RearSuspensionPressure(it)
                                }
                            )
                        },
                        label =
                            stringResource(
                                id = R.string.pressure
                            ),
                        suffix = stringResource(id = R.string.bar),
                        modifier = Modifier.weight(1.0f),
                        keyboardOptions = KeyboardOptions.number(ImeAction.Next),
                        isError = showError
                    )

                    TextInput(
                        value = suspension.tokens.toString(),
                        onValueChange = {
                            intents(
                                if (isFront) {
                                    BikeDetailsContract.Intent.Input.FrontSuspensionTokens(
                                        it
                                    )
                                } else {
                                    BikeDetailsContract.Intent.Input.RearSuspensionTokens(
                                        it
                                    )
                                }
                            )
                        },
                        label =
                            stringResource(
                                id = R.string.tokens
                            ),
                        modifier = Modifier.weight(1.0f),
                        keyboardOptions = KeyboardOptions.number(ImeAction.Done),
                        isError = showError
                    )
                }
            } else {
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
                    Spacer(Modifier.weight(1.0f))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    AppTheme {
        Content(
            state =
                BikeDetailsContract.State(
                    Bike.empty()
                        .copy(
                            name = "Specialized Stumpjumper",
                            type = BikeType.ALL_MTN,
                            frontSuspension =
                                Suspension(
                                    pressure = Pressure(10.0),
                                    compression = Damping(0, 1),
                                    rebound = Damping(2, 3),
                                    travel = 150,
                                    tokens = 5
                                ),
                            rearSuspension = Suspension(150)
                        )
                ),
            intents = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EditPreview() {
    AppTheme {
        Content(
            state =
                BikeDetailsContract.State(
                    Bike.empty()
                        .copy(
                            name = "Specialized Stumpjumper",
                            type = BikeType.ALL_MTN,
                            frontSuspension =
                                Suspension(
                                    pressure = Pressure(10.0),
                                    compression = Damping(0, 1),
                                    rebound = Damping(2, 3),
                                    travel = 150,
                                    tokens = 5
                                ),
                            rearSuspension = Suspension(150)
                        ),
                    validationFailure =
                        ValidateBikeUseCase.DetailsFailure(
                            false,
                            false,
                            false,
                            false,
                            false,
                            false
                        ),
                    editMode = true
                ),
            intents = {}
        )
    }
}