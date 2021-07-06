package com.example.workmanagerapplication.service

import android.content.Context
import android.graphics.BitmapFactory
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.workmanagerapplication.util.ImageUtil
import com.example.workmanagerapplication.model.RecyclerItemModel
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request


class DownloadImages(private val context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(
        context,
        workerParams
    ) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        // This dispatcher is optimized to perform disk or network I/O outside of the main thread.
        val list = inputData.getStringArray("list")?.toList()!!
        try {
            list.map { url ->
                async {
                    downloadImages(url)
                }
            }.awaitAll() //  awaitAll to wait for both network requests

            Result.success()

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }


    private fun downloadImages(url: String) {
        println("indiriliyor: " + url)
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()
        try {
            val response = client.newCall(request).execute()
            val inputStream = response.body?.byteStream()
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val path = ImageUtil.saveImage(bitmap, context)
            val item = RecyclerItemModel(url, path.isNotEmpty(), path)
            val dao = ImagesDatabase(context).roomDao()
            dao.insertAll(item)
            println("indi: " + url)

        } catch (e: Exception) {
            e.printStackTrace()
            val item = RecyclerItemModel(url, false, null)
            val dao = ImagesDatabase(context).roomDao()
            dao.insertAll(item)
        }
    }
}