package me.dhivo.android.pingpongscorekeeper.validators;

import java.util.List;

import me.dhivo.android.pingpongscorekeeper.components.PingPongSet;

public class SetValidator {
    public static boolean validateSets(List<PingPongSet> pingPongSets, int setPoints) {
        for(PingPongSet set: pingPongSets) {
            int playerOneScore = set.getPlayerOneScore();
            int playerTwoSCore = set.getPlayerTwoScore();

            if(playerOneScore < 0 || playerTwoSCore < 0) return false;

            if(playerOneScore >= setPoints && playerTwoSCore >= setPoints) {
                if(Math.abs(playerOneScore - playerTwoSCore) == 2) {
                    continue;
                } else {
                    return false;
                }
            }
            if(playerOneScore == setPoints && playerTwoSCore <= setPoints - 2 ||
                    playerTwoSCore == setPoints && playerOneScore <= setPoints - 2) {
                continue;
            }

            return false;
        }

        return true;
    }
}
