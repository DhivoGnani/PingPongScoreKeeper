package com.example.android.pingpongscorekeeper.components;

public class PingPongPlayer
{
    private final String name;
    private int currentSetScore;
    private int numberOfSetsWon;

    public PingPongPlayer(String name)
    {
        this.name = name;
        currentSetScore = 0;
        numberOfSetsWon = 0;
    }

    public void resetNumberOfSetsWon()
    {
        numberOfSetsWon = 0;
    }

    public void resetCurrentSetScore()
    {
        currentSetScore = 0;
    }

    public int getCurrentSetScore()
    {
        return currentSetScore;
    }

    public int getNumberOfSetsWon()
    {
        return numberOfSetsWon;
    }

    public void incrementCurrentSetScore()
    {
        currentSetScore++;
    }

    public void incrementNumberOfSetsWon()
    {
        numberOfSetsWon++;
    }

    public String getName() {
        return name;
    }
}
