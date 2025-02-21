package com.example.scstrade.repository

import android.content.Context
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.stock.StockItem
import com.example.scstrade.model.response.watchList.WatchListDetailItem
import com.example.scstrade.model.response.watchList.WatchListItem
import com.example.scstrade.services.ApiService
import com.example.scstrade.services.AppDatabase
import retrofit2.http.Query

class WatchListRepository(var apiService: ApiService,val context: Context) {

    suspend fun createWatchList(name:String, position:Int, userId:Int): Resource<String> {
        try{
            return Resource.Success(apiService.createWatchList("Create",name, position, userId))
        }catch (e:Exception){
            return  Resource.Error(e.message?:"An error occurred",null)
        }
    }

    suspend fun deleteWatchList(watchListId:Int, userId:Int): Resource<String> {
        try{
            return Resource.Success(apiService.deleteWatchList("DeleteWatchList",watchListId, userId))
        }catch (e:Exception){
            return  Resource.Error(e.message?:"An error occurred",null)
        }
    }

    suspend fun getWatchList(userId:Int): Resource<List<WatchListItem>> {
        try{
            return Resource.Success(apiService.getWatchList("GetWatchList", userId))
        }catch (e:Exception){
            return  Resource.Error(e.message?:"An error occurred",null)
        }
    }

    suspend fun getWatchListDetail(position:Int): Resource<List<WatchListDetailItem>> {
        try{
            val symbols=apiService.getWatchListDetail("GetSymbols", position).sortedBy { it.watchListPosition }.map { it.watchListSymbol.lowercase() }



            return Resource.Success(apiService.getWatchListDetail("GetSymbols", position).sortedBy { it.watchListPosition })
        }catch (e:Exception){
            return  Resource.Error(e.message?:"An error occurred",null)
        }
    }

    suspend fun deleteSymbol(symId:Int): Resource<String>{
        try{
            return Resource.Success(apiService.deleteSymbol("DeleteSymbol",symId))
        }catch (e:Exception){
            return  Resource.Error(e.message?:"An error occurred",null)
        }
    }
}