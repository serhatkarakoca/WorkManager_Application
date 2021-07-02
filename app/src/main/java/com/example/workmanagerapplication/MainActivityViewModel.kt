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
     fun getDataFromAPI() {

        viewModelScope.launch {
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

}