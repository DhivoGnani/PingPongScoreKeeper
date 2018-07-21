package com.example.android.pingpongscorekeeper.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class PingPongContract {

    private PingPongContract() {}

    public static final String CONTENT_AUTHORITY = "com.example.android.pingpongscorekeeper";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MATCH = "matches";

    public static class PingPongMatch implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MATCH);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MATCH;

        public static final String TABLE_NAME = "PingPongMatch";

        public final static String _ID = BaseColumns._ID;

        public static final String COLUMN_PLAYER_ONE_NAME_TITLE = "PlayerOneName";
        public static final String COLUMN_PLAYER_TWO_NAME_TITLE = "PlayerTwoName";
        public static final String COLUMN_PLAYER_ONE_SETS_WON_TITLE = "PlayerOneSetsWon";
        public static final String COLUMN_PLAYER_TWO_SETS_WON_TITLE = "PlayerTwoSetsWon";
    }
}