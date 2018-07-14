package com.example.android.pingpongscorekeeper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.pingpongscorekeeper.components.PingPongGameConfiguration;

import org.w3c.dom.Text;

//android:launchMode="singleTop"
public class NewGameActivity extends AppCompatActivity {
    PingPongGameConfiguration  configuration;
    private EditText playerOne;
    private EditText playerTwo;

    CheckBox playerOneC;
    CheckBox playerTwoC;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);
        configuration = new PingPongGameConfiguration();

        playerOne = findViewById(R.id.player_one);

        playerOne.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                configuration.setPlayerOneName(playerOne.getText().toString());

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

        });

        playerTwo = findViewById(R.id.player_two);

        playerTwo.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                configuration.setPlayerTwoName(playerTwo.getText().toString());

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

        });

        playerOneC = findViewById(R.id.player_one_serve);
        playerTwoC = findViewById(R.id.player_two_serve);
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
        String playerOneName   = configuration.getPlayerOneName();
        String playerTwoName   =  configuration.getPlayerTwoName();
        String numSets     = configuration.getNumberOfSets() + "";
        String servingPlayer = configuration.getServingPlayer().toString();

        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("playerOneName", playerOneName);
        intent.putExtra("playerTwoName", playerTwoName);
        intent.putExtra("numSets", numSets);
        intent.putExtra("servingPlayer", servingPlayer);

        startActivity(intent);
    }

    public void player_two_check_box(View view) {
        configuration.switchServingPlayer();
        if(playerTwoC.isChecked())
        {
           if(playerOneC.isChecked())
           {
               playerOneC.toggle();
           }
        } else {
            if(!playerOneC.isChecked())
            {
                playerOneC.toggle();
            }
        }
    }

    public void player_one_check_box(View view) {
        configuration.switchServingPlayer();
        if(playerOneC.isChecked())
        {
            if(playerTwoC.isChecked())
            {
                playerTwoC.toggle();
            }
        } else {
            if(!playerTwoC.isChecked())
            {
                playerTwoC.toggle();
            }
        }
    }
}
