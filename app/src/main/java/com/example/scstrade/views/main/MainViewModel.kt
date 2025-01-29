package com.example.scstrade.views.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scstrade.model.Resource
import com.example.scstrade.model.summary.KSEIndices
import com.example.scstrade.repository.MainRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: MainRepository) : ViewModel() {
    var mutableIndices=  MutableLiveData<Resource<List<KSEIndices>>>(Resource.Loading())
 fun fetchIndices(){
     viewModelScope.launch {
        mutableIndices.value=repository.getIndices()
     }
 }
}