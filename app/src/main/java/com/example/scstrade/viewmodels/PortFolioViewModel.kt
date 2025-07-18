package com.example.scstrade.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.scstrade.model.Resource
import com.example.scstrade.model.data.ResultData
import com.example.scstrade.model.response.portfolio.PortfolioDetailItem
import com.example.scstrade.model.response.portfolio.PortfolioDetails
import com.example.scstrade.model.response.portfolio.PortfolioItem
import com.example.scstrade.model.response.portfolio.ResultPortFolioList
import com.example.scstrade.repository.MainRepository
import com.example.scstrade.repository.PortfolioRepository
import com.example.scstrade.services.ApiService
import com.example.scstrade.services.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PortFolioViewModel(application: Application): AndroidViewModel(application) {
    private val repository= PortfolioRepository(RetrofitInstance.create(ApiService::class.java),application)
    val mutableResultPortFolioList= MutableLiveData<Resource<ResultPortFolioList>>()
    val mutablePortfolioFinalDetailOnce= MutableLiveData<Resource<PortfolioDetailItem>>()
    fun getPortfolio(registrationId:Int?){
        mutableResultPortFolioList.value = Resource.Loading()
        viewModelScope.launch (Dispatchers.IO){
            if(registrationId!=null) {
                val result = repository.getPortfolio(registrationId).data

                val portfolioListDetail= ArrayList<PortfolioDetails>()
                result?.forEach {
                    it.numberCompany = repository.getPortfolioDetails(it.portfolioMainID).data?.size?:0
                    portfolioListDetail.addAll(repository.getPortfolioDetails(it.portfolioMainID).data?: emptyList())
                }
                withContext(Dispatchers.Main){
                    mutableResultPortFolioList.value = Resource.Success(ResultPortFolioList(result?: emptyList(),portfolioListDetail))
                }
            }
        }
    }

    fun getPortfolioFinalDetailOnce(portfolioMainID: Int?){
        mutablePortfolioFinalDetailOnce.value = Resource.Loading()
        viewModelScope.launch (Dispatchers.IO){
            val result = repository.getPortfolioDetail(portfolioMainID ?: -1)
            withContext(Dispatchers.Main) {
                mutablePortfolioFinalDetailOnce.value = result
            }
        }
    }

   /* fun cretePortfolio(name:String?,registrationId: Int?){
        mutablePortfolio.value = Resource.Loading()
        if(isConnected.value == true){
            viewModelScope.launch (Dispatchers.IO){
                val result = repository.createPortfolio(name?:"",registrationId?:-1)
                withContext(Dispatchers.Main){
                    mutablePortfolio.value = result
                }
            }
        }
    }

    fun getPortfolioFinalDetail(portfolioMainID: Int?){
        mutablePortfolioFinalDetail.value = Resource.Loading()

        if(isConnected.value == true){
            portfolioJob?.cancel()
            portfolioJob= viewModelScope.launch (Dispatchers.IO){

                while (isFetchPortfolioFinal) {

                    Log.e("PortfolioDetail",portfolioMainID.toString())
                    val result = repository.getPortfolioDetail(portfolioMainID?:0)

                    withContext(Dispatchers.Main) {
                        mutablePortfolioFinalDetail.value = Resource.Success(result.data)
                    }

                    delay(5000)
                }
            }
        }
    }


    fun buyTrade(portfolioMainID: Int,portfolioDate:String,portfolioSymbol:String,portfolioQuantity:String,portfolioRate:String,portfolioCommission:String,portfolioCommissionType:String,portfolioPosition:String,portfolioDetailID:String){
        if(isConnected.value == true){
            viewModelScope.launch (Dispatchers.IO){
                val result = repository.buyTrade(portfolioMainID = portfolioMainID, portfolioDate = portfolioDate, portfolioSymbol = portfolioSymbol, portfolioQuantity = portfolioQuantity,
                    portfolioRate = portfolioRate, portfolioCommission = portfolioCommission, portfolioCommissionType = portfolioCommissionType, portfolioPosition = portfolioPosition, portfolioDetailID = portfolioDetailID)
                withContext(Dispatchers.Main){
                    val result = repository.getPortfolioDetail(portfolioMainID ?: -1)
//                    mutablePortfolioDetail.value = result
                }
            }
        }
    }

    fun updateTrade(portfolioMainID: Int,portfolioDate:String,portfolioSymbol:String,portfolioQuantity:String,portfolioType:String,portfolioRate:String,portfolioCommission:String,portfolioCommissionType:String,portfolioPosition:String,portfolioDetailID:String){
        if(isConnected.value == true){
            viewModelScope.launch (Dispatchers.IO){
                val result = repository.updateTrade(portfolioMainID = portfolioMainID, portfolioDate = portfolioDate, portfolioType = portfolioType, portfolioSymbol = portfolioSymbol, portfolioQuantity = portfolioQuantity,
                    portfolioRate = portfolioRate, portfolioCommission = portfolioCommission, portfolioCommissionType = portfolioCommissionType, portfolioPosition = portfolioPosition, portfolioDetailID = portfolioDetailID)
                withContext(Dispatchers.Main){
                    mutablePortfolioDetails.value = result
                }
            }
        }
    }

    fun deleteTrade(portfolioMainID: Int,portfolioDetailID:Int){
        if(isConnected.value == true){
            viewModelScope.launch (Dispatchers.IO){
                val result = repository.deleteTrade(portfolioMainID = portfolioMainID, portfolioDetailID = portfolioDetailID)
                withContext(Dispatchers.Main){
                    mutablePortfolioDetails.value = result
                }
            }
        }
    }

    fun buyStock(portfolioMainID: Int,portfolioDate:String,portfolioSymbol:String,portfolioQuantity:String,portfolioRate:String,portfolioCommission:String,portfolioCommissionType:String,portfolioPosition:String){
        if(isConnected.value == true){
            viewModelScope.launch (Dispatchers.IO){
                val result = repository.buyStock(portfolioMainID = portfolioMainID, portfolioDate = portfolioDate, portfolioSymbol = portfolioSymbol, portfolioQuantity = portfolioQuantity,
                    portfolioRate = portfolioRate, portfolioCommission = portfolioCommission, portfolioCommissionType = portfolioCommissionType, portfolioPosition = portfolioPosition)
//                 repository.getPortfolioDetail(portfolioMainID ?: -1)
                withContext(Dispatchers.Main){

//                    mutablePortfolioDetail.value = result
                }
            }
        }
    }

    fun sellStock(portfolioMainID: Int,portfolioDate:String,portfolioSymbol:String,portfolioQuantity:String,portfolioRate:String,portfolioCommission:String,portfolioCommissionType:String,portfolioPosition:String){
        if(isConnected.value == true){
            viewModelScope.launch (Dispatchers.IO){
                val result = repository.sellStock(portfolioMainID = portfolioMainID, portfolioDate = portfolioDate, portfolioSymbol = portfolioSymbol, portfolioQuantity = portfolioQuantity,
                    portfolioRate = portfolioRate, portfolioCommission = portfolioCommission, portfolioCommissionType = portfolioCommissionType, portfolioPosition = portfolioPosition)
                repository.getPortfolioDetail(portfolioMainID ?: -1)
                withContext(Dispatchers.Main){

//                    mutablePortfolioDetail.value = result
                }
            }
        }
    }

    fun addDividend(dividendSymbol:String,dividendQuantity:String,dividendPerShare:String, dividendDate:String, portfolioMainID:String){
        if(isConnected.value == true){
            viewModelScope.launch (Dispatchers.IO){
                val result = repository.addDividend(dividendSymbol, dividendQuantity, dividendPerShare, dividendDate, portfolioMainID)
                withContext(Dispatchers.Main){

                    mutableDividend.value=result
                }
            }
        }
    }

    fun getPortfolioItemDetail(portfolioMainID:Int,portfolioSymbol:String){
        mutablePortfolioItemDetail.value = Resource.Loading()
        if(isConnected.value == true){
            viewModelScope.launch (Dispatchers.IO){
                val result = repository.getPortfolioItemDetail(portfolioMainID, portfolioSymbol)
                withContext(Dispatchers.Main){

                    mutablePortfolioItemDetail.value=result
                }
            }
        }
    }

    fun getPortfolioDetails(portfolioMainID:Int){
        mutablePortfolioDetails.value = Resource.Loading()
        if(isConnected.value==true){
            viewModelScope.launch (Dispatchers.IO){
                val  result = repository.getPortfolioDetails(portfolioMainID)
                withContext(Dispatchers.Main){
                    mutablePortfolioDetails.value = result
                }
            }
        }
    }
    fun getHistory(portfolioMainID:String){
        viewModelScope.launch {
            val portfolioDetailsDeffered= async { repository.getPortfolioDetail(portfolioMainID.toInt()) }
            val dividendDeffered= async { repository.getDividend(portfolioMainID) }
            val portfolioDetails=portfolioDetailsDeffered.await()
            val dividend = dividendDeffered.await()
            mutableHistory.value= ResultData(portfolioDetails,dividend)

        }

    }
    fun getDividend(portfolioMainID:String){
        mutableDividend.value= Resource.Loading()
        if(isConnected.value == true){
            viewModelScope.launch (Dispatchers.IO){
                val result = repository.getDividend( portfolioMainID)
                withContext(Dispatchers.Main){
                    mutableDividend.value=result
                }
            }
        }
    }

    fun deletePortfolio(portfolioMainID: Int, registrationID: Int) {
        mutablePortfolio.value = Resource.Loading()
        if(isConnected.value == true){
            viewModelScope.launch (Dispatchers.IO){
                val result = repository.deletePortfolio(portfolioMainID,registrationID)
                withContext(Dispatchers.Main){
                    mutablePortfolio.value = result
                }
            }
        }

    }*/
}