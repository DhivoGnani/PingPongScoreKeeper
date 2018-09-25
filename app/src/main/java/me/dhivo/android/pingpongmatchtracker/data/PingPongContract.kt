package me.dhivo.android.pingpongmatchtracker.data

import android.content.ContentResolver
import android.net.Uri
import android.provider.BaseColumns

object PingPongContract {

    val CONTENT_AUTHORITY = "me.dhivo.android.pingpongmatchtracker"

    val BASE_CONTENT_URI = Uri.parse("content://$CONTENT_AUTHORITY")

    val PATH_MATCH = "matches"

    val PATH_SET = "sets"

    val PATH_PLAYER = "players"

    class PingPongMatch : BaseColumns {
        companion object {
            val CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MATCH)

            val CONTENT_LIST_TYPE =
                    ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MATCH

            val TABLE_NAME = "PingPongMatch"

            val _ID = BaseColumns._ID

            val COLUMN_PLAYER_ONE_ID_TITLE = "PlayerOneId"
            val COLUMN_PLAYER_TWO_ID_TITLE = "PlayerTwoId"
            val COLUMN_SERVING_PLAYER_ID_TITLE = "ServingPlayerId"
            val COLUMN_PLAYER_ONE_SETS_WON_TITLE = "PlayerOneSetsWon"
            val COLUMN_PLAYER_TWO_SETS_WON_TITLE = "PlayerTwoSetsWon"
            val COLUMN_GAME_TIME_DONE_TITLE = "GameFinishedDate"
            val COLUMN_GAME_TIME_DONE_LOCAL_TITLE = "datetime(" + PingPongMatch.COLUMN_GAME_TIME_DONE_TITLE + ", 'localtime')"
            val SORTED_GAME_TIME_DONE_LOCAL_DESC = "$COLUMN_GAME_TIME_DONE_TITLE DESC"

            val COLUMN_GAME_TIME_DONE_LOCAL_TITLE_ALIAS = "datetime(matchTable." + PingPongMatch.COLUMN_GAME_TIME_DONE_TITLE + ", 'localtime')"
        }
    }

    class Set : BaseColumns {
        companion object {
            val CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_SET)

            val CONTENT_LIST_TYPE =
                    ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SET

            val TABLE_NAME = "PingPongSet"

            val _ID = BaseColumns._ID

            // Foreign key
            val MATCH_ID = "MatchId"

            val SET_NUMBER = "SetNumber"

            val PLAYER_ONE_SCORE = "PlayerOneScore"

            val PLAYER_TWO_SCORE = "PlayerTwoScore"

            val SORTED_SETS = "$SET_NUMBER ASC"
        }
    }

    class Player : BaseColumns {
        companion object {
            val CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PLAYER)

            val CONTENT_LIST_TYPE =
                    ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PLAYER

            val TABLE_NAME = "Players"

            val _ID = BaseColumns._ID

            val COLUMN_NAME_TITLE = "Name"

            val COLUMN_PROFILE_PICTURE_TITLE = "ProfilePicture"
        }
    }
}