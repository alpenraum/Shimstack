package com.alpenraum.shimstack.data.cardsetup

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class CardSetup(val type: CardType, val bigCard: Boolean) {
    companion object {
        fun defaultConfig() = persistentListOf(
            CardSetup(CardType.TIRES, false),
            CardSetup(CardType.FORK, false),
            CardSetup(CardType.SHOCK, false),
            CardSetup(CardType.SHOCK, true)
        )
    }
}

enum class CardType {
    TIRES,
    FORK,
    SHOCK
}
