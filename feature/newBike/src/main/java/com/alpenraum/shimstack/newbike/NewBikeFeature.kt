package com.alpenraum.shimstack.newbike

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alpenraum.shimstack.newbike.screens.EnterDetailsScreen
import com.alpenraum.shimstack.newbike.screens.EnterSetupScreen
import com.alpenraum.shimstack.newbike.screens.EntryScreen
import com.alpenraum.shimstack.newbike.screens.NewBikeSuccessScreen
import com.alpenraum.shimstack.newbike.screens.SetupDecisionScreen
import com.alpenraum.shimstack.ui.base.use
import com.alpenraum.shimstack.ui.compose.components.AttachToLifeCycle
import kotlinx.coroutines.flow.collectLatest

@Composable
fun NewBikeFeature(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: NewBikeViewModel = hiltViewModel()
) {
    AttachToLifeCycle(viewModel = viewModel)
    val (state, intent, event) = use(viewModel, navController)

    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        event.collectLatest {
            when (it) {
                NewBikeContract.Event.NavigateToHomeScreen -> navController.popBackStack()
                is NewBikeContract.Event.ShowToast ->
                    Toast
                        .makeText(
                            context,
                            it.messageRes,
                            Toast.LENGTH_LONG
                        ).show()

                else -> {}
            }
        }
    }
    Scaffold {
        Column(
            modifier =
            modifier
                .padding(16.dp)
                .padding(it)
        ) {
            Text(
                text = stringResource(id = R.string.header_new_bike_entry),
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                modifier =
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 8.dp)
            )
            // FIXME: INTEGRATE IN ACTUAL NAV GRAPH
            val subNavController = rememberNavController()
            NavHost(
                navController = subNavController,
                startDestination = NewBikeDestinations.ENTRY.route
            ) {
                composable(NewBikeDestinations.ENTRY.route) { _ ->
                    EntryScreen(subNavController, state = state, intent = intent, event = event)
                }

                composable(NewBikeDestinations.ENTER_DETAILS.route) { _ ->
                    EnterDetailsScreen(subNavController, state = state, intent = intent, event = event)
                }

                composable(NewBikeDestinations.SETUP_DECISION.route) { _ ->
                    SetupDecisionScreen(subNavController)
                }
                composable(NewBikeDestinations.ENTER_SETUP.route) { _ ->
                    EnterSetupScreen(state, intent, event, subNavController)
                }
                composable(NewBikeDestinations.SUCCESS.route) { _ ->
                    NewBikeSuccessScreen(subNavController, intent)
                }
            }
        }
    }
}

enum class NewBikeDestinations(
    val route: String
) {
    ENTRY("entry"),
    ENTER_DETAILS("enterDetails"),
    SETUP_DECISION("setupDecision"),
    ENTER_SETUP("enterSetup"),
    SUCCESS("success")
}