package me.dhivo.android.pingpongmatchtracker.components

internal class GameConfiguration {

    // Default number of sets in a match
    var numberOfSets: Int = 3
        private set

    var maxNumSets: Int  = 9
    var minNumSets: Int = 1

    // Default number of points in a set
    var numberOfPointsPerSet = 11
    var minNumPointsPerSet = 11
    var maxNumPointsPerSet = 21

    var servingPlayer: PlayerEnum = PlayerEnum.PLAYER_ONE
        private set

    var playerOneName: String = PlayerEnum.PLAYER_ONE.toString()
        set(name){
            if (name.isNullOrBlank()) return
            field = name
        }

    var playerTwoName: String = PlayerEnum.PLAYER_TWO.toString()
        set(name) {
            if (name.isNullOrBlank()) return
            field = name
        }

    fun incrementNumberOfSets() {
        if(numberOfSets == maxNumSets) return
        numberOfSets += 2
    }

    fun decrementNumberOfSets() {
        if(numberOfSets == minNumSets) return
        numberOfSets -= 2
    }

    fun incrementNumberOfPointersPerSet()
    {
        numberOfPointsPerSet = maxNumPointsPerSet
    }

    fun decrementNumberOfPointsPerSet()
    {
        numberOfPointsPerSet = minNumPointsPerSet
    }

    fun switchServingPlayer() {
        servingPlayer = when (servingPlayer) {
            PlayerEnum.PLAYER_ONE -> PlayerEnum.PLAYER_TWO
            else -> PlayerEnum.PLAYER_ONE
        }
    }
}
