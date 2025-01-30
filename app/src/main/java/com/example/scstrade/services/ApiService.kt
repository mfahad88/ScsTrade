package com.example.scstrade.services

import com.example.scstrade.model.response.chart.ChartItem
import com.example.scstrade.model.summary.KSEIndices
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET(value = "/Data?que=KSE Indices")
    suspend fun getIndices():List<KSEIndices>

    @GET(value = "/Chart")
    suspend fun getChart(@Query("symbol")symbol:String, @Query("resolution")resolution:Int):List<ChartItem>
}