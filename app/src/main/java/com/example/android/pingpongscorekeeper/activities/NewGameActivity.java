package com.example.android.pingpongscorekeeper.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.pingpongscorekeeper.R;
import com.example.android.pingpongscorekeeper.components.PingPongGameConfiguration;

public class NewGameActivity extends AppCompatActivity
{
    private PingPongGameConfiguration  configuration;

    private EditText playerOneDisplay;
    private EditText playerTwoDisplay;
    private CheckBox playerOneServe;
    private CheckBox playerTwoServe;

    // TODO: Duplicate code in TextWatcher
    private TextWatcher playerOneDisplayWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            configuration.setPlayerOneName(playerOneDisplay.getText().toString());

        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
        }

    };

    // TODO: Duplicate code in TextWatcher
    private TextWatcher playerTwoDisplayWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            configuration.setPlayerTwoName(playerTwoDisplay.getText().toString());

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

        configuration = new PingPongGameConfiguration();

        playerOneDisplay = findViewById(R.id.player_one);
        playerTwoDisplay = findViewById(R.id.player_two);

        playerOneDisplay.addTextChangedListener(playerOneDisplayWatcher);
        playerTwoDisplay.addTextChangedListener(playerTwoDisplayWatcher);

        playerOneServe = findViewById(R.id.player_one_serve);
        playerTwoServe = findViewById(R.id.player_two_serve);
    }

    public void decrementSets(View view) {
        configuration.decrementNumberOfSets();
        displayNumberOfSets();
    }

    public void incrementSets(View view) {
        configuration.incrementNumberOfSets();
        displayNumberOfSets();
    }

    public void displayNumberOfSets()
    {
        ((TextView)findViewById(R.id.num_of_sets)).
                setText(Integer.toString(configuration.getNumberOfSets()));
    }

    public void startGameOnClick(View view)
    {
        String playerOneName = configuration.getPlayerOneName();
        String playerTwoName =  configuration.getPlayerTwoName();
        String numSets = configuration.getNumberOfSets() + "";
        String servingPlayer = configuration.getServingPlayer().toString();

        // TODO: Find better way to pass necessary data to GameActivity
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("playerOneName", playerOneName);
        intent.putExtra("playerTwoName", playerTwoName);
        intent.putExtra("numSets", numSets);
        intent.putExtra("servingPlayer", servingPlayer);
        startActivity(intent);
    }

    // TODO: remove duplicate code
    public void player_two_check_box(View view) {
        configuration.switchServingPlayer();
        if(playerTwoServe.isChecked())
        {
           if(playerOneServe.isChecked()) playerOneServe.toggle();
        }
        else
        {
            if(!playerOneServe.isChecked()) playerOneServe.toggle();
        }
    }

    // TODO: remove duplicate code
    public void player_one_check_box(View view) {
        configuration.switchServingPlayer();
        if(playerOneServe.isChecked())
        {
            if(playerTwoServe.isChecked())  playerTwoServe.toggle();
        }
        else
        {
            if(!playerTwoServe.isChecked()) playerTwoServe.toggle();
        }
    }
}
