package me.dhivo.android.pingpongmatchtracker.adapters

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.TextView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_players_details.view.*
import me.dhivo.android.pingpongmatchtracker.R
import me.dhivo.android.pingpongmatchtracker.data.PingPongContract
import me.dhivo.android.pingpongmatchtracker.helpers.ImageHelper


class PlayersAdapter(context: Context, c: Cursor?) : CursorAdapter(context, c, 0) {

    override fun newView(context: Context, cursor: Cursor, parent: ViewGroup): View {
        return LayoutInflater.from(context).inflate(R.layout.item_players_details, parent, false)
    }

    override fun bindView(view: View, context: Context, cursor: Cursor) {
        val playerNameView = view.findViewById<TextView>(R.id.usernameTextView)

        val setNumberCol = cursor.getColumnIndex(PingPongContract.Player.COLUMN_NAME_TITLE)
        val setProfileCol = cursor.getColumnIndex(PingPongContract.Player.COLUMN_PROFILE_PICTURE_TITLE)

        if(cursor.getString(setProfileCol) != null) {
            var uri: Uri= Uri.parse(cursor.getString(setProfileCol))
            var degrees = ImageHelper.getOrientation(context.contentResolver, uri)
            Picasso.get().load(uri).resize(1000, 1000)
            .centerCrop().rotate(degrees.toFloat()).into(view.profile_image)

        } else {
            view.profile_image.setImageResource(R.drawable.default_profile)
        }
        playerNameView.text = cursor.getString(setNumberCol)

    }
}


