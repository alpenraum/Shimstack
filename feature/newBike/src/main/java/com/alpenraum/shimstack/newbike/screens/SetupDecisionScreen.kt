package com.alpenraum.shimstack.newbike.screens

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.alpenraum.shimstack.newbike.NewBikeDestinations
import com.alpenraum.shimstack.newbike.R
import com.alpenraum.shimstack.ui.compose.DecisionButtonConfig
import com.alpenraum.shimstack.ui.compose.DecisionScreen
import com.alpenraum.shimstack.ui.theme.AppTheme
import com.alpenraum.shimstack.ui.R as CommonR

@Composable
fun SetupDecisionScreen(navController: NavController? = null) {
    val context = LocalContext.current
    DecisionScreen(
        imageRes = null,
        contentRes = R.string.copy_new_bike_existing_setup,
        modifier = Modifier,
        buttons =
            listOf(
                DecisionButtonConfig(CommonR.string.label_yes, true) {
                    navController?.navigate(NewBikeDestinations.ENTER_SETUP.route)
                },
                DecisionButtonConfig(CommonR.string.label_no, false) {
                    Toast.makeText(context, "TODO: Insert Setup Wizard here", Toast.LENGTH_LONG).show()
                }
            )
    )
}

@Preview(showBackground = true)
@Composable
private fun SetupDecisionScreenPreview() {
    AppTheme {
        SetupDecisionScreen(null)
    }
}