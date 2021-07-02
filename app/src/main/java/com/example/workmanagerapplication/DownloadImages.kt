package com.example.workmanagerapplication

import android.content.Context
import android.graphics.BitmapFactory
import androidx.work.Worker
import androidx.work.WorkerParameters
import okhttp3.OkHttpClient
import okhttp3.Request

class DownloadImages(val context: Context, workerParams: WorkerParameters) : Worker(context,
    workerParams
) {
    override fun doWork(): Result {
        val list = inputData.getStringArray("list")?.toList()
        list?.forEachIndexed{ _, url ->
            downloadImages(url)
        }

        return Result.success()
    }

    fun downloadImages(url:String){
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()
        try {
            val response = client.newCall(request).execute()
            val bitmap = BitmapFactory.decodeStream(response.body?.byteStream())
            ImageUtil.saveImage(bitmap, context)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}