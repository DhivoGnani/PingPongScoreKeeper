package me.dhivo.android.pingpongmatchtracker.data

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.util.Log

import me.dhivo.android.pingpongmatchtracker.data.PingPongContract.PingPongMatch
import me.dhivo.android.pingpongmatchtracker.data.PingPongContract.Player
import me.dhivo.android.pingpongmatchtracker.data.PingPongContract.Set

import me.dhivo.android.pingpongmatchtracker.data.PingPongContract.PingPongMatch.COLUMN_GAME_TIME_DONE_LOCAL_TITLE_ALIAS
import me.dhivo.android.pingpongmatchtracker.data.PingPongContract.PingPongMatch.COLUMN_PLAYER_ONE_SETS_WON_TITLE
import me.dhivo.android.pingpongmatchtracker.data.PingPongContract.PingPongMatch.COLUMN_PLAYER_TWO_SETS_WON_TITLE


class PingPongProvider : ContentProvider() {
    private var mDbHelper: PingPongDBHelper? = null

    internal val pingPongMatchTableName = "matchTable"
    internal val playerOneTableName = "playerOneTable"
    internal val playerTwoTableName = "playerTwoTable"
    internal val servingPlayerTableName = "servingPlayerTable"

    val matchesWithPlayerInfo: Cursor
        get() {
            val database = mDbHelper!!.readableDatabase

            val query = ("SELECT " + pingPongMatchTableName + "." + PingPongMatch._ID + ","
                    + pingPongMatchTableName + "." + COLUMN_PLAYER_ONE_SETS_WON_TITLE + ","
                    + pingPongMatchTableName + "." + COLUMN_PLAYER_TWO_SETS_WON_TITLE + ","
                    + COLUMN_GAME_TIME_DONE_LOCAL_TITLE_ALIAS + "time,"
                    + playerOneTableName + "." + Player.COLUMN_NAME_TITLE + " PlayerOneName,"
                    + playerOneTableName + "." + Player.COLUMN_PROFILE_PICTURE_TITLE + " PlayerOnePicture,"
                    + playerTwoTableName + "." + Player.COLUMN_NAME_TITLE + " PlayerTwoName,"
                    + playerTwoTableName + "." + Player.COLUMN_PROFILE_PICTURE_TITLE + " PlayerTwoPicture,"
                    + servingPlayerTableName + "." + Player.COLUMN_NAME_TITLE + " ServingPlayerName"
                    + " FROM " + PingPongMatch.TABLE_NAME + " " + pingPongMatchTableName
                    + " INNER JOIN " + Player.TABLE_NAME + " " + playerOneTableName + " ON "
                    + pingPongMatchTableName + "." + PingPongMatch.COLUMN_PLAYER_ONE_ID_TITLE + "=" + playerOneTableName + "." + Player._ID
                    + " INNER JOIN " + Player.TABLE_NAME + " " + playerTwoTableName + " ON "
                    + pingPongMatchTableName + "." + PingPongMatch.COLUMN_PLAYER_TWO_ID_TITLE + "=" + playerTwoTableName + "." + Player._ID
                    + " INNER JOIN " + Player.TABLE_NAME + " " + servingPlayerTableName + " ON "
                    + pingPongMatchTableName + "." + PingPongMatch.COLUMN_SERVING_PLAYER_ID_TITLE + "=" + servingPlayerTableName + "." + Player._ID
                    + " ORDER BY time DESC;")

            return database.rawQuery(query, null)
        }

    override fun onCreate(): Boolean {
        mDbHelper = PingPongDBHelper(context)
        return true
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?,
                       sortOrder: String?): Cursor? {
        var selection = selection
        var selectionArgs = selectionArgs
        val database = mDbHelper!!.readableDatabase
        val cursor: Cursor
        val match = sUriMatcher.match(uri)

        when (match) {
            MATCHES ->
                //                cursor = database.query(PingPongMatch.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                cursor = matchesWithPlayerInfo
            MATCHES_ID -> {
                selection = PingPongMatch._ID + "=?"
                selectionArgs = arrayOf(ContentUris.parseId(uri).toString())
                cursor = database.query(PingPongMatch.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder)
            }
            SETS -> cursor = database.query(Set.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder)
            PLAYERS -> cursor = database.query(Player.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder)
            PLAYERS_ID -> {
                selection = Player._ID + "=?"
                selectionArgs = arrayOf(ContentUris.parseId(uri).toString())
                cursor = database.query(Player.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder)
            }
            else -> throw IllegalStateException("Unknown URI $uri with match $match")
        }
        cursor.setNotificationUri(context!!.contentResolver, uri)

        return cursor
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        var selection = selection
        var selectionArgs = selectionArgs
        val database = mDbHelper!!.writableDatabase
        val match = sUriMatcher.match(uri)
        val rowsDeleted: Int
        when (match) {
            MATCHES -> rowsDeleted = database.delete(PingPongMatch.TABLE_NAME, selection, selectionArgs)
            MATCHES_ID -> {
                selection = PingPongMatch._ID + "=?"
                selectionArgs = arrayOf(ContentUris.parseId(uri).toString())
                rowsDeleted = database.delete(PingPongMatch.TABLE_NAME, selection, selectionArgs)
            }
            PLAYERS -> rowsDeleted = database.delete(Player.TABLE_NAME, selection, selectionArgs)
            PLAYERS_ID -> {
                selection = Player._ID + "=?"
                selectionArgs = arrayOf(ContentUris.parseId(uri).toString())
                rowsDeleted = database.delete(Player.TABLE_NAME, selection, selectionArgs)
            }
            else -> throw IllegalArgumentException("Cannot query unknown URI $uri")
        }

        if (rowsDeleted != 0) {
            context!!.contentResolver.notifyChange(uri, null)
        }

        return rowsDeleted
    }

