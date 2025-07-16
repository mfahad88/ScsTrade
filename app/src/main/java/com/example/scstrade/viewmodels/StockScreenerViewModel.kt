package com.example.scstrade.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.stockscreener.StockScreenerItem
import com.example.scstrade.repository.MainRepository
import com.example.scstrade.repository.StockScreenerRepository
import com.example.scstrade.services.ApiService
import com.example.scstrade.services.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StockScreenerViewModel(application: Application): AndroidViewModel(application) {
    private val repository= StockScreenerRepository(RetrofitInstance.create(ApiService::class.java),application)
    val mutableStockScreener = MutableLiveData<Resource<List<StockScreenerItem>>>()
    var mutableFiltered= MutableLiveData<List<StockScreenerItem>>()

    fun getStockScreener(){
        mutableStockScreener.value = Resource.Loading()
        viewModelScope.launch (Dispatchers.IO) {
            val result = repository.getStockScreener()

            withContext(Dispatchers.Main){
                mutableStockScreener.value = result
            }
        }
    }

}
