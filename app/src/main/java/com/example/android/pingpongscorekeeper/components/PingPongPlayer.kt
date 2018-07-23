package com.example.android.pingpongscorekeeper.components

class PingPongPlayer(var name: String) {

    var currentSetScore: Int = 0
        private set

    var numberOfSetsWon: Int = 0
        private set

    fun resetNumberOfSetsWon() {
        numberOfSetsWon = 0
    }

    fun resetCurrentSetScore() {
        currentSetScore = 0
    }

    fun reset() {
        resetNumberOfSetsWon()
        resetCurrentSetScore()
    }

    fun incrementCurrentSetScore() = currentSetScore++

    fun incrementNumberOfSetsWon() = numberOfSetsWon++
}
