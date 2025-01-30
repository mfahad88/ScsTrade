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
            val list=apiService.getIndices().map {
                if(it.iNDEXCODE.lowercase().contains("kse all")){
                    it.charts=apiService.getChart("kseall",1)
                }else if(it.iNDEXCODE.lowercase().contains("kse 30")){
                    it.charts=apiService.getChart("kse30",1)
                }else if(it.iNDEXCODE.lowercase().contains("kse 100")){
                    it.charts=apiService.getChart("kse",1)
                }else if(it.iNDEXCODE.lowercase().contains("kmi 30")){
                    it.charts=apiService.getChart("kmi30",1)
                }
                it
            }


            return  Resource.Success(list)
        }catch (e:Exception){

            return  Resource.Error(e.message?:"An error occurred",null)
        }
    }
}