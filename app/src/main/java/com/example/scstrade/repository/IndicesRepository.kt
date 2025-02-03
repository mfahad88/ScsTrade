package com.example.scstrade.repository

import com.example.scstrade.model.Resource
import com.example.scstrade.model.summary.KSEIndices
import com.example.scstrade.services.ApiService

class IndicesRepository(private val apiService: ApiService) {

    suspend fun fetchIndices(): Resource<List<KSEIndices>> {
        try {
           return Resource.Success(apiService.getIndices())
        }catch (e:Exception){
            return Resource.Error(e.message?:"An error occurred")
        }
    }
}