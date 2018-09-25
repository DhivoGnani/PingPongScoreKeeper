package me.dhivo.android.pingpongmatchtracker.validators

import me.dhivo.android.pingpongmatchtracker.components.PingPongSet

object SetValidator {
    fun validateSets(pingPongSets: List<PingPongSet>, setPoints: Int): Boolean {
        pingPongSets.forEach lit@{ (playerOneScore, playerTwoSCore) ->
            if (playerOneScore < 0 || playerTwoSCore < 0) return false

            if (playerOneScore >= setPoints && playerTwoSCore >= setPoints) {
                return if (Math.abs(playerOneScore - playerTwoSCore) == 2) {
                    return@lit
                } else {
                    false
                }
            }
            if (playerOneScore == setPoints && playerTwoSCore <= setPoints - 2
                    || playerTwoSCore == setPoints && playerOneScore <= setPoints - 2) {
                return@lit
            }

            return false
        }

        return true
    }
}
