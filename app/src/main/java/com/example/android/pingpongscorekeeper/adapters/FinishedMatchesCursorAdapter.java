package com.example.android.pingpongscorekeeper.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.pingpongscorekeeper.R;
import com.example.android.pingpongscorekeeper.activities.MatchActivity;
import com.example.android.pingpongscorekeeper.data.PingPongContract;

import static android.support.v4.content.ContextCompat.startActivity;
import static com.example.android.pingpongscorekeeper.data.PingPongContract.PingPongMatch.COLUMN_GAME_TIME_DONE_LOCAL_TITLE;
import static com.example.android.pingpongscorekeeper.data.PingPongContract.PingPongMatch.COLUMN_PLAYER_ONE_NAME_TITLE;
import static com.example.android.pingpongscorekeeper.data.PingPongContract.PingPongMatch.COLUMN_PLAYER_ONE_SETS_WON_TITLE;
import static com.example.android.pingpongscorekeeper.data.PingPongContract.PingPongMatch.COLUMN_PLAYER_TWO_NAME_TITLE;
import static com.example.android.pingpongscorekeeper.data.PingPongContract.PingPongMatch.COLUMN_PLAYER_TWO_SETS_WON_TITLE;
import static com.example.android.pingpongscorekeeper.data.PingPongContract.PingPongMatch._ID;

public class FinishedMatchesCursorAdapter extends CursorAdapter
{
    public FinishedMatchesCursorAdapter(Context context, Cursor c) {
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
        final int  idCol = cursor.getColumnIndex(_ID);
        final int id =  cursor.getInt(idCol);

        final String playerOneNamer = cursor.getString(playerOneNameCol);
        final String playerTwoNamer = cursor.getString(playerTwoNameCol);

        playerOneName.setText(cursor.getString(playerOneNameCol));
        playerTwoName.setText(cursor.getString(playerTwoNameCol));

        final int playerOneSetsWon = cursor.getInt(playerOneScoreCol);
        final int playerTwoSetsWon = cursor.getInt(playerTwoScoreCol);

        gameEndTimeStamp.setText(cursor.getString(gameEndTimeLocal).substring(0,16));

        playerOneScore.setText(String.valueOf(playerOneSetsWon));
        playerTwoScore.setText(String.valueOf(playerTwoSetsWon));

        final Context x = context;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(x, MatchActivity.class);
                intent.setData(PingPongContract.Set.CONTENT_URI);
                String matchId = id + "";
                intent.putExtra("matchId", matchId);
                intent.putExtra("playerOneName", playerOneNamer);
                intent.putExtra("playerTwoName", playerTwoNamer);
                if(playerOneSetsWon > playerTwoSetsWon) {
                    intent.putExtra("won", "p1");
                } else {
                    intent.putExtra("won", "p2");
                }

                ((AppCompatActivity)x).startActivity(intent);
            }
        });

        playerTwoScore.setTypeface(
                Typeface.create(playerTwoScore.getTypeface(), Typeface.NORMAL), Typeface.NORMAL
        );
        playerOneScore.setTypeface(
                Typeface.create(playerOneScore.getTypeface(), Typeface.NORMAL), Typeface.NORMAL
        );

        if(playerOneSetsWon > playerTwoSetsWon) {
            playerOneScore.setTypeface(playerOneScore.getTypeface(), Typeface.BOLD);
        }
        else {
            playerTwoScore.setTypeface(playerTwoScore.getTypeface(), Typeface.BOLD);
        }
    }
}
