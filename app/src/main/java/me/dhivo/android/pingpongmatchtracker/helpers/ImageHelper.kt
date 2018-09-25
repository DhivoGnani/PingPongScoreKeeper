package me.dhivo.android.pingpongmatchtracker.helpers

import android.content.ContentResolver
import android.media.ExifInterface
import android.net.Uri
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import com.squareup.picasso.Picasso
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

object ImageHelper {
    fun getOrientation(contentResolver: ContentResolver, uri: Uri): Int {
        val inputStream: InputStream = contentResolver.openInputStream(uri)
        val exifInterface =  ExifInterface(inputStream)

        val orientation = exifInterface.getAttributeInt(
                ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL
        )

        return when (orientation)
        {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
    }

    @Throws(IOException::class)
    @JvmStatic
    fun createImageFile(activity: AppCompatActivity): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir      /* directory */
        )
    }

    @JvmStatic
    fun displayImage(contentResolver: ContentResolver, uri: Uri, imageView: ImageView) {
        val rotation = ImageHelper.getOrientation(contentResolver, uri)
        Picasso.get().load(uri).resize(1000, 1000)
                .centerCrop().rotate(rotation.toFloat()).into(imageView)
    }
}
