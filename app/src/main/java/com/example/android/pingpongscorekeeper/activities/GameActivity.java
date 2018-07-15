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
    PingPongGame pingPongGame;
    private TextToSpeech tts;
    Random rand = new Random();
    EditText playerOne;
    EditText playerTwo;

    private static final String [] congratulatingWords =
            {"Good Job", "Great Shot", "Nice Move", "Incredible"};

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


    private void displaySettings() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String playerOneName = getIntent().getExtras().getString("playerOneName");
        String playerTwoName = getIntent().getExtras().getString("playerTwoName");
        int numSets = Integer.valueOf(getIntent().getExtras().getString("numSets"));
        String servingPlayer = getIntent().getExtras().getString("servingPlayer");


        pingPongGame = new PingPongGame(playerOneName, playerTwoName, numSets, servingPlayer);

        displayPlayersName();
        displayCurrentServingPlayer();

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.UK);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "This Language is not supported");
                    }

                } else {
                    Log.e("TTS", "Initilization Failed!");
                }
            }
        });

        playerOne = (EditText)findViewById(R.id.player_one);

        playerOne.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pingPongGame.setPlayerOneName(playerOne.getText().toString());

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

        });

        playerTwo = (EditText)findViewById(R.id.player_two);

        playerTwo.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pingPongGame.setPlayerTwoName(playerTwo.getText().toString());
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

        });
    }

    private void speak(String text){
        SharedPreferences sharedPref =
                PreferenceManager.getDefaultSharedPreferences(this);
        Boolean switchPref = sharedPref.getBoolean
                ("audio_switch", false);
        if(!switchPref) return;
        HashMap<String, String> onlineSpeech = new HashMap<>();
        onlineSpeech.put(TextToSpeech.Engine.KEY_FEATURE_NETWORK_SYNTHESIS, "true");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }else{
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, onlineSpeech);
        }
    }

    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
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
//        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.great);
//        mediaPlayer.start();

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
