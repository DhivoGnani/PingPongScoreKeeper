package me.dhivo.android.pingpongmatchtracker.validators;

import java.util.List;

import me.dhivo.android.pingpongmatchtracker.components.PingPongSet;

public class SetValidator {
    public static boolean validateSets(List<PingPongSet> pingPongSets, int setPoints) {
        for(PingPongSet set: pingPongSets) {
            final int playerOneScore = set.getPlayerOneScore();
            final int playerTwoScore = set.getPlayerTwoScore();

            if(playerOneScore < 0 || playerTwoScore < 0) return false;

            if(playerOneScore >= setPoints && playerTwoScore >= setPoints) {
                if(Math.abs(playerOneScore - playerTwoScore) == 2) {
                    continue;
                } else {
                    return false;
                }
            }
            if(playerOneScore == setPoints && playerTwoScore <= setPoints - 2 ||
                    playerTwoScore == setPoints && playerOneScore <= setPoints - 2) {
                continue;
            }

            return false;
        }

        return true;
    }
}