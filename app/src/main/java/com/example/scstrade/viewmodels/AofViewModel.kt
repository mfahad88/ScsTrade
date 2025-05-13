package com.example.scstrade.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.scstrade.model.data.AccountOpening
import com.example.scstrade.repository.AofRepository

class AofViewModel(application: Application): AndroidViewModel(application) {
    val repository=AofRepository(application)


    public fun saveSelfInfo(fname:String,email:String, residential:String,nicType: String,nicNumber:String){
        repository.saveSelfInfo(fname, email, residential, nicType, nicNumber)
    }

    public fun getSelfInfo(): AccountOpening {
        return repository.getSelfInfo()
    }
}