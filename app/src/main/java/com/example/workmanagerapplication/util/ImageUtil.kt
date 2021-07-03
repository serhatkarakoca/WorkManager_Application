package com.example.workmanagerapplication.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import java.io.*

object ImageUtil {

    fun saveImage(bitmap: Bitmap, context: Context): String? {

        val filename = "${System.currentTimeMillis()}.jpg"


        var fos: OutputStream? = null
        var imagesDir: File? = null
        var image: File? = null


        imagesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        image = File(imagesDir, filename)

        context.contentResolver?.also { resolver ->

            val contentValues = ContentValues().apply {


                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }


            val imageUri: Uri? =
                resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

            fos = imageUri?.let { resolver.openOutputStream(it) }
        }

        fos?.use {

            bitmap.compress(Bitmap.CompressFormat.PNG, 80, it)
            it.flush()
            it.close()
            return image.absolutePath
        }
        return null
    }
}