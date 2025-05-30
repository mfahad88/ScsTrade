package com.example.scstrade.repository

import android.content.Context
import com.example.scstrade.helper.AppConstants
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
import com.example.scstrade.services.ApiService
import org.json.JSONObject
import kotlin.reflect.full.primaryConstructor

class AofRepository (val apiService: ApiService,val context: Context){

    private val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    private val accountOpening = AccountOpening(null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null)
    private val basicData = BasicData(null,null,null,null,null,null,null,null,null,null,null,null,null,null,null)
    private val contactDetail = ContactDetail(null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null)
    /*public fun saveSelfInfo(
        fname: String,
        email: String,
        residential: String,
        nicType: String,
        nicNumber: String,
        issue_date: String
    ){
        sharedPreferences.edit().apply {
            putString(AppConstants.ACCOUNT_OPENING_FULLNAME,fname)
            putString(AppConstants.ACCOUNT_OPENING_EMAIL,email)
            putString(AppConstants.ACCOUNT_OPENING_RESIDENTIAL,residential)
            putString(AppConstants.ACCOUNT_OPENING_NIC_TYPE,nicType)
            putString(AppConstants.ACCOUNT_OPENING_NIC_NUMBER,nicNumber)
            putString(AppConstants.ACCOUNT_OPENING_NIC_ISSUE_DATE,issue_date)
            apply()
        }
    }

    public fun getSelfInfo(): AccountOpening {
        accountOpening.fullName = sharedPreferences.getString(AppConstants.ACCOUNT_OPENING_FULLNAME,"")
        accountOpening.emailAddress = sharedPreferences.getString(AppConstants.ACCOUNT_OPENING_EMAIL,"")
        accountOpening.residentialStatus = sharedPreferences.getString(AppConstants.ACCOUNT_OPENING_RESIDENTIAL,"")
        accountOpening.nicType = sharedPreferences.getString(AppConstants.ACCOUNT_OPENING_NIC_TYPE,"")
        accountOpening.nicNumber = sharedPreferences.getString(AppConstants.ACCOUNT_OPENING_NIC_NUMBER,"")
        accountOpening.nicIssueDate = sharedPreferences.getString(AppConstants.ACCOUNT_OPENING_NIC_ISSUE_DATE,"")
        accountOpening.reference = sharedPreferences.getString(AppConstants.ACCOUNT_OPENING_REFERENCE,"")
        return accountOpening

    }*/

    fun saveContactIban(
        mobileNumber: String,
        registerUnder: String,
        iban: String,
        relativeName: String,
        relativeUin: String,
        relationship_type: String?
    ) {
        sharedPreferences.edit().apply {
            putString(AppConstants.ACCOUNT_OPENING_MOBILE_NUMBER,mobileNumber)
            putString(AppConstants.ACCOUNT_OPENING_REGISTERED_UNDER,registerUnder)
            putString(AppConstants.ACCOUNT_OPENING_BANK_IBAN,iban)
            putString(AppConstants.ACCOUNT_OPENING_RELATIVE_NAME,relativeName)
            putString(AppConstants.ACCOUNT_OPENING_RELATIVE_UIN,relativeUin)
            putString(AppConstants.ACCOUNT_OPENING_RELATIONSHIP_TYPE,relationship_type)
            apply()
        }
    }

