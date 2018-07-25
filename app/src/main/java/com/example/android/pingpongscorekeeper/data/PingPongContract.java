package com.example.android.pingpongscorekeeper.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class PingPongContract {

    private PingPongContract() {}

    public static final String CONTENT_AUTHORITY = "com.example.android.pingpongscorekeeper";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MATCH = "matches";

    public static final String PATH_SET = "sets";

    public static final String PATH_PLAYER = "players";

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
        public static final String COLUMN_GAME_TIME_DONE_TITLE = "GameFinishedDate";
        public static final String COLUMN_GAME_TIME_DONE_LOCAL_TITLE
                = "datetime(" + PingPongMatch.COLUMN_GAME_TIME_DONE_TITLE +", 'localtime')";
        public static final String SORTED_GAME_TIME_DONE_LOCAL_DESC
                = COLUMN_GAME_TIME_DONE_TITLE + " DESC";
    }

    public static class Set implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_SET);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SET;

        public static final String TABLE_NAME = "PingPongSet";

        public final static String _ID = BaseColumns._ID;

        // Foreign key
        public final static String MATCH_ID = "MatchId";

        public final static String SET_NUMBER = "SetNumber";

        public final static String PLAYER_ONE_SCORE = "PlayerOneScore";

        public final static String PLAYER_TWO_SCORE = "PlayerTwoScore";

        public static final String SORTED_SETS = SET_NUMBER + " ASC";
    }

    public static class Player implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PLAYER);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PLAYER;

        public static final String TABLE_NAME = "Players";

        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_NAME_TITLE = "Name";

        public final static String COLUMN_PROFILE_PICTURE_TITLE = "ProfilePicture";
    }
}