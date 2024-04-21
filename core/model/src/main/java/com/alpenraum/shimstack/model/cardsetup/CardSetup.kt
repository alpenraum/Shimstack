package com.alpenraum.shimstack.model.cardsetup

import kotlinx.collections.immutable.persistentListOf

data class CardSetup(val type: CardType, val bigCard: Boolean) {
    companion object {
        fun defaultConfig() =
            persistentListOf(
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