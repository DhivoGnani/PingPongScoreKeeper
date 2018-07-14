package com.example.android.pingpongscorekeeper.components;

public class PingPongGame
{
    public PingPongPlayer playerOne;
    public PingPongPlayer playerTwo;
    public PingPongPlayer previousSetFirstServingPlayer;
    public PingPongPlayer currentServingPlayer;

    public String playerOneName = "Player 1";
    public String playerTwoName = "Player 2";

    private final static int minNumberOfPointsToWinASet = 11;
    private final static int numberOfPointsPlayerNeedsToBeatOpponent = 2;
    private int winningNumberOfSets = 3;
    private final static int numOfServesForEachPlayerBeforeDeuce = 2;
    private final static int numOfServersForEachPlayerAfterDeuce = 1;

    private int runningNumberOfServesForCurrentServingPlayer = 0;

    public PingPongGame()
    {
        playerOne = new PingPongPlayer(playerOneName);
        playerTwo = new PingPongPlayer(playerTwoName);

        currentServingPlayer = playerOne;
        previousSetFirstServingPlayer = currentServingPlayer;
    }

    public PingPongGame(String playerOneName,String playerTwoName, int numSets, String servingPlayer)
    {
        this.playerOneName = playerOneName;
        this.playerTwoName = playerTwoName;

        winningNumberOfSets = numSets % 2 == 1 ? numSets/2 + 1 : numSets / 2;


        playerOne = new PingPongPlayer(playerOneName);
        playerTwo = new PingPongPlayer(playerTwoName);

        currentServingPlayer = servingPlayer == PlayerEnum.PLAYER_ONE.toString() ? playerOne : playerTwo;
        previousSetFirstServingPlayer = currentServingPlayer;
    }


    public void setPlayerOneName(String name)
    {
        playerOneName = name;
    }

    public void setPlayerTwoName(String name)
    {
        playerTwoName = name;
    }

    public void resetGame()
    {
        playerOne.resetCurrentSetScore();
        playerOne.resetNumberOfSetsWon();
        playerTwo.resetCurrentSetScore();
        playerTwo.resetNumberOfSetsWon();

        currentServingPlayer = playerOne;
        previousSetFirstServingPlayer = currentServingPlayer;
        runningNumberOfServesForCurrentServingPlayer =  0;
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
        updateServingPlayer();
    }

    private void switchCurrentServingPlayer()
    {
        currentServingPlayer = currentServingPlayer == playerOne ? playerTwo : playerOne;
    }

    public void updateServingPlayer()
    {
        runningNumberOfServesForCurrentServingPlayer++;
        if(playerOne.getCurrentSetScore() < minNumberOfPointsToWinASet -1 ||
                playerTwo.getCurrentSetScore() < minNumberOfPointsToWinASet - 1)
        {
            if(runningNumberOfServesForCurrentServingPlayer == numOfServesForEachPlayerBeforeDeuce)
            {
                runningNumberOfServesForCurrentServingPlayer = 0;
                switchCurrentServingPlayer();
            }
        }
        else
        {
            if(runningNumberOfServesForCurrentServingPlayer >= numOfServersForEachPlayerAfterDeuce)
            {
                runningNumberOfServesForCurrentServingPlayer = 0;
                switchCurrentServingPlayer();
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

        switchStartingServerForSet();
    }

    private void switchStartingServerForSet() {
        if(previousSetFirstServingPlayer == playerOne)
        {
            currentServingPlayer = playerTwo;
            previousSetFirstServingPlayer = playerTwo;

        }
        else
        {
            currentServingPlayer = playerOne;
            previousSetFirstServingPlayer = playerOne;
        }
        runningNumberOfServesForCurrentServingPlayer = 0;
    }

    public void resetCurrentScore()
    {
        playerOne.resetCurrentSetScore();
        playerTwo.resetCurrentSetScore();
    }
}
