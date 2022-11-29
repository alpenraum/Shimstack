package com.alpenraum.shimstack.ui.compose

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.fade
import com.google.accompanist.placeholder.material.placeholder

@Composable
fun CardWithPlaceholder(
    showPlaceholder: Boolean,
    modifier: Modifier = Modifier,
    colors: CardColors,
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
        colors = colors,
        content = content
    )
}
