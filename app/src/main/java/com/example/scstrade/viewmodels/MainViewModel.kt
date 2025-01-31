package com.example.scstrade.viewmodels

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scstrade.model.Resource
import com.example.scstrade.model.summary.KSEIndices
import com.example.scstrade.repository.MainRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel(private val repository: MainRepository) : ViewModel() {
    var mutableIndices=  MutableLiveData<Resource<List<KSEIndices>>>(Resource.Loading())
    private val handler = Handler(Looper.getMainLooper())
    private var delayMillis:Long=1*60*1000
    private val updateTask= object :Runnable {
        override fun run() {
            fetchIndices()
            handler.postDelayed(this,delayMillis)

        }
    }

    private fun startTimer() {
        handler.post(updateTask)
    }

    private fun stopTimer(){
        handler.removeCallbacks(updateTask)
    }

    init {
//        startTimer()
        fetchIndices()
    }
 fun fetchIndices(){
     viewModelScope.launch {
       while (true){
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
//        stopTimer()
    }
}