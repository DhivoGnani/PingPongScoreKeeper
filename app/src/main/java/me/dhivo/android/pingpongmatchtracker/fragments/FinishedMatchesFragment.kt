package me.dhivo.android.pingpongmatchtracker.fragments

import android.database.Cursor
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView

import me.dhivo.android.pingpongmatchtracker.R
import me.dhivo.android.pingpongmatchtracker.adapters.FinishedMatchesCursorAdapter

import me.dhivo.android.pingpongmatchtracker.data.PingPongContract.PingPongMatch.COLUMN_GAME_TIME_DONE_LOCAL_TITLE
import me.dhivo.android.pingpongmatchtracker.data.PingPongContract.PingPongMatch.COLUMN_PLAYER_ONE_ID_TITLE
import me.dhivo.android.pingpongmatchtracker.data.PingPongContract.PingPongMatch.COLUMN_PLAYER_ONE_SETS_WON_TITLE
import me.dhivo.android.pingpongmatchtracker.data.PingPongContract.PingPongMatch.COLUMN_PLAYER_TWO_ID_TITLE
import me.dhivo.android.pingpongmatchtracker.data.PingPongContract.PingPongMatch.COLUMN_PLAYER_TWO_SETS_WON_TITLE
import me.dhivo.android.pingpongmatchtracker.data.PingPongContract.PingPongMatch.CONTENT_URI
import me.dhivo.android.pingpongmatchtracker.data.PingPongContract.PingPongMatch.SORTED_GAME_TIME_DONE_LOCAL_DESC
import me.dhivo.android.pingpongmatchtracker.data.PingPongContract.PingPongMatch._ID

class FinishedMatchesFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor> {

    internal lateinit var finishedMatchesCursorAdapter: FinishedMatchesCursorAdapter
    private var finishedMatchesListView: ListView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_finished_matches, container, false)

        finishedMatchesListView = rootView.findViewById(R.id.list)

        finishedMatchesCursorAdapter = FinishedMatchesCursorAdapter(activity!!, null)
        finishedMatchesListView!!.adapter = finishedMatchesCursorAdapter

        this.loaderManager.initLoader(PING_PONG_LOADER, null, this)

        val emptyView = rootView.findViewById<View>(R.id.empty_view)
        finishedMatchesListView!!.emptyView = emptyView

        return rootView
    }


    override fun onCreateLoader(i: Int, bundle: Bundle?): Loader<Cursor> {
        val projection = arrayOf(_ID, COLUMN_PLAYER_ONE_ID_TITLE, COLUMN_PLAYER_TWO_ID_TITLE, COLUMN_PLAYER_ONE_SETS_WON_TITLE, COLUMN_PLAYER_TWO_SETS_WON_TITLE, COLUMN_GAME_TIME_DONE_LOCAL_TITLE)

        return CursorLoader(activity!!, CONTENT_URI, projection, null, null,
                SORTED_GAME_TIME_DONE_LOCAL_DESC)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, cursor: Cursor) {
        finishedMatchesCursorAdapter.swapCursor(cursor)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        finishedMatchesCursorAdapter.swapCursor(null)
    }

    companion object {

        private val PING_PONG_LOADER = 0
    }
}
