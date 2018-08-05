package com.example.android.pingpongscorekeeper.activities

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.android.pingpongscorekeeper.R
import com.example.android.pingpongscorekeeper.data.PingPongContract
import com.example.android.pingpongscorekeeper.handlers.PingPongAsyncHandler
import kotlinx.android.synthetic.main.activity_player_editor.*

class PlayerEditorActivity : AppCompatActivity() {

    var playerName: String? = null

    private val playerNameWatcher = object : TextWatcher {
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            playerName = s.toString()
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                       after: Int) {
        }

        override fun afterTextChanged(s: Editable) {}

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_editor)
        player_name_editor.addTextChangedListener(playerNameWatcher)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.new_player_menu, menu)
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
            R.id.action_done -> {
                addNewPlayer()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addNewPlayer() {
        if(playerName == null || playerName.isNullOrBlank()) {
            Toast.makeText(this, "Enter a name",
                    Toast.LENGTH_SHORT).show()
        }
        insertDummyPlayer(playerName!!, contentResolver)
        finish()
        Toast.makeText(this, "player saved",
                Toast.LENGTH_SHORT).show()
    }

    fun insertDummyPlayer(name: String, contentResolver: ContentResolver) {
        val values = ContentValues()
        values.put(PingPongContract.Player.COLUMN_NAME_TITLE, name)
        val pingPongAsyncHandler = PingPongAsyncHandler(contentResolver, null)
        pingPongAsyncHandler.startInsert(-1, null, PingPongContract.Player.CONTENT_URI, values)
    }
}
