package com.example.scstrade.viewmodels

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.scstrade.helper.Utils
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
import com.example.scstrade.model.request.aof.nomineeDetail.NomineeDetailDto
import com.example.scstrade.model.request.aof.otherDetail.OtherDetailDto
import com.example.scstrade.model.request.aof.verifyOtp.VerifyOtpDto
import com.example.scstrade.model.response.aof.attorneyDetail.AttorneyDetailResponse
import com.example.scstrade.model.response.aof.basicDetails.BasicDetailResponse
import com.example.scstrade.model.response.aof.contactDetails.ContactDetailResponse
import com.example.scstrade.model.response.aof.login.Data
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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream

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
    val mutableLoginUser = MutableLiveData<Resource<ApiResponse<Data>>>()
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

    val mutableDocumentDataResponse = MutableLiveData<Resource<ApiResponse<DocumentDto>>>()
    val mutableBasicDataResponse = MutableLiveData<Resource<ApiResponse<BasicDetailResponse>>>()
    val mutableContactDetailResponse = MutableLiveData<Resource<ApiResponse<ContactDetailResponse>>>()
    val mutableAttorneyDetailResponse = MutableLiveData<Resource<ApiResponse<AttorneyDetailResponse>>>()
    val mutableNomineeDetailResponse = MutableLiveData<Resource<ApiResponse<NomineeDetailResponse>>>()
    val mutableOtherDetailResponse = MutableLiveData<Resource<ApiResponse<OtherDetailResponse>>>()
    val mutableLifeCycle = MutableLiveData<Int>()
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

  /*  public fun getbasicData(): BasicData? {
        return repository.getbasicData()
    }*/

    fun getContactDetails(): ContactDetail {
        return repository.getContactDetails()!!
    }

    fun getAttorneyDetails(): AttorneyDetail {
        return repository.getAttorneyDetails()!!

    }

    fun  getnominee():Nominee{
        return  repository.getNominee()!!
    }

  /*  fun getotherDetail():OtherDetail{
        return  repository.getotherDetail()!!
    }*/

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
//        mutableLoginUser.value =Resource.Loading()
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
                mutableLifeCycle.value = result.data?.user?.lifecycleStatusId
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
                mutableLifeCycle.value = result.data?.status
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

    fun getDocuments(){
        mutableDocumentDataResponse.value = Resource.Loading()
        viewModelScope.launch{
            val result = repository.getDocuments(applicationId)
            mutableDocumentDataResponse.value = result

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
                mutableLifeCycle.value = result.data?.status
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
                mutableLifeCycle.value = result.data?.status
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
                mutableLifeCycle.value = result.data?.status
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

    fun documents(/*documentDto: DocumentDto*/
                  context: Context,
                  zakatStatus: String,
                  accountType: String,
                  terms: String,
                  idType: String,
                  sigUri: String,
                  empUri: String,
                  addUri: String,
                  zakaatUri: String){
        mutableDocument.value = Resource.Loading()
        viewModelScope.launch (Dispatchers.IO){
            val zakat = zakatStatus.toRequestBody("text/plain".toMediaTypeOrNull())
            val account = accountType.toRequestBody("text/plain".toMediaTypeOrNull())
            val termsBody = terms.toRequestBody("text/plain".toMediaTypeOrNull())
            val id = idType.toRequestBody("text/plain".toMediaTypeOrNull())
            val signatureProof = sigUri.toRequestBody("text/plain".toMediaTypeOrNull())
            val empAddProof = empUri.toRequestBody("text/plain".toMediaTypeOrNull())
            val addProof = addUri.toRequestBody("text/plain".toMediaTypeOrNull())
            val zakaatDeclaration = zakaatUri.toRequestBody("text/plain".toMediaTypeOrNull())
            /*val signature = uriToPart(context, sigUri, "signatureProof")
            val emp = uriToPart(context, empUri, "empAddProof")
            val add = uriToPart(context, addUri, "addProof")
            val zakaat = uriToPart(context, zakaatUri, "zakaatDeclaration")*/
            val result = repository.documents(zakat, account, termsBody, id, signatureProof, empAddProof, addProof, zakaatDeclaration)
            withContext(Dispatchers.Main){
                mutableLifeCycle.value = result.data?.status
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