package com.example.scstrade.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.scstrade.helper.ConnectivityObserver
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.announcement.AnnouncementDataItem
import com.example.scstrade.model.response.announcement.AnnouncementTypeDataItem
import com.example.scstrade.model.response.insider.InsiderDataItem
import com.example.scstrade.repository.MainRepository
import com.example.scstrade.services.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SnapshotViewModel(application: Application, private  val sharedViewModel: SharedViewModel): AndroidViewModel(application) {
    val mutableAnnouncementItem= MutableLiveData<Resource<List<AnnouncementDataItem>>>()
    val mutableAnnouncementType= MutableLiveData<Resource<List<AnnouncementTypeDataItem>>>()
    val mutableInsider= MutableLiveData<Resource<List<InsiderDataItem>>>()
    private val repository= MainRepository(RetrofitInstance.api,application)
    val isConnected = ConnectivityObserver(application)
    fun announcementType() {
        mutableAnnouncementType.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO){
            val result = repository.announcementType()

            withContext(Dispatchers.Main){
                mutableAnnouncementType.value = result
            }
        }
    }

    fun announcement(symbol: String,type:String){
        mutableAnnouncementItem.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO){
            val result = repository.announcements( symbol,type)
            withContext(Dispatchers.Main){
                mutableAnnouncementItem.value = result
            }
        }
    }

    fun insider(symbol: String){
        mutableInsider.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO){
            val result = repository.insider( symbol)
            withContext(Dispatchers.Main){
                mutableInsider.value = result
            }
        }
    }

 /*   fun filterByType(symbol:String,type: String, date: String) {
        mutableFilteredList.value = Resource.Loading()
        try {
            announcement(symbol, type)
            mutableFilteredList.value = Resource.Success(
                listAnnouncement.filter {
                    Utils.compareDates(it.bmDate?:"0L",date)
                }
            )
        }catch ( e:Exception){
            mutableFilteredList.value = Resource.Error(e.message?:"An error occurred...")
        }
    }*/
}