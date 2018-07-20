package com.example.android.pingpongscorekeeper.components;

public class PingPongGame
{
    public PingPongPlayer playerOne;
    public PingPongPlayer playerTwo;
    public PingPongPlayer previousSetFirstServingPlayer;
    public PingPongPlayer currentServingPlayer;

    // TODO: This global variable is only needed for reset functionality. Not really needed in the
    // future.
    private PingPongPlayer initialServingPlayer;

    public String playerOneName = "Player 1";
    public String playerTwoName = "Player 2";

    private final static int minNumberOfPointsToWinASet = 11;
    private final static int numberOfPointsPlayerNeedsToBeatOpponent = 2;
    private int winningNumberOfSets = 3;

    // TODO: Unnecessary constructor anymore.
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

        currentServingPlayer = servingPlayer.equals(PlayerEnum.PLAYER_ONE.toString()) ? playerOne : playerTwo;
        initialServingPlayer = currentServingPlayer;
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

        currentServingPlayer = initialServingPlayer;
        previousSetFirstServingPlayer = currentServingPlayer;
    }

    public int getCurrentSetNumber()
    {
        return playerOne.getNumberOfSetsWon() + playerTwo.getNumberOfSetsWon();
    }

    public int getCurrentSet()
    {
        return (playerOne.getNumberOfSetsWon() + playerTwo.getNumberOfSetsWon() + 1 > winningNumberOfSets) ?  playerOne.getNumberOfSetsWon() + playerTwo.getNumberOfSetsWon() :
            playerOne.getNumberOfSetsWon() + playerTwo.getNumberOfSetsWon() + 1;
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
        updateServingPlayer();
        player.incrementCurrentSetScore();
    }

    private void switchCurrentServingPlayer()
    {
        currentServingPlayer = currentServingPlayer == playerOne ? playerTwo : playerOne;
    }

    public void updateServingPlayer()
    {
        if(playerOne.getCurrentSetScore() < minNumberOfPointsToWinASet - 1 ||
                playerTwo.getCurrentSetScore() < minNumberOfPointsToWinASet - 1)
        {
            if(((playerOne.getCurrentSetScore() + playerTwo.getCurrentSetScore())  % 2 ) == 1)
            {
                switchCurrentServingPlayer();
            }
        }
        else
        {
            switchCurrentServingPlayer();
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
    }

    public void resetCurrentScore()
    {
        playerOne.resetCurrentSetScore();
        playerTwo.resetCurrentSetScore();
    }
}
