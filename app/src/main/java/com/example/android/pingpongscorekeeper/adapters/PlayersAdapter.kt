package com.example.android.pingpongscorekeeper.adapters

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.TextView
import com.example.android.pingpongscorekeeper.R
import com.example.android.pingpongscorekeeper.R.id.profile_image
import com.example.android.pingpongscorekeeper.data.PingPongContract
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.players_list_item.view.*

class PlayersAdapter(context: Context, c: Cursor?) : CursorAdapter(context, c, 0) {

    override fun newView(context: Context, cursor: Cursor, parent: ViewGroup): View {
        return LayoutInflater.from(context).inflate(R.layout.players_list_item, parent, false)
    }

    override fun bindView(view: View, context: Context, cursor: Cursor) {
        val playerNameView = view.findViewById<TextView>(R.id.usernameTextView)

        val setNumberCol = cursor.getColumnIndex(PingPongContract.Player.COLUMN_NAME_TITLE)
        val setProfileCol = cursor.getColumnIndex(PingPongContract.Player.COLUMN_PROFILE_PICTURE_TITLE)

        if(cursor.getString(setProfileCol) != null) {
            val bitmap: Bitmap= MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(cursor.getString(setProfileCol)) )
            view.profile_image.setImageBitmap(bitmap)
        }
        playerNameView.text = cursor.getString(setNumberCol)

    }
}
