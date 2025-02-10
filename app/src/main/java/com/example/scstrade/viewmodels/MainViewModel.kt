package com.example.scstrade.viewmodels

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scstrade.model.Resource
import com.example.scstrade.model.summary.KSEIndices
import com.example.scstrade.repository.MainRepository
import com.example.scstrade.services.RetrofitInstance
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel() : ViewModel() {
    private val repository: MainRepository=MainRepository(RetrofitInstance.api)
    var mutableIndices=  MutableLiveData<Resource<List<KSEIndices>>>(Resource.Loading())
    private var delayMillis:Long=1*60*1000
    private var isFetch=true


    init {
//        startTimer()
//        fetchIndices()
    }
 fun fetchIndices(){
     isFetch=true
     viewModelScope.launch {
       while (isFetch){
           var resource= repository.getIndices()
           if(resource.data?.first()?.marketStatus?.lowercase()=="open"){
               delayMillis=5000
           }else{
               delayMillis=1*60*1000
           }
           mutableIndices.value=resource
           delay(delayMillis)
       }
     }
 }

    override fun onCleared() {
        super.onCleared()
        stopIndices()
    }

    fun stopIndices() {
        isFetch = false
    }
}