package me.dhivo.android.pingpongscorekeeper.adapters

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.TextView
import me.dhivo.android.pingpongscorekeeper.data.PingPongContract
import kotlinx.android.synthetic.main.players_list_item.view.*
import me.dhivo.android.pingpongscorekeeper.R


class PlayersAdapter(context: Context, c: Cursor?) : CursorAdapter(context, c, 0) {

    override fun newView(context: Context, cursor: Cursor, parent: ViewGroup): View {
        return LayoutInflater.from(context).inflate(R.layout.players_list_item, parent, false)
    }

    override fun bindView(view: View, context: Context, cursor: Cursor) {
        val playerNameView = view.findViewById<TextView>(R.id.usernameTextView)

        val setNumberCol = cursor.getColumnIndex(PingPongContract.Player.COLUMN_NAME_TITLE)
        val setProfileCol = cursor.getColumnIndex(PingPongContract.Player.COLUMN_PROFILE_PICTURE_TITLE)

        if(cursor.getString(setProfileCol) != null) {
            val myTask = Task1()
            myTask.context = context
            myTask.view = view
            myTask.uri = Uri.parse(cursor.getString(setProfileCol))
            myTask.execute()

        } else {
            view.profile_image.setImageResource(R.drawable.default_profile)
        }
        playerNameView.text = cursor.getString(setNumberCol)

    }

    internal inner class Task1 : AsyncTask<Void, Void, Bitmap>() {

        var context: Context? = null
        var uri: Uri? = null;
        var view: View? = null;

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg arg0: Void): Bitmap? {
            //Record method
            val bitmap: Bitmap= MediaStore.Images.Media.getBitmap(context?.contentResolver, uri )
            return bitmap
        }

        override fun onPostExecute(result: Bitmap?) {
            // TODO: needs to be fixed
            var scaled: Bitmap = Bitmap.createScaledBitmap(result, 100, 100, false)
            view?.profile_image?.setImageBitmap(scaled)

        }
    }
}


