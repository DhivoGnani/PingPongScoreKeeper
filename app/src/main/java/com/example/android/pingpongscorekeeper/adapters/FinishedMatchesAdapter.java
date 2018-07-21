package com.example.android.pingpongscorekeeper.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.pingpongscorekeeper.R;

import static com.example.android.pingpongscorekeeper.data.PingPongContract.PingPongMatch.COLUMN_GAME_TIME_DONE_LOCAL_TITLE;
import static com.example.android.pingpongscorekeeper.data.PingPongContract.PingPongMatch.COLUMN_PLAYER_ONE_NAME_TITLE;
import static com.example.android.pingpongscorekeeper.data.PingPongContract.PingPongMatch.COLUMN_PLAYER_ONE_SETS_WON_TITLE;
import static com.example.android.pingpongscorekeeper.data.PingPongContract.PingPongMatch.COLUMN_PLAYER_TWO_NAME_TITLE;
import static com.example.android.pingpongscorekeeper.data.PingPongContract.PingPongMatch.COLUMN_PLAYER_TWO_SETS_WON_TITLE;

public class FinishedMatchesAdapter  extends CursorAdapter
{
    public FinishedMatchesAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final TextView playerOneName = view.findViewById(R.id.player_one_name);
        final TextView playerTwoName = view.findViewById(R.id.player_two_name);
        final TextView playerOneScore  = view.findViewById(R.id.player_one_score);
        final TextView playerTwoScore = view.findViewById(R.id.player_two_score);
        final TextView gameEndTimeStamp = view.findViewById(R.id.game_end_timestamp);

        final int playerOneNameCol = cursor.getColumnIndex(COLUMN_PLAYER_ONE_NAME_TITLE);
        final int playerTwoNameCol = cursor.getColumnIndex(COLUMN_PLAYER_TWO_NAME_TITLE);
        final int playerOneScoreCol = cursor.getColumnIndex(COLUMN_PLAYER_ONE_SETS_WON_TITLE);
        final int playerTwoScoreCol = cursor.getColumnIndex(COLUMN_PLAYER_TWO_SETS_WON_TITLE);
        final int gameEndTimeLocal = cursor.getColumnIndex(COLUMN_GAME_TIME_DONE_LOCAL_TITLE);

        playerOneName.setText(cursor.getString(playerOneNameCol));
        playerTwoName.setText(cursor.getString(playerTwoNameCol));

        final int playerOneSetsWon = cursor.getInt(playerOneScoreCol);
        final int playerTwoSetsWon = cursor.getInt(playerTwoScoreCol);

        gameEndTimeStamp.setText(cursor.getString(gameEndTimeLocal).substring(0,16));

        playerOneScore.setText(String.valueOf(playerOneSetsWon));
        playerTwoScore.setText(String.valueOf(playerTwoSetsWon));

        if(playerOneSetsWon > playerTwoSetsWon)
            makeTextViewBold(playerOneScore);
        else
            makeTextViewBold(playerTwoScore);
    }

    private void makeTextViewBold(TextView textView)
    {
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
    }
}
