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
import kotlinx.coroutines.withContext

class WatchListViewModel(application: Application):AndroidViewModel(application) {

    val repository=WatchListRepository(RetrofitInstance.api,application)
    val mutableCreate=MutableLiveData<Resource<List<WatchListItem>>>()
    val mutableDelete=MutableLiveData<Resource<List<WatchListItem>>>()
    val mutableSymDelete=MutableLiveData<Resource<List<WatchListDetailItem>>>()
    val mutableSymAdd=MutableLiveData<Resource<List<WatchListDetailItem>>>()
    val mutableWatchListItem=MutableLiveData<Resource<List<WatchListItem>>>()
    val mutableWatchListDetail=MutableLiveData<Resource<List<StockItem>>>()
    val mutableWatchListDetailItem=MutableLiveData<List<WatchListDetailItem>>()
    fun createWatchList(name:String, userId:Int){
        viewModelScope.launch {
            mutableCreate.value=Resource.Loading()
            mutableCreate.value=repository.createWatchList(name, userId)
//            mutableWatchListItem.value = mutableCreate.value
        }
    }

    fun deleteWatchList(watchListId:Int, userId:Int) {
       viewModelScope.launch {
           mutableDelete.value=Resource.Loading()
           mutableDelete.value=repository.deleteWatchList(watchListId, userId)
           mutableWatchListItem.value = mutableDelete.value
       }
    }

    fun deleteSymbol(watchListDetailId:Int, watchListID:Int){
        viewModelScope.launch {
            mutableSymDelete.value = Resource.Loading()
            mutableSymDelete.value = repository.deleteSymbol(watchListDetailId, watchListID)
//            mutableWatchListDetailItem.value = mutableSymDelete.value?.data?: emptyList()
        }
    }

    fun addSymbol(sharedViewModel:SharedViewModel,watchListId: Int,symbol:String){
        mutableSymAdd.value = Resource.Loading()
        viewModelScope.launch {
            val result=repository.addSymbol(watchListId, symbol)
            mutableWatchListDetailItem.value=result.data?: emptyList()
            mutableSymAdd.value = result
            val filterList = ArrayList<StockItem>()
            result.data?.forEach { it1 ->
                sharedViewModel.mutableAllData.value?.data?.forEach { it2 ->
                    if (it1.watchListSymbol.equals(it2.sYM, true)) {
                        filterList.add(it2)
                    }
                }
            }
            if(filterList.size==result.data?.size) {
                mutableWatchListDetail.value = Resource.Success(filterList)
            }

        }
    }

    fun getWatchList(userId:Int) {
        viewModelScope.launch {
            mutableWatchListItem.value=Resource.Loading()
            mutableWatchListItem.value=repository.getWatchList(userId)
        }
    }
    fun getWatchListDetail(sharedViewModel: SharedViewModel, watchListId:Int){
        mutableWatchListDetail.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {



            while (true){
                try{
                    val watchList=repository.getWatchListDetail(watchListId).data?.sortedBy { it.watchListPosition }
                    withContext(Dispatchers.Main){
                        mutableWatchListDetailItem.value=watchList?: emptyList()
                    }

                    val filterList=
                        sharedViewModel.mutableAllData.value?.data?.filter { p1-> watchList!!.any { p2-> p1.sYM.equals(p2.watchListSymbol,true) } }
                            ?.toList()?: emptyList()
                    withContext(Dispatchers.Main){
                        if(watchList?.size==filterList.size)
                        mutableWatchListDetail.value = Resource.Success(filterList)
                    }
                }catch (e:Exception){
                    withContext(Dispatchers.Main){
                        mutableWatchListDetail.value = Resource.Error(e.message?:"An error occurred")
                    }
                }
                delay(5000)
            }
        }
    }

    fun updateWatchList(text: String, watchListItem: WatchListItem, registrationID: Int) {
        viewModelScope.launch {
            mutableWatchListItem.value = Resource.Loading()
            mutableWatchListItem.value =  repository.updateWatchList(text,watchListItem.WatchListMainID,registrationID)

        }
    }
}