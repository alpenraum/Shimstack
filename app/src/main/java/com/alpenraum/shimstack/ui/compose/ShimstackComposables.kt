package com.alpenraum.shimstack.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.DividerDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.alpenraum.shimstack.ui.base.BaseViewModel
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.fade
import com.google.accompanist.placeholder.material.placeholder

val CARD_DIMENSION = 160.dp

@Composable
fun AttachToLifeCycle(viewModel: BaseViewModel) {
    DisposableEffect(key1 = viewModel) {
        viewModel.onStart()
        onDispose { viewModel.onStop() }
    }
}

@Composable
fun VerticalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = DividerDefaults.Thickness,
    color: Color = DividerDefaults.color
) {
    val targetThickness = if (thickness == Dp.Hairline) {
        (1f / LocalDensity.current.density).dp
    } else {
        thickness
    }
    Box(
        modifier
            .fillMaxHeight()
            .width(targetThickness)
            .background(color = color)
    )
}

@Composable
fun CardWithPlaceholder(
    showPlaceholder: Boolean,
    modifier: Modifier = Modifier,
    placeholderColor: Color,
    content: @Composable() ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.placeholder(
            visible = showPlaceholder,
            highlight = PlaceholderHighlight.fade(),
            color = placeholderColor,
            shape = RoundedCornerShape(8.dp)
        ),
        content = content
    )
}

fun shimstackRoundedCornerShape() = RoundedCornerShape(20.dp)
