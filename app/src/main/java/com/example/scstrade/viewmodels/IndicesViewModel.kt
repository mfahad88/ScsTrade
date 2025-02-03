package com.example.scstrade.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scstrade.model.Resource
import com.example.scstrade.model.summary.KSEIndices
import com.example.scstrade.repository.IndicesRepository
import kotlinx.coroutines.launch

class IndicesViewModel(private  val repository: IndicesRepository) : ViewModel() {
    val mutableLiveData = MutableLiveData<Resource<List<KSEIndices>>>()
    fun fetchIndices(){
        viewModelScope.launch {
            mutableLiveData.value=Resource.Loading()
            mutableLiveData.value = repository.fetchIndices()
        }
    }
}