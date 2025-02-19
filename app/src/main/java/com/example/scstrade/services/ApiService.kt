package com.example.scstrade.services

import com.example.scstrade.model.response.chart.ChartItem
import com.example.scstrade.model.response.login.LoginDataItem
import com.example.scstrade.model.response.stock.StockItem
import com.example.scstrade.model.response.watchList.WatchListDetailItem
import com.example.scstrade.model.response.watchList.WatchListItem
import com.example.scstrade.model.summary.KSEIndices
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET(value = "/Data?que=KSE Indices")
    suspend fun getIndices():List<KSEIndices>

    @GET(value = "/Chart")
    suspend fun getChart(@Query("symbol")symbol:String, @Query("resolution")resolution:Int):List<ChartItem>

    @GET(value = "/Data?que=AllData")
    suspend fun fetchAllData():List<StockItem>

    @GET(value = "/Login")
    suspend fun fetchLogin(@Query("RegistrationEmail")email:String,@Query("RegistrationPassword")password:String):List<LoginDataItem>

    @GET(value = "/WatchList")
    suspend fun createWatchList(@Query("ActionType")type:String,@Query("WatchListName")name:String,
                                @Query("WatchListPosition")position:Int,@Query("RegistrationID") userId:Int):String

    @GET(value = "/WatchList")
    suspend fun deleteWatchList(@Query("ActionType")type:String,@Query("WatchListID")watchListId:Int, @Query("RegistrationID") userId:Int):String

    @GET(value = "/WatchList")
    suspend fun getWatchList(@Query("ActionType")type:String, @Query("RegistrationID") userId:Int):List<WatchListItem>

    @GET(value = "/WatchList")
    suspend fun getWatchListDetail(@Query("ActionType")type:String, @Query("WatchListPosition")position: Int):List<WatchListDetailItem>
}