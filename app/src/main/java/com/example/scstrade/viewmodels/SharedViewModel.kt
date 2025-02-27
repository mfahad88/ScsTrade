package com.example.scstrade.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.scstrade.helper.ConnectivityObserver
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.fundamental.FundamentalData
import com.example.scstrade.model.response.chart.ChartItem
import com.example.scstrade.model.response.login.LoginDataItem
import com.example.scstrade.model.response.stock.StockItem
import com.example.scstrade.model.response.technicals.TechnicalData
import com.example.scstrade.model.response.technicals.TechnicalDetailData
import com.example.scstrade.model.response.fundamental.FundamentalDetailData
import com.example.scstrade.model.response.news.NewsData
import com.example.scstrade.model.summary.KSEIndices
import com.example.scstrade.repository.MainRepository
import com.example.scstrade.services.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SharedViewModel(application: Application) : AndroidViewModel(application) {
    private val repository=MainRepository(RetrofitInstance.api,application)
     val mutableAllData=MutableLiveData<Resource<List<StockItem>>>()
    val mutableIndices=MutableLiveData<Resource<List<KSEIndices>>>()
    val mutableLogin=MutableLiveData<Resource<List<LoginDataItem>>>()
    val mutableRegister=MutableLiveData<Resource<List<LoginDataItem>>>()
    val mutableChart=MutableLiveData<Resource<List<ChartItem>>>()
    val mutableTechnical=MutableLiveData<Resource<List<TechnicalData>>>()
    val mutableTechnicalDetail=MutableLiveData<Resource<List<TechnicalDetailData>>>()

    val mutableFundamental=MutableLiveData<Resource<List<FundamentalData>>>()
    val mutableFundamentalDetail=MutableLiveData<Resource<List<FundamentalDetailData>>>()
    val mutableNews=MutableLiveData<Resource<List<NewsData>>>()
    var isFetchAllData=true
    var isFetchIndices=true
    val isConnected = ConnectivityObserver(application)
    fun fetchAllData(){
        viewModelScope.launch(Dispatchers.IO) {
            while(true) {
                if(isConnected.value==true) {
                    val result = repository.fetchAllData()
                    withContext(Dispatchers.Main) {
                        mutableAllData.value = result
                    }
                    delay(5000)
                }
            }
        }

    }

    fun fetchChart(symbol:String){
        viewModelScope.launch (Dispatchers.IO){
            if(isConnected.value==true) {
                val result = repository.getIndexChart(symbol, 1)
                while (true) {
                    withContext(Dispatchers.Main) {
                        mutableChart.value = result
                    }
                    delay(5000)
                }
            }
        }
    }

    fun fetchIndices(){

        viewModelScope.launch(Dispatchers.IO) {
            while(true) {
//            mutableAllData.value = Resource.Loading()
                if (isConnected.value == true) {
                    val result = repository.getIndices()
                    withContext(Dispatchers.Main) {
                        mutableIndices.value = result
                    }

                    delay(5000)
                }
            }
        }
    }

    fun fetchLogin(email:String,password:String){
        viewModelScope.launch {
            if(isConnected.value==true) {
                mutableLogin.value = Resource.Loading()
                mutableLogin.value = repository.fetchLogin(email, password)
            }
        }
    }

    fun registerUser(fullName:String,email: String,mobile:String,password: String){
        viewModelScope.launch {
            if(isConnected.value==true) {
                mutableRegister.value = Resource.Loading()
                mutableRegister.value = repository.registerUser(fullName, email, mobile, password)
            }
        }
    }

    fun getTechnicals(){
        mutableTechnical.value = Resource.Loading()
        viewModelScope.launch (Dispatchers.IO){
            if(isConnected.value==true){
            val result=repository.getTechnicals()
            withContext(Dispatchers.Main){
                mutableTechnical.value = result
            }
                }
        }
    }

    fun getTechnicalDetail(que:String){
        mutableTechnicalDetail.value = Resource.Loading()
        if(isConnected.value==true) {
            viewModelScope.launch(Dispatchers.IO) {
                val result = repository.getTechnicalDetails(que)
                withContext(Dispatchers.Main) {
                    mutableTechnicalDetail.value = result
                }
            }
        }
    }

    fun getFundamental(){
        mutableFundamental.value = Resource.Loading()
        if(isConnected.value==true) {
            viewModelScope.launch(Dispatchers.IO) {
                val result = repository.getFundamental()
                withContext(Dispatchers.Main) {
                    mutableFundamental.value = result
                }
            }
        }
    }

    fun getFundamentalDetail(que:String){
        mutableFundamentalDetail.value = Resource.Loading()
        if(isConnected.value==true) {
            viewModelScope.launch(Dispatchers.IO) {
                val result = repository.getFundamentalDetails(que)
                withContext(Dispatchers.Main) {
                    mutableFundamentalDetail.value = result
                }
            }
        }
    }

    fun news(){
        mutableNews.value = Resource.Loading()
        if(isConnected.value==true){
            viewModelScope.launch (Dispatchers.IO){
                val result=repository.news()
                withContext(Dispatchers.Main){
                    mutableNews.value = result
                }
            }
        }
    }

    fun stopAll(){
        isFetchIndices=false
        isFetchAllData=false
    }


}