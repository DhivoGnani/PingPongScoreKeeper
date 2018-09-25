package me.dhivo.android.pingpongmatchtracker.helpers

import android.content.ContentResolver
import android.media.ExifInterface
import android.net.Uri
import java.io.InputStream

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
}
