package com.example.scstrade.repository

import android.content.Context
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.portfolio.DividendItem
import com.example.scstrade.model.response.portfolio.PortfolioDetailItem
import com.example.scstrade.model.response.portfolio.PortfolioDetails
import com.example.scstrade.model.response.portfolio.PortfolioItem
import com.example.scstrade.model.response.portfolio.PortfolioItemDetail
import com.example.scstrade.services.ApiService

class PortfolioRepository(var apiService: ApiService, val context: Context) {

    suspend fun getPortfolio(registrationId:Int): Resource<List<PortfolioItem>> {
        try{
            return Resource.Success(apiService.getPortfolio(registrationID = registrationId))
        }catch (e:Exception){
            return Resource.Error(e.message?:"An error occurred",null)
        }

    }

    suspend fun createPortfolio(name: String, registrationId: Int): Resource<List<PortfolioItem>> {
        try{
            return Resource.Success(apiService.createPortfolio(portfolioName = name, registrationID = registrationId))
        }catch (e:Exception){
            return Resource.Error(e.message?:"An error occurred",null)
        }
    }

    suspend fun deletePortfolio(portfolioMainID: Int, registrationID: Int): Resource<List<PortfolioItem>> {
        try{
            return Resource.Success(apiService.deletePortfolio(portfolioMainID = portfolioMainID, registrationID = registrationID))
        }catch (e:Exception){
            return Resource.Error(e.message?:"An error occurred",null)
        }
    }

    suspend fun getPortfolioDetail(portfolioMainID: Int): Resource<PortfolioDetailItem> {
        try{
            return  Resource.Success(apiService.getPortfolioDetail(portfolioMainID = portfolioMainID))
        }catch (e:Exception){
            return  Resource.Error(e.message?:"An error occurred",null)
        }
    }

    suspend fun buyTrade(portfolioMainID: Int,portfolioDate:String,portfolioSymbol:String,portfolioQuantity:String,portfolioRate:String,portfolioCommission:String,portfolioCommissionType:String,portfolioPosition:String,portfolioDetailID:String): Resource<List<PortfolioDetailItem>> {
        try{
            return  Resource.Success(apiService.buyTrade(portfolioMainID = portfolioMainID, portfolioDate = portfolioDate, portfolioSymbol = portfolioSymbol, portfolioQuantity = portfolioQuantity,
                portfolioRate = portfolioRate, portfolioCommission = portfolioCommission, portfolioCommissionType = portfolioCommissionType, portfolioPosition = portfolioPosition, portfolioDetailID = portfolioDetailID))
        }catch (e:Exception){
            return  Resource.Error(e.message?:"An error occurred",null)
        }
    }

    suspend fun updateTrade(portfolioMainID: Int,portfolioDate:String,portfolioSymbol:String,portfolioQuantity:String,portfolioType:String,portfolioRate:String,portfolioCommission:String,portfolioCommissionType:String,portfolioPosition:String,portfolioDetailID:String): Resource<List<PortfolioDetails>> {
        try{
            return  Resource.Success(apiService.updateTrade(portfolioMainID = portfolioMainID, portfolioDate = portfolioDate, portfolioSymbol = portfolioSymbol, portfolioQuantity = portfolioQuantity, portfolioType = portfolioType,
                portfolioRate = portfolioRate, portfolioCommission = portfolioCommission, portfolioCommissionType = portfolioCommissionType, portfolioPosition = portfolioPosition, portfolioDetailID = portfolioDetailID))
        }catch (e:Exception){
            return  Resource.Error(e.message?:"An error occurred",null)
        }
    }

    suspend fun deleteTrade(portfolioMainID: Int,portfolioDetailID:Int): Resource<List<PortfolioDetails>> {
        try{
            return  Resource.Success(apiService.deleteTrade(portfolioMainID = portfolioMainID, portfolioDetailID = portfolioDetailID))
        }catch (e:Exception){
            return  Resource.Error(e.message?:"An error occurred",null)
        }
    }

    suspend fun buyStock(portfolioMainID: Int,portfolioDate:String,portfolioSymbol:String,portfolioQuantity:String,portfolioRate:String,portfolioCommission:String,portfolioCommissionType:String,portfolioPosition:String): Resource<List<PortfolioDetailItem>> {
        try{
            return  Resource.Success(apiService.buyStock(portfolioMainID = portfolioMainID, portfolioDate = portfolioDate, portfolioSymbol = portfolioSymbol, portfolioQuantity = portfolioQuantity,
                portfolioRate = portfolioRate, portfolioCommission = portfolioCommission, portfolioCommissionType = portfolioCommissionType, portfolioPosition = portfolioPosition))
        }catch (e:Exception){
            return  Resource.Error(e.message?:"An error occurred",null)
        }
    }

    suspend fun sellStock(portfolioMainID: Int,portfolioDate:String,portfolioSymbol:String,portfolioQuantity:String,portfolioRate:String,portfolioCommission:String,portfolioCommissionType:String,portfolioPosition:String): Resource<List<PortfolioDetailItem>> {
        try{
            return  Resource.Success(apiService.sellStock(portfolioMainID = portfolioMainID, portfolioDate = portfolioDate, portfolioSymbol = portfolioSymbol, portfolioQuantity = portfolioQuantity,
                portfolioRate = portfolioRate, portfolioCommission = portfolioCommission, portfolioCommissionType = portfolioCommissionType, portfolioPosition = portfolioPosition))
        }catch (e:Exception){
            return  Resource.Error(e.message?:"An error occurred",null)
        }
    }

    suspend fun addDividend(dividendSymbol:String,dividendQuantity:String,dividendPerShare:String, dividendDate:String, portfolioMainID:String): Resource<List<DividendItem>> {
        try {
            return Resource.Success(apiService.addDividend(dividendSymbol = dividendSymbol, portfolioMainID = portfolioMainID, dividendQuantity = dividendQuantity, dividendDate = dividendDate, dividendPerShare = dividendPerShare))
        }catch (e:Exception){
            return  Resource.Error(e.message?:"An error occurred",null)
        }
    }

    suspend fun getDividend(portfolioMainID:String): Resource<List<DividendItem>> {
        try {
            return Resource.Success(apiService.getDividend(portfolioMainID = portfolioMainID))
        }catch (e:Exception){
            return  Resource.Error(e.message?:"An error occurred",null)
        }
    }

    suspend fun getPortfolioItemDetail(portfolioMainID:Int,portfolioSymbol:String): Resource<List<PortfolioItemDetail>> {
        try {
            return Resource.Success(apiService.getPortfolioItemDetail(portfolioMainID = portfolioMainID, portfolioSymbol = portfolioSymbol))
        }catch (e:Exception){
            return  Resource.Error(e.message?:"An error occurred",null)
        }
    }

    suspend fun getPortfolioDetails(portfolioMainID:Int): Resource<List<PortfolioDetails>> {
        try {
            return Resource.Success(apiService.getPortfolioDetails(portfolioMainID = portfolioMainID))
        }catch (e:Exception){
            return  Resource.Error(e.message?:"An error occurred",null)
        }
    }

}