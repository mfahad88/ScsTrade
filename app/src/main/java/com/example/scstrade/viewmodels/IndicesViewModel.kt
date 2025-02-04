package com.example.scstrade.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.stock.StockItem
import com.example.scstrade.model.summary.KSEIndices
import com.example.scstrade.repository.IndicesRepository
import com.example.scstrade.services.RetrofitInstance
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class IndicesViewModel() : ViewModel() {
    private  val repository: IndicesRepository=IndicesRepository(RetrofitInstance.api)
    private var isFetchIndices=true
    private var isFetchAllData=true
    private var isFetchMarket=true
    val mutableLiveData = MutableLiveData<Resource<List<KSEIndices>>>()
    val mutableStockLiveData = MutableLiveData<Resource<List<StockItem>>>()
    fun fetchIndices(){
        isFetchIndices=true
        viewModelScope.launch {
            mutableLiveData.value=Resource.Loading()
            while (isFetchIndices){
                mutableLiveData.value = repository.fetchIndices()
                delay(5000)
            }
        }
    }

    fun fetchMarket(){
        isFetchMarket=true
        viewModelScope.launch {
            mutableLiveData.value = Resource.Loading()
            while (isFetchMarket){
                mutableLiveData.value = repository.fetchIndices()
                delay(60000)
            }
        }
    }

    fun stopMarket(){
        isFetchMarket = false
    }
    fun stopIndices() {
        isFetchIndices = false
    }

    fun fetchAllData(){
        isFetchAllData=true
        viewModelScope.launch {
            mutableStockLiveData.value = Resource.Loading()
            while (isFetchAllData){
                mutableStockLiveData.value = repository.fetchAllData()
                delay(5000)
            }
        }
    }

    fun stopAllData(){
        isFetchAllData=false
    }
}