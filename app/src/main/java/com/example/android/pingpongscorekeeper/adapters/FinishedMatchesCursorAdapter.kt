package com.example.android.pingpongscorekeeper.adapters

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.TextView
import com.example.android.pingpongscorekeeper.R
import com.example.android.pingpongscorekeeper.activities.MatchActivity
import com.example.android.pingpongscorekeeper.data.PingPongContract
import com.example.android.pingpongscorekeeper.data.PingPongContract.PingPongMatch.*

class FinishedMatchesCursorAdapter(context: Context, c: Cursor?) : CursorAdapter(context, c, 0) {

    override fun newView(context: Context, cursor: Cursor, parent: ViewGroup): View {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
    }

    override fun bindView(view: View, context: Context, cursor: Cursor) {
        val playerOneNameView = view.findViewById<TextView>(R.id.player_one_name)
        val playerTwoNameView = view.findViewById<TextView>(R.id.player_two_name)
        val playerOneScoreView = view.findViewById<TextView>(R.id.player_one_score)
        val playerTwoScoreView = view.findViewById<TextView>(R.id.player_two_score)
        val gameEndTimeStamp = view.findViewById<TextView>(R.id.game_end_timestamp)

        val playerOneNameCol = cursor.getColumnIndex(COLUMN_PLAYER_ONE_NAME_TITLE)
        val playerTwoNameCol = cursor.getColumnIndex(COLUMN_PLAYER_TWO_NAME_TITLE)
        val playerOneScoreCol = cursor.getColumnIndex(COLUMN_PLAYER_ONE_SETS_WON_TITLE)
        val playerTwoScoreCol = cursor.getColumnIndex(COLUMN_PLAYER_TWO_SETS_WON_TITLE)
        val gameEndTimeLocal = cursor.getColumnIndex(COLUMN_GAME_TIME_DONE_LOCAL_TITLE)
        val matchIdCol = cursor.getColumnIndex(_ID)

        playerOneNameView.text = cursor.getString(playerOneNameCol)
        playerTwoNameView .text = cursor.getString(playerTwoNameCol)

        val playerOneSetsWon = cursor.getInt(playerOneScoreCol)
        val playerTwoSetsWon = cursor.getInt(playerTwoScoreCol)

        gameEndTimeStamp.text = cursor.getString(gameEndTimeLocal).substring(0, 16)

        playerOneScoreView.text = playerOneSetsWon.toString()
        playerTwoScoreView.text = playerTwoSetsWon.toString()

        view.setOnClickListener {
            val intent = Intent(context, MatchActivity::class.java)
            intent.data = PingPongContract.Set.CONTENT_URI
            val matchId = cursor.getLong(matchIdCol).toString()
            intent.putExtra("matchId", matchId)
            intent.putExtra("playerOneName", cursor.getString(playerOneNameCol))
            intent.putExtra("playerTwoName", cursor.getString(playerTwoNameCol))
            if (playerOneSetsWon > playerTwoSetsWon) {
                intent.putExtra("won", "p1")
            } else {
                intent.putExtra("won", "p2")
            }

            context.startActivity(intent)
        }

        playerTwoScoreView.setTypeface(
                Typeface.create(playerTwoScoreView.typeface, Typeface.NORMAL), Typeface.NORMAL
        )
        playerOneScoreView.setTypeface(
                Typeface.create(playerOneScoreView.typeface, Typeface.NORMAL), Typeface.NORMAL
        )

        if (playerOneSetsWon > playerTwoSetsWon) {
            playerOneScoreView.setTypeface(playerOneScoreView.typeface, Typeface.BOLD)
        } else {
            playerTwoScoreView.setTypeface(playerTwoScoreView.typeface, Typeface.BOLD)
        }
    }
}
