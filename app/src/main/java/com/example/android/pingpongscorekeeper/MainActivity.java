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
        displayCurrentServingPlayer();
    }

    private void clearServingPlayer()
    {
        ((TextView)findViewById(R.id.player_one_serve)).
                setText("");
        ((TextView)findViewById(R.id.player_two_serve)).
                setText("");
    }

    private void displayCurrentServingPlayer() {
        if(pingPongGame.currentServingPlayer == pingPongGame.playerOne)
        {
            ((TextView)findViewById(R.id.player_one_serve)).
                    setText("Serve");
            ((TextView)findViewById(R.id.player_two_serve)).
                    setText("");
        }
        else if(pingPongGame.currentServingPlayer == pingPongGame.playerTwo)
        {
            ((TextView)findViewById(R.id.player_two_serve)).
                    setText("Serve");
            ((TextView)findViewById(R.id.player_one_serve)).
                    setText("");
        }
    }

    private void displayPlayersName() {
        ((TextView)findViewById(R.id.player_one)).setText(pingPongGame.playerOne.getName());
        ((TextView)findViewById(R.id.player_two)).setText(pingPongGame.playerTwo.getName());
    }

    public void playerOneScoreOnClick(View view)
    {
        if(pingPongGame.isGameOver()) return;
        playerScored(pingPongGame.playerOne);
        displayPointsAndSets();
    }

    public void displayPointsAndSets()
    {
        displayPlayerOneScore();
        displayPlayerTwoScore();
        displayNumberOfSetsPlayerOneWon();
        displayNumberOfSetsPlayerTwoWon();
        displayCurrentServingPlayer();
    }

    public void playerTwoScoreOnClick(View view)
    {
        if(pingPongGame.isGameOver()) return;
        playerScored(pingPongGame.playerTwo);
        displayPointsAndSets();
    }

    public void playerScored(PingPongPlayer player)
    {
        pingPongGame.playerHasScored(player);
        if(!pingPongGame.hasPlayerWonCurrentSet(player)) return;

        pingPongGame.incrementNumberOfSetsWon(player);
        pingPongGame.resetCurrentScore();

        if(pingPongGame.hasPlayerWonMatch(player))
        {
            displayMessage(
                    getString(
                            R.string.player_won_match,
                            player.getName()
                    )
            );
            clearServingPlayer();
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

    public void resetCurrentScoreDisplay()
    {
        ((TextView)findViewById(R.id.player_one_current_set_score)).
                setText(getString(R.string.initial_set_score));
        ((TextView)findViewById(R.id.player_two_current_set_score)).
                setText(getString(R.string.initial_set_score));
    }

    public void resetNumberOfSetsWonDisplay()
    {
        ((TextView)findViewById(R.id.player_one_sets_won)).
                setText(getString(R.string.initial_sets_won));
        ((TextView)findViewById(R.id.player_two_sets_won)).
                setText(getString(R.string.initial_sets_won));
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

    public void resetOnClick(View view)
    {
        resetCurrentScoreDisplay();
        resetNumberOfSetsWonDisplay();
        clearMessage();
        pingPongGame.resetGame();
        displayCurrentServingPlayer();
    }
}
