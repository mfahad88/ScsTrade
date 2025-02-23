package com.example.scstrade.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.stock.StockItem
import com.example.scstrade.model.response.watchList.WatchListDetailItem
import com.example.scstrade.model.response.watchList.WatchListItem
import com.example.scstrade.repository.WatchListRepository
import com.example.scstrade.services.AppDatabase
import com.example.scstrade.services.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class WatchListViewModel(application: Application):AndroidViewModel(application) {

    val repository=WatchListRepository(RetrofitInstance.api,application)
    val mutableCreate=MutableLiveData<Resource<String>>()
    val mutableDelete=MutableLiveData<Resource<String>>()
    val mutableSymDelete=MutableLiveData<Resource<String>>()
    val mutableWatchListItem=MutableLiveData<Resource<List<WatchListItem>>>()
    val mutableWatchListDetail=MutableLiveData<Resource<List<StockItem>>>()
    val mutableWatchDetail=MutableLiveData<List<WatchListDetailItem>>()
    fun createWatchList(name:String, position:Int, userId:Int){
        viewModelScope.launch {
            mutableCreate.value=Resource.Loading()
            mutableCreate.value=repository.createWatchList(name, position, userId)
        }
    }

    fun deleteWatchList(watchListId:Int, userId:Int) {
       viewModelScope.launch {
           mutableDelete.value=Resource.Loading()
           mutableDelete.value=repository.deleteWatchList(watchListId, userId)
       }
    }

    fun deleteSymbol(symId:Int){
        viewModelScope.launch {
            mutableSymDelete.value = Resource.Loading()
            mutableSymDelete.value = repository.deleteSymbol(symId)
        }
    }

    fun getWatchList(userId:Int) {
        viewModelScope.launch {
            mutableWatchListItem.value=Resource.Loading()
            mutableWatchListItem.value=repository.getWatchList(userId)
        }
    }
    fun getWatchListDetail(sharedViewModel: SharedViewModel, watchListId:Int){
        viewModelScope.launch {
            mutableWatchListDetail.value = Resource.Loading()
            val watchList=repository.getWatchListDetail(watchListId).data
            try{
                val filterList = ArrayList<StockItem>()
                watchList?.forEach { it1 ->
                    sharedViewModel.mutableAllData.value?.data?.forEach { it2 ->
                        if (it1.watchListSymbol.equals(it2.sYM, true)) {
                            filterList.add(it2)
                        }
                    }
                }
                mutableWatchListDetail.value = Resource.Success(filterList)
            }catch (e:Exception){
                mutableWatchListDetail.value = Resource.Error(e.message?:"An error occurred")
            }
        }
    }
}