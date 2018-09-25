package me.dhivo.android.pingpongmatchtracker.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class PingPongDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val SQL_CREATE_PLAYERS_TABLE = ("CREATE TABLE " + PingPongContract.Player.TABLE_NAME + " ("
                + PingPongContract.Player._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PingPongContract.Player.COLUMN_NAME_TITLE + " TEXT NOT NULL, "
                + PingPongContract.Player.COLUMN_PROFILE_PICTURE_TITLE + " TEXT);")

        val SQL_CREATE_PING_PONG_TABLE = ("CREATE TABLE " + PingPongContract.PingPongMatch.TABLE_NAME + " ("
                + PingPongContract.PingPongMatch._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PingPongContract.PingPongMatch.COLUMN_PLAYER_ONE_ID_TITLE + " INTEGER NOT NULL, "
                + PingPongContract.PingPongMatch.COLUMN_PLAYER_TWO_ID_TITLE + " INTEGER NOT NULL, "
                + PingPongContract.PingPongMatch.COLUMN_SERVING_PLAYER_ID_TITLE + " INTEGER NOT NULL, "
                + PingPongContract.PingPongMatch.COLUMN_PLAYER_ONE_SETS_WON_TITLE + " INTEGER NOT NULL, "
                + PingPongContract.PingPongMatch.COLUMN_PLAYER_TWO_SETS_WON_TITLE + " INTEGER NOT NULL, "
                + PingPongContract.PingPongMatch.COLUMN_GAME_TIME_DONE_TITLE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL, "
                + " FOREIGN KEY (" + PingPongContract.PingPongMatch.COLUMN_PLAYER_ONE_ID_TITLE + ") REFERENCES "
                + PingPongContract.Player.TABLE_NAME + "(" + PingPongContract.Player._ID + "), "
                + " FOREIGN KEY (" + PingPongContract.PingPongMatch.COLUMN_PLAYER_TWO_ID_TITLE + ") REFERENCES "
                + PingPongContract.Player.TABLE_NAME + "(" + PingPongContract.Player._ID + "), "
                + " FOREIGN KEY (" + PingPongContract.PingPongMatch.COLUMN_SERVING_PLAYER_ID_TITLE + ") REFERENCES "
                + PingPongContract.Player.TABLE_NAME + "(" + PingPongContract.Player._ID + "));")

        val SQL_CREATE_SET_TABLE = ("CREATE TABLE " + PingPongContract.Set.TABLE_NAME + " ("
                + PingPongContract.Set._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PingPongContract.Set.MATCH_ID + " INTEGER NOT NULL, "
                + PingPongContract.Set.SET_NUMBER + " INTEGER NOT NULL, "
                + PingPongContract.Set.PLAYER_ONE_SCORE + " INTEGER NOT NULL, "
                + PingPongContract.Set.PLAYER_TWO_SCORE + " INTEGER NOT NULL, "
                + " FOREIGN KEY (" + PingPongContract.Set.MATCH_ID + ") REFERENCES "
                + PingPongContract.PingPongMatch.TABLE_NAME + "(" + PingPongContract.PingPongMatch._ID + ") ON DELETE CASCADE)")


        db.execSQL(SQL_CREATE_PING_PONG_TABLE)
        db.execSQL(SQL_CREATE_SET_TABLE)
        db.execSQL(SQL_CREATE_PLAYERS_TABLE)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {}

    override fun onOpen(db: SQLiteDatabase) {
        super.onOpen(db)
        db.execSQL("PRAGMA foreign_keys = ON;")
    }

    companion object {

        val LOG_TAG = PingPongDBHelper::class.java.simpleName

        private val DATABASE_NAME = "pingpong.db"

        private val DATABASE_VERSION = 1
    }
}
