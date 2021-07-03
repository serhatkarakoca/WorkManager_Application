package com.example.workmanagerapplication


import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ImagesApiService {

    private val BASE_URL = "https://raw.githubusercontent.com/"
    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(RetrofitAPI::class.java)

   suspend fun getData(): Response<List<ImageModel>> {
        return api.getImages()
    }


}
