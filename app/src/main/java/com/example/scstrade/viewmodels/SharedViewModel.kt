package com.example.scstrade.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scstrade.model.Resource
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
    var isFetchAllData=true
    var isFetchIndices=true

    fun fetchAllData(){
        viewModelScope.launch(Dispatchers.IO) {
            mutableAllData.postValue(repository.fetchAllData())
        }
       /* isFetchAllData=true
        viewModelScope.launch {
          while (isFetchAllData){

              delay(5000)
          }
        }*/
    }

    fun fetchIndices(){
      /*  isFetchIndices=true
        viewModelScope.launch {
            while (isFetchIndices){
                mutableAllData.value = Resource.Loading()
                mutableIndices.value = repository.getWithChartIndices()
                delay(5000)
            }
        }*/

        viewModelScope.launch(Dispatchers.IO) {
//            mutableAllData.value = Resource.Loading()
            mutableIndices.postValue(repository.getWithChartIndices())
        }
    }

    fun fetchLogin(email:String,password:String){
        viewModelScope.launch {
            mutableLogin.value = Resource.Loading()
            mutableLogin.value = repository.fetchLogin(email, password)
        }
    }

    fun stopAll(){
        isFetchIndices=false
        isFetchAllData=false
    }


}