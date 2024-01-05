package com.alpenraum.shimstack.ui.compose

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.alpenraum.shimstack.ui.base.BaseViewModel
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.fade
import com.google.accompanist.placeholder.material.placeholder

val CARD_DIMENSION = 160.dp
val CARD_MARGIN = 16.dp

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
    val targetThickness =
        if (thickness == Dp.Hairline) {
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
    placeholderColor: Color,
    modifier: Modifier = Modifier,
    content:
    @Composable()
    ColumnScope.() -> Unit
) {
    Card(
        modifier =
        modifier.placeholder(
            visible = showPlaceholder,
            highlight = PlaceholderHighlight.fade(),
            color = placeholderColor,
            shape = RoundedCornerShape(8.dp)
        ),
        content = content
    )
}

@Composable
fun LargeButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        colors = colors,
        content = content
    )
}

@Composable
fun LargeSecondaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.outlinedButtonColors(),
    content: @Composable RowScope.() -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        content = content,
        colors = colors
    )
}

@Composable
fun InfoText(
    @StringRes textRes: Int,
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(id = textRes),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontStyle = FontStyle.Italic,
        modifier = modifier
    )
}

fun shimstackRoundedCornerShape() = RoundedCornerShape(20.dp)

@Composable
fun TextInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    shape: Shape = shimstackRoundedCornerShape(),
    singleLine: Boolean = true,
    suffix: String? = null,
    readOnly: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors()
) = OutlinedTextField(
    shape = shape,
    singleLine = singleLine,
    value = value,
    onValueChange = onValueChange,
    suffix = suffix?.let { { Text(text = it) } },
    modifier = modifier,
    label =
    label?.let {
        { Text(text = it) }
    },
    isError = isError,
    keyboardOptions = keyboardOptions,
    keyboardActions = keyboardActions,
    readOnly = readOnly,
    trailingIcon = trailingIcon,
    colors = colors
)