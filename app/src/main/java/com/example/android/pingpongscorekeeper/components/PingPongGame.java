package com.example.android.pingpongscorekeeper.components;

import java.util.ArrayList;

public class PingPongGame
{
    public PingPongPlayer playerOne;
    public PingPongPlayer playerTwo;
    public PingPongPlayer previousSetFirstServingPlayer;
    public PingPongPlayer currentServingPlayer;
    private PingPongPlayer initialServingPlayer;

    public String playerOneName;
    public String playerTwoName;

    public ArrayList<PingPongSet> pingPongSets;

    private final static int minNumberOfPointsToWinASet = 11;
    private final static int numberOfPointsPlayerNeedsToBeatOpponent = 2;

    private int winningNumberOfSets;

    public PingPongGame(
            String playerOneName,
            String playerTwoName,
            int numSets,
            String servingPlayer
    )
    {
        this.playerOneName = playerOneName;
        this.playerTwoName = playerTwoName;
        pingPongSets = new ArrayList<>();
        winningNumberOfSets = numSets % 2 == 1 ? numSets/2 + 1 : numSets / 2;


        playerOne = new PingPongPlayer(playerOneName);
        playerTwo = new PingPongPlayer(playerTwoName);

        currentServingPlayer = servingPlayer
                .equals(PlayerEnum.PLAYER_ONE.toString()) ? playerOne : playerTwo;
        initialServingPlayer = currentServingPlayer;
        previousSetFirstServingPlayer = currentServingPlayer;
    }


    public void setPlayerOneName(String name)
    {
        playerOne.setName(name);
    }

    public void setPlayerTwoName(String name)
    {
        playerTwo.setName(name);
    }

    public void resetGame()
    {
        playerOne.reset();
        playerTwo.reset();

        pingPongSets = new ArrayList<>();

        currentServingPlayer = initialServingPlayer;
        previousSetFirstServingPlayer = currentServingPlayer;
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
        pingPongSets.add(new PingPongSet(
                playerOne.getNumberOfSetsWon() + playerTwo.getNumberOfSetsWon() + 1,
                playerOne.getCurrentSetScore(), playerTwo.getCurrentSetScore()));

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

    public void resetPlayersCurrentSetScore()
    {
        playerOne.resetCurrentSetScore();
        playerTwo.resetCurrentSetScore();
    }
}
