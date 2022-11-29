package com.alpenraum.shimstack.common

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
