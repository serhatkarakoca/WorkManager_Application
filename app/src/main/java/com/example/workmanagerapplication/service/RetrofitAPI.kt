package com.example.workmanagerapplication.service


import com.example.workmanagerapplication.model.ImageModel
import retrofit2.Response
import retrofit2.http.GET

interface RetrofitAPI {

    @GET("serhatkarakoca/WorkManager_Application/master/app/images.json")
    suspend fun getImages(): Response<List<ImageModel>>
}