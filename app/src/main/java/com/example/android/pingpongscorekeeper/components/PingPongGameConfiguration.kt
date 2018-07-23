package com.example.android.pingpongscorekeeper.components

class PingPongGameConfiguration {

    // Default number of sets in a match
    var numberOfSets: Int = 3
        private set

    var servingPlayer: PlayerEnum = PlayerEnum.PLAYER_ONE
        private set

    var playerOneName: String = PlayerEnum.PLAYER_ONE.toString()
        set(name){
            if (name.isNullOrBlank()) return
            playerOneName = name
        }

    var playerTwoName: String = PlayerEnum.PLAYER_TWO.toString()
        set(name) {
            if (name.isNullOrBlank()) return
            playerTwoName = name
        }

    fun incrementNumberOfSets() {
        numberOfSets += 2
    }

    fun decrementNumberOfSets() {
        if (numberOfSets - 2 < 1) return
        numberOfSets -= 2
    }

    fun switchServingPlayer() {
        servingPlayer = when (servingPlayer) {
            PlayerEnum.PLAYER_ONE -> PlayerEnum.PLAYER_TWO
            else -> PlayerEnum.PLAYER_ONE
        }
    }
}
