package me.dhivo.android.pingpongmatchtracker.adapters

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Typeface
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.TextView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item.view.*
import me.dhivo.android.pingpongmatchtracker.R
import me.dhivo.android.pingpongmatchtracker.activities.MatchActivity
import me.dhivo.android.pingpongmatchtracker.data.PingPongContract
import me.dhivo.android.pingpongmatchtracker.data.PingPongContract.PingPongMatch.*
import me.dhivo.android.pingpongmatchtracker.helpers.ImageHelper

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

        val playerOneNameCol = cursor.getColumnIndex("PlayerOneName")
        val playerTwoNameCol = cursor.getColumnIndex("PlayerTwoName")
        val playerOneProfilePictureCol = cursor.getColumnIndex("PlayerOnePicture")
        val playerTwoProfilePictureCol = cursor.getColumnIndex("PlayerTwoPicture")

        if(cursor.getString(playerOneProfilePictureCol) != null) {
            var uri: Uri = Uri.parse(cursor.getString(playerOneProfilePictureCol))
            var degrees = ImageHelper.getOrientation(context.contentResolver, uri)
            Picasso.get().load(uri).resize(1000, 1000)
                    .centerCrop().rotate(degrees.toFloat()).into(view.profile_image_playerone)
        } else {
            view.profile_image_playerone.setImageResource(R.drawable.default_profile)
        }

        if(cursor.getString(playerTwoProfilePictureCol) != null) {
            var uri: Uri = Uri.parse(cursor.getString(playerTwoProfilePictureCol))
            var degrees = ImageHelper.getOrientation(context.contentResolver, uri)
            Picasso.get().load(uri).resize(1000, 1000)
                    .centerCrop().rotate(degrees.toFloat()).into(view.profile_image_playertwo)
        } else {
            view.profile_image_playertwo.setImageResource(R.drawable.default_profile)
        }

        val servingPlayerNameCol = cursor.getColumnIndex("ServingPlayerName");
        val playerOneScoreCol = cursor.getColumnIndex(COLUMN_PLAYER_ONE_SETS_WON_TITLE)
        val playerTwoScoreCol = cursor.getColumnIndex(COLUMN_PLAYER_TWO_SETS_WON_TITLE)
        val gameEndTimeLocal = cursor.getColumnIndex("time")
        val matchIdCol = cursor.getColumnIndex(_ID)

        val servingPlayerName = cursor.getString(servingPlayerNameCol)
        val playerOneName: String  = cursor.getString(playerOneNameCol)
        val playerTwoName: String = cursor.getString(playerTwoNameCol)

        val matchId =  cursor.getLong(matchIdCol);

        playerOneNameView.text = if(playerOneName == servingPlayerName) "$playerOneName *" else playerOneName
        playerTwoNameView .text = if(playerTwoName == servingPlayerName) "$playerTwoName *" else playerTwoName

        val playerOneSetsWon = cursor.getInt(playerOneScoreCol)
        val playerTwoSetsWon = cursor.getInt(playerTwoScoreCol)

        gameEndTimeStamp.text = cursor.getString(gameEndTimeLocal).substring(0, 16)

        playerOneScoreView.text = playerOneSetsWon.toString()
        playerTwoScoreView.text = playerTwoSetsWon.toString()


        view.setOnClickListener {
            val intent = Intent(context, MatchActivity::class.java)
            intent.data = PingPongContract.Set.CONTENT_URI
            intent.putExtra("matchId", matchId)
            intent.putExtra("playerOneName", playerOneName)
            intent.putExtra("playerTwoName", playerTwoName)
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