  /*  fun getContactIban(): AccountOpening {
        accountOpening.mobileNumber = sharedPreferences.getString(AppConstants.ACCOUNT_OPENING_MOBILE_NUMBER,"")
        accountOpening.registerUnder = sharedPreferences.getString(AppConstants.ACCOUNT_OPENING_REGISTERED_UNDER,"")
        accountOpening.ibanNumber = sharedPreferences.getString(AppConstants.ACCOUNT_OPENING_BANK_IBAN,"")
        accountOpening.relativeName = sharedPreferences.getString(AppConstants.ACCOUNT_OPENING_RELATIVE_NAME,"")
        accountOpening.relativeUin = sharedPreferences.getString(AppConstants.ACCOUNT_OPENING_RELATIVE_UIN,"")
        accountOpening.relationshipType = sharedPreferences.getString(AppConstants.ACCOUNT_OPENING_RELATIONSHIP_TYPE,"")

        return accountOpening
    }
*/
    fun saveDocuments(
        ibanFileName: String,
        iban: String,
        nicFrontFileName: String,
        nicFront: String,
        nicBackFileName: String,
        nicBack: String,
        proofRelationshipFileName: String,
        proofRelationship: String
    ){
        sharedPreferences.edit().apply{
            putString(AppConstants.DOCUMENT_IBAN_NAME,ibanFileName)
            putString(AppConstants.DOCUMENT_IBAN,iban)
            putString(AppConstants.DOCUMENT_NIC_FRONT_NAME,nicFrontFileName)
            putString(AppConstants.DOCUMENT_NIC_FRONT,nicFront)
            putString(AppConstants.DOCUMENT_NIC_BACK_NAME,nicBackFileName)
            putString(AppConstants.DOCUMENT_NIC_BACK,nicBack)
            putString(AppConstants.DOCUMENT_RELATIONSHIP_NAME,proofRelationshipFileName)
            putString(AppConstants.DOCUMENT_RELATIONSHIP,proofRelationship)
            apply()
        }
    }

    /*fun getDocuments(): AccountOpening {
        accountOpening.proofIban=sharedPreferences.getString(AppConstants.DOCUMENT_IBAN_NAME,"")
        accountOpening.proofIbanImage=sharedPreferences.getString(AppConstants.DOCUMENT_IBAN,"")

        accountOpening.nicFront=sharedPreferences.getString(AppConstants.DOCUMENT_NIC_FRONT_NAME,"")
        accountOpening.nicFrontImage=sharedPreferences.getString(AppConstants.DOCUMENT_NIC_FRONT,"")

        accountOpening.nicBack=sharedPreferences.getString(AppConstants.DOCUMENT_NIC_BACK_NAME,"")
        accountOpening.nicBackImage=sharedPreferences.getString(AppConstants.DOCUMENT_NIC_BACK,"")

        accountOpening.proofRelative=sharedPreferences.getString(AppConstants.DOCUMENT_RELATIONSHIP_NAME,"")
        accountOpening.proofRelativeImage=sharedPreferences.getString(AppConstants.DOCUMENT_RELATIONSHIP,"")
        return accountOpening
    }*/

    fun saveReference(name: String) {
        sharedPreferences.edit().apply{
            putString(AppConstants.ACCOUNT_OPENING_REFERENCE,name)
            apply()
        }
    }

 /*   fun getReference(): AccountOpening {
        accountOpening.reference = sharedPreferences.getString(AppConstants.ACCOUNT_OPENING_REFERENCE,"")
        return accountOpening
    }*/


    fun saveBasicData(basicData: BasicData){
        sharedPreferences.edit().apply {
            if(basicData.uinType?.isNotEmpty()?:false){
                putString(AppConstants.BASIC_DATA_UIN_TYPE,basicData.uinType)
            }

            if(basicData.uinNumber?.isNotEmpty()?:false){
                putString(AppConstants.BASIC_DATA_UIN_NUMBER,basicData.uinNumber)
            }

            if(basicData.fullNicName?.isNotEmpty()?:false){
                putString(AppConstants.BASIC_DATA_FULL_NIC_NAME,basicData.fullNicName)
            }

            if(basicData.salutation?.isNotEmpty()?:false){
                putString(AppConstants.BASIC_DATA_SALUTATION,basicData.salutation)
            }

            if(basicData.dob?.isNotEmpty()?:false){
                putString(AppConstants.BASIC_DATA_DOB,basicData.dob)
            }

            if(basicData.motherMaidenName?.isNotEmpty()?:false){
                putString(AppConstants.BASIC_DATA_MOTHER_MAIDEN_NAME,basicData.motherMaidenName)
            }

            if(basicData.nationality?.isNotEmpty()?:false){
                putString(AppConstants.BASIC_DATA_NATIONALITY,basicData.nationality)
            }

            if(basicData.maritalStatus?.isNotEmpty()?:false){
                putString(AppConstants.BASIC_DATA_MARITAL_STATUS,basicData.maritalStatus)
            }

            if(basicData.relationShip?.isNotEmpty()?:false){
                putString(AppConstants.BASIC_DATA_RELATIONSHIP,basicData.relationShip)
            }

            if(basicData.relationshipName?.isNotEmpty()?:false){
                putString(AppConstants.BASIC_DATA_RELATIONSHIP_NAME,basicData.relationshipName)
            }

            if(basicData.nicType?.isNotEmpty()?:false){
                putString(AppConstants.BASIC_DATA_NIC_TYPE,basicData.nicType)
            }

            if(basicData.nicValid?.isNotEmpty()?:false){
                putString(AppConstants.BASIC_DATA_NIC_EXPIRY,basicData.nicValid)
            }

            if(basicData.pobCountry?.isNotEmpty()?:false){
                putString(AppConstants.BASIC_DATA_POB_COUNTRY,basicData.pobCountry)
            }

            if(basicData.pobCity?.isNotEmpty()?:false){
                putString(AppConstants.BASIC_DATA_POB_CITY,basicData.pobCity)
            }

            if(basicData.ivrService?.isNotEmpty()?:false){
                putString(AppConstants.BASIC_DATA_IVR_SERVICE,basicData.ivrService)
            }
            apply()
        }
    }

