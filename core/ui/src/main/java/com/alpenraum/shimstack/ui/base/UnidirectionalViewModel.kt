package com.alpenraum.shimstack.ui.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface UnidirectionalViewModel<STATE, INTENT, EVENT> {
    val state: StateFlow<STATE>
    val event: SharedFlow<EVENT>

    fun intent(
        intent: INTENT,
        navController: NavController
    )
}

@Composable
inline fun <reified STATE, INTENT, EVENT> use(
    viewModel: UnidirectionalViewModel<STATE, INTENT, EVENT>,
    navController: NavController
): StateDispatchEffect<STATE, INTENT, EVENT> {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val dispatch: (INTENT) -> Unit = { event ->
        viewModel.intent(event, navController)
    }

    return StateDispatchEffect(
        state = state,
        eventFlow = viewModel.event,
        dispatch = dispatch
    )
}

data class StateDispatchEffect<STATE, INTENT, EVENT>(
    val state: STATE,
    val dispatch: (INTENT) -> Unit,
    val eventFlow: SharedFlow<EVENT>
)