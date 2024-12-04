package com.alpenraum.shimstack.home.settings

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
import com.alpenraum.shimstack.home.R
import com.alpenraum.shimstack.ui.base.use
import com.alpenraum.shimstack.ui.compose.components.AttachToLifeCycle
import com.alpenraum.shimstack.ui.compose.components.MultiOptionToggle
import com.alpenraum.shimstack.ui.theme.AppTheme
import com.alpenraum.shimstack.ui.R as BaseR

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
        state.settings.forEach { setting ->
                when (setting) {
                    is SettingsContract.Settings.DynamicTheme ->
                        SettingsToggleRow(setting.label, setting.setting) {
                            intents(
                                SettingsContract.Intent.OnUseDynamicThemeChange(it)
                            )
                        }

                    is SettingsContract.Settings.AllowAnalytics ->
                        SettingsToggleRow(setting.label, setting.setting) {
                            intents(
                                SettingsContract.Intent.OnAllowAnalyticsChange(it)
                            )
                        }

                    is SettingsContract.Settings.MeasurementUnit ->
                        SettingsMultiSwitch(
                            setting.options,
                            setting.selectedIndex
                        ) { intents(SettingsContract.Intent.OnMeasurementUnitTypeChange(it)) }
                }
        }
    }
}

@Composable
private fun SettingsToggleRow(
    label: Int,
    setting: Boolean,
    modifier: Modifier = Modifier,
    onDataChange: (Boolean) -> Unit
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
private fun SettingsMultiSwitch(
    options: List<Int>,
    selectedIndex: Int,
    modifier: Modifier = Modifier,
    onOptionSelect: (Int) -> Unit
) {
    Column(modifier.padding(8.dp)) {
        Text(stringResource(R.string.settings_measurement_unit_type), modifier = Modifier.padding(bottom = 4.dp))
        MultiOptionToggle(options, selectedIndex, onOptionSelect = onOptionSelect)
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingPreview() {
    AppTheme {
        SettingsToggleRow(
            SettingsContract.Settings.DynamicTheme(false).label,
            false
        ) {}
    }
}

@Preview(showBackground = true)
@Composable
private fun MultiSettingPreview() {
    AppTheme {
        MultiOptionToggle(
            listOf(BaseR.string.metric, BaseR.string.imperial),
            selectedIndex = 0
        ) { }
    }
}