    fun getbasicData(): BasicData? {
        basicData.uinType = sharedPreferences.getString(AppConstants.BASIC_DATA_UIN_TYPE,"")
        basicData.uinNumber = sharedPreferences.getString(AppConstants.BASIC_DATA_UIN_NUMBER,"")
        basicData.salutation = sharedPreferences.getString(AppConstants.BASIC_DATA_SALUTATION,"")
        basicData.fullNicName = sharedPreferences.getString(AppConstants.BASIC_DATA_FULL_NIC_NAME,"")
        basicData.dob = sharedPreferences.getString(AppConstants.BASIC_DATA_DOB,"")
        basicData.motherMaidenName = sharedPreferences.getString(AppConstants.BASIC_DATA_MOTHER_MAIDEN_NAME,"")
        basicData.nationality = sharedPreferences.getString(AppConstants.BASIC_DATA_NATIONALITY,"")
        basicData.maritalStatus = sharedPreferences.getString(AppConstants.BASIC_DATA_MARITAL_STATUS,"")
        basicData.relationShip = sharedPreferences.getString(AppConstants.BASIC_DATA_RELATIONSHIP,"")
        basicData.relationshipName = sharedPreferences.getString(AppConstants.BASIC_DATA_RELATIONSHIP_NAME,"")
        basicData.nicType = sharedPreferences.getString(AppConstants.BASIC_DATA_NIC_TYPE,"")
        basicData.nicValid = sharedPreferences.getString(AppConstants.BASIC_DATA_NIC_EXPIRY,"")
        basicData.pobCountry = sharedPreferences.getString(AppConstants.BASIC_DATA_POB_COUNTRY,"")
        basicData.pobCity = sharedPreferences.getString(AppConstants.BASIC_DATA_POB_CITY,"")
        basicData.ivrService = sharedPreferences.getString(AppConstants.BASIC_DATA_IVR_SERVICE,"")
        return basicData
    }

    fun saveaccountOpening(accountOpening: AccountOpening){
        sharedPreferences.edit().apply{
            val kClass = AccountOpening::class
            val properties = kClass.members.filterIsInstance<kotlin.reflect.KProperty1<Any, *>>()
            for (property in properties){
                val value = property.get(accountOpening)
                val name = property.name
                if(value!=null) {
                    if ((value as String).isNotEmpty()) {
                        putString(name, value)
                    }
                }
            }
            apply()
        }
    }

    fun saveContactDetails(contactDetail: ContactDetail){

        sharedPreferences.edit().apply{
            val kClass = ContactDetail::class
            val properties = kClass.members.filterIsInstance<kotlin.reflect.KProperty1<Any, *>>()
            for (property in properties){
                val value = property.get(contactDetail)
                val name = property.name
                if(value!=null) {
                    if ((value as String).isNotEmpty()) {
                        putString(name, value)
                    }
                }
            }
            apply()
        }
    }


