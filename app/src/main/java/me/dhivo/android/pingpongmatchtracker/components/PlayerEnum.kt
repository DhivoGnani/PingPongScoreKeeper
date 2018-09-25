package me.dhivo.android.pingpongmatchtracker.components

enum class PlayerEnum (private val text: String) {
    PLAYER_ONE("Player 1"),
    PLAYER_TWO("Player 2");

    override fun toString(): String {
        return text
    }
}
