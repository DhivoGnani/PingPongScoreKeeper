package com.example.android.pingpongscorekeeper.components;

public class PingPongPlayer
{
    private int currentSetScore;
    private int numberOfSetsWon;

    public PingPongPlayer()
    {
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
}
