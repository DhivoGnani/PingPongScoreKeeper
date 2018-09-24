package me.dhivo.android.pingpongmatchtracker.helpers;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;

import me.dhivo.android.pingpongmatchtracker.components.PingPongSet;
import me.dhivo.android.pingpongmatchtracker.data.PingPongContract;
import me.dhivo.android.pingpongmatchtracker.handlers.PingPongAsyncHandler;

import java.util.ArrayList;

public class DummyDataHelper {

    private static final String testPlayerOneName = "Test Player 1";
    private static final String testPlayerTwoName = "Test Player 2";

    public static void insertDummyMatchData(ContentResolver contentResolver)
    {
        PingPongAsyncHandler.AsyncQueryListener listener = new PingPongAsyncHandler.AsyncQueryListener() {
            @Override
            public void onInsertComplete(int token, Object cookie, Uri uri) {
                final long matchId = Long.valueOf(uri.getLastPathSegment());
                insertDummySetData(matchId, (ContentResolver) cookie);
            }
        };

        PingPongAsyncHandler pingPongAsyncHandler = new PingPongAsyncHandler(contentResolver, listener);

        final ContentValues values = new ContentValues();
        values.put(PingPongContract.PingPongMatch.COLUMN_PLAYER_ONE_ID_TITLE, testPlayerOneName);
        values.put(PingPongContract.PingPongMatch.COLUMN_PLAYER_TWO_ID_TITLE, testPlayerTwoName);
        values.put(PingPongContract.PingPongMatch.COLUMN_PLAYER_ONE_SETS_WON_TITLE, 1);
        values.put(PingPongContract.PingPongMatch.COLUMN_PLAYER_TWO_SETS_WON_TITLE, 2);


        pingPongAsyncHandler
                .startInsert(-1 ,contentResolver, PingPongContract.PingPongMatch.CONTENT_URI, values);
    }

    private static void insertDummySetData(long matchId, ContentResolver contentResolver) {
        final ArrayList<PingPongSet> pingPongSets = new ArrayList<>();
        pingPongSets.add(new PingPongSet(1, 2, 11) );
        pingPongSets.add(new PingPongSet(1, 11, 8) );
        pingPongSets.add(new PingPongSet(1, 9, 11) );

        for(final PingPongSet set: pingPongSets)
        {
            final ContentValues values = new ContentValues();
            values.put(PingPongContract.Set.MATCH_ID, matchId);
            values.put(PingPongContract.Set.SET_NUMBER, set.getSetNumber());
            values.put(PingPongContract.Set.PLAYER_ONE_SCORE,  set.getPlayerOneScore());
            values.put(PingPongContract.Set.PLAYER_TWO_SCORE, set.getPlayerTwoScore());

            PingPongAsyncHandler pingPongAsyncHandler = new PingPongAsyncHandler(contentResolver, null);
            pingPongAsyncHandler.startInsert(-1, null, PingPongContract.Set.CONTENT_URI, values);
        }
    }

    public static void insertDummyPlayer(String name, ContentResolver contentResolver)
    {
        ContentValues values = new ContentValues();
        values.put(PingPongContract.Player.COLUMN_NAME_TITLE, name);
        PingPongAsyncHandler pingPongAsyncHandler = new PingPongAsyncHandler(contentResolver, null);
        pingPongAsyncHandler.startInsert(-1, null, PingPongContract.Player.CONTENT_URI, values);
    }
}
