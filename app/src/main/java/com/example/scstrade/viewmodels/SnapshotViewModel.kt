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
import com.example.scstrade.services.ApiService
import com.example.scstrade.services.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SnapshotViewModel(application: Application, private  val sharedViewModel: SharedViewModel): AndroidViewModel(application) {
    val mutableAnnouncementItem= MutableLiveData<Resource<List<AnnouncementDataItem>>>()
    val mutableAnnouncementType= MutableLiveData<Resource<List<AnnouncementTypeDataItem>>>()
    val mutableInsider= MutableLiveData<Resource<List<InsiderDataItem>>>()
    private val repository= MainRepository(RetrofitInstance.create(ApiService::class.java),application)
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

    fun announcement(symbol: String,type:String,date:String?){
        mutableAnnouncementItem.value = Resource.Loading()
        if(type.equals("all",true)){
            insider(symbol)
        }
        viewModelScope.launch(Dispatchers.IO){
            val result = repository.announcements( symbol,type)

            withContext(Dispatchers.Main){
                val list=ArrayList<AnnouncementDataItem>()
                if(date.isNullOrBlank()) {
                    list.addAll(result.data?: emptyList())
                    if(type.equals("all",true)) {
                        list.addAll(mutableInsider.value?.data?.map {
                            AnnouncementDataItem(
                                "Insider",
                                bmDesc = it.insiderTransactionDesc,
                                bmDate = it.insiderTransactionPostDate,
                                bmImageLink = it.insiderTransactionImageLink,
                                bmPDFLink = it.insiderTransactionPDFLink,
                                companyCode = null,
                                bmEpsQuarter = null,
                                bmEpsCum = null,
                                bmQuarterNumber = null,
                                bmRightPrice = null,
                                bmRightD = null,
                                bmRightP = null,
                                bmRightPer = null,
                                bmBcStartd = null,
                                bmDividend = null,
                                bmBcLd = null,
                                bmTime = null,
                                bmYear = null,
                                bmBcExp = null,
                                bmBonus = null,
                                bmPlace = null,
                                bmBcEndd = null
                            )
                        }
                            ?: emptyList())
                    }
                }else{
                    list.addAll(result.data?.filter { Utils.compareDates(it.bmDate?:"0L",date) }?: emptyList())
                    if(type.equals("all",true)) {
                        list.addAll(mutableInsider.value?.data?.filter {
                            Utils.compareDates(
                                it.insiderTransactionPostDate ?: "0L", date
                            )
                        }?.map {
                            AnnouncementDataItem(
                                "Insider",
                                bmDesc = it.insiderTransactionDesc,
                                bmDate = it.insiderTransactionPostDate,
                                bmImageLink = it.insiderTransactionImageLink,
                                bmPDFLink = it.insiderTransactionPDFLink,
                                companyCode = null,
                                bmEpsQuarter = null,
                                bmEpsCum = null,
                                bmQuarterNumber = null,
                                bmRightPrice = null,
                                bmRightD = null,
                                bmRightP = null,
                                bmRightPer = null,
                                bmBcStartd = null,
                                bmDividend = null,
                                bmBcLd = null,
                                bmTime = null,
                                bmYear = null,
                                bmBcExp = null,
                                bmBonus = null,
                                bmPlace = null,
                                bmBcEndd = null
                            )
                        }
                            ?: emptyList())
                    }
                }

                mutableAnnouncementItem.value = Resource.Success(list.sortedByDescending { it.bmDate })

            }
        }
    }

    fun insider(symbol: String){
        mutableInsider.value = Resource.Loading()
        viewModelScope.launch{
            val result = repository.insider( symbol)
            mutableInsider.value = result
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