    fun getaccountOpening():AccountOpening?{
        val constructor = AccountOpening::class.primaryConstructor?:return null
        val args = constructor.parameters.associateWith { param ->
            val key = param.name ?: return@associateWith null
            when (param.type.classifier) {
                String::class -> sharedPreferences.getString(key, "")
                Int::class -> sharedPreferences.getInt(key, 0)
                Boolean::class -> sharedPreferences.getBoolean(key, false)
                Float::class -> sharedPreferences.getFloat(key, 0f)
                Long::class -> sharedPreferences.getLong(key, 0L)
                else -> null
            }
        }
        return constructor.callBy(args)
    }

    fun getContactDetails(): ContactDetail? {
        val constructor = ContactDetail::class.primaryConstructor?:return null
        val args = constructor.parameters.associateWith { param ->
            val key = param.name ?: return@associateWith null
            when (param.type.classifier) {
                String::class -> sharedPreferences.getString(key, "")
                Int::class -> sharedPreferences.getInt(key, 0)
                Boolean::class -> sharedPreferences.getBoolean(key, false)
                Float::class -> sharedPreferences.getFloat(key, 0f)
                Long::class -> sharedPreferences.getLong(key, 0L)
                else -> null
            }
        }
        return constructor.callBy(args)
    }

    fun saveAttorneyDetails(attorneyDetail: AttorneyDetail) {
        sharedPreferences.edit().apply{
            val kClass = AttorneyDetail::class
            val properties = kClass.members.filterIsInstance<kotlin.reflect.KProperty1<Any, *>>()
            for (property in properties){
                val value = property.get(attorneyDetail)
                val name = property.name
                if(value!=null) {
                    if ((value as String).isNotEmpty()) {
                        putString(name, value)
                    }
                }
            }
            apply()
        }
    }

    fun getAttorneyDetails(): AttorneyDetail? {
        val constructor = AttorneyDetail::class.primaryConstructor?:return null
        val args = constructor.parameters.associateWith { param ->
            val key = param.name ?: return@associateWith null
            when (param.type.classifier) {
                String::class -> sharedPreferences.getString(key, "")
                Int::class -> sharedPreferences.getInt(key, 0)
                Boolean::class -> sharedPreferences.getBoolean(key, false)
                Float::class -> sharedPreferences.getFloat(key, 0f)
                Long::class -> sharedPreferences.getLong(key, 0L)
                else -> null
            }
        }
        return constructor.callBy(args)
    }

    fun saveNominee(nominee: Nominee) {
        sharedPreferences.edit().apply{
            val kClass = Nominee::class
            val properties = kClass.members.filterIsInstance<kotlin.reflect.KProperty1<Any, *>>()
            for (property in properties){
                val value = property.get(nominee)
                val name = property.name
                if(value!=null) {
                    if ((value as String).isNotEmpty()) {
                        putString(name, value)
                    }
                }
            }
            apply()
        }
    }

    fun getNominee(): Nominee? {
        val constructor = Nominee::class.primaryConstructor?:return null
        val args = constructor.parameters.associateWith { param ->
            val key = param.name ?: return@associateWith null
            when (param.type.classifier) {
                String::class -> sharedPreferences.getString(key, "")
                Int::class -> sharedPreferences.getInt(key, 0)
                Boolean::class -> sharedPreferences.getBoolean(key, false)
                Float::class -> sharedPreferences.getFloat(key, 0f)
                Long::class -> sharedPreferences.getLong(key, 0L)
                else -> null
            }
        }
        return constructor.callBy(args)
    }

    fun saveotherDetail(otherDetail: OtherDetail) {
        sharedPreferences.edit().apply{
            val kClass = OtherDetail::class
            val properties = kClass.members.filterIsInstance<kotlin.reflect.KProperty1<Any, *>>()
            for (property in properties){
                val value = property.get(otherDetail)
                val name = property.name
                if(value!=null) {
                    if ((value as String).isNotEmpty()) {
                        putString(name, value)
                    }
                }
            }
            apply()
        }
    }



