package com.example.android.pingpongscorekeeper.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.android.pingpongscorekeeper.R;
import com.example.android.pingpongscorekeeper.components.PingPongSet;
import com.example.android.pingpongscorekeeper.data.PingPongContract;

import java.util.ArrayList;

public class SetEditorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_editor);

        String playerOneName = getIntent().getExtras().getString("playerOneName");
        String playerTwoName = getIntent().getExtras().getString("playerTwoName");
        long playerOneId = getIntent().getExtras().getLong("playerOneId");
        long playerTwoId = getIntent().getExtras().getLong("playerTwoId");
        TextView p1 = findViewById(R.id.player_one_id);
        p1.setText(playerOneName);

        TextView p2 = findViewById(R.id.player_two_id);
        p2.setText(playerTwoName);


    }

    private ArrayList<PingPongSet> createTempPingPongSets(int numOfSets)
    {
        ArrayList<PingPongSet> pingPongSets = new ArrayList<>();

        for(int i = 0; i < numOfSets; i++)
        {
            pingPongSets.add(new PingPongSet(i + 1, -1, -1));
        }

        return pingPongSets;
    }

    private void insertFinishedMatch(long playerOneId, long playerTwoId, int numberOfSetsPlayerOneWon, int numberOfSetsPlayerTwoWon, ArrayList<PingPongSet> pingPongSets)
    {
        ContentValues values = new ContentValues();
        values.put(PingPongContract.PingPongMatch.COLUMN_PLAYER_ONE_ID_TITLE, playerOneId);
        values.put(PingPongContract.PingPongMatch.COLUMN_PLAYER_TWO_ID_TITLE,  playerTwoId);
        values.put(PingPongContract.PingPongMatch.COLUMN_PLAYER_ONE_SETS_WON_TITLE,  numberOfSetsPlayerOneWon);
        values.put(PingPongContract.PingPongMatch.COLUMN_PLAYER_TWO_SETS_WON_TITLE, numberOfSetsPlayerTwoWon);

        Uri uri =getContentResolver().insert(PingPongContract.PingPongMatch.CONTENT_URI, values);

        long id = Long.valueOf(uri.getLastPathSegment());

        for(PingPongSet set: pingPongSets)
        {
            values = new ContentValues();
            values.put(PingPongContract.Set.MATCH_ID, id+ "");
            values.put(PingPongContract.Set.SET_NUMBER, set.getSetNumber());
            values.put(PingPongContract.Set.PLAYER_ONE_SCORE,  set.getPlayerOneScore());
            values.put(PingPongContract.Set.PLAYER_TWO_SCORE, set.getPlayerTwoScore());

            getContentResolver().insert(PingPongContract.Set.CONTENT_URI, values);
        }
    }
}
