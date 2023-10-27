package com.alpenraum.shimstack.ui.screens.mainScreens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alpenraum.shimstack.ui.base.use
import com.alpenraum.shimstack.ui.compose.AttachToLifeCycle
import com.alpenraum.shimstack.ui.theme.AppTheme
import dev.olshevski.navigation.reimagined.hilt.hiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier
) {
    val viewModel: SettingsViewModel = hiltViewModel()
    AttachToLifeCycle(viewModel = viewModel)
    val (state, intents, event) = use(viewModel = viewModel)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .verticalScroll(rememberScrollState())
    ) {
        state.settings.forEach {
            SettingsToggleRow(it, intents)
        }
    }
}

@Composable
private fun SettingsToggleRow(
    data: Pair<SettingsContract.Settings, Flow<Boolean>?>,
    intents: (SettingsContract.Intent) -> Unit,
    modifier: Modifier = Modifier
) {
    data.second?.collectAsState(false)?.let {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = data.first.label),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1.0f)
            )
            Switch(
                checked = it.value,
                onCheckedChange = {
                    intents(SettingsContract.Intent.OnSettingsChanged(data.first, it))
                }
            )
        }
    }
}

@Preview
@Composable
private fun SettingPreview() {
    AppTheme {
        SettingsToggleRow(
            Pair(
                SettingsContract.Settings.USE_DYNAMIC_THEME,
                flow { emit(false) }
            ),
            {}
        )
    }
}
