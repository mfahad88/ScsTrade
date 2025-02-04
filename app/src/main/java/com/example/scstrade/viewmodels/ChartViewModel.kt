package com.example.scstrade.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.chart.ChartItem
import com.example.scstrade.repository.ChartRepository
import com.example.scstrade.services.RetrofitInstance
import kotlinx.coroutines.launch

class ChartViewModel() : ViewModel() {
    private val repository: ChartRepository=ChartRepository(RetrofitInstance.api)
    val mutableChart=MutableLiveData<Resource<List<ChartItem>>>()
    fun getIndexChart(symbol:String,resolution:Int){
        viewModelScope.launch {
            mutableChart.postValue(Resource.Loading())
            mutableChart.postValue(repository.getIndexChart(symbol,resolution))
        }
    }

}