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

    public fun saveContactIban(mobileNumber:String, registerUnder:String,iban:String){
        repository.saveContactIban(mobileNumber, registerUnder,iban)
    }
    fun saveDocuments(ibanFileName:String,iban:String,nicFrontFileName:String,nicFront:String,nicBackFileName:String,nicBack:String){
        repository.saveDocuments(ibanFileName, iban, nicFrontFileName, nicFront, nicBackFileName, nicBack)
    }

    public fun getSelfInfo(): AccountOpening {
        return repository.getSelfInfo()
    }

    public fun getContactIban(): AccountOpening {
        return repository.getContactIban()
    }

    fun getDocuments(): AccountOpening {
        return repository.getDocuments()
    }

}