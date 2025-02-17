package com.example.scstrade.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.chart.ChartItem
import com.example.scstrade.model.response.stock.StockItem
import com.example.scstrade.model.summary.KSEIndices
import com.example.scstrade.repository.MainRepository
import com.example.scstrade.services.RetrofitInstance
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SharedViewModel : ViewModel() {
    private val indicesRepository=MainRepository(RetrofitInstance.api)
     val mutableAllData=MutableLiveData<Resource<List<StockItem>>>()
    val mutableIndices=MutableLiveData<Resource<List<KSEIndices>>>()
    val mutablePreviousIndices=MutableLiveData<List<KSEIndices>?>()
    private var isFetchAllData=true
    private var isFetchIndices=true
    var selectedIndex:KSEIndices?=null


    fun fetchAllData(){
        isFetchAllData=true
        viewModelScope.launch {
            /*mutableAllData.value = Resource.Loading()
            mutableAllData.value = indicesRepository.fetchAllData()*/
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


            /*if(selectedIndex==null){
                selectedIndex = indicesRepository.fetchIndices().data?.first()
                isLoading=false
            }*/
            while (isFetchIndices){
                mutablePreviousIndices.value = mutableIndices.value?.data
                mutableAllData.value = Resource.Loading()
                mutableIndices.value = indicesRepository.getIndices()
                if(selectedIndex==null){
                    selectedIndex = indicesRepository.getIndices().data?.first()
                }
                delay(5000)
            }
        }
    }

    fun stopAll(){
        isFetchIndices=false
        isFetchAllData=false
    }


}