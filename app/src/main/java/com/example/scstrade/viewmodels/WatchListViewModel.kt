package com.example.scstrade.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.watchList.WatchListDetailItem
import com.example.scstrade.model.response.watchList.WatchListItem
import com.example.scstrade.repository.WatchListRepository
import com.example.scstrade.services.RetrofitInstance
import kotlinx.coroutines.launch

class WatchListViewModel:ViewModel() {
    val repository=WatchListRepository(RetrofitInstance.api)
    val mutableCreate=MutableLiveData<Resource<String>>()
    val mutableDelete=MutableLiveData<Resource<String>>()
    val mutableWatchListItem=MutableLiveData<Resource<List<WatchListItem>>>()
    val mutableWatchListDetail=MutableLiveData<Resource<List<WatchListDetailItem>>>()
    var selectedItem:WatchListItem?=null
    fun createWatchList(name:String, position:Int, userId:Int){
        viewModelScope.launch {
            mutableCreate.value=Resource.Loading()
            mutableCreate.value=repository.createWatchList(name, position, userId)
        }
    }

    fun deleteWatchList(watchListId:Int, userId:Int) {
       viewModelScope.launch {
           mutableDelete.value=Resource.Loading()
           mutableCreate.value=repository.deleteWatchList(watchListId, userId)
       }
    }

    fun getWatchList(userId:Int) {
        viewModelScope.launch {
            mutableWatchListItem.value=Resource.Loading()
            mutableWatchListItem.value=repository.getWatchList(userId)
        }
    }
    fun getWatchListDetail(watchListId:Int){
        viewModelScope.launch {
            mutableWatchListDetail.value = Resource.Loading()
            mutableWatchListDetail.value = repository.getWatchListDetail(watchListId)
        }
    }
}