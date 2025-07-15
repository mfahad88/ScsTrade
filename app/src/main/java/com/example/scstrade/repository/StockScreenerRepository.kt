package com.example.scstrade.repository

import android.content.Context
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.stockscreener.StockScreenerItem
import com.example.scstrade.services.ApiService

class StockScreenerRepository(val apiService: ApiService, val context: Context) {
    suspend fun getStockScreener(): Resource<List<StockScreenerItem>> {
        try{
            return  Resource.Success(apiService.stockScreener())
        }catch (e:Exception){
            return Resource.Error(e.message?:"An error occurred")
        }
    }
}