    override fun update(uri: Uri, contentValues: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        var selection = selection
        var selectionArgs = selectionArgs
        val match = sUriMatcher.match(uri)
        val rowsUpdated: Int
        val database = mDbHelper!!.writableDatabase

        when (match) {
            PLAYERS_ID -> {
                selection = Player._ID + "=?"
                selectionArgs = arrayOf(ContentUris.parseId(uri).toString())
                rowsUpdated = database.update(Player.TABLE_NAME, contentValues, selection, selectionArgs)
                context!!.contentResolver.notifyChange(PingPongMatch.CONTENT_URI, null)
            }
            else -> throw IllegalArgumentException("Cannot query unknown URI $uri")
        }

        context!!.contentResolver.notifyChange(uri, null)
        return rowsUpdated
    }

    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? {
        val match = sUriMatcher.match(uri)

        when (match) {
            MATCHES -> return insertPingPongMatch(uri, contentValues!!)
            SETS -> return insertPingPongSet(uri, contentValues)
            PLAYERS -> return insertPingPongPlayer(uri, contentValues)
            else -> throw IllegalArgumentException("Cannot query unknown URI $uri")
        }
    }

    private fun insertPingPongSet(uri: Uri, contentValues: ContentValues?): Uri? {
        val database = mDbHelper!!.writableDatabase

        val id = database.insert(Set.TABLE_NAME, null, contentValues)

        if (id.toInt() == -1) {
            Log.e(LOG_TAG, "Failed to insert row for $uri")
            return null
        }

        context!!.contentResolver.notifyChange(uri, null)

        return ContentUris.withAppendedId(uri, id)
    }

    private fun insertPingPongPlayer(uri: Uri, values: ContentValues?): Uri? {
        val database = mDbHelper!!.writableDatabase

        val id = database.insert(Player.TABLE_NAME, null, values)

        if (id.toInt() == -1) {
            Log.e(LOG_TAG, "Failed to insert row for $uri")
            return null
        }

        context!!.contentResolver.notifyChange(uri, null)

        return ContentUris.withAppendedId(uri, id)
    }

    private fun insertPingPongMatch(uri: Uri, values: ContentValues): Uri? {
        val playerOneName = values.getAsString(PingPongMatch.COLUMN_PLAYER_ONE_ID_TITLE)
                ?: throw IllegalArgumentException("Player One requires a name")

        val playerTwoName = values.getAsString(PingPongMatch.COLUMN_PLAYER_TWO_ID_TITLE)
                ?: throw IllegalArgumentException("Player Two requires a name")


        val playerOneSetsWon = values.getAsInteger(COLUMN_PLAYER_ONE_SETS_WON_TITLE)
                ?: throw IllegalArgumentException("Player One Sets Won requires a number")

        val playerTwoSetsWon = values.getAsInteger(PingPongMatch.COLUMN_PLAYER_TWO_SETS_WON_TITLE)
                ?: throw IllegalArgumentException("Player Twp Sets Won requires a number")

        val database = mDbHelper!!.writableDatabase

        val id = database.insert(PingPongMatch.TABLE_NAME, null, values)

        if (id.toInt() == -1) {
            Log.e(LOG_TAG, "Failed to insert row for $uri")
            return null
        }

        context!!.contentResolver.notifyChange(uri, null)

        return ContentUris.withAppendedId(uri, id)
    }

    override fun getType(uri: Uri): String? {
        val match = sUriMatcher.match(uri)
        when (match) {
            MATCHES -> return PingPongMatch.CONTENT_LIST_TYPE
            SETS -> return PingPongContract.Set.CONTENT_LIST_TYPE
            PLAYERS -> return PingPongContract.Player.CONTENT_LIST_TYPE
            else -> throw IllegalStateException("Unknown URI $uri with match $match")
        }
    }

    companion object {

        val LOG_TAG = PingPongProvider::class.java.simpleName

        private val MATCHES = 100
        private val MATCHES_ID = 102
        private val SETS = 101
        private val PLAYERS = 103
        private val PLAYERS_ID = 104

        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            sUriMatcher.addURI(PingPongContract.CONTENT_AUTHORITY, PingPongContract.PATH_MATCH, MATCHES)
            sUriMatcher.addURI(PingPongContract.CONTENT_AUTHORITY, PingPongContract.PATH_MATCH + "/#", MATCHES_ID)
            sUriMatcher.addURI(PingPongContract.CONTENT_AUTHORITY, PingPongContract.PATH_SET, SETS)
            sUriMatcher.addURI(PingPongContract.CONTENT_AUTHORITY, PingPongContract.PATH_PLAYER, PLAYERS)
            sUriMatcher.addURI(PingPongContract.CONTENT_AUTHORITY, PingPongContract.PATH_PLAYER + "/#", PLAYERS_ID)
        }
    }
}