    fun getotherDetail(): OtherDetail? {
        val constructor = OtherDetail::class.primaryConstructor?:return null
        val args = constructor.parameters.associateWith { param ->
            val key = param.name ?: return@associateWith null
            when (param.type.classifier) {
                String::class -> sharedPreferences.getString(key, "")
                Int::class -> sharedPreferences.getInt(key, 0)
                Boolean::class -> sharedPreferences.getBoolean(key, false)
                Float::class -> sharedPreferences.getFloat(key, 0f)
                Long::class -> sharedPreferences.getLong(key, 0L)
                else -> null
            }
        }
        return constructor.callBy(args)
    }


    fun clearBasicData() {
        sharedPreferences.edit().apply {
            remove(AppConstants.BASIC_DATA_UIN_TYPE)
            remove(AppConstants.BASIC_DATA_UIN_NUMBER)
            remove(AppConstants.BASIC_DATA_FULL_NIC_NAME)
            remove(AppConstants.BASIC_DATA_SALUTATION)
            remove(AppConstants.BASIC_DATA_DOB)
            remove(AppConstants.BASIC_DATA_MOTHER_MAIDEN_NAME)
            remove(AppConstants.BASIC_DATA_NATIONALITY)
            remove(AppConstants.BASIC_DATA_MARITAL_STATUS)
            remove(AppConstants.BASIC_DATA_RELATIONSHIP)
            remove(AppConstants.BASIC_DATA_RELATIONSHIP_NAME)
            remove(AppConstants.BASIC_DATA_NIC_TYPE)
            remove(AppConstants.BASIC_DATA_NIC_EXPIRY)
            remove(AppConstants.BASIC_DATA_POB_COUNTRY)
            remove(AppConstants.BASIC_DATA_POB_CITY)
            remove(AppConstants.BASIC_DATA_IVR_SERVICE)
            apply()
        }
    }

    fun clearAccountOpeningData() {
        sharedPreferences.edit().apply {
            val properties = AccountOpening::class.members
                .filterIsInstance<kotlin.reflect.KProperty1<AccountOpening, *>>()

            for (property in properties) {
                val name = property.name
                remove(name)
            }
            apply()
        }
    }


    fun clearAttorneyDetails() {
        sharedPreferences.edit().apply {
            val properties = AttorneyDetail::class.members
                .filterIsInstance<kotlin.reflect.KProperty1<AttorneyDetail, *>>()

            for (property in properties) {
                val name = property.name
                remove(name)
            }
            apply()
        }
    }

    fun clearNominee() {
        sharedPreferences.edit().apply {
            val properties = Nominee::class.members
                .filterIsInstance<kotlin.reflect.KProperty1<Nominee, *>>()

            for (property in properties) {
                val name = property.name
                remove(name)
            }
            apply()
        }
    }

    suspend fun registerUser(registerUser: RegisterUser): Resource<ResponseRegisterUser> {
        try{
            return  Resource.Success(apiService.registerAof(registerUser))
        }catch (e:Exception){
            return Resource.Error(e.message?:"An error occurred")
        }
    }

    suspend fun loginUser(loginUser: LoginUser):Resource<ApiResponse<Data>> {


        try{
            val response = apiService.loginAof(loginUser)
            if(response.isSuccessful) {
                return Resource.Success(response.body()!!)
            }else{
                    val jsonObject = response.errorBody()?.string()?.let { JSONObject(it) }

                return Resource.Error(jsonObject?.getString("message")?:"An error occurred...")
            }
        }catch (e:Exception){
            return Resource.Error(e.localizedMessage ?: "An error occurred")
        }
    }

    suspend fun protectedAppId(): Resource<ProtectedResponse> {
        try{
            return  Resource.Success(apiService.protected())
        }catch (e:Exception){
            return Resource.Error(e.message?:"An error occurred")
        }
    }

