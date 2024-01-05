package com.alpenraum.shimstack.ui.compose

import androidx.compose.animation.core.tween

fun fadeIn() = androidx.compose.animation.fadeIn(animationSpec = tween(400))

fun fadeOut() = androidx.compose.animation.fadeOut(animationSpec = tween(400))