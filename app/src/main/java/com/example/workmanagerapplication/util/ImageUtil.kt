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

    fun saveImage(bitmap: Bitmap, context: Context): String {
        lateinit var newDirection: File

        val fileName = "${System.currentTimeMillis()}.jpg"
        val direction = File(context.filesDir.toString()) // default folder /files

        newDirection = File(direction, "/pics") // if you want to create new folder
        if (!newDirection.exists())
            newDirection.createNewFile()


        val file = File(newDirection, fileName)
        context.openFileOutput(fileName, Context.MODE_PRIVATE).use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            it.close();
        }
        return file.absolutePath
    }
}