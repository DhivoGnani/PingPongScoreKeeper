package com.example.android.pingpongscorekeeper.adapters;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.pingpongscorekeeper.R;
import com.example.android.pingpongscorekeeper.data.PingPongContract;

import java.util.ArrayList;

public class FinishedMatchesAdapter  extends CursorAdapter
{

    public FinishedMatchesAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView playerOneName = (TextView) view.findViewById(R.id.home_name);
        TextView playerTwoName = (TextView) view.findViewById(R.id.away_name);

        TextView r  = view.findViewById(R.id.player_one_score);
        TextView s = view.findViewById(R.id.player_two_score);

        int playerOneCol = cursor.getColumnIndex(PingPongContract.PingPongMatch.COLUMN_PLAYER_ONE_NAME_TITLE);
        int playerTwoCol = cursor.getColumnIndex(PingPongContract.PingPongMatch.COLUMN_PLAYER_TWO_NAME_TITLE);


        int p1Col = cursor.getColumnIndex(PingPongContract.PingPongMatch.COLUMN_PLAYER_ONE_SETS_WON_TITLE);
        int p2Col = cursor.getColumnIndex(PingPongContract.PingPongMatch.COLUMN_PLAYER_TWO_SETS_WON_TITLE);


        String p1 = cursor.getString(playerOneCol);
        String p2 = cursor.getString(playerTwoCol);

        int p1sets = cursor.getInt(p1Col);
        int p2sets = cursor.getInt(p2Col);

        int done = cursor.getColumnIndex(PingPongContract.PingPongMatch.COLUMN_GAME_TIME_DONE_TITLE);

        String x = cursor.getString(done);

        TextView q = view.findViewById(R.id.date_game);
        q.setText(x.substring(0, 10));

        r.setText(p1sets + "");
        s.setText(p2sets + "");
        if(p1sets > p2sets) r.setTypeface(r.getTypeface(), Typeface.BOLD);
        else s.setTypeface(r.getTypeface(), Typeface.BOLD);

        playerOneName.setText(p1);
        playerTwoName.setText(p2);
    }
}
