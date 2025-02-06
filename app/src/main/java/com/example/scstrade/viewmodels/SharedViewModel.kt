package com.example.scstrade.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.stock.StockItem
import com.example.scstrade.model.summary.KSEIndices
import com.example.scstrade.repository.ChartRepository
import com.example.scstrade.repository.IndicesRepository
import com.example.scstrade.services.RetrofitInstance
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SharedViewModel : ViewModel() {
    private val indicesRepository=IndicesRepository(RetrofitInstance.api)
    private val chartRepository = ChartRepository(RetrofitInstance.api)
     val mutableAllData=MutableLiveData<Resource<List<StockItem>>>()
    val mutableIndices=MutableLiveData<Resource<List<KSEIndices>>>()
    private var isFetchAllData=true
    private var isFetchIndices=true


    fun fetchAllData(){
        isFetchAllData=true
        viewModelScope.launch {
          while (isFetchAllData){
              mutableAllData.value = Resource.Loading()
              mutableAllData.value = indicesRepository.fetchAllData()
              delay(5000)
          }
        }
    }

    fun fetchIndices(){
        isFetchIndices=true
        viewModelScope.launch {
            while (isFetchIndices){
                mutableAllData.value = Resource.Loading()
                mutableIndices.value = indicesRepository.fetchIndices()
                delay(5000)
            }
        }
    }

    fun stopAll(){
        isFetchIndices=false
        isFetchAllData=false
    }
}