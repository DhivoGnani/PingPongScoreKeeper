package me.dhivo.android.pingpongmatchtracker.fragments

import android.content.ContentUris
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView

import me.dhivo.android.pingpongmatchtracker.R
import me.dhivo.android.pingpongmatchtracker.activities.PlayerEditorActivity
import me.dhivo.android.pingpongmatchtracker.adapters.PlayersAdapter
import me.dhivo.android.pingpongmatchtracker.data.PingPongContract

class PlayersListFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor> {

    private lateinit var playersAdapter: PlayersAdapter
    private lateinit var playersListView: ListView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_players, container, false)

        playersListView = rootView.findViewById(R.id.player_list)

        playersAdapter = PlayersAdapter(activity!!, null)

        playersListView.adapter = playersAdapter

        this.loaderManager.initLoader(PING_PONG_LOADER, null, this)

        rootView.findViewById<View>(R.id.fab).setOnClickListener {
            val intent = Intent(activity, PlayerEditorActivity::class.java)
            startActivity(intent)
        }

        playersListView.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, id ->
            val intent = Intent(activity, PlayerEditorActivity::class.java)

            val currentPetUri = ContentUris.withAppendedId(PingPongContract.Player.CONTENT_URI, id)

            intent.data = currentPetUri
            startActivity(intent)
        }
        return rootView
    }


    override fun onCreateLoader(i: Int, bundle: Bundle?): Loader<Cursor> {

        return CursorLoader(activity!!, PingPongContract.Player.CONTENT_URI, null,
                null, null, null)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, cursor: Cursor) {
        playersAdapter.swapCursor(cursor)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        playersAdapter.swapCursor(null)
    }

    companion object {
        private const val PING_PONG_LOADER = 0
    }
}
