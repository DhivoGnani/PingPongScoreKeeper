package com.example.android.pingpongscorekeeper.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.pingpongscorekeeper.R;
import com.example.android.pingpongscorekeeper.components.PingPongGame;
import com.example.android.pingpongscorekeeper.components.PingPongPlayer;

import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

//TODO: Why is AppCompactActivity and not Activity used here?
public class GameActivity extends AppCompatActivity
{
    private PingPongGame pingPongGame;
    private TextToSpeech textToSpeech;
    private Random rand = new Random();
    private EditText playerOneNameDisplay;
    private EditText playerTwoNameDisplay;

    // TODO: Remove hardcoded strings
    private static final String [] congratulatingWords =
            {"Good Job", "Great Shot", "Nice Move", "Incredible"};

    // TODO: Remove hardcoded strings
    private TextToSpeech.OnInitListener textToSpeechListener = new TextToSpeech.OnInitListener() {
        @Override
        public void onInit(int status) {
            if (status == TextToSpeech.SUCCESS) {
                int result = textToSpeech.setLanguage(Locale.UK);
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "This Language is not supported");
                }

            } else {
                Log.e("TTS", "Initilization Failed!");
            }
        }
    };

    // TODO: Remove duplicate code
    private TextWatcher playerOneNameChanged = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            pingPongGame.setPlayerOneName(playerOneNameDisplay.getText().toString());

        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
        }

    };

    // TODO: Remove duplicate code
    private TextWatcher playerTwoNameChanged = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            pingPongGame.setPlayerTwoName(playerTwoNameDisplay.getText().toString());
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
        }

    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO: Find a better way to get necessary data
        final String playerOneName = getIntent().getExtras().getString("playerOneName");
        final String playerTwoName = getIntent().getExtras().getString("playerTwoName");
        final int numSets = Integer.valueOf(getIntent().getExtras().getString("numSets"));
        final String servingPlayer = getIntent().getExtras().getString("servingPlayer");

        pingPongGame = new PingPongGame(playerOneName, playerTwoName, numSets, servingPlayer);

        displayPlayersName();
        displayCurrentServingPlayer();

        textToSpeech = new TextToSpeech(this, textToSpeechListener);

        playerOneNameDisplay = findViewById(R.id.player_one);
        playerOneNameDisplay.addTextChangedListener(playerOneNameChanged);

        playerTwoNameDisplay = findViewById(R.id.player_two);
        playerTwoNameDisplay.addTextChangedListener(playerTwoNameChanged);
    }

    private void speak(String text){
        SharedPreferences sharedPref =
                PreferenceManager.getDefaultSharedPreferences(this);

        // TODO: Remove hardcoded string
        Boolean switchPref = sharedPref.getBoolean("audio_switch", false);

        if(!switchPref) return;

        // TODO: Use a better voice for TextToSpeech (not robotic sounding voice)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }else{
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    public void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    private void clearServingPlayer()
    {
        ((TextView)findViewById(R.id.player_one_serve)).
                setText("");
        ((TextView)findViewById(R.id.player_two_serve)).
                setText("");
    }

    //TODO: Remove hardcoded strings
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
        if(!pingPongGame.hasPlayerWonCurrentSet(pingPongGame.playerOne))
        {
            if(pingPongGame.isDeuce()) {
                playAudio("Deuce!", false);
            }
            else if(pingPongGame.playerOne.getCurrentSetScore() != 0) {
                playAudio(pingPongGame.playerOneName, true);
            }
        }
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

    public void playAudio(String playerName, boolean en)
    {
        // TODO: Change audio file
        // TODO: Use voice library

        String text = en ? getRandomEncouragingWord() + " " + playerName : playerName;
        speak(text );
    }

    public String getRandomEncouragingWord()
    {
        int Low = 0;
        int High = congratulatingWords.length;
        int Result = rand.nextInt(High-Low) + Low;
        return congratulatingWords[Result];
    }

    public void playerTwoScoreOnClick(View view)
    {
        if(pingPongGame.isGameOver()) return;
        playerScored(pingPongGame.playerTwo);
        if(!pingPongGame.hasPlayerWonCurrentSet(pingPongGame.playerTwo))
        {
            if(pingPongGame.isDeuce()) {
                playAudio("Deuce!", false);
            }
            else if(pingPongGame.playerTwo.getCurrentSetScore() != 0) {
                playAudio(pingPongGame.playerTwoName, true);
            }
        }
        displayPointsAndSets();
    }

    public void playerScored(PingPongPlayer player)
    {
        pingPongGame.playerHasScored(player);
        if(!pingPongGame.hasPlayerWonCurrentSet(player)) {
            if(pingPongGame.isDeuce()) {
                displayMessage("Deuce !");
            } else {
                displayMessage("");
            }
            return;
        }

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

            playAudio(getString(
                    R.string.player_won_match,
                    player.getName()
            ), false);
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
            playAudio(getString(
                    R.string.player_won_set,
                    player.getName(),
                    pingPongGame.getCurrentSetNumber()
            ), false);
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
