package me.dhivo.android.pingpongscorekeeper.activities;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.FileProvider;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import me.dhivo.android.pingpongscorekeeper.R;
import me.dhivo.android.pingpongscorekeeper.data.PingPongContract;
import me.dhivo.android.pingpongscorekeeper.handlers.PingPongAsyncHandler;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlayerEditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    String mCurrentPhotoPath;
    String uriStr;

    private Uri mCurrentPlayerUri;
    EditText nameEditText;
    CircleImageView imageView;
    String playerName;



    final int REQUEST_TAKE_PHOTO =  1;

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        if (playerName != null) return null;
        return new CursorLoader(this,
                mCurrentPlayerUri,
                new String[] {PingPongContract.Player._ID, PingPongContract.Player.COLUMN_NAME_TITLE, PingPongContract.Player.COLUMN_PROFILE_PICTURE_TITLE}, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if(data == null || loader == null) return;

        data.moveToFirst();

        int nameColumnIndex = data.getColumnIndex(PingPongContract.Player.COLUMN_NAME_TITLE);
        int pictureColumnIndex = data.getColumnIndex(PingPongContract.Player.COLUMN_PROFILE_PICTURE_TITLE);


        String name = data.getString(nameColumnIndex);
        String picture = data.getString(pictureColumnIndex);
        nameEditText.setText(name);

        if(picture != null)
        {
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(picture) );
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageView.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    private TextWatcher playerNameWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            playerName = charSequence.toString();
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_editor);

        EditText playerNameEditor = findViewById(R.id.player_name_editor);
        playerNameEditor.addTextChangedListener(playerNameWatcher);
        imageView = findViewById(R.id.profile_image);
        nameEditText = findViewById(R.id.player_name_editor);
        mCurrentPlayerUri = getIntent().getData();

        if(mCurrentPlayerUri != null)
        {
            this.getSupportLoaderManager().initLoader(0, null, this);
//            loaderManager.initLoader<Cursor>(0, null, this as LoaderManager.LoaderCallbacks<Cursor?>)
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_player_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        } else if(item.getItemId()  == R.id.action_close)  {
            finish();
            return true;
        } else if(item.getItemId() == R.id.action_done) {
            addNewPlayer();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addNewPlayer() {
        if(playerName == null || playerName.trim().length() <= 0) {
            Toast.makeText(this, "Enter a name",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if(getIntent().getData() != null)
        {
            ContentValues values = new ContentValues();
            values.put(PingPongContract.Player.COLUMN_NAME_TITLE, playerName);
            if(uriStr != null) {
                values.put(PingPongContract.Player.COLUMN_PROFILE_PICTURE_TITLE, uriStr);
            }
            getContentResolver().update(mCurrentPlayerUri, values, null, null);
            finish();
            return;
        }
        insertDummyPlayer(playerName, getContentResolver());
        finish();
        Toast.makeText(this, "player saved",
                Toast.LENGTH_SHORT).show();
    }

    public void insertDummyPlayer(String name, ContentResolver contentResolver) {
        ContentValues values = new ContentValues();
        values.put(PingPongContract.Player.COLUMN_NAME_TITLE, name);
        if(uriStr != null)
        {
            values.put(PingPongContract.Player.COLUMN_PROFILE_PICTURE_TITLE, uriStr);
        }
        PingPongAsyncHandler pingPongAsyncHandler = new PingPongAsyncHandler(contentResolver, null);
        pingPongAsyncHandler.startInsert(-1, null, PingPongContract.Player.CONTENT_URI, values);
    }


    public void takePicture(View view) {
        dispatchTakePictureIntent();
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "me.dhivo.android.fileprovider",
                        photoFile);
                uriStr = photoURI.toString();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {

            Bitmap bitmap  = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(uriStr) );
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageView.setImageBitmap(bitmap);
        }
    }

    private Bitmap getRotatedBitmap(Bitmap bitmap) {
        Matrix matrix = new Matrix();

        matrix.postRotate(90);

        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return rotatedBitmap;
    }
}
