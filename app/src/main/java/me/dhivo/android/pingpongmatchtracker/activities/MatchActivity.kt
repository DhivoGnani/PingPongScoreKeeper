package me.dhivo.android.pingpongmatchtracker.activities

import android.content.ContentUris
import android.content.Intent
import android.database.Cursor
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.LoaderManager.LoaderCallbacks
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import android.widget.TextView

import me.dhivo.android.pingpongmatchtracker.R
import me.dhivo.android.pingpongmatchtracker.adapters.MatchSetsAdapter
import me.dhivo.android.pingpongmatchtracker.data.PingPongContract

import me.dhivo.android.pingpongmatchtracker.data.PingPongContract.Set.SORTED_SETS

class MatchActivity : AppCompatActivity(), LoaderCallbacks<Cursor> {
    private var mCurrentUri: Uri? = null
    private var adapter: MatchSetsAdapter? = null
    private var matchId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match)

        mCurrentUri = intent.data
        matchId = intent.extras.getLong("matchId")

        val playerOneName = intent.extras.getString("playerOneName")
        val playerTwoName = intent.extras.getString("playerTwoName")

        val p1 = findViewById<TextView>(R.id.player_one_id)
        p1.text = playerOneName

        val p2 = findViewById<TextView>(R.id.player_two_id)
        p2.text = playerTwoName

        p1.setTypeface(Typeface.create(p1.typeface, Typeface.NORMAL), Typeface.NORMAL)
        p2.setTypeface(Typeface.create(p2.typeface, Typeface.NORMAL), Typeface.NORMAL)

        val won = intent.extras.getString("won") == "p1"

        if (won) p1.setTypeface(p1.typeface, Typeface.BOLD)
        else p2.setTypeface(p2.typeface, Typeface.BOLD)

        val list = findViewById<ListView>(R.id.setlist)
        adapter = MatchSetsAdapter(this, null)

        list.adapter = adapter

        this.supportLoaderManager.initLoader(SET_LOADER, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        val selection = PingPongContract.Set.MATCH_ID + "=?"
        val selectionArgs = arrayOf(matchId.toString())

        return CursorLoader(this, mCurrentUri!!, null, selection, selectionArgs,
                SORTED_SETS)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor) {
        adapter?.swapCursor(data)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        adapter?.swapCursor(null)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.game_details_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_close -> {
                finish()
                return true
            }
            R.id.delete_a -> deleteMatch()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteMatch() {
        val mId = ContentUris.withAppendedId(PingPongContract.PingPongMatch.CONTENT_URI, matchId)
        contentResolver.delete(mId, null, null)
        finish()
    }

    companion object
    {
        private const val SET_LOADER = 0
    }
}
