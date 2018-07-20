package com.example.android.pingpongscorekeeper.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.pingpongscorekeeper.R;
import com.example.android.pingpongscorekeeper.activities.GameActivity;
import com.example.android.pingpongscorekeeper.components.PingPongGameConfiguration;

public class NewGameFragment extends Fragment
{
    private PingPongGameConfiguration  configuration;

    private EditText playerOneDisplay;
    private EditText playerTwoDisplay;
    private CheckBox playerOneServe;
    private CheckBox playerTwoServe;

    private View rootView;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_new_game, container, false);

        configuration = new PingPongGameConfiguration();

        playerOneDisplay = rootView.findViewById(R.id.player_one);
        playerTwoDisplay = rootView.findViewById(R.id.player_two);

        playerOneDisplay.addTextChangedListener(playerOneDisplayWatcher);
        playerTwoDisplay.addTextChangedListener(playerTwoDisplayWatcher);

        playerOneServe = rootView.findViewById(R.id.player_one_serve);
        playerTwoServe = rootView.findViewById(R.id.player_two_serve);

        ((View)playerOneServe).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player_one_check_box(view);
            }
        });

        ((View)playerTwoServe).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player_two_check_box(view);
            }
        });

        rootView.findViewById(R.id.increase).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                incrementSets(view);
            }
        });

        rootView.findViewById(R.id.decrease).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decrementSets(view);
            }
        });

        rootView.findViewById(R.id.start_game).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGameOnClick(view);
            }
        });

        return rootView;
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
        ((TextView)rootView.findViewById(R.id.num_of_sets)).
                setText(Integer.toString(configuration.getNumberOfSets()));
    }

    public void startGameOnClick(View view)
    {
        String playerOneName = configuration.getPlayerOneName();
        String playerTwoName =  configuration.getPlayerTwoName();
        String numSets = configuration.getNumberOfSets() + "";
        String servingPlayer = configuration.getServingPlayer().toString();

        // TODO: Find better way to pass necessary data to GameActivity
        Intent intent = new Intent(getActivity(), GameActivity.class);
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
