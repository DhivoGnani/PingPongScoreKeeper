package com.example.android.pingpongscorekeeper.components;

public class PingPongGame
{
    public PingPongPlayer playerOne;
    public PingPongPlayer playerTwo;

    private final static String playerOneName = "Player 1";
    private final static String playerTwoName = "Player 2";

    private final static int minNumberOfPointsToWinASet = 11;
    private final static int numberOfPointsPlayerNeedsToBeatOpponent = 2;
    private final static int winningNumberOfSets = 3;

    public PingPongPlayer previousSetFirstPlayer;
    public PingPongPlayer currentPlayer;
    private int runningNumOfServes = 0;

    public PingPongGame()
    {
        playerOne = new PingPongPlayer(playerOneName);
        playerTwo = new PingPongPlayer(playerTwoName);
        currentPlayer = playerOne;
        previousSetFirstPlayer = currentPlayer;
    }

    public void resetGame()
    {
        playerOne.resetCurrentSetScore();
        playerOne.resetNumberOfSetsWon();
        playerTwo.resetCurrentSetScore();
        playerTwo.resetNumberOfSetsWon();
        currentPlayer = playerOne;
        previousSetFirstPlayer = currentPlayer;
        runningNumOfServes =  0;
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

    public boolean hasPlayerOneWonMatch()
    {
        return hasPlayerWonMatch(playerOne);
    }

    public boolean hasPlayerTwoWonMatch()
    {
        return hasPlayerWonMatch(playerTwo);
    }

    public boolean hasPlayerWonMatch(PingPongPlayer player)
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

    public boolean isGameOver()
    {
        return hasPlayerOneWonMatch() || hasPlayerTwoWonMatch();
    }

    public void playerHasScored(PingPongPlayer player)
    {
        player.incrementCurrentSetScore();
        test();
    }

    public void test()
    {
        runningNumOfServes++;
        if(playerOne.getCurrentSetScore() < 10 || playerTwo.getCurrentSetScore() < 10)
        {
            if(runningNumOfServes == 2)
            {
                runningNumOfServes = 0;
                if(currentPlayer == playerOne)
                {
                    currentPlayer = playerTwo;
                }
                else
                {
                    currentPlayer = playerOne;
                }
            }
        }
        else
        {
            if(runningNumOfServes >= 1)
            {
                runningNumOfServes = 0;
                if(currentPlayer == playerOne)
                {
                    currentPlayer = playerTwo;
                }
                else
                {
                    currentPlayer = playerOne;
                }
            }
        }
    }

    public boolean hasPlayerWonCurrentSet(PingPongPlayer player)
    {
        PingPongPlayer opponentPlayer = player == playerOne  ? playerTwo : playerOne;
        return hasPlayerWonCurrentSet(player, opponentPlayer);
    }

    public void incrementNumberOfSetsWon(PingPongPlayer player)
    {
        player.incrementNumberOfSetsWon();
        if(previousSetFirstPlayer == playerOne)
        {
            currentPlayer = playerTwo;
            previousSetFirstPlayer = playerTwo;

        }
        else
        {
            currentPlayer = playerOne;
            previousSetFirstPlayer = playerOne;
        }

        if(isGameOver())
        {
            currentPlayer = null;
        }
    }

    public void resetCurrentScore()
    {
        playerOne.resetCurrentSetScore();
        playerTwo.resetCurrentSetScore();
    }
}
