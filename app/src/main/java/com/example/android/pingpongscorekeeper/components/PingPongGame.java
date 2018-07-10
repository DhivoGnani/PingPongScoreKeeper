package com.example.android.pingpongscorekeeper.components;

public class PingPongGame
{
    private PingPongPlayer playerOne;
    private PingPongPlayer playerTwo;

    private final static int minNumberOfPointsToWinASet = 11;
    private final static int numberOfPointsPlayerNeedsToBeatOpponent = 2;
    private final static int winningNumberOfSets = 3;

    public PingPongGame()
    {
        playerOne = new PingPongPlayer();
        playerTwo = new PingPongPlayer();
    }

    public void startNewSet()
    {
        playerOne.resetCurrentSetScore();
        playerTwo.resetCurrentSetScore();
    }

    public void resetGame()
    {
        playerOne.resetCurrentSetScore();
        playerOne.resetNumberOfSetsWon();
        playerTwo.resetCurrentSetScore();
        playerTwo.resetNumberOfSetsWon();
    }

    public int getCurrentSetNumber()
    {
        return playerOne.getNumberOfSetsWon() + playerTwo.getNumberOfSetsWon();
    }

    public int getPlayerOneCurrentSetScore()
    {
        return playerOne.getCurrentSetScore();
    }

    public int getPlayerTwoCurrentSetScore()
    {
        return playerTwo.getCurrentSetScore();
    }

    public int getNumSetsPlayerOneWon()
    {
        return playerOne.getNumberOfSetsWon();
    }

    public int getNumSetsPlayerTwoWon()
    {
        return playerTwo.getNumberOfSetsWon();
    }

    public void playerOneHasScored()
    {
        playerOne.incrementCurrentSetScore();
    }

    public void playerTwoHasScored()
    {
        playerTwo.incrementCurrentSetScore();
    }

    public void playerOneHasWonSet()
    {
        playerOne.incrementNumberOfSetsWon();
    }

    public void playerTwoHasWonSet()
    {
        playerTwo.incrementNumberOfSetsWon();
    }

    public boolean hasPlayerOneWonMatch()
    {
        return hasPlayerWonMatch(playerOne);
    }

    public boolean hasPlayerTwoWonMatch()
    {
        return hasPlayerWonMatch(playerTwo);
    }

    public boolean hasPlayerOneWonCurrentSet()
    {
        return hasPlayerWonCurrentSet(playerOne, playerTwo);
    }

    public boolean hasPlayerTwoWonCurrentSet()
    {
        return hasPlayerWonCurrentSet(playerTwo, playerOne);
    }

    private boolean hasPlayerWonMatch(PingPongPlayer player)
    {
        return player.getNumberOfSetsWon() == winningNumberOfSets;
    }

    private boolean hasPlayerWonCurrentSet(PingPongPlayer player, PingPongPlayer opponent)
    {
        return player.getCurrentSetScore() >= minNumberOfPointsToWinASet &&
                player.getCurrentSetScore() - opponent.getCurrentSetScore() >=
                        numberOfPointsPlayerNeedsToBeatOpponent;
    }

    public boolean isDeuce()
    {
        return playerOne.getCurrentSetScore() >= minNumberOfPointsToWinASet - 1 &&
                playerOne.getCurrentSetScore() == playerTwo.getCurrentSetScore();
    }
}
