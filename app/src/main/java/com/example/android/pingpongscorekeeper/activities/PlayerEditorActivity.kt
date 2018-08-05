package com.example.android.pingpongscorekeeper.activities

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
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
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

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
            return
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

////////////////////////////////////////////////////////////////////////////////////////////////////

    var mCurrentPhotoPath: String? = null


    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir      /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.absolutePath
        return image
    }

    internal val REQUEST_TAKE_PHOTO = 1

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            // Create the File where the photo should go
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {
                // Error occurred while creating the File
                //            ...
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                val photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
            }
        }
    }
}
