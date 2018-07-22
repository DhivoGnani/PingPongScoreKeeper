package com.example.android.pingpongscorekeeper.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.pingpongscorekeeper.R;

import static com.example.android.pingpongscorekeeper.data.PingPongContract.Set.PLAYER_ONE_SCORE;
import static com.example.android.pingpongscorekeeper.data.PingPongContract.Set.PLAYER_TWO_SCORE;
import static com.example.android.pingpongscorekeeper.data.PingPongContract.Set.SET_NUMBER;

public class MatchSetsAdapter extends CursorAdapter {

    public MatchSetsAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate( R.layout.test_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final TextView setNumberView = view.findViewById(R.id.setNumber);
        final TextView setScoreView = view.findViewById(R.id.setScore);

        final int playerOneSetScoreCol = cursor.getColumnIndex(PLAYER_ONE_SCORE);
        final int playerTwoSetScoreCol = cursor.getColumnIndex(PLAYER_TWO_SCORE);
        final int setNumberCol = cursor.getColumnIndex(SET_NUMBER);

        final int playerOneScore = cursor.getInt(playerOneSetScoreCol);
        final int playerTwoScore = cursor.getInt(playerTwoSetScoreCol);
        final int setNumber = cursor.getInt(setNumberCol);

        setNumberView.setText("Set Number: " + setNumber);
        setScoreView.setText(playerOneScore + " - " + playerTwoScore);
    }
}
