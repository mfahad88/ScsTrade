package com.example.scstrade.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.scstrade.model.data.AccountOpening
import com.example.scstrade.model.data.AttorneyDetail
import com.example.scstrade.model.data.BasicData
import com.example.scstrade.model.data.ContactDetail
import com.example.scstrade.model.data.Nominee
import com.example.scstrade.model.data.OtherDetail
import com.example.scstrade.repository.AofRepository

class AofViewModel(application: Application): AndroidViewModel(application) {
    private val repository=AofRepository(application)
    val basicData = BasicData(null,null,null,null,null,null,null,null,null,null,null,null,null,null,null)
    val contactDetail = ContactDetail(null,null,null,null,null,null,null,null,null,null,null,null,null,null)
    val attorneyDetail = AttorneyDetail(null,null,null,null,null,null,null,null,null,null,null)
    val nominee = Nominee(null,null,null,null,null,null,null,null,null,null,null,null)
    val otherDetail = OtherDetail(null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null)
    public fun saveSelfInfo(fname:String,email:String, residential:String,nicType: String,nicNumber:String){
        repository.saveSelfInfo(fname, email, residential, nicType, nicNumber)
    }

    public fun saveContactIban(mobileNumber:String, registerUnder:String,iban:String){
        repository.saveContactIban(mobileNumber, registerUnder,iban)
    }
    fun saveDocuments(ibanFileName:String,iban:String,nicFrontFileName:String,nicFront:String,nicBackFileName:String,nicBack:String){
        repository.saveDocuments(ibanFileName, iban, nicFrontFileName, nicFront, nicBackFileName, nicBack)
    }

    fun saveReference(name:String){
        repository.saveReference(name)
    }

    fun saveBasicData(){
        repository.saveBasicData(basicData)
    }

    public fun saveContactDetails(){
        repository.saveContactDetails(contactDetail)
    }

    public fun savenominee(){
        repository.saveNominee(nominee)
    }

    fun saveAttorneyDetails(){
        repository.saveAttorneyDetails(attorneyDetail)
    }

    fun saveotherDetail(){
        repository.saveotherDetail(otherDetail)
    }

    public fun getSelfInfo(): AccountOpening {
        return repository.getSelfInfo()
    }

    public fun getContactIban(): AccountOpening {
        return repository.getContactIban()
    }

    public fun getDocuments(): AccountOpening {
        return repository.getDocuments()
    }

    public fun getReference(): AccountOpening {
        return repository.getReference()
    }

    public fun getbasicData(): BasicData {
        return repository.getbasicData()
    }

    fun getContactDetails(): ContactDetail {
        return repository.getContactDetails()!!
    }

    fun getAttorneyDetails(): AttorneyDetail {
        return repository.getAttorneyDetails()!!

    }

    fun  getnominee():Nominee{
        return  repository.getNominee()!!
    }

    fun getotherDetail():OtherDetail{
        return  repository.getotherDetail()!!
    }
}