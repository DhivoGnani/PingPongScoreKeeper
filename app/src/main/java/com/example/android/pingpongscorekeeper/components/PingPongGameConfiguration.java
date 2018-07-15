package com.example.android.pingpongscorekeeper.components;

public class PingPongGameConfiguration
{
    private int numberOfSets;
    private PlayerEnum servingPlayer;
    private String playerOneName;
    private String playerTwoName;

    public PingPongGameConfiguration()
    {
        // The default number of sets for each match will be one.
        numberOfSets = 1;
        this.servingPlayer =  PlayerEnum.PLAYER_ONE;
        playerOneName = PlayerEnum.PLAYER_ONE.toString();
        playerTwoName = PlayerEnum.PLAYER_TWO.toString();
    }

    public void incrementNumberOfSets()
    {
        numberOfSets = numberOfSets + 2;
    }

    public void decrementNumberOfSets()
    {
        if(numberOfSets - 2 < 1) return;
        numberOfSets = numberOfSets - 2;
    }

    public int getNumberOfSets()
    {
        return numberOfSets;
    }

    public void switchServingPlayer()
    {
        servingPlayer = servingPlayer == PlayerEnum.PLAYER_ONE ? PlayerEnum.PLAYER_TWO :
                PlayerEnum.PLAYER_ONE;
    }

    public void setServingPlayer(PlayerEnum player)
    {
        servingPlayer = player;
    }

    public void setPlayerOneName(String name)
    {
        if (name == null || name.trim().length() ==  0) return;
        playerOneName = name;
    }

    public void setPlayerTwoName(String name)
    {
        if (name == null || name.trim().length() == 0) return;
        playerTwoName = name;
    }

    public String getPlayerOneName()
    {
        return playerOneName;
    }

    public String getPlayerTwoName()
    {
        return playerTwoName;
    }

    public PlayerEnum getServingPlayer() {
         return servingPlayer;
    }
}
