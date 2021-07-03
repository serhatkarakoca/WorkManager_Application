package com.example.workmanagerapplication.viewmodel


import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workmanagerapplication.service.ImagesDatabase
import com.example.workmanagerapplication.model.RecyclerItemModel
import com.example.workmanagerapplication.service.ImagesApiService
import kotlinx.coroutines.launch


class MainActivityViewModel : ViewModel() {

    private val imagesApiService = ImagesApiService()
    val imagesListLiveData = MutableLiveData<List<String>>()
    val listOfImages = MutableLiveData<List<RecyclerItemModel>>()

     fun getDataFromAPI(context: Context) {
        viewModelScope.launch {
            val dao = ImagesDatabase(context).roomDao()
            dao.deleteAllImages()

           try{
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
           }catch (e:Exception){
               e.printStackTrace()
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