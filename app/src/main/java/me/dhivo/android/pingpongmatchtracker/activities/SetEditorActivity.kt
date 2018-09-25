package me.dhivo.android.pingpongmatchtracker.activities

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast

import me.dhivo.android.pingpongmatchtracker.R
import me.dhivo.android.pingpongmatchtracker.adapters.SetAdapter
import me.dhivo.android.pingpongmatchtracker.components.PingPongSet
import me.dhivo.android.pingpongmatchtracker.data.PingPongContract
import me.dhivo.android.pingpongmatchtracker.validators.SetValidator

import java.util.ArrayList

class SetEditorActivity : AppCompatActivity() {

    private var sets: ArrayList<PingPongSet>? = null
    private var playerOneId: Long = 0
    private var playerTwoId: Long = 0
    private var servingPlayerId: Long = 0
    private var numOfPointsPerSet: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_editor)

        val playerOneName = intent.extras!!.getString("playerOneName")
        val playerTwoName = intent.extras!!.getString("playerTwoName")
        playerOneId = intent.extras!!.getLong("playerOneId")
        playerTwoId = intent.extras!!.getLong("playerTwoId")
        val numOfSets = Integer.valueOf(intent.extras!!.getString("numSets"))
        numOfPointsPerSet = Integer.valueOf(intent.extras!!.getString("numPointsPerSet"))
        val servingPlayer = intent.extras!!.getString("servingPlayer")

        servingPlayerId = if ("Player 1" == servingPlayer) playerOneId else playerTwoId

        val p1 = findViewById<TextView>(R.id.player_one_id)
        p1.text = playerOneName

        val p2 = findViewById<TextView>(R.id.player_two_id)
        p2.text = playerTwoName

        sets = createTempPingPongSets(numOfSets)

        val setList = findViewById<ListView>(R.id.setlist)

        val adapter = SetAdapter(this, 0, sets!!)

        setList.adapter = adapter
    }

    private fun createTempPingPongSets(numOfSets: Int): ArrayList<PingPongSet> {
        val pingPongSets = ArrayList<PingPongSet>()

        for (i in 0 until numOfSets) {
            pingPongSets.add(PingPongSet(i + 1, -1, -1))
        }

        return pingPongSets
    }

    private fun insertFinishedMatch(playerOneId: Long, playerTwoId: Long, numberOfSetsPlayerOneWon: Int, numberOfSetsPlayerTwoWon: Int, pingPongSets: ArrayList<PingPongSet>) {
        var values = ContentValues()
        values.put(PingPongContract.PingPongMatch.COLUMN_PLAYER_ONE_ID_TITLE, playerOneId)
        values.put(PingPongContract.PingPongMatch.COLUMN_PLAYER_TWO_ID_TITLE, playerTwoId)
        values.put(PingPongContract.PingPongMatch.COLUMN_PLAYER_ONE_SETS_WON_TITLE, numberOfSetsPlayerOneWon)
        values.put(PingPongContract.PingPongMatch.COLUMN_PLAYER_TWO_SETS_WON_TITLE, numberOfSetsPlayerTwoWon)
        values.put(PingPongContract.PingPongMatch.COLUMN_SERVING_PLAYER_ID_TITLE, servingPlayerId)

        val uri = contentResolver.insert(PingPongContract.PingPongMatch.CONTENT_URI, values)

        val id = java.lang.Long.valueOf(uri!!.lastPathSegment)

        for ((setNumber, playerOneScore, playerTwoScore) in pingPongSets) {
            values = ContentValues()
            values.put(PingPongContract.Set.MATCH_ID, id.toString() + "")
            values.put(PingPongContract.Set.SET_NUMBER, setNumber)
            values.put(PingPongContract.Set.PLAYER_ONE_SCORE, playerOneScore)
            values.put(PingPongContract.Set.PLAYER_TWO_SCORE, playerTwoScore)

            contentResolver.insert(PingPongContract.Set.CONTENT_URI, values)
        }
    }

    fun done(): Boolean {
        var numOfPlayerOneWon = 0
        var numOfPlayerTwoWon = 0

        for (i in sets!!.indices) {
            if (sets!![i].playerOneScore == -1 || sets!![i].playerTwoScore == -1) {
                Toast.makeText(this, "Add set values",
                        Toast.LENGTH_SHORT).show()
                return false
            }
            if (sets!![i].playerOneScore > sets!![i].playerTwoScore) {
                numOfPlayerOneWon++
            } else {
                numOfPlayerTwoWon++
            }
        }

        val isSetsValid = SetValidator.validateSets(sets!!, numOfPointsPerSet)
        if (!isSetsValid) {
            Toast.makeText(this, "Set points are invalid",
                    Toast.LENGTH_SHORT).show()
            return false
        }
        insertFinishedMatch(playerOneId, playerTwoId, numOfPlayerOneWon, numOfPlayerTwoWon, sets!!)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.new_player_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.settings) {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
            return true
        } else if (item.itemId == R.id.action_close) {
            finish()
            return true
        } else if (item.itemId == R.id.action_done) {
            if (done()) {
                finish()
                return true
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
