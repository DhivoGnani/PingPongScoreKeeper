package com.example.android.pingpongscorekeeper.activities

import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.content.CursorLoader
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.LoaderManager
import android.support.v4.content.FileProvider
import android.support.v4.content.Loader
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.android.pingpongscorekeeper.R
import com.example.android.pingpongscorekeeper.data.PingPongContract
import com.example.android.pingpongscorekeeper.handlers.PingPongAsyncHandler
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_player_editor.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class PlayerEditorActivity2 : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {

    private var mCurrentPlayerUri: Uri? = null
    private var nameEditText: EditText? = null

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {

        val projection = arrayOf<String>(PingPongContract.Player._ID, PingPongContract.Player.COLUMN_NAME_TITLE, PingPongContract.Player.COLUMN_PROFILE_PICTURE_TITLE)

        return CursorLoader(this,
                mCurrentPlayerUri!!,
                projection, null, null, null) as Loader<Cursor>
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        if(data == null) return
        val nameColumnIndex = data.getColumnIndex(PingPongContract.Player.COLUMN_NAME_TITLE)
        val pictureColumnIndex = data.getColumnIndex(PingPongContract.Player.COLUMN_PROFILE_PICTURE_TITLE)


        val name = data.getString(nameColumnIndex)
        val picture = data.getString(pictureColumnIndex)
        nameEditText?.setText(name)

        if(picture != null)
        {
            val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),Uri.parse(picture) );
            imageView?.setImageBitmap(getRotatedBitmap(bitmap))
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    var imageView: CircleImageView? = null;
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
        imageView = findViewById(R.id.profile_image)
        nameEditText = findViewById(R.id.player_name_editor)
        mCurrentPlayerUri = intent.data

        if(mCurrentPlayerUri != null)
        {
//            loaderManager.initLoader<Cursor>(0, null, this as LoaderManager.LoaderCallbacks<Cursor?>)
        }
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
        if(uriStr != null)
        {
            values.put(PingPongContract.Player.COLUMN_PROFILE_PICTURE_TITLE, uriStr)
        }
        val pingPongAsyncHandler = PingPongAsyncHandler(contentResolver, null)
        pingPongAsyncHandler.startInsert(-1, null, PingPongContract.Player.CONTENT_URI, values)
    }

    fun takePicture(view: View) {
        dispatchTakePictureIntent()
    }

////////////////////////////////////////////////////////////////////////////////////////////////////

    var mCurrentPhotoPath: String? = null
    var uriStr: String? = null

    val REQUEST_TAKE_PHOTO: Int = 1;

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

    fun dispatchTakePictureIntent() {
        val takePictureIntent: Intent =  Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            var photoFile: File? = null;
            try {
                photoFile = createImageFile();
            } catch (ex: IOException) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                val photoURI: Uri = FileProvider.getUriForFile(this,
                "com.example.android.fileprovider",
                photoFile)
                uriStr = photoURI.toString()
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {

            val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),Uri.parse(uriStr) );
            imageView?.setImageBitmap(getRotatedBitmap(bitmap))
        }

    }

    fun getRotatedBitmap(bitmap: Bitmap): Bitmap {
        val matrix = Matrix()

        matrix.postRotate(90.toFloat())

        val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        return rotatedBitmap
    }

}
