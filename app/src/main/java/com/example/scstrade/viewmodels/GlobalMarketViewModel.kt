package com.example.scstrade.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.scstrade.helper.ConnectivityObserver
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.globalMarket.GlobalMarketItem
import com.example.scstrade.repository.MainRepository
import com.example.scstrade.services.ApiService
import com.example.scstrade.services.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GlobalMarketViewModel(application: Application) : AndroidViewModel(application) {
    private val repository=
        MainRepository(RetrofitInstance.create(ApiService::class.java),application)

    val mutableIndices= MutableLiveData<Resource<List<GlobalMarketItem>>>()

    fun globalMarket(){
        mutableIndices.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            while(true) {
                val result = repository.globalMarket()
                withContext(Dispatchers.Main) {
                    mutableIndices.value = result

                }
                delay(5000)
            }
        }
    }


}