package me.dhivo.android.pingpongmatchtracker.components

import java.util.ArrayList

class PingPongGame(
        var playerOneName: String,
        var playerTwoName: String,
        numSets: Int,
        private val minNumberOfPointsToWinASet: Int,
        servingPlayer: String,
        val playerOneId: Long,
        val playerTwoId: Long
) {
    var playerOne: PingPongPlayer
    var playerTwo: PingPongPlayer
    var previousSetFirstServingPlayer: PingPongPlayer
    var currentServingPlayer: PingPongPlayer
    private val initialServingPlayer: PingPongPlayer

    var pingPongSets: ArrayList<PingPongSet>

    private val winningNumberOfSets: Int

    val currentSetNumber: Int
        get() = playerOne.numberOfSetsWon + playerTwo.numberOfSetsWon

    val playerOneCurrentSetScore: Int
        get() = playerOne.currentSetScore

    val playerTwoCurrentSetScore: Int
        get() = playerTwo.currentSetScore

    val numSetsPlayerOneWon: Int
        get() = playerOne.numberOfSetsWon

    val numSetsPlayerTwoWon: Int
        get() = playerTwo.numberOfSetsWon

    val isDeuce: Boolean
        get() = playerOne.currentSetScore >= minNumberOfPointsToWinASet - 1
                && playerOne.currentSetScore == playerTwo.currentSetScore

    val isGameOver: Boolean
        get() = hasPlayerOneWonMatch() || hasPlayerTwoWonMatch()

    init {
        pingPongSets = ArrayList()
        winningNumberOfSets = if (numSets % 2 == 1) numSets / 2 + 1 else numSets / 2


        playerOne = PingPongPlayer(playerOneName)
        playerTwo = PingPongPlayer(playerTwoName)

        currentServingPlayer = if (servingPlayer == PlayerEnum.PLAYER_ONE.toString())
            playerOne
        else
            playerTwo
        initialServingPlayer = currentServingPlayer
        previousSetFirstServingPlayer = currentServingPlayer
    }

    fun resetGame() {
        playerOne.reset()
        playerTwo.reset()

        pingPongSets = ArrayList()

        currentServingPlayer = initialServingPlayer
        previousSetFirstServingPlayer = currentServingPlayer
    }

    private fun hasPlayerOneWonMatch(): Boolean {
        return hasPlayerWonMatch(playerOne)
    }

    private fun hasPlayerTwoWonMatch(): Boolean {
        return hasPlayerWonMatch(playerTwo)
    }

    fun hasPlayerWonMatch(player: PingPongPlayer): Boolean {
        return player.numberOfSetsWon == winningNumberOfSets
    }

    private fun hasPlayerWonCurrentSet(player: PingPongPlayer, opponent: PingPongPlayer): Boolean {
        return player.currentSetScore >= minNumberOfPointsToWinASet
                && player.currentSetScore - opponent.currentSetScore >= numberOfPointsPlayerNeedsToBeatOpponent
    }

    fun playerHasScored(player: PingPongPlayer) {
        updateServingPlayer()
        player.incrementCurrentSetScore()
    }

    private fun switchCurrentServingPlayer() {
        currentServingPlayer = if (currentServingPlayer == playerOne) playerTwo else playerOne
    }

    private fun updateServingPlayer() {
        if (playerOne.currentSetScore < minNumberOfPointsToWinASet - 1
                || playerTwo.currentSetScore < minNumberOfPointsToWinASet - 1) {
            if ((playerOne.currentSetScore + playerTwo.currentSetScore) % 2 == 1) {
                switchCurrentServingPlayer()
            }
        } else {
            switchCurrentServingPlayer()
        }
    }

    fun hasPlayerWonCurrentSet(player: PingPongPlayer): Boolean {
        val opponentPlayer = if (player == playerOne) playerTwo else playerOne
        return hasPlayerWonCurrentSet(player, opponentPlayer)
    }

    fun incrementNumberOfSetsWon(player: PingPongPlayer) {
        pingPongSets.add(PingPongSet(
                playerOne.numberOfSetsWon + playerTwo.numberOfSetsWon + 1,
                playerOne.currentSetScore, playerTwo.currentSetScore))

        player.incrementNumberOfSetsWon()


        switchStartingServerForSet()
    }

    private fun switchStartingServerForSet() {
        if (previousSetFirstServingPlayer == playerOne) {
            currentServingPlayer = playerTwo
            previousSetFirstServingPlayer = playerTwo

        } else {
            currentServingPlayer = playerOne
            previousSetFirstServingPlayer = playerOne
        }
    }

    fun resetPlayersCurrentSetScore() {
        playerOne.resetCurrentSetScore()
        playerTwo.resetCurrentSetScore()
    }

    companion object {
        private const val numberOfPointsPlayerNeedsToBeatOpponent = 2
    }
}
