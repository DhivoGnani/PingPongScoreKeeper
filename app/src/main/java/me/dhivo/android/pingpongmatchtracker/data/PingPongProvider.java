package me.dhivo.android.pingpongmatchtracker.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import me.dhivo.android.pingpongmatchtracker.data.PingPongContract.PingPongMatch;
import me.dhivo.android.pingpongmatchtracker.data.PingPongContract.Player;
import me.dhivo.android.pingpongmatchtracker.data.PingPongContract.Set;

import static me.dhivo.android.pingpongmatchtracker.data.PingPongContract.PingPongMatch.COLUMN_GAME_TIME_DONE_LOCAL_TITLE_ALIAS;
import static me.dhivo.android.pingpongmatchtracker.data.PingPongContract.PingPongMatch.COLUMN_PLAYER_ONE_SETS_WON_TITLE;
import static me.dhivo.android.pingpongmatchtracker.data.PingPongContract.PingPongMatch.COLUMN_PLAYER_TWO_SETS_WON_TITLE;


public class PingPongProvider extends ContentProvider {

    public static final String LOG_TAG = PingPongProvider.class.getSimpleName();
    private PingPongDBHelper mDbHelper;

    private static final int MATCHES = 100;
    private static final int MATCHES_ID = 102;
    private static final int SETS = 101;
    private static final int PLAYERS = 103;
    private static final int PLAYERS_ID = 104;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(PingPongContract.CONTENT_AUTHORITY, PingPongContract.PATH_MATCH, MATCHES);
        sUriMatcher.addURI(PingPongContract.CONTENT_AUTHORITY, PingPongContract.PATH_MATCH + "/#", MATCHES_ID);
        sUriMatcher.addURI(PingPongContract.CONTENT_AUTHORITY, PingPongContract.PATH_SET, SETS);
        sUriMatcher.addURI(PingPongContract.CONTENT_AUTHORITY, PingPongContract.PATH_PLAYER, PLAYERS);
        sUriMatcher.addURI(PingPongContract.CONTENT_AUTHORITY, PingPongContract.PATH_PLAYER + "/#", PLAYERS_ID);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new PingPongDBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        Cursor cursor;
        final int match = sUriMatcher.match(uri);

        switch(match)
        {
            case MATCHES :
//                cursor = database.query(PingPongMatch.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                cursor = getMatchesWithPlayerInfo();
                break;
            case MATCHES_ID:
                selection = PingPongMatch._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                cursor = database.query(PingPongMatch.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case SETS :
                cursor = database.query(Set.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case PLAYERS :
                cursor = database.query(Player.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case PLAYERS_ID :
                selection = Player._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                cursor = database.query(Player.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default :
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int rowsDeleted;
        switch (match) {
            case MATCHES:
                rowsDeleted = database.delete(PingPongMatch.TABLE_NAME, selection, selectionArgs);
                break;
            case MATCHES_ID:
                selection = PingPongMatch._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(PingPongMatch.TABLE_NAME, selection, selectionArgs);
                break;
            case PLAYERS:
                rowsDeleted = database.delete(Player.TABLE_NAME, selection, selectionArgs);
                break;
            case PLAYERS_ID:
                selection = Player._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(Player.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        switch (match) {
            case PLAYERS_ID:
                selection = Player._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsUpdated = database.update(Player.TABLE_NAME, contentValues, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(PingPongMatch.CONTENT_URI, null);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        int match = sUriMatcher.match(uri);

        switch (match) {
            case MATCHES:
                return insertPingPongMatch(uri, contentValues);
            case SETS:
                return insertPingPongSet(uri, contentValues);
            case PLAYERS:
                return insertPingPongPlayer(uri, contentValues);
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
    }

    private Uri insertPingPongSet(Uri uri, ContentValues contentValues) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(Set.TABLE_NAME, null, contentValues);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    private Uri insertPingPongPlayer(Uri uri, ContentValues values)
    {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(Player.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    private Uri insertPingPongMatch(Uri uri, ContentValues values) {
        String playerOneName = values.getAsString(PingPongMatch.COLUMN_PLAYER_ONE_ID_TITLE);
        if (playerOneName == null) {
            throw new IllegalArgumentException("Player One requires a name");
        }

        String playerTwoName = values.getAsString(PingPongMatch.COLUMN_PLAYER_TWO_ID_TITLE);
        if (playerTwoName == null) {
            throw new IllegalArgumentException("Player Two requires a name");
        }


        Integer playerOneSetsWon = values.getAsInteger(COLUMN_PLAYER_ONE_SETS_WON_TITLE);
        if (playerOneSetsWon == null) {
            throw new IllegalArgumentException("Player One Sets Won requires a number");
        }

        Integer playerTwoSetsWon = values.getAsInteger(PingPongMatch.COLUMN_PLAYER_TWO_SETS_WON_TITLE);
        if (playerTwoSetsWon == null) {
            throw new IllegalArgumentException("Player Twp Sets Won requires a number");
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(PingPongMatch.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MATCHES:
                return PingPongMatch.CONTENT_LIST_TYPE;
            case SETS:
                return PingPongContract.Set.CONTENT_LIST_TYPE;
            case PLAYERS:
                return PingPongContract.Player.CONTENT_LIST_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    final String pingPongMatchTableName = "matchTable";
    final String playerOneTableName = "playerOneTable";
    final String playerTwoTableName = "playerTwoTable";
    final String servingPlayerTableName = "servingPlayerTable";

    public Cursor getMatchesWithPlayerInfo()
    {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        String query = "SELECT " + pingPongMatchTableName + "." + PingPongMatch._ID + ","
                +  pingPongMatchTableName + "." + COLUMN_PLAYER_ONE_SETS_WON_TITLE + ","
                +  pingPongMatchTableName + "." + COLUMN_PLAYER_TWO_SETS_WON_TITLE + ","
                + COLUMN_GAME_TIME_DONE_LOCAL_TITLE_ALIAS + "time,"
                + playerOneTableName + "." + Player.COLUMN_NAME_TITLE + " PlayerOneName,"
                + playerOneTableName + "." + Player.COLUMN_PROFILE_PICTURE_TITLE + " PlayerOnePicture,"
                + playerTwoTableName + "." + Player.COLUMN_NAME_TITLE + " PlayerTwoName,"
                + playerTwoTableName + "." + Player.COLUMN_PROFILE_PICTURE_TITLE + " PlayerTwoPicture,"
                + servingPlayerTableName + "." + Player.COLUMN_NAME_TITLE + " ServingPlayerName"
                + " FROM " + PingPongMatch.TABLE_NAME + " " + pingPongMatchTableName
                + " INNER JOIN " + Player.TABLE_NAME  + " " + playerOneTableName + " ON "
                +  pingPongMatchTableName + "." + PingPongMatch.COLUMN_PLAYER_ONE_ID_TITLE + "=" + playerOneTableName + "." + Player._ID
                + " INNER JOIN " + Player.TABLE_NAME + " " + playerTwoTableName + " ON "
                + pingPongMatchTableName + "." + PingPongMatch.COLUMN_PLAYER_TWO_ID_TITLE + "=" + playerTwoTableName + "." + Player._ID
                + " INNER JOIN " + Player.TABLE_NAME + " " + servingPlayerTableName + " ON "
                + pingPongMatchTableName + "." + PingPongMatch.COLUMN_SERVING_PLAYER_ID_TITLE + "=" + servingPlayerTableName + "." + Player._ID
                + " ORDER BY time DESC;";

        Cursor test =  database.rawQuery(query, null);
        return test;
    }
}