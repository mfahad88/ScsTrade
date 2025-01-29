package com.example.scstrade.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.scstrade.model.Resource
import com.example.scstrade.model.summary.KSEIndices
import com.example.scstrade.services.ApiService
import com.example.scstrade.services.RetrofitInstance

class MainRepository(var apiService: ApiService) {

    suspend fun getIndices(): Resource<List<KSEIndices>> {

        try {
            return  Resource.Success(apiService.getIndices())
        }catch (e:Exception){

            return  Resource.Error(e.message?:"An error occurred",null)
        }
    }
}