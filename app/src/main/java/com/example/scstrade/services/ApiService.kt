package com.example.scstrade.services

import com.example.scstrade.model.response.fundamental.FundamentalData
import com.example.scstrade.model.response.chart.ChartItem
import com.example.scstrade.model.response.technicals.TechnicalData
import com.example.scstrade.model.response.login.LoginDataItem
import com.example.scstrade.model.response.stock.StockItem
import com.example.scstrade.model.response.technicals.TechnicalDetailData
import com.example.scstrade.model.response.fundamental.FundamentalDetailData
import com.example.scstrade.model.response.news.NewsData
import com.example.scstrade.model.response.watchList.WatchListDetailItem
import com.example.scstrade.model.response.watchList.WatchListItem
import com.example.scstrade.model.summary.KSEIndices
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET(value = "/Data?que=KSE Indices")
    suspend fun getIndices(): List<KSEIndices>

    @GET(value = "/Chart")
    suspend fun getChart(
        @Query("symbol") symbol: String,
        @Query("resolution") resolution: Int
    ): List<ChartItem>

    @GET(value = "/Data")
    suspend fun fetchAllData(@Query("que") que: String): List<StockItem>

    @GET(value = "/Login")
    suspend fun fetchLogin(
        @Query("RegistrationEmail") email: String,
        @Query("RegistrationPassword") password: String
    ): List<LoginDataItem>

    @GET(value = "/WatchList")
    suspend fun createWatchList(
        @Query("ActionType") type: String, @Query("WatchListName") name: String,@Query("RegistrationID") userId: Int
    ): List<WatchListItem>

    @GET(value = "/WatchList")
    suspend fun deleteWatchList(
        @Query("ActionType") type: String,
        @Query("WatchListID") watchListId: Int,
        @Query("RegistrationID") userId: Int
    ): List<WatchListItem>

    @GET(value = "/WatchList")
    suspend fun getWatchList(
        @Query("ActionType") type: String,
        @Query("RegistrationID") userId: Int
    ): List<WatchListItem>

    @GET(value = "/WatchList")
    suspend fun getWatchListDetail(
        @Query("ActionType") type: String,
        @Query("WatchListID") position: Int
    ): List<WatchListDetailItem>

    @GET(value = "/Registration")
    suspend fun registration(
        @Query("RegistrationEmail") email: String,
        @Query("RegistrationName") fullName: String,
        @Query("RegistrationPhone") phone: String,
        @Query("RegistrationPassword") password: String
    ): List<LoginDataItem>

    @GET(value = "/WatchList")
    suspend fun deleteSymbol(
        @Query("ActionType") type:String,
        @Query("WatchListDetailID") watchListDetailId:Int,
        @Query("WatchListID") watchListID:Int
    ):List<WatchListDetailItem>

    @GET(value = "/WatchList")
    suspend fun addSymbol(
        @Query("ActionType") type:String,
        @Query("WatchListID")watchListId:Int,
        @Query("symbol") symbol:String
    ):List<WatchListDetailItem>

    @GET(value = "/WatchList")
    suspend fun updateWatchList(
        @Query("ActionType") type:String,
        @Query("WatchListName") watchListName:String,
        @Query("WatchListID") watchListId:Int,
        @Query("RegistrationID") registrationID:Int
    ):List<WatchListItem>

    @GET(value = "/Data?que=Technicals")
    suspend fun getTechnicals():List<TechnicalData>

    @GET(value = "/Data")
    suspend fun getTechnicalDetails(@Query("que")que:String):List<TechnicalDetailData>

    @GET(value = "/Data?que=Fundamentals")
    suspend fun getFundamental():List<FundamentalData>

    @GET(value = "/Data")
    suspend fun getFundamentalDetails(@Query("que")que:String):List<FundamentalDetailData>

    @GET(value = "/Data?que=News")
    suspend fun news():List<NewsData>
}