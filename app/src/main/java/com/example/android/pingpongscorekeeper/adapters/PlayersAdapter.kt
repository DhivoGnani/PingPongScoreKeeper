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
import com.example.android.pingpongscorekeeper.data.PingPongContract
import com.example.android.pingpongscorekeeper.data.PingPongContract.Set.*

class PlayersAdapter(context: Context, c: Cursor?) : CursorAdapter(context, c, 0) {

    override fun newView(context: Context, cursor: Cursor, parent: ViewGroup): View {
        return LayoutInflater.from(context).inflate(R.layout.players_list_item, parent, false)
    }

    override fun bindView(view: View, context: Context, cursor: Cursor) {
        val playerNameView = view.findViewById<TextView>(R.id.usernameTextView)

        val setNumberCol = cursor.getColumnIndex(PingPongContract.Player.COLUMN_NAME_TITLE)

        playerNameView.text = cursor.getString(setNumberCol)
    }
}
