package com.example.scstrade.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class WatchListViewModelFactory(private val application: Application,private val sharedViewModel: SharedViewModel):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(WatchListViewModel::class.java)){
            return  WatchListViewModel(application,sharedViewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}