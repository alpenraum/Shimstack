package com.alpenraum.shimstack.ui.compose

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

fun KeyboardOptions.Companion.number(imeAction: ImeAction = ImeAction.Default) =
    Default.copy(
        keyboardType = KeyboardType.Number,
        imeAction = imeAction
    )