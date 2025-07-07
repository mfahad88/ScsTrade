package com.example.scstrade.viewmodels

import android.app.Application
import android.text.TextUtils
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
import okhttp3.internal.filterList

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

    fun announcement(symbol: String?, type: String, date: String?) {
        mutableAnnouncementItem.value = Resource.Loading()

        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.announcements(if(symbol?.equals("")?:false)null else symbol, date?.let {
                Utils.formatDateString(
                    inputPattern = "dd/MM/yy",
                    outputPattern = "yyyy-MM-dd",
                    inputDate = it
                )
            }, type)


            // ---------- 1.  Handle error or empty response ----------
            if (result !is Resource.Success) {
                withContext(Dispatchers.Main) { mutableAnnouncementItem.value = result }
                return@launch
            }

            // ---------- 2.  Build one lookup map (sYM -> nM) ----------
            val symToName: Map<String, String?> =
                sharedViewModel.mutableAllData.value?.data
                    ?.associate { it.sYM.trim() to it.nM }
                    ?: emptyMap()

            // ---------- 3.  Filter + mutate in ONE pass --------------
            val filtered: MutableList<AnnouncementDataItem> = result.data
                .orEmpty()
                .asSequence()                              // lazy pipeline
              /*  .filter { ann ->                           // (a) TYPE filter
                    type.equals("all", true) ||
                            ann.AnnouncementType.equals(type, true)
                }
                .filter { ann ->                           // (b) DATE filter
                    if (date.isNullOrBlank()) return@filter true
                    val raw = ann.Announcement_Date?.toString()
                        .takeIf { !it.isNullOrBlank() }
                        ?: ann.Meeting_Date?.toString().orEmpty()
                    Utils.compareDates(raw, date)          // keep if ≥ input date
                }*/
                .map { ann -> ann.apply {                  // (c) SET name
                    val code = companyCode?.trim().orEmpty()
                    name = symToName[code].orEmpty()       // "" if not found
                }}
                .toMutableList()

            // ---------- 4.  Post back to UI --------------------------
            withContext(Dispatchers.Main) {
                mutableAnnouncementItem.value = Resource.Success(filtered)
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