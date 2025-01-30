package com.example.scstrade.repository

import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.chart.ChartItem
import com.example.scstrade.services.ApiService

class ChartRepository(private  val apiService: ApiService) {

    suspend fun getIndexChart(symbol:String, resolution:Int): Resource<List<ChartItem>> {
        try {
            return Resource.Success(apiService.getChart(symbol, resolution))
        }catch (e:Exception){
            return Resource.Error(e.message?:"An error occurred",null)
        }
    }
}