    suspend fun basicData(basicDetailDto: BasicDetailDto): Resource<ApiResponse<Nothing>> {
        try{
            val response = apiService.basicData(basicDetailDto)
            if(response.isSuccessful){
                return Resource.Success(response.body()!!)
            }else{
                val jsonObject = response.errorBody()?.string()?.let { JSONObject(it) }

                return Resource.Error(jsonObject?.getString("message")?:"An error occurred...")
            }
        }catch (e:Exception){
            return Resource.Error(e.message?:"An error occurred")
        }
    }

    suspend fun createContactDetails(contactDetailDto: ContactDetailDto):Resource<ApiResponse<Nothing>>{
        try{
            val response = apiService.createContactDetails(contactDetailDto)
            if(response.isSuccessful){
                return Resource.Success(response.body()!!)
            }else{
                val jsonObject = response.errorBody()?.string()?.let { JSONObject(it) }

                return Resource.Error(jsonObject?.getString("message")?:"An error occurred...")
            }
        }catch (e:Exception){
            return Resource.Error(e.message?:"An error occurred")
        }
    }

    suspend fun country():Resource<ApiResponse<List<CountryDto>>>{
        try{
            val response = apiService.country()
            if(response.isSuccessful){
                return Resource.Success(response.body()!!)
            }else{
                val jsonObject = response.errorBody()?.string()?.let { JSONObject(it) }

                return Resource.Error(jsonObject?.getString("message")?:"An error occurred...")
            }
        }catch (e:Exception){
            return Resource.Error(e.message?:"An error occurred")
        }
    }

    suspend fun city():Resource<ApiResponse<List<CityDto>>>{
        try{
            val response = apiService.city()
            if(response.isSuccessful){
                return Resource.Success(response.body()!!)
            }else{
                val jsonObject = response.errorBody()?.string()?.let { JSONObject(it) }

                return Resource.Error(jsonObject?.getString("message")?:"An error occurred...")
            }
        }catch (e:Exception){
            return Resource.Error(e.message?:"An error occurred")
        }
    }

    suspend fun attorneyDetails(attorneyDetailDto: AttorneyDetailDto):Resource<ApiResponse<Nothing>>{
        try {
            val response = apiService.attorneyDetails(attorneyDetailDto)
            if(response.isSuccessful){
                return Resource.Success(response.body()!!)
            }else{
                val jsonObject = response.errorBody()?.string()?.let { JSONObject(it) }

                return Resource.Error(jsonObject?.getString("message")?:"An error occurred...")
            }
        }catch (e:Exception){
            return Resource.Error(e.message?:"An error occurred")
        }
    }

    suspend fun nomineeDetails(nomineeDetailDto: NomineeDetailDto):Resource<ApiResponse<Nothing>>{
        try {
            val response = apiService.nomineeDetails(nomineeDetailDto)
            if(response.isSuccessful){
                return Resource.Success(response.body()!!)
            }else{
                val jsonObject = response.errorBody()?.string()?.let { JSONObject(it) }

                return Resource.Error(jsonObject?.getString("message")?:"An error occurred...")
            }
        }catch (e:Exception){
            return Resource.Error(e.message?:"An error occurred")
        }
    }

    suspend fun otherDetails(otherDetailDto: OtherDetailDto):Resource<ApiResponse<Nothing>>{
        try {
            val response = apiService.otherDetails(otherDetailDto)
            if(response.isSuccessful){
                return Resource.Success(response.body()!!)
            }else{
                val jsonObject = response.errorBody()?.string()?.let { JSONObject(it) }

                return Resource.Error(jsonObject?.getString("message")?:"An error occurred...")
            }
        }catch (e:Exception){
            return Resource.Error(e.message?:"An error occurred")
        }
    }

    suspend fun documents(documentDto: DocumentDto):Resource<ApiResponse<Nothing>>{
        try {
            val response = apiService.documents(documentDto)
            if(response.isSuccessful){
                return Resource.Success(response.body()!!)
            }else{
                val jsonObject = response.errorBody()?.string()?.let { JSONObject(it) }

                return Resource.Error(jsonObject?.getString("message")?:"An error occurred...")
            }
        }catch (e:Exception){
            return Resource.Error(e.message?:"An error occurred")
        }
    }

