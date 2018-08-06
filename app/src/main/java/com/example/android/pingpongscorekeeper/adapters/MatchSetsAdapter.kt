package com.example.android.pingpongscorekeeper.adapters

import android.content.Context
import android.database.Cursor
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.TextView
import com.example.android.pingpongscorekeeper.R
import com.example.android.pingpongscorekeeper.data.PingPongContract.Set.*

class MatchSetsAdapter(context: Context, c: Cursor?)  : CursorAdapter(context, c, 0)  {

    override fun newView(context: Context, cursor: Cursor, parent: ViewGroup): View {
        return LayoutInflater.from(context).inflate(R.layout.details_item, parent, false)
    }

    override fun bindView(view: View, context: Context, cursor: Cursor) {
        val setNumberView = view.findViewById<TextView>(R.id.set_n)
        val playerOneScoreView = view.findViewById<TextView>(R.id.player_one_num)
        val playerTwoScoreView = view.findViewById<TextView>(R.id.player_two_num)

        val playerOneSetScoreCol = cursor.getColumnIndex(PLAYER_ONE_SCORE)
        val playerTwoSetScoreCol = cursor.getColumnIndex(PLAYER_TWO_SCORE)
        val setNumberCol = cursor.getColumnIndex(SET_NUMBER)

        val playerOneScore = cursor.getInt(playerOneSetScoreCol)
        val playerTwoScore = cursor.getInt(playerTwoSetScoreCol)
        val setNumber = cursor.getInt(setNumberCol)

        setNumberView.text = "SET $setNumber"
        playerOneScoreView.text = playerOneScore.toString()
        playerTwoScoreView.text = playerTwoScore.toString()

        playerOneScoreView.setTypeface(
                Typeface.create(playerOneScoreView.typeface, Typeface.NORMAL), Typeface.NORMAL
        )
        playerTwoScoreView.setTypeface(
                Typeface.create(playerTwoScoreView.typeface, Typeface.NORMAL), Typeface.NORMAL
        )

        if (playerOneScore > playerTwoScore) {
            playerOneScoreView.setTypeface(playerOneScoreView.typeface, Typeface.BOLD)
        } else {
            playerTwoScoreView.setTypeface(playerTwoScoreView.typeface, Typeface.BOLD)
        }
    }
}
