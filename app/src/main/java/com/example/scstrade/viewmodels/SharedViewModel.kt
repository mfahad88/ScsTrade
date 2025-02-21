package com.example.scstrade.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.chart.ChartItem
import com.example.scstrade.model.response.login.LoginDataItem
import com.example.scstrade.model.response.stock.StockItem
import com.example.scstrade.model.summary.KSEIndices
import com.example.scstrade.repository.MainRepository
import com.example.scstrade.services.AppDatabase
import com.example.scstrade.services.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SharedViewModel(application: Application) : AndroidViewModel(application) {
    private val repository=MainRepository(RetrofitInstance.api,application)
     val mutableAllData=MutableLiveData<Resource<List<StockItem>>>()
    val mutableIndices=MutableLiveData<Resource<List<KSEIndices>>>()
    val mutableLogin=MutableLiveData<Resource<List<LoginDataItem>>>()
    val mutableRegister=MutableLiveData<Resource<List<LoginDataItem>>>()
    val mutableChart=MutableLiveData<Resource<List<ChartItem>>>()
    var isFetchAllData=true
    var isFetchIndices=true

    fun fetchAllData(){
        viewModelScope.launch(Dispatchers.IO) {
            while(true) {
                mutableAllData.postValue(repository.fetchAllData())
                delay(5000)
            }
        }

    }

    fun fetchChart(symbol:String){
        viewModelScope.launch {
            while (true){
                mutableChart.value=repository.getIndexChart(symbol,1)
            }
        }
    }

    fun fetchIndices(){

        viewModelScope.launch(Dispatchers.IO) {
            while(true) {
//            mutableAllData.value = Resource.Loading()
                mutableIndices.postValue(repository.getWithChartIndices())
                delay(5000)
            }
        }
    }

    fun fetchLogin(email:String,password:String){
        viewModelScope.launch {
            mutableLogin.value = Resource.Loading()
            mutableLogin.value = repository.fetchLogin(email, password)
        }
    }

    fun registerUser(fullName:String,email: String,mobile:String,password: String){
        viewModelScope.launch {
            mutableRegister.value = Resource.Loading()
            mutableRegister.value = repository.registerUser(fullName, email, mobile, password)
        }
    }

    fun stopAll(){
        isFetchIndices=false
        isFetchAllData=false
    }


}