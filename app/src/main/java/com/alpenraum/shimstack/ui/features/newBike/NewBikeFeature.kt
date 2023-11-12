package com.alpenraum.shimstack.ui.features.newBike

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.alpenraum.shimstack.R
import com.alpenraum.shimstack.common.popBackstackOrNavigate
import com.alpenraum.shimstack.ui.base.use
import com.alpenraum.shimstack.ui.compose.AttachToLifeCycle
import com.alpenraum.shimstack.ui.features.newBike.screens.EnterDetailsScreen
import com.alpenraum.shimstack.ui.features.newBike.screens.EntryScreen
import dev.olshevski.navigation.reimagined.AnimatedNavHost
import dev.olshevski.navigation.reimagined.NavBackHandler
import dev.olshevski.navigation.reimagined.hilt.hiltViewModel
import dev.olshevski.navigation.reimagined.rememberNavController

@Composable
fun NewBikeFeature() {
    val navController = rememberNavController<NewBikeDestinations>(
        startDestination = NewBikeDestinations.Entry
    )
    NavBackHandler(controller = navController)
    val viewModel: NewBikeViewModel = hiltViewModel()
    AttachToLifeCycle(viewModel = viewModel)
    val (state, intent, event) = use(viewModel)

    LaunchedEffect(key1 = state) {
        val destination: NewBikeDestinations = when (state) {
            is NewBikeContract.State.Entry -> NewBikeDestinations.Entry
            is NewBikeContract.State.Details -> NewBikeDestinations.Details
        }

        navController.popBackstackOrNavigate(destination)
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = stringResource(id = R.string.header_new_bike_entry),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        AnimatedNavHost(controller = navController) {
            when (it) {
                is NewBikeDestinations.Entry -> EntryScreen(
                    state = state as NewBikeContract.State.Entry,
                    intent = intent
                )

                NewBikeDestinations.Details -> EnterDetailsScreen(
                    state = state as NewBikeContract.State.Details,
                    intent = intent
                )
            }
        }
    }
}
