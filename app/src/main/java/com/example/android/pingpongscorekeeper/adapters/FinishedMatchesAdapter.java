package com.example.android.pingpongscorekeeper.adapters;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
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

        TextView r  = view.findViewById(R.id.data_textview);

        int playerOneCol = cursor.getColumnIndex(PingPongContract.PingPongMatch.COLUMN_PLAYER_ONE_NAME_TITLE);
        int playerTwoCol = cursor.getColumnIndex(PingPongContract.PingPongMatch.COLUMN_PLAYER_TWO_NAME_TITLE);


        int p1Col = cursor.getColumnIndex(PingPongContract.PingPongMatch.COLUMN_PLAYER_ONE_SETS_WON_TITLE);
        int p2Col = cursor.getColumnIndex(PingPongContract.PingPongMatch.COLUMN_PLAYER_TWO_SETS_WON_TITLE);


        String p1 = cursor.getString(playerOneCol);
        String p2 = cursor.getString(playerTwoCol);

        int p1sets = cursor.getInt(p1Col);
        int p2sets = cursor.getInt(p2Col);

        r.setText(p1sets + " - " + p2sets);
        playerOneName.setText(p1);
        playerTwoName.setText(p2);
    }
}
