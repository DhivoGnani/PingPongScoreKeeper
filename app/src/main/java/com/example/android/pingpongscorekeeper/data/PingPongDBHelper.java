package com.example.android.pingpongscorekeeper.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.pingpongscorekeeper.data.PingPongContract.PingPongMatch;

public class PingPongDBHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = PingPongDBHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "pingpong.db";

    private static final int DATABASE_VERSION = 1;

    public PingPongDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_PING_PONG_TABLE =  "CREATE TABLE " + PingPongMatch.TABLE_NAME + " ("
                + PingPongMatch._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PingPongMatch.COLUMN_PLAYER_ONE_NAME_TITLE + " TEXT NOT NULL, "
                + PingPongMatch.COLUMN_PLAYER_TWO_NAME_TITLE + " TEXT NOT NULL, "
                + PingPongMatch.COLUMN_PLAYER_ONE_SETS_WON_TITLE + " INTEGER NOT NULL, "
                + PingPongMatch.COLUMN_PLAYER_TWO_SETS_WON_TITLE + " INTEGER NOT NULL, "
                + PingPongMatch.COLUMN_GAME_TIME_DONE_TITLE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL);";

        String SQL_CREATE_SET_TABLE =  "CREATE TABLE " + PingPongContract.Set.TABLE_NAME + " ("
                + PingPongContract.Set._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PingPongContract.Set.MATCH_ID + " INTEGER NOT NULL, "
                + PingPongContract.Set.SET_NUMBER + " INTEGER NOT NULL, "
                + PingPongContract.Set.PLAYER_ONE_SCORE + " INTEGER NOT NULL, "
                + PingPongContract.Set.PLAYER_TWO_SCORE + " INTEGER NOT NULL, "
                + " FOREIGN KEY ("+PingPongContract.Set.MATCH_ID+") REFERENCES "
                + PingPongMatch.TABLE_NAME +"("+PingPongMatch._ID+") ON DELETE CASCADE)";




        db.execSQL(SQL_CREATE_PING_PONG_TABLE);
        db.execSQL(SQL_CREATE_SET_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys = ON;");
    }
}
