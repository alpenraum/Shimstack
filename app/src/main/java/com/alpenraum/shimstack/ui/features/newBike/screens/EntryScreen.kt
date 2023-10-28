package com.alpenraum.shimstack.ui.features.newBike.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alpenraum.shimstack.R
import com.alpenraum.shimstack.data.bike.Bike
import com.alpenraum.shimstack.data.bikeTemplates.BikeTemplate
import com.alpenraum.shimstack.ui.compose.LargeButton
import com.alpenraum.shimstack.ui.compose.ShimstackRoundedCornerShape
import com.alpenraum.shimstack.ui.features.newBike.NewBikeContract
import com.alpenraum.shimstack.ui.theme.AppTheme
import kotlinx.collections.immutable.toImmutableList

@Composable
fun EntryScreen(state: NewBikeContract.State.Entry, intent: (NewBikeContract.Intent) -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = stringResource(id = R.string.header_new_bike_entry),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Text(
            text = stringResource(id = R.string.copy_new_bike_entry),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 8.dp)
        )
        var userInput by remember { mutableStateOf("") }
        OutlinedTextField(
            shape = ShimstackRoundedCornerShape(),
            singleLine = true,
            value = userInput,
            onValueChange = {
                userInput = it
                intent(NewBikeContract.Intent.Filter(it))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            label = { Text(text = "Bike name") } // TODO: localisation
        )
        AnimatedContent(
            modifier = Modifier
                .fillMaxWidth(),
            targetState = state.bikeTemplates.isNotEmpty(),
            label = "",
            transitionSpec = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Down) togetherWith fadeOut() + slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up
                )
            }
        ) {
            if (it) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1.0f, fill = false)
                        .padding(vertical = 16.dp)
                ) {
                    LazyColumn(
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        itemsIndexed(state.bikeTemplates) { index, item ->
                            Row(
                                modifier = Modifier
                                    .padding(
                                        vertical = 8.dp
                                    )
                                    .padding(horizontal = 16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                // todo: style that list element
                                Text(text = item.name)
                                Spacer(modifier = Modifier.weight(1.0f))
                                Text(text = item.type.name)
                            }
                            if (index < state.bikeTemplates.lastIndex) Divider()
                        }
                    }
                }
            } else {
                LargeButton(onClick = { /*TODO*/ }, modifier = Modifier.padding(vertical = 16.dp)) {
                    Text(text = stringResource(id = R.string.label_next_step)) // todo: better label
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun EntryPreview() {
    AppTheme {
        val templates = buildList {
            for (i in 0 until 30) {
                add(
                    BikeTemplate(
                        id = i,
                        name = "bike $i",
                        type = Bike.Type.ENDURO,
                        false,
                        150,
                        130
                    )
                )
            }
        }.toImmutableList()
        EntryScreen(NewBikeContract.State.Entry(templates)) {}
    }
}
