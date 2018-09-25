package me.dhivo.android.pingpongmatchtracker.activities

import android.content.ContentValues
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.speech.tts.TextToSpeech
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import me.dhivo.android.pingpongmatchtracker.R
import me.dhivo.android.pingpongmatchtracker.components.PingPongGame
import me.dhivo.android.pingpongmatchtracker.components.PingPongPlayer
import me.dhivo.android.pingpongmatchtracker.constants.Audio
import me.dhivo.android.pingpongmatchtracker.data.PingPongContract
import java.util.*

class ScoreKeeperActivity : AppCompatActivity() {
    private lateinit var pingPongGame: PingPongGame
    private lateinit var textToSpeech: TextToSpeech
    private val rand = Random()
    private lateinit var playerOneNameDisplay: EditText
    private lateinit  var playerTwoNameDisplay: EditText
    private lateinit  var reset: Button
    private var servingPlayerId: Long = 0

    private val textToSpeechListener = TextToSpeech.OnInitListener { status ->
        if (status == TextToSpeech.SUCCESS) {
            val result = textToSpeech.setLanguage(Locale.UK)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported")
            }

        } else {
            Log.e("TTS", "Initilization Failed!")
        }
    }

    // TODO: Remove duplicate code
    private val playerOneNameChanged = object : TextWatcher {

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            pingPongGame.playerOneName = playerOneNameDisplay.text.toString()

        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                       after: Int) {
        }

        override fun afterTextChanged(s: Editable) {}

    }

    // TODO: Remove duplicate code
    private val playerTwoNameChanged = object : TextWatcher {

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            pingPongGame.playerTwoName = playerTwoNameDisplay.text.toString()
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                       after: Int) {
        }

        override fun afterTextChanged(s: Editable) {}
    }

    private val randomEncouragingWord: String
        get() {
            val Low = 0
            val High = Audio.congratulatingWords.size
            val Result = rand.nextInt(High - Low) + Low
            return Audio.congratulatingWords[Result]
        }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                return true
            }
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val prepared: Boolean = super.onPrepareOptionsMenu(menu)
        menu?.findItem(R.id.action_close)?.isVisible = false
        return prepared
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score_keeper)

        // TODO: Find a better way to get necessary data
        val playerOneName = intent.extras.getString("playerOneName")
        val playerTwoName = intent.extras.getString("playerTwoName")
        val numSets = Integer.valueOf(intent.extras.getString("numSets"))
        val numPointsPerSet = Integer.valueOf(intent.extras.getString("numPointsPerSet"))
        val servingPlayer = intent.extras.getString("servingPlayer")
        val playerOneId = intent.extras.getLong("playerOneId")
        val playerTwoId = intent.extras.getLong("playerTwoId")

        servingPlayerId = if ("Player 1" == servingPlayer) playerOneId else playerTwoId

        pingPongGame = PingPongGame(playerOneName, playerTwoName, numSets, numPointsPerSet, servingPlayer,
                playerOneId, playerTwoId)

        displayPlayersName()
        displayCurrentServingPlayer()

        textToSpeech = TextToSpeech(this, textToSpeechListener)

        playerOneNameDisplay = findViewById(R.id.player_one)
        playerOneNameDisplay.addTextChangedListener(playerOneNameChanged)

        playerTwoNameDisplay = findViewById(R.id.player_two)
        playerTwoNameDisplay.addTextChangedListener(playerTwoNameChanged)
        reset = findViewById(R.id.reset_button)
        reset.setOnClickListener { view -> resetOnClick(view) }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun speak(text: String) {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)

        // TODO: Remove hardcoded string
        val switchPref = sharedPref.getBoolean("audio_switch", false)

        if (!switchPref) return

        // TODO: Use a better voice for TextToSpeech (not robotic sounding voice)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        } else {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null)
        }
    }

    public override fun onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
        super.onDestroy()
    }

    private fun clearServingPlayer() {
        (findViewById<View>(R.id.player_one_serve) as TextView).text = ""
        (findViewById<View>(R.id.player_two_serve) as TextView).text = ""
    }

    private fun displayCurrentServingPlayer() {
        if (pingPongGame.currentServingPlayer == pingPongGame.playerOne) {
            (findViewById<View>(R.id.player_one_serve) as TextView).text = "Serve"
            (findViewById<View>(R.id.player_two_serve) as TextView).text = ""
        } else if (pingPongGame.currentServingPlayer == pingPongGame.playerTwo) {
            (findViewById<View>(R.id.player_two_serve) as TextView).text = "Serve"
            (findViewById<View>(R.id.player_one_serve) as TextView).text = ""
        }
    }

    private fun displayPlayersName() {
        (findViewById<View>(R.id.player_one) as TextView).text = pingPongGame.playerOne.name
        (findViewById<View>(R.id.player_two) as TextView).text = pingPongGame.playerTwo.name
    }

    fun playerOneScoreOnClick(view: View) {
        if (pingPongGame.isGameOver) return
        playerScored(pingPongGame.playerOne)
        if (!pingPongGame.hasPlayerWonCurrentSet(pingPongGame.playerOne)) {
            if (pingPongGame.isDeuce) {
                playAudio("Deuce!", false)
            } else if (pingPongGame.playerOne.currentSetScore != 0) {
                playAudio(pingPongGame.playerOneName, true)
            }
        }
        displayPointsAndSets()
    }

    private fun displayPointsAndSets() {
        displayPlayerOneScore()
        displayPlayerTwoScore()
        displayNumberOfSetsPlayerOneWon()
        displayNumberOfSetsPlayerTwoWon()
        displayCurrentServingPlayer()
    }

    private fun playAudio(playerName: String, en: Boolean) {
        val text = if (en) "$randomEncouragingWord $playerName" else playerName
        speak(text)
    }

    fun playerTwoScoreOnClick(view: View) {
        if (pingPongGame.isGameOver) return
        playerScored(pingPongGame.playerTwo)
        if (!pingPongGame.hasPlayerWonCurrentSet(pingPongGame.playerTwo)) {
            if (pingPongGame.isDeuce) {
                playAudio("Deuce!", false)
            } else if (pingPongGame.playerTwo.currentSetScore != 0) {
                playAudio(pingPongGame.playerTwoName, true)
            }
        }
        displayPointsAndSets()
    }

    private fun playerScored(player: PingPongPlayer) {
        pingPongGame.playerHasScored(player)
        if (!pingPongGame.hasPlayerWonCurrentSet(player)) {
            if (pingPongGame.isDeuce) {
                displayMessage("Deuce !")
            } else {
                displayMessage("")
            }
            return
        }

        pingPongGame.incrementNumberOfSetsWon(player)
        pingPongGame.resetPlayersCurrentSetScore()

        if (pingPongGame.hasPlayerWonMatch(player)) {
            displayMessage(
                    getString(
                            R.string.player_won_match,
                            player.name
                    )
            )

            var values = ContentValues()
            values.put(PingPongContract.PingPongMatch.COLUMN_PLAYER_ONE_ID_TITLE, pingPongGame.playerOneId)
            values.put(PingPongContract.PingPongMatch.COLUMN_PLAYER_TWO_ID_TITLE, pingPongGame.playerTwoId)
            values.put(PingPongContract.PingPongMatch.COLUMN_SERVING_PLAYER_ID_TITLE, servingPlayerId)
            values.put(PingPongContract.PingPongMatch.COLUMN_PLAYER_ONE_SETS_WON_TITLE, pingPongGame.numSetsPlayerOneWon)
            values.put(PingPongContract.PingPongMatch.COLUMN_PLAYER_TWO_SETS_WON_TITLE, pingPongGame.numSetsPlayerTwoWon)

            val uri = contentResolver.insert(PingPongContract.PingPongMatch.CONTENT_URI, values)

            val id = java.lang.Long.valueOf(uri.lastPathSegment)

            for ((setNumber, playerOneScore, playerTwoScore) in pingPongGame.pingPongSets) {
                values = ContentValues()
                values.put(PingPongContract.Set.MATCH_ID, id.toString() + "")
                values.put(PingPongContract.Set.SET_NUMBER, setNumber)
                values.put(PingPongContract.Set.PLAYER_ONE_SCORE, playerOneScore)
                values.put(PingPongContract.Set.PLAYER_TWO_SCORE, playerTwoScore)

                contentResolver.insert(PingPongContract.Set.CONTENT_URI, values)
            }

            val intent = Intent(this, MatchActivity::class.java)
            intent.data = PingPongContract.Set.CONTENT_URI
            val matchId = id.toString() + ""
            intent.putExtra("matchId", java.lang.Long.valueOf(matchId))
            intent.putExtra("playerOneName", pingPongGame.playerOne.name)
            intent.putExtra("playerTwoName", pingPongGame.playerTwo.name)
            if (pingPongGame.playerOne.numberOfSetsWon > pingPongGame.playerTwo.numberOfSetsWon) {
                intent.putExtra("won", "p1")
            } else {
                intent.putExtra("won", "p2")
            }
            startActivity(intent)
            clearServingPlayer()
            changeResetButton()
        } else {
            displayMessage(
                    getString(
                            R.string.player_won_set,
                            player.name,
                            pingPongGame.currentSetNumber
                    )
            )
            playAudio(getString(
                    R.string.player_won_set,
                    player.name,
                    pingPongGame.currentSetNumber
            ), false)
        }
    }

    private fun displayMessage(message: String) {
        val messageDisplay = findViewById<TextView>(R.id.message)
        messageDisplay.text = message
    }

    private fun clearMessage() {
        displayMessage("")
    }

    private fun resetCurrentScoreDisplay() {
        (findViewById<View>(R.id.player_one_current_set_score) as TextView).text = getString(R.string.initial_set_score)
        (findViewById<View>(R.id.player_two_current_set_score) as TextView).text = getString(R.string.initial_set_score)
    }

    private fun resetNumberOfSetsWonDisplay() {
        (findViewById<View>(R.id.player_one_sets_won) as TextView).text = getString(R.string.initial_sets_won)
        (findViewById<View>(R.id.player_two_sets_won) as TextView).text = getString(R.string.initial_sets_won)
    }

    private fun displayNumberOfSetsPlayerOneWon() {
        (findViewById<View>(R.id.player_one_sets_won) as TextView).text = Integer.toString(pingPongGame.numSetsPlayerOneWon)
    }

    private fun displayNumberOfSetsPlayerTwoWon() {
        (findViewById<View>(R.id.player_two_sets_won) as TextView).text = Integer.toString(pingPongGame.numSetsPlayerTwoWon)
    }

    private fun displayPlayerOneScore() {
        (findViewById<View>(R.id.player_one_current_set_score) as TextView).text = Integer.toString(pingPongGame.playerOneCurrentSetScore)
    }

    private fun displayPlayerTwoScore() {
        (findViewById<View>(R.id.player_two_current_set_score) as TextView).text = Integer.toString(pingPongGame.playerTwoCurrentSetScore)
    }

    private fun resetOnClick(view: View) {
        resetCurrentScoreDisplay()
        resetNumberOfSetsWonDisplay()
        clearMessage()
        pingPongGame.resetGame()
        displayCurrentServingPlayer()
        val reset = view as TextView
        if (reset.text == getString(R.string.restart)) {
            reset.text = getString(R.string.reset).toUpperCase()
        }
        pingPongGame.pingPongSets = ArrayList()
    }

    private fun changeResetButton() {
        reset.text = getString(R.string.restart)
    }
}
