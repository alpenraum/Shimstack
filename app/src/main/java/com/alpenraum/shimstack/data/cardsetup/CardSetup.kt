package com.alpenraum.shimstack.data.cardsetup

data class CardSetup(val type: CardType, val bigCard: Boolean) {
    companion object {
        fun defaultConfig() = listOf(
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
    SHOCK,
}
