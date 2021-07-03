package com.example.workmanagerapplication

import android.app.Activity
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import kotlinx.coroutines.launch


class MainActivityViewModel : ViewModel() {

    private val imagesApiService = ImagesApiService()
    val imagesListLiveData = MutableLiveData<List<String>>()
    val listOfImages = MutableLiveData<List<RecyclerItemModel>>()

     fun getDataFromAPI(context: Context) {
        viewModelScope.launch {
            val dao = ImagesDatabase(context).roomDao()
            dao.deleteAllImages()
           val response = imagesApiService.getData()
            if (response.isSuccessful){
                response.body()?.let {
                    val list = arrayListOf<String>()
                    it.forEach {
                        list.add(it.url)
                    }
                    imagesListLiveData.value = list

                }
            }
        }

    }

     fun getImagesFromDatabase(context:Context){
        viewModelScope.launch {
            val dao = ImagesDatabase(context).roomDao()
            if (dao.getAllImages().isNotEmpty())
                listOfImages.value = dao.getAllImages()
        }

    }

}