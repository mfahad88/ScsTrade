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

    suspend fun createWatchList(name:String, userId:Int): Resource<List<WatchListItem>> {
        try{
            return Resource.Success(apiService.createWatchList("Create",name, userId))
        }catch (e:Exception){
            return  Resource.Error(e.message?:"An error occurred",null)
        }
    }

    suspend fun deleteWatchList(watchListId:Int, userId:Int): Resource<List<WatchListItem>> {
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
            return Resource.Success(apiService.getWatchListDetail("GetSymbols", position).sortedBy { it.watchListPosition })
        }catch (e:Exception){
            return  Resource.Error(e.message?:"An error occurred",null)
        }
    }

    suspend fun deleteSymbol(watchListDetailId:Int, watchListID:Int): Resource<List<WatchListDetailItem>>{
        try{
            return Resource.Success(apiService.deleteSymbol("DeleteSymbol",watchListDetailId, watchListID))
        }catch (e:Exception){
            return  Resource.Error(e.message?:"An error occurred",null)
        }
    }

    suspend fun addSymbol(watchListId: Int,symbol:String): Resource<List<WatchListDetailItem>> {
        try{
            return Resource.Success(apiService.addSymbol("AddSymbol",watchListId, symbol))
        }catch (e:Exception){
            return Resource.Error(e.message?:"An error occurred",null)
        }
    }
    suspend fun updateWatchList(watchListName:String,watchListId: Int,userId: Int): Resource<List<WatchListItem>>{
        try {
            return Resource.Success(apiService.updateWatchList("UpdateWatchList",watchListName,watchListId, userId))
        }catch (e:Exception){
            return Resource.Error(e.message?:"An error occurred",null)
        }
    }
}