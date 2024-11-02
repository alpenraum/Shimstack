package com.alpenraum.shimstack.home.settings

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.alpenraum.shimstack.ui.base.use
import com.alpenraum.shimstack.ui.compose.components.AttachToLifeCycle
import com.alpenraum.shimstack.ui.compose.components.MultiOptionToggle
import com.alpenraum.shimstack.ui.theme.AppTheme

@Composable
fun SettingsScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    AttachToLifeCycle(viewModel = viewModel)
    val (state, intents, _) = use(viewModel = viewModel, navController)

    Column(
        modifier =
        modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .verticalScroll(rememberScrollState())
    ) {
        AnimatedContent(state.settings) {
            it.forEach { setting ->
                when (setting) {
                    is SettingsContract.Settings.DynamicTheme -> SettingsToggleRow(setting.label, setting.setting) {
                        intents(
                            SettingsContract.Intent.OnUseDynamicThemeChange(it)
                        )
                    }

                    is SettingsContract.Settings.AllowAnalytics -> SettingsToggleRow(setting.label, setting.setting) {
                        intents(
                            SettingsContract.Intent.OnAllowAnalyticsChange(it)
                        )
                    }

                    is SettingsContract.Settings.MeasurementUnit -> SettingsMultiSwitch(
                        setting.options,
                        setting.selectedIndex
                    ) { TODO() } // TODO()
                }
            }
        }
    }
}

@Composable
private fun SettingsToggleRow(
    label: Int,
    setting: Boolean,
    modifier: Modifier = Modifier, onDataChange: (Boolean) -> Unit,
) {
    Row(
        modifier =
        modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = label),
            style =
            MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier.weight(1.0f)
        )
        Switch(
            checked = setting,
            onCheckedChange = onDataChange
        )
    }
}

@Composable
private fun SettingsMultiSwitch(options: List<Int>, selectedIndex: Int, onOptionSelect: (Int) -> Unit) {
    MultiOptionToggle(options, selectedIndex, onOptionSelect = onOptionSelect)

}


@Preview
@Composable
private fun SettingPreview() {
    AppTheme {
        SettingsToggleRow(
            SettingsContract.Settings.DynamicTheme(false).label, false
        ) {}
    }
    SettingsToggleRow(
        SettingsContract.Settings.DynamicTheme(false).label, true
    ) {}
}