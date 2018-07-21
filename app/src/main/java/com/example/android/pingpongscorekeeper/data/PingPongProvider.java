package com.example.android.pingpongscorekeeper.data;

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

import com.example.android.pingpongscorekeeper.data.PingPongContract.PingPongMatch;

import static com.example.android.pingpongscorekeeper.data.PingPongContract.PingPongMatch.TABLE_NAME;

public class PingPongProvider extends ContentProvider {

    public static final String LOG_TAG = PingPongProvider.class.getSimpleName();

    private static final int MATCHES = 100;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(PingPongContract.CONTENT_AUTHORITY, PingPongContract.PATH_MATCH, MATCHES);
    }

    private PingPongDBHelper mDbHelper;

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

        int match = sUriMatcher.match(uri);
        switch (match) {
            case MATCHES:
                cursor = database.query(TABLE_NAME, projection, selection, selectionArgs, null,
                        null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int rowsDeleted;
        switch (match) {
            case MATCHES:
                rowsDeleted = database.delete(TABLE_NAME, s, strings);
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
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    private Uri insertPingPongMatch(Uri uri, ContentValues values) {
        String playerOneName = values.getAsString(PingPongMatch.COLUMN_PLAYER_ONE_NAME_TITLE);
        if (playerOneName == null) {
            throw new IllegalArgumentException("Player One requires a name");
        }

        String playerTwoName = values.getAsString(PingPongMatch.COLUMN_PLAYER_TWO_NAME_TITLE);
        if (playerTwoName == null) {
            throw new IllegalArgumentException("Player Two requires a name");
        }


        Integer playerOneSetsWon = values.getAsInteger(PingPongMatch.COLUMN_PLAYER_ONE_SETS_WON_TITLE);
        if (playerOneSetsWon == null) {
            throw new IllegalArgumentException("Player One Sets Won requires a number");
        }

        Integer playerTwoSetsWon = values.getAsInteger(PingPongMatch.COLUMN_PLAYER_TWO_SETS_WON_TITLE);
        if (playerTwoSetsWon == null) {
            throw new IllegalArgumentException("Player Twp Sets Won requires a number");
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(TABLE_NAME, null, values);

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
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return insertPingPongMatch(uri, contentValues);
    }
}