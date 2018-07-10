package com.example.android.pingpongscorekeeper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.android.pingpongscorekeeper.components.PingPongGame;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity
{
    PingPongGame pingPongGame;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pingPongGame = new PingPongGame();
    }

    public void playerOneScored(View view)
    {
        if(pingPongGame.gameOver) return;
        pingPongGame.playerOneHasScored();
        if(pingPongGame.hasPlayerOneWonCurrentSet()) {
            pingPongGame.playerOneHasWonSet();
            if(pingPongGame.hasPlayerOneWonMatch())
            {
                displayMessage(getResources().getString(R.string.player_one_won_match));
                pingPongGame.gameOver = true;
            }
            else
            {
                displayMessage(getResources().getString(
                        R.string.player_won_set, 1,
                        pingPongGame.getCurrentSetNumber()
                ));
            }
            clearCurrentSetScoreBothPlayers();
            displayNumberOfSetsPlayerOneWon();
        } else {
            displayPlayerOneScore();
        }
    }

    public void playerTwoScored(View view)
    {
        if(pingPongGame.gameOver) return;
        pingPongGame.playerTwoHasScored();
        if(pingPongGame.hasPlayerTwoWonCurrentSet()) {
            pingPongGame.playerTwoHasWonSet();
            if(pingPongGame.hasPlayerTwoWonMatch())
            {
                displayMessage(getResources().getString(R.string.player_two_won_match));
                pingPongGame.gameOver = true;
            }
            else
            {
                displayMessage(getResources().getString(
                        R.string.player_won_set, 2,
                        pingPongGame.getCurrentSetNumber()
                ));
            }
            clearCurrentSetScoreBothPlayers();
            displayNumberOfSetsPlayerTwoWon();
        }
        else
        {
            displayPlayerTwoScore();
        }

    }

    public void displayMessage(String message)
    {
        TextView messageDisplay = (TextView) findViewById(R.id.message);
        messageDisplay.setText(message);
    }

    public void clearMessage()
    {
        displayMessage("");
    }

    public void clearCurrentSetScoreBothPlayers()
    {
        ((TextView)findViewById(R.id.player_one_current_set_score)).setText("0");
        ((TextView)findViewById(R.id.player_two_current_set_score)).setText("0");
        pingPongGame.playerOne.resetCurrentSetScore();
        pingPongGame.playerTwo.resetCurrentSetScore();

    }

    public void displayNumberOfSetsPlayerOneWon()
    {
        ((TextView)findViewById(R.id.player_one_sets_won)).setText(pingPongGame.getNumSetsPlayerOneWon()+"");
    }

    public void displayNumberOfSetsPlayerTwoWon()
    {
        ((TextView)findViewById(R.id.player_two_sets_won)).setText(pingPongGame.getNumSetsPlayerTwoWon()+"");
    }

    public void displayPlayerOneScore()
    {
        ((TextView)findViewById(R.id.player_one_current_set_score)).setText(pingPongGame.getPlayerOneCurrentSetScore()+"");
    }

    public void displayPlayerTwoScore()
    {
        ((TextView)findViewById(R.id.player_two_current_set_score)).setText(pingPongGame.getPlayerTwoCurrentSetScore()+"");
    }


}
