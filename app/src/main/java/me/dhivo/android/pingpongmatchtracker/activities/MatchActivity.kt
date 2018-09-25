package me.dhivo.android.pingpongmatchtracker.activities

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

    private lateinit var mCurrentUri: Uri
    private lateinit var adapter: MatchSetsAdapter
    private var matchId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match)

        mCurrentUri = intent.data
        matchId = intent.extras.getLong("matchId")

        val playerOneName: String = intent.extras.getString("playerOneName")
        val playerTwoName: String = intent.extras.getString("playerTwoName")

        val playerOneNameDisplay: TextView = findViewById(R.id.player_one_id)
        val playerTwoNameDisplay: TextView = findViewById(R.id.player_two_id)

        playerOneNameDisplay.text = playerOneName
        playerTwoNameDisplay.text = playerTwoName

        playerOneNameDisplay.setTypeface(Typeface.create(playerOneNameDisplay.typeface, Typeface.NORMAL), Typeface.NORMAL)
        playerTwoNameDisplay.setTypeface(Typeface.create(playerTwoNameDisplay.typeface, Typeface.NORMAL), Typeface.NORMAL)

        val won = intent.extras.getString("won") == "p1"

        if (won) playerOneNameDisplay.setTypeface(playerOneNameDisplay.typeface, Typeface.BOLD)
        else playerTwoNameDisplay.setTypeface(playerTwoNameDisplay.typeface, Typeface.BOLD)

        val list: ListView = findViewById(R.id.setlist)
        adapter = MatchSetsAdapter(this, null)

        list.adapter = adapter

        this.supportLoaderManager.initLoader(SET_LOADER, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        val selection = PingPongContract.Set.MATCH_ID + "=?"
        val selectionArgs = arrayOf(matchId.toString())

        return CursorLoader(this, mCurrentUri, null, selection, selectionArgs,
                SORTED_SETS)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor) {
        adapter.swapCursor(data)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        adapter.swapCursor(null)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
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
        }
        return super.onOptionsItemSelected(item)
    }

    companion object
    {
        private const val SET_LOADER = 0
    }
}