    suspend fun getDocuments(applicationId:String):Resource<ApiResponse<DocumentDto>>{
        try {
            val response = apiService.getDocuments(applicationId)
            if(response.isSuccessful){
                return Resource.Success(response.body()!!)
            }else{
                val jsonObject = response.errorBody()?.string()?.let { JSONObject(it) }

                return Resource.Error(jsonObject?.getString("message")?:"An error occurred...")
            }
        }catch (e:Exception){
            return Resource.Error(e.message?:"An error occurred")
        }
    }

    suspend fun getBasicData(applicationId:String):Resource<ApiResponse<BasicDetailResponse>>{
        try {
            val response = apiService.getBasicData(applicationId)
            if(response.isSuccessful){
                return Resource.Success(response.body()!!)
            }else{
                val jsonObject = response.errorBody()?.string()?.let { JSONObject(it) }

                return Resource.Error(jsonObject?.getString("message")?:"An error occurred...")
            }
        }catch (e:Exception){
            return Resource.Error(e.message?:"An error occurred")
        }
    }

    suspend fun getContactDetails(applicationId:String):Resource<ApiResponse<ContactDetailResponse>>{
        try {
            val response = apiService.getContactDetails(applicationId)
            if(response.isSuccessful){apiService.getContactDetails(applicationId)
                return Resource.Success(response.body()!!)
            }else{
                val jsonObject = response.errorBody()?.string()?.let { JSONObject(it) }

                return Resource.Error(jsonObject?.getString("message")?:"An error occurred...")
            }
        }catch (e:Exception){
            return Resource.Error(e.message?:"An error occurred")
        }
    }

    suspend fun getAttorneyDetails(applicationId:String):Resource<ApiResponse<AttorneyDetailResponse>>{
        try {
            val response = apiService.getAttorneyDetails(applicationId)
            if(response.isSuccessful){
                return Resource.Success(response.body()!!)
            }else{
                val jsonObject = response.errorBody()?.string()?.let { JSONObject(it) }

                return Resource.Error(jsonObject?.getString("message")?:"An error occurred...")
            }
        }catch (e:Exception){
            return Resource.Error(e.message?:"An error occurred")
        }
    }

    suspend fun getNomineeDetails(applicationId:String):Resource<ApiResponse<NomineeDetailResponse>>{
        try {
            val response = apiService.getNomineeDetails(applicationId)
            if(response.isSuccessful){
                return Resource.Success(response.body()!!)
            }else{
                val jsonObject = response.errorBody()?.string()?.let { JSONObject(it) }

                return Resource.Error(jsonObject?.getString("message")?:"An error occurred...")
            }
        }catch (e:Exception){
            return Resource.Error(e.message?:"An error occurred")
        }
    }

    suspend fun getOtherDetails(applicationId:String):Resource<ApiResponse<OtherDetailResponse>>{
        try {
            val response = apiService.getOtherDetails(applicationId)
            if(response.isSuccessful){
                return Resource.Success(response.body()!!)
            }else{
                val jsonObject = response.errorBody()?.string()?.let { JSONObject(it) }

                return Resource.Error(jsonObject?.getString("message")?:"An error occurred...")
            }
        }catch (e:Exception){
            return Resource.Error(e.message?:"An error occurred")
        }
    }
    suspend fun verifyOtp(verifyOtpDto: VerifyOtpDto):Resource<ApiResponse<Nothing>>{
        try {
            val response = apiService.verifyOtp(verifyOtpDto)
            if(response.isSuccessful){
                return Resource.Success(response.body()!!)
            }else{
                val jsonObject = response.errorBody()?.string()?.let { JSONObject(it) }

                return Resource.Error(jsonObject?.getString("message")?:"An error occurred...")
            }
        }catch (e:Exception){
            return Resource.Error(e.message?:"An error occurred")
        }
    }


    fun saveAccessToken(accessToken: String?) {
        sharedPreferences.edit().apply{
            putString(AppConstants.ACCESS_TOKEN,accessToken)
            apply()
        }
    }

    fun getaccessToken(): String? {
        return sharedPreferences.getString(AppConstants.ACCESS_TOKEN,"")
    }
}