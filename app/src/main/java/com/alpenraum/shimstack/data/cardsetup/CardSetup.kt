package com.alpenraum.shimstack.data.cardsetup

data class CardSetup(val type: CardType, val bigCard: Boolean) {
    companion object {
        fun defaultConfig() = listOf(
            CardSetup(CardType.TIRES, false),
            CardSetup(CardType.FORK, false),
            CardSetup(CardType.SHOCK_DETAILED, true),
            CardSetup(CardType.FORK_DETAILED, true)
        )
    }
}

enum class CardType {
    TIRES,
    FORK,
    FORK_DETAILED,
    SHOCK,
    SHOCK_DETAILED
}
