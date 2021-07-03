package com.example.workmanagerapplication.service

import android.content.Context
import android.graphics.BitmapFactory
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.workmanagerapplication.util.ImageUtil
import com.example.workmanagerapplication.model.RecyclerItemModel
import okhttp3.OkHttpClient
import okhttp3.Request


class DownloadImages(private val context: Context, workerParams: WorkerParameters) : Worker(context,
    workerParams
) {

    override fun doWork(): Result {
        return try {
            val list = inputData.getStringArray("list")?.toList()
            list?.forEachIndexed{ _, url ->
                downloadImages(url)
            }

            Result.success()

        }catch (e:Exception){
            e.printStackTrace()
            Result.failure()
        }
    }

    private fun downloadImages(url:String){
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()
        try {
            val response = client.newCall(request).execute()
            val inputStream = response.body?.byteStream()
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val path = ImageUtil.saveImage(bitmap, context)
            val item = RecyclerItemModel(url, !path.isNullOrEmpty(),path)
            val dao = ImagesDatabase(context).roomDao()
            dao.insertAll(item)


        } catch (e: Exception) {
            e.printStackTrace()
            val item = RecyclerItemModel(url,false,null)
            val dao = ImagesDatabase(context).roomDao()
            dao.insertAll(item)
        }
    }
}