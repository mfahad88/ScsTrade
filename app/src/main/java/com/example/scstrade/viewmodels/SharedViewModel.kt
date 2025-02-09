package com.example.scstrade.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.chart.ChartItem
import com.example.scstrade.model.response.stock.StockItem
import com.example.scstrade.model.summary.KSEIndices
import com.example.scstrade.repository.ChartRepository
import com.example.scstrade.repository.IndicesRepository
import com.example.scstrade.services.RetrofitInstance
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SharedViewModel : ViewModel() {
    private val indicesRepository=IndicesRepository(RetrofitInstance.api)
     val mutableAllData=MutableLiveData<Resource<List<StockItem>>>()
    val mutableIndices=MutableLiveData<Resource<List<KSEIndices>>>()
    val mutableChart=MutableLiveData<Resource<List<ChartItem>>>()
    private var isFetchAllData=true
    private var isFetchIndices=true
    public var isLoading=true
    var selectedIndex:KSEIndices?=null
    var selectedTime:Long?=1
    var jobChart: Job?=null
    var symbol:String?=null


    fun fetchAllData(){
        isFetchAllData=true
        viewModelScope.launch {
          while (isFetchAllData){
              mutableAllData.value = Resource.Loading()
              mutableAllData.value = indicesRepository.fetchAllData()
//              delay(5000)
          }
        }
    }

    fun fetchIndices(){
        isFetchIndices=true
        viewModelScope.launch {
            while (isFetchIndices){
                mutableAllData.value = Resource.Loading()
                mutableIndices.value = indicesRepository.fetchIndices()
                if(selectedIndex==null){
                    selectedIndex = indicesRepository.fetchIndices().data?.first()
                    isLoading=false
                }
//                delay(5000)
            }
        }
    }

    fun stopAll(){
        isFetchIndices=false
        isFetchAllData=false
    }


}