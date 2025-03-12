package com.example.scstrade.viewmodels

import RssFeed
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.scstrade.helper.ConnectivityObserver
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.fundamental.FundamentalData
import com.example.scstrade.model.response.chart.ChartItem
import com.example.scstrade.model.response.contact.ContactData
import com.example.scstrade.model.response.login.LoginDataItem
import com.example.scstrade.model.response.stock.StockItem
import com.example.scstrade.model.response.technicals.TechnicalData
import com.example.scstrade.model.response.technicals.TechnicalDetailData
import com.example.scstrade.model.response.fundamental.FundamentalDetailData
import com.example.scstrade.model.response.news.NewsData
import com.example.scstrade.model.response.news.brecoder.RssWrapper
import com.example.scstrade.model.response.snapshot.Overview
import com.example.scstrade.model.response.snapshot.chart.Charting
import com.example.scstrade.model.response.snapshot.detail.DetailItem
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
    val mutableFuture=MutableLiveData<Resource<List<StockItem>>>()
    val mutableIndices=MutableLiveData<Resource<List<KSEIndices>>>()
    val mutableLogin=MutableLiveData<Resource<List<LoginDataItem>>>()
    val mutableRegister=MutableLiveData<Resource<List<LoginDataItem>>>()
    val mutableChart=MutableLiveData<Resource<List<ChartItem>>>()
    val mutableTechnical=MutableLiveData<Resource<List<TechnicalData>>>()
    val mutableTechnicalDetail=MutableLiveData<Resource<List<TechnicalDetailData>>>()

    val mutableFundamental=MutableLiveData<Resource<List<FundamentalData>>>()
    val mutableFundamentalDetail=MutableLiveData<Resource<List<FundamentalDetailData>>>()
    val mutableNews=MutableLiveData<Resource<List<NewsData>>>()
    val mutableTribune=MutableLiveData<Resource<RssFeed>>()
    val mutableBrecoder=MutableLiveData<Resource<RssWrapper>>()
    val mutableProfit=MutableLiveData<Resource<com.example.scstrade.model.response.news.profit.RssFeed>>()
    val mutableMettis=MutableLiveData<Resource<com.example.scstrade.model.response.news.mettis.RssFeed>>()
    val mutableDawn=MutableLiveData<Resource<com.example.scstrade.model.response.news.dawn.RssFeed>>()
    val mutableContactUs=MutableLiveData<Resource<List<ContactData>>>()
    val mutableOverview=MutableLiveData<Resource<Overview>>()
    val mutableDetail=MutableLiveData<Resource<List<DetailItem>>>()
    val mutableSnapShotChart=MutableLiveData<Resource<Charting>>()
    var isFetchAllData=true
    var isFetchIndices=true
    val isConnected = ConnectivityObserver(application)
    fun fetchAllData(){
        viewModelScope.launch(Dispatchers.IO) {
            while(isFetchAllData) {
                if(isConnected.value==true) {
                    val result = repository.fetchAllData("AllData")
                    val result1 = repository.fetchAllData("FutureData")
                    withContext(Dispatchers.Main) {
                        mutableAllData.value = result
                        mutableFuture.value = result1
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
            while(isFetchIndices) {
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
                var result:Resource<List<NewsData>> = Resource.Loading()
                result=repository.news()
                withContext(Dispatchers.Main){
                    mutableNews.value = result
                }
            }
        }
    }

    fun tribuneNews(){
        mutableTribune.value  = Resource.Loading()
        if(isConnected.value==true){
            viewModelScope.launch (Dispatchers.IO){
                val result = repository.newsTribune()
                withContext(Dispatchers.Main){
                    mutableTribune.value = result
                }
            }
        }
    }

    fun brecoderNews(){
        mutableBrecoder.value  = Resource.Loading()
        if(isConnected.value==true){
            viewModelScope.launch (Dispatchers.IO){
                val result = repository.newsBusiness()
                withContext(Dispatchers.Main){
                    mutableBrecoder.value = result
                }
            }
        }
    }

    fun profitNews(){
        mutableProfit.value  = Resource.Loading()
        if(isConnected.value==true){
            viewModelScope.launch (Dispatchers.IO){
                val result = repository.newsProfit()
                withContext(Dispatchers.Main){
                    mutableProfit.value = result
                }
            }
        }
    }


    fun mettisNews(){
        mutableMettis.value  = Resource.Loading()
        if(isConnected.value==true){
            viewModelScope.launch (Dispatchers.IO){
                val result = repository.newsMettis()
                withContext(Dispatchers.Main){
                    mutableMettis.value = result
                }
            }
        }
    }

    fun dawnNews(){
        mutableDawn.value  = Resource.Loading()
        if(isConnected.value==true){
            viewModelScope.launch (Dispatchers.IO){
                val result = repository.newsDawn()
                withContext(Dispatchers.Main){
                    mutableDawn.value = result
                }
            }
        }
    }

    fun contactUs(){
        mutableContactUs.value = Resource.Loading()
        if(isConnected.value==true){
            viewModelScope.launch (Dispatchers.IO){
                val result = repository.contactUs()
                withContext(Dispatchers.Main){
                    mutableContactUs.value = result
                }
            }
        }
    }

    fun snapshotOverview(symbol: String){
        mutableOverview.value = Resource.Loading()
        if(isConnected.value==true){
            viewModelScope.launch (Dispatchers.IO){
                val result = repository.snapshotOverview(symbol)
                withContext(Dispatchers.Main){
                    mutableOverview.value = result
                }
            }
        }
    }

    fun snapshotDetail(symbol: String){
        mutableDetail.value = Resource.Loading()
        if(isConnected.value==true){
            viewModelScope.launch (Dispatchers.IO){
                val result = repository.snapshotDetail(symbol)
                withContext(Dispatchers.Main){
                    mutableDetail.value = result
                }
            }
        }
    }

    fun snapshotChart(symbol: String){
        mutableSnapShotChart.value = Resource.Loading()
        if(isConnected.value==true){
            viewModelScope.launch (Dispatchers.IO){
                val result = repository.snapshotChart(symbol)
                withContext(Dispatchers.Main){
                    mutableSnapShotChart.value = result
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("SharedViewModel", "ViewModel is cleared")
    }
    fun stopAll(){
        isFetchIndices=false
        isFetchAllData=false
    }


}