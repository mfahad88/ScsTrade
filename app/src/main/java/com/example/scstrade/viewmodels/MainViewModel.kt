package com.example.scstrade.viewmodels

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scstrade.model.Resource
import com.example.scstrade.model.summary.KSEIndices
import com.example.scstrade.repository.MainRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: MainRepository) : ViewModel() {
    var mutableIndices=  MutableLiveData<Resource<List<KSEIndices>>>(Resource.Loading())
    private val handler = Handler(Looper.getMainLooper())
    private val updateTask= object :Runnable {
        override fun run() {
            fetchIndices()
            handler.postDelayed(this,5000)
        }
    }

    init {
//        handler.post(updateTask)
        fetchIndices()
    }
 fun fetchIndices(){
     viewModelScope.launch {
        mutableIndices.value=repository.getIndices()

     }
 }

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacks(updateTask)
    }
}