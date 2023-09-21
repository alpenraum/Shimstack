package com.alpenraum.shimstack.common

import android.content.Context
import android.content.ContextWrapper
import androidx.appcompat.app.AppCompatActivity
import dev.olshevski.navigation.reimagined.NavAction
import dev.olshevski.navigation.reimagined.NavController

fun <T> NavController<T>.moveLastEntryToStart() {
    setNewBackstack(
        entries = backstack.entries.toMutableList().also {
            val entry = it.removeLast()
            it.add(0, entry)
        },
        action = NavAction.Pop
    )
}

fun Context.getActivity(): AppCompatActivity? = when (this) {
    is AppCompatActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}

fun Context.getConfigSharedPreferences() =
    this.getSharedPreferences(ConfigConstants.SETTINGS_PREFERENCES_NAME, Context.MODE_PRIVATE)
