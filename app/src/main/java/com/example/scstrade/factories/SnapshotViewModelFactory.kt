package com.example.scstrade.factories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.viewmodels.SnapshotViewModel

class SnapshotViewModelFactory (private val application: Application, private val sharedViewModel: SharedViewModel):
    ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SnapshotViewModel::class.java)){
            return  SnapshotViewModel(application,sharedViewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
