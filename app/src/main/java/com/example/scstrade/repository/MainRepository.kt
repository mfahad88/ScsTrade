package com.example.scstrade.repository

import android.content.Context
import com.example.scstrade.model.Resource
import com.example.scstrade.model.dao.StockDao
import com.example.scstrade.model.response.chart.ChartItem
import com.example.scstrade.model.response.login.LoginDataItem
import com.example.scstrade.model.response.stock.StockItem
import com.example.scstrade.model.summary.KSEIndices
import com.example.scstrade.services.ApiService
import com.example.scstrade.services.AppDatabase

class MainRepository(val apiService: ApiService,val context: Context) {

    suspend fun getIndices():Resource<List<KSEIndices>>{
        try{
            return  Resource.Success(apiService.getIndices())
        }catch (e:Exception){
            return Resource.Error(e.message?:"An error occurred")
        }
    }
    suspend fun getWithChartIndices(): Resource<List<KSEIndices>> {

        try {
          /*  val list=apiService.getIndices().map {
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
            }*/

           return Resource.Success(AppDatabase.getDatabase(context).marketDao().getIndices())
//            return  Resource.Success(apiService.getIndices())
        }catch (e:Exception){

            return  Resource.Error(e.message?:"An error occurred",null)
        }
    }

    suspend fun fetchAllData(): Resource<List<StockItem>>{
        try {

//            return Resource.Success(apiService.fetchAllData())
            return Resource.Success(AppDatabase.getDatabase(context).marketDao().getMarkets())
        }catch (e:Exception){
            return  Resource.Error(e.message?:"An error occurred")
        }
    }



    suspend fun getIndexChart(symbol:String, resolution:Int): Resource<List<ChartItem>> {
        try {
            return Resource.Success(apiService.getChart(symbol, resolution))
        }catch (e:Exception){
            return Resource.Error(e.message?:"An error occurred",null)
        }
    }

    suspend fun fetchLogin(email:String,password:String): Resource<List<LoginDataItem>> {
        try {
            return Resource.Success(apiService.fetchLogin(email,password))
        }catch (e:Exception){
            return Resource.Error(e.message?:"An error occurred",null)
        }
    }

    suspend fun registerUser(fullName:String,email: String,mobile:String,password: String): Resource<List<LoginDataItem>> {
        try{
            return Resource.Success(apiService.registration(email,fullName,mobile,password))
        }catch (e:Exception){
            return Resource.Error(e.message?:"An error occurred",null)
        }
    }
}