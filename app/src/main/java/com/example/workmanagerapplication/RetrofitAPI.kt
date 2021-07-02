package com.example.workmanagerapplication


import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET

interface RetrofitAPI {

    @GET("serhatkarakoca/WorkManager_Application/master/app/images.json")
   suspend fun getImages(): Response<List<ImageModel>>
}