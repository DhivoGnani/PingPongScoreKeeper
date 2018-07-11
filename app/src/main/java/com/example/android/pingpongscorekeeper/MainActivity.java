package com.example.android.pingpongscorekeeper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.android.pingpongscorekeeper.components.PingPongGame;
import com.example.android.pingpongscorekeeper.components.PingPongPlayer;

public class MainActivity extends AppCompatActivity
{
    PingPongGame pingPongGame;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pingPongGame = new PingPongGame();

        displayPlayersName();
    }

    private void displayPlayersName() {
        ((TextView)findViewById(R.id.player_one)).setText(pingPongGame.playerOne.getName());
        ((TextView)findViewById(R.id.player_two)).setText(pingPongGame.playerTwo.getName());
    }

    public void playerOneScoreOnClick(View view)
    {
        playerScored(pingPongGame.playerOne);
        displayPlayerOneScore();
        displayNumberOfSetsPlayerOneWon();
    }

    public void playerTwoScoreOnClick(View view)
    {
        playerScored(pingPongGame.playerTwo);
        displayPlayerTwoScore();
        displayNumberOfSetsPlayerTwoWon();
    }

    public void playerScored(PingPongPlayer player)
    {
        if(pingPongGame.isGameOver()) return;

        pingPongGame.playerHasScored(player);

        if(!pingPongGame.hasPlayerWonCurrentSet(player)) return;

        pingPongGame.incrementNumberOfSetsWon(player);
        clearCurrentSetScoreBothPlayers();

        if(pingPongGame.hasPlayerWonMatch(player))
        {
            displayMessage(
                    getString(
                            R.string.player_won_match,
                            player.getName()
                    )
            );
        }
        else
        {
            displayMessage(
                    getString(
                            R.string.player_won_set,
                            player.getName(),
                            pingPongGame.getCurrentSetNumber()
                    )
            );
        }
    }

    public void displayMessage(String message)
    {
        TextView messageDisplay = findViewById(R.id.message);
        messageDisplay.setText(message);
    }

    public void clearMessage()
    {
        displayMessage("");
    }

    public void clearCurrentSetScoreBothPlayers()
    {
        ((TextView)findViewById(R.id.player_one_current_set_score)).
                setText(getString(R.string.initial_set_score));
        ((TextView)findViewById(R.id.player_two_current_set_score)).
                setText(getString(R.string.initial_set_score));
        pingPongGame.playerOne.resetCurrentSetScore();
        pingPongGame.playerTwo.resetCurrentSetScore();
    }

    public void clearSetsWonBothPlayers()
    {
        ((TextView)findViewById(R.id.player_one_sets_won)).
                setText(getString(R.string.initial_sets_won));
        ((TextView)findViewById(R.id.player_two_sets_won)).
                setText(getString(R.string.initial_sets_won));
        pingPongGame.playerOne.resetNumberOfSetsWon();
        pingPongGame.playerTwo.resetNumberOfSetsWon();
    }

    public void displayNumberOfSetsPlayerOneWon()
    {
        ((TextView)findViewById(R.id.player_one_sets_won)).
                setText(Integer.toString(pingPongGame.getNumSetsPlayerOneWon()));
    }

    public void displayNumberOfSetsPlayerTwoWon()
    {
        ((TextView)findViewById(R.id.player_two_sets_won)).
                setText(Integer.toString(pingPongGame.getNumSetsPlayerTwoWon()));
    }

    public void displayPlayerOneScore()
    {
        ((TextView)findViewById(R.id.player_one_current_set_score)).
                setText(Integer.toString(pingPongGame.getPlayerOneCurrentSetScore()));
    }

    public void displayPlayerTwoScore()
    {
        ((TextView)findViewById(R.id.player_two_current_set_score)).
                setText(Integer.toString(pingPongGame.getPlayerTwoCurrentSetScore()));
    }

    public void reset(View view)
    {
        clearCurrentSetScoreBothPlayers();
        clearMessage();
        clearSetsWonBothPlayers();
        pingPongGame.resetGame();
    }


}
