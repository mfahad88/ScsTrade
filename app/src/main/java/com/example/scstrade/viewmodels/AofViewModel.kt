package com.example.scstrade.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.scstrade.model.Resource
import com.example.scstrade.model.data.AccountOpening
import com.example.scstrade.model.data.AttorneyDetail
import com.example.scstrade.model.data.BasicData
import com.example.scstrade.model.data.ContactDetail
import com.example.scstrade.model.data.Nominee
import com.example.scstrade.model.data.OtherDetail
import com.example.scstrade.model.request.aof.LoginUser
import com.example.scstrade.model.request.aof.RegisterUser
import com.example.scstrade.model.response.ApiResponse
import com.example.scstrade.model.request.aof.attorneyDetail.AttorneyDetailDto
import com.example.scstrade.model.request.aof.basicDetails.BasicDetailDto
import com.example.scstrade.model.response.aof.city.CityDto
import com.example.scstrade.model.request.aof.contactDetails.ContactDetailDto
import com.example.scstrade.model.request.aof.document.DocumentDto
import com.example.scstrade.model.response.aof.country.CountryDto
import com.example.scstrade.model.response.aof.login.LoginResponse
import com.example.scstrade.model.request.aof.nomineeDetail.NomineeDetailDto
import com.example.scstrade.model.request.aof.otherDetail.OtherDetailDto
import com.example.scstrade.model.request.aof.verifyOtp.VerifyOtpDto
import com.example.scstrade.model.response.aof.attorneyDetail.AttorneyDetailResponse
import com.example.scstrade.model.response.aof.basicDetails.BasicDetailResponse
import com.example.scstrade.model.response.aof.contactDetails.ContactDetailResponse
import com.example.scstrade.model.response.aof.nomineeDetail.NomineeDetailResponse
import com.example.scstrade.model.response.aof.otherDetails.OtherDetailResponse
import com.example.scstrade.model.response.aof.protectedApplication.ProtectedResponse
import com.example.scstrade.model.response.aof.register.ResponseRegisterUser
import com.example.scstrade.repository.AofRepository
import com.example.scstrade.services.ApiService
import com.example.scstrade.services.RetrofitInstanceAof
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AofViewModel(application: Application): AndroidViewModel(application) {
    val apiClient=RetrofitInstanceAof.create(ApiService::class.java)
    private val repository = AofRepository(apiClient,application)
    val accountOpening = AccountOpening(null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null)
    val basicData = BasicData(null,null,null,null,null,null,null,null,null,null,null,null,null,null,null)
    val contactDetail = ContactDetail(null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null)
    val attorneyDetail = AttorneyDetail(null,null,null,null,null,null,null,null,null,null,null,null,null,null)
    val nominee = Nominee(null,null,null,null,null,null,null,null,null,null,null,null,null)
    val otherDetail = OtherDetail(null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null)
    val mutableRegisterUser = MutableLiveData<Resource<ResponseRegisterUser>>()
    val mutableLoginUser = MutableLiveData<Resource<LoginResponse>>()
    val mutableProtected = MutableLiveData<Resource<ProtectedResponse>>()
    val mutableBasicData = MutableLiveData<Resource<ApiResponse<Nothing>>>()
    val mutableCreateContactDetail = MutableLiveData<Resource<ApiResponse<Nothing>>>()
    val mutableAttorneyDetail = MutableLiveData<Resource<ApiResponse<Nothing>>>()
    val mutableNomineeDetail = MutableLiveData<Resource<ApiResponse<Nothing>>>()
    val mutableOtherDetail = MutableLiveData<Resource<ApiResponse<Nothing>>>()
    val mutableVerifyOtp = MutableLiveData<Resource<ApiResponse<Nothing>>>()

    var applicationId = "-1"
    val mutableCounty=MutableLiveData<Resource<ApiResponse<List<CountryDto>>>>()
    val mutableCity=MutableLiveData<Resource<ApiResponse<List<CityDto>>>>()
    val mutableDocument=MutableLiveData<Resource<ApiResponse<Nothing>>>()

    val mutableBasicDataResponse = MutableLiveData<Resource<ApiResponse<BasicDetailResponse>>>()
    val mutableContactDetailResponse = MutableLiveData<Resource<ApiResponse<ContactDetailResponse>>>()
    val mutableAttorneyDetailResponse = MutableLiveData<Resource<ApiResponse<AttorneyDetailResponse>>>()
    val mutableNomineeDetailResponse = MutableLiveData<Resource<ApiResponse<NomineeDetailResponse>>>()
    val mutableOtherDetailResponse = MutableLiveData<Resource<ApiResponse<OtherDetailResponse>>>()





    public fun saveContactIban(
        mobileNumber: String,
        registerUnder: String,
        iban: String,
        relativeName: String,
        relativeUin: String,
        relationship_type: String?
    ){
        repository.saveContactIban(mobileNumber, registerUnder,iban,relativeName,relativeUin,relationship_type)
    }
    fun saveDocuments(ibanFileName:String,iban:String,nicFrontFileName:String,nicFront:String,nicBackFileName:String,nicBack:String,proofRelationship:String,proofRelationshipFileName:String){
        repository.saveDocuments(ibanFileName, iban, nicFrontFileName, nicFront, nicBackFileName, nicBack,proofRelationshipFileName,proofRelationship)
    }

    fun saveReference(name:String){
        repository.saveReference(name)
    }

    fun saveaccountOpening(){
        repository.saveaccountOpening(accountOpening)
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

    fun getaccountOpening(): AccountOpening {
        return repository.getaccountOpening()!!
    }
   /* public fun getSelfInfo(): AccountOpening {
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
    }*/

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

    fun registerUser(registerUser: RegisterUser){
        mutableRegisterUser.value =Resource.Loading()
        viewModelScope.launch (Dispatchers.IO){
            val result = repository.registerUser(registerUser)
            withContext(Dispatchers.Main){
                mutableRegisterUser.value = result
            }
        }
    }

    fun loginUser(loginUser: LoginUser){
        mutableLoginUser.value =Resource.Loading()
        viewModelScope.launch (Dispatchers.IO){
            val result = repository.loginUser(loginUser)
            withContext(Dispatchers.Main){
                mutableLoginUser.value = result
            }
        }
    }

    fun protectedAppId(){
        mutableProtected.value = Resource.Loading()
        viewModelScope.launch (Dispatchers.IO){
            val result = repository.protectedAppId()
            withContext(Dispatchers.Main){
                mutableProtected.value = result
            }
        }
    }


    fun basicData(basicDetailDto: BasicDetailDto){
        mutableBasicData.value = Resource.Loading()
        viewModelScope.launch (Dispatchers.IO){
            val result = repository.basicData(basicDetailDto)
            withContext(Dispatchers.Main){
                mutableBasicData.value = result
            }
        }
    }

    fun createContactDetail(contactDetailDto: ContactDetailDto){
        mutableCreateContactDetail.value = Resource.Loading()
        viewModelScope.launch (Dispatchers.IO){
            val result = repository.createContactDetails(contactDetailDto)
            withContext(Dispatchers.Main){
                mutableCreateContactDetail.value = result
            }
        }
    }

    fun getcontactDetails(){
        mutableContactDetailResponse.value = Resource.Loading()
        viewModelScope.launch (Dispatchers.IO){
            val result = repository.getContactDetails(applicationId)
            withContext(Dispatchers.Main){
                mutableContactDetailResponse.value = result
            }
        }
    }
    fun getBasicData(){
        mutableBasicDataResponse.value = Resource.Loading()
        viewModelScope.launch{
            val result = repository.getBasicData(applicationId)
            mutableBasicDataResponse.value = result

        }
    }


    fun country(){
        mutableCounty.value =Resource.Loading()
        viewModelScope.launch (Dispatchers.IO){
            val result = repository.country()
            withContext(Dispatchers.Main){
                mutableCounty.value =result
            }
        }
    }

    fun city(){
        mutableCity.value =Resource.Loading()
        viewModelScope.launch (Dispatchers.IO){
            val result = repository.city()
            withContext(Dispatchers.Main){
                mutableCity.value =result
            }
        }
    }

    fun attorneyDetails(attorneyDetailDto: AttorneyDetailDto){
        mutableAttorneyDetail.value = Resource.Loading()
        viewModelScope.launch (Dispatchers.IO){
            val result = repository.attorneyDetails(attorneyDetailDto)
            withContext(Dispatchers.Main){
                mutableAttorneyDetail.value =result
            }
        }
    }


    fun getattorneyDetails(){
        mutableAttorneyDetailResponse.value = Resource.Loading()
        viewModelScope.launch (Dispatchers.IO){
            val result = repository.getAttorneyDetails(applicationId)
            withContext(Dispatchers.Main){
                mutableAttorneyDetailResponse.value =result
            }
        }
    }

    fun nomineeDetails(nomineeDetailDto: NomineeDetailDto){
        mutableNomineeDetail.value = Resource.Loading()
        viewModelScope.launch (Dispatchers.IO){
            val result = repository.nomineeDetails(nomineeDetailDto)
            withContext(Dispatchers.Main){
                mutableNomineeDetail.value =result
            }
        }
    }

    fun getNomineeDetails(){
        mutableNomineeDetailResponse.value = Resource.Loading()
        viewModelScope.launch (Dispatchers.IO){
            val result = repository.getNomineeDetails(applicationId)
            withContext(Dispatchers.Main){
                mutableNomineeDetailResponse.value =result
            }
        }
    }

    fun otherDetails(otherDetailDto: OtherDetailDto){
        mutableOtherDetail.value = Resource.Loading()
        viewModelScope.launch (Dispatchers.IO){
            val result = repository.otherDetails(otherDetailDto)
            withContext(Dispatchers.Main){
                mutableOtherDetail.value =result
            }
        }
    }

    fun getotherDetails(){
        mutableOtherDetailResponse.value = Resource.Loading()
        viewModelScope.launch (Dispatchers.IO){
            val result = repository.getOtherDetails(applicationId)
            withContext(Dispatchers.Main){
                mutableOtherDetailResponse.value =result
            }
        }
    }
    fun verifyOtp(verifyOtpDto: VerifyOtpDto){
        mutableVerifyOtp.value = Resource.Loading()
        viewModelScope.launch (Dispatchers.IO){
            val result = repository.verifyOtp(verifyOtpDto)
            withContext(Dispatchers.Main){
                mutableVerifyOtp.value =result
            }
        }
    }

    fun documents(documentDto: DocumentDto){
        mutableDocument.value = Resource.Loading()
        viewModelScope.launch (Dispatchers.IO){
            val result = repository.documents(documentDto)
            withContext(Dispatchers.Main){
                mutableDocument.value =result
            }
        }
    }

    fun saveAccessToken(accessToken: String?) {
        repository.saveAccessToken(accessToken)
    }


    fun getaccessToken(): String? {
        return repository.getaccessToken()
    }
}