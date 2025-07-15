package com.example.scstrade.services

import com.example.scstrade.model.request.aof.LoginUser
import com.example.scstrade.model.request.aof.RegisterUser
import com.example.scstrade.model.response.ApiResponse
import com.example.scstrade.model.response.announcement.AnnouncementTypeDataItem
import com.example.scstrade.model.request.aof.attorneyDetail.AttorneyDetailDto
import com.example.scstrade.model.request.aof.basicDetails.BasicDetailDto
import com.example.scstrade.model.response.aof.city.CityDto
import com.example.scstrade.model.request.aof.contactDetails.ContactDetailDto
import com.example.scstrade.model.request.aof.document.DocumentDto
import com.example.scstrade.model.response.aof.country.CountryDto
import com.example.scstrade.model.request.aof.nomineeDetail.NomineeDetailDto
import com.example.scstrade.model.request.aof.otherDetail.OtherDetailDto
import com.example.scstrade.model.request.aof.verifyOtp.VerifyOtpDto
import com.example.scstrade.model.response.announcement.AnnouncementDataItem
import com.example.scstrade.model.response.aof.attorneyDetail.AttorneyDetailResponse
import com.example.scstrade.model.response.aof.basicDetails.BasicDetailResponse
import com.example.scstrade.model.response.aof.contactDetails.ContactDetailResponse
import com.example.scstrade.model.response.aof.login.Data
import com.example.scstrade.model.response.aof.nomineeDetail.NomineeDetailResponse
import com.example.scstrade.model.response.aof.otherDetails.OtherDetailResponse
import com.example.scstrade.model.response.aof.protectedApplication.ProtectedResponse
import com.example.scstrade.model.response.aof.register.ResponseRegisterUser
import com.example.scstrade.model.response.balancesheet.BalanceSheetDataItem
import com.example.scstrade.model.response.fundamental.FundamentalData
import com.example.scstrade.model.response.chart.ChartItem
import com.example.scstrade.model.response.contact.ContactData
import com.example.scstrade.model.response.distribution.DistributionDataItem
import com.example.scstrade.model.response.technicals.TechnicalData
import com.example.scstrade.model.response.login.LoginDataItem
import com.example.scstrade.model.response.stock.StockItem
import com.example.scstrade.model.response.technicals.TechnicalDetailData
import com.example.scstrade.model.response.fundamental.FundamentalDetailData
import com.example.scstrade.model.response.globalMarket.GlobalMarketItem
import com.example.scstrade.model.response.incomestatement.IncomeStatementDataItem
import com.example.scstrade.model.response.insider.InsiderDataItem
import com.example.scstrade.model.response.news.NewsData
import com.example.scstrade.model.response.notification.NotificationDto
import com.example.scstrade.model.response.portfolio.DividendItem
import com.example.scstrade.model.response.portfolio.PortfolioDetailItem
import com.example.scstrade.model.response.portfolio.PortfolioDetails
import com.example.scstrade.model.response.portfolio.PortfolioItem
import com.example.scstrade.model.response.portfolio.PortfolioItemDetail
import com.example.scstrade.model.response.snapshot.CompanyDetailItem
import com.example.scstrade.model.response.snapshot.Overview
import com.example.scstrade.model.response.snapshot.chart.Charting
import com.example.scstrade.model.response.snapshot.detail.DetailItem
import com.example.scstrade.model.response.snapshot.year.YearDetailsItem
import com.example.scstrade.model.response.stockscreener.StockScreenerItem
import com.example.scstrade.model.response.toppicks.TopPickItem
import com.example.scstrade.model.response.watchList.WatchListDetailItem
import com.example.scstrade.model.response.watchList.WatchListItem
import com.example.scstrade.model.summary.KSEIndices
import com.google.gson.JsonElement
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET(value = "/Data?que=KSE Indices")
    suspend fun getIndices(): List<KSEIndices>

    @GET(value = "/Chart")
    suspend fun getChart(
        @Query("symbol") symbol: String,
        @Query("resolution") resolution: String
    ): List<ChartItem>

    @GET(value = "/Data")
    suspend fun fetchAllData(@Query("que") que: String): List<StockItem>

    @GET(value = "/Data")
    suspend fun fetchTopPicks(@Query("que")que: String="TopPicks"):List<TopPickItem>

    @GET(value = "/Login")
    suspend fun fetchLogin(
        @Query("RegistrationEmail") email: String,
        @Query("RegistrationPassword") password: String,
        @Query("FireBaseID")fireBaseID: String
    ): List<LoginDataItem>

    @GET(value = "/WatchList")
    suspend fun createWatchList(
        @Query("ActionType") type: String, @Query("WatchListName") name: String,@Query("RegistrationID") userId: Int
    ): List<WatchListItem>

    @GET(value = "/WatchList")
    suspend fun deleteWatchList(
        @Query("ActionType") type: String,
        @Query("WatchListID") watchListId: Int,
        @Query("RegistrationID") userId: Int
    ): List<WatchListItem>

    @GET(value = "/WatchList")
    suspend fun getWatchList(
        @Query("ActionType") type: String,
        @Query("RegistrationID") userId: Int
    ): List<WatchListItem>

    @GET(value = "/WatchList")
    suspend fun getWatchListDetail(
        @Query("ActionType") type: String,
        @Query("WatchListID") position: Int
    ): List<WatchListDetailItem>

    @GET(value = "/WatchList")
    suspend fun watchListSort(
        @Query("ActionType") type:String="Sorting",
        @Query("WatchListDetailID") watchListDetailID:Int,
        @Query("NewPosition") newPosition:Int,
        @Query("OldPosition") oldPosition:Int,
        @Query("WatchListID") watchListId:Int
    ): List<WatchListDetailItem>

    @GET(value = "/Registration")
    suspend fun registration(
        @Query("RegistrationEmail") email: String,
        @Query("RegistrationName") fullName: String,
        @Query("RegistrationPhone") phone: String,
        @Query("RegistrationPassword") password: String,
        @Query("FireBaseID")fireBaseID: String
    ): JsonElement

    @GET(value = "/WatchList")
    suspend fun deleteSymbol(
        @Query("ActionType") type:String,
        @Query("WatchListDetailID") watchListDetailId:Int,
        @Query("WatchListID") watchListID:Int
    ):List<WatchListDetailItem>

    @GET(value = "/WatchList")
    suspend fun addSymbol(
        @Query("ActionType") type:String,
        @Query("WatchListID")watchListId:Int,
        @Query("symbol") symbol:String
    ):List<WatchListDetailItem>

    @GET(value = "/WatchList")
    suspend fun updateWatchList(
        @Query("ActionType") type:String,
        @Query("WatchListName") watchListName:String,
        @Query("WatchListID") watchListId:Int,
        @Query("RegistrationID") registrationID:Int
    ):List<WatchListItem>

    @GET(value = "/Data?que=Technicals")
    suspend fun getTechnicals():List<TechnicalData>

    @GET(value = "/Data")
    suspend fun getTechnicalDetails(@Query("que")que:String):JsonElement

    @GET(value = "/Data?que=Fundamentals")
    suspend fun getFundamental():List<FundamentalData>

    @GET(value = "/Data")
    suspend fun getFundamentalDetails(@Query("que")que:String):JsonElement

    @GET(value = "/Data?que=News")
    suspend fun news():List<NewsData>

    @GET(value = "ContactUS")
    suspend fun contact():List<ContactData>

    @GET(value = "/SnapOverview")
    suspend fun snapshotOver(@Query("symbolin")symbol:String):Overview

    @GET(value = "/SnapDetails")
    suspend fun snapshotDetail(@Query("symbolin")symbol:String):List<DetailItem>

    @GET(value = "/SnapCharting")
    suspend fun snapshotChart(@Query("symbolin")symbol:String) : Charting

    @GET(value = "/Profile")
    suspend fun getCompanyDetail(@Query("symbol")symbol:String) : List<CompanyDetailItem>

    @GET(value = "/YearsDetails")
    suspend fun yearsDetails(@Query("symbol")symbol:String): List<YearDetailsItem>

    @GET(value = "/QuartersDetails")
    suspend fun quartersDetails(@Query("symbol")symbol:String): List<YearDetailsItem>

    @GET(value = "/IncomeStatement")
    suspend fun incomeStatement1(@Query("symbol")symbol:String,@Query("year")year:String,@Query("quarter1")q:String?):List<IncomeStatementDataItem>

    @GET(value = "/IncomeStatement")
    suspend fun incomeStatement2(@Query("symbol")symbol:String,@Query("year")year:String,@Query("quarter2")q:String?):List<IncomeStatementDataItem>

    @GET(value = "/IncomeStatement")
    suspend fun incomeStatement3(@Query("symbol")symbol:String,@Query("year")year:String,@Query("quarter3")q:String?):List<IncomeStatementDataItem>

    @GET(value = "/IncomeStatement")
    suspend fun incomeStatement4(@Query("symbol")symbol:String,@Query("year")year:String,@Query("quarter4")q:String?):List<IncomeStatementDataItem>

    @GET(value = "/BalanceSheet")
    suspend fun balanceSheet1(@Query("symbol")symbol:String,@Query("year")year:String,@Query("quarter1")q:String?):List<BalanceSheetDataItem>

    @GET(value = "/BalanceSheet")
    suspend fun balanceSheet2(@Query("symbol")symbol:String,@Query("year")year:String,@Query("quarter2")q:String?):List<BalanceSheetDataItem>

    @GET(value = "/BalanceSheet")
    suspend fun balanceSheet3(@Query("symbol")symbol:String,@Query("year")year:String,@Query("quarter3")q:String?):List<BalanceSheetDataItem>

    @GET(value = "/BalanceSheet")
    suspend fun balanceSheet4(@Query("symbol")symbol:String,@Query("year")year:String,@Query("quarter4")q:String?):List<BalanceSheetDataItem>

    @GET(value = "/Distribution")
    suspend fun distribution1(@Query("symbol")symbol:String,@Query("year")year:String,@Query("quarter1")q:String?):List<DistributionDataItem>

    @GET(value = "/Distribution")
    suspend fun distribution2(@Query("symbol")symbol:String,@Query("year")year:String,@Query("quarter2")q:String?):List<DistributionDataItem>

    @GET(value = "/Distribution")
    suspend fun distribution3(@Query("symbol")symbol:String,@Query("year")year:String,@Query("quarter3")q:String?):List<DistributionDataItem>

    @GET(value = "/Distribution")
    suspend fun distribution4(@Query("symbol")symbol:String,@Query("year")year:String,@Query("quarter4")q:String?):List<DistributionDataItem>

    @GET(value = "/Announcements")
    suspend fun announcementType(@Query("type")type:String):List<AnnouncementTypeDataItem>

    @GET(value = "/Announcements")
    suspend fun announcements(@Query("type")type:String,@Query("date")date:String?,@Query("symbol")symbol:String?):List<AnnouncementDataItem>

    @GET(value = "/Announcements")
    suspend fun announcements(@Query("type")type:String):List<AnnouncementDataItem>
    @GET(value = "/Announcements")
    suspend fun insider(@Query("type")type:String,@Query("symbol")symbol:String):List<InsiderDataItem>

    @GET(value = "/Announcements")
    suspend fun insider(@Query("type")type:String):List<InsiderDataItem>

    @GET(value = "/StockScreener")
    suspend fun stockScreener():List<StockScreenerItem>

    @GET(value="/Registration")
    suspend fun updateProfile(@Query("RegistrationEmail") email: String,
                         @Query("RegistrationName") fullName: String,
                         @Query("RegistrationPhone") phone: String,
                         @Query("RegistrationPassword") password: String,
                         @Query("RegistrationID")id:Int):Response<JsonElement>


    @GET(value = "/Portfolio")
    suspend fun getPortfolio(@Query("ActionType")actionType:String="GetPortfolio",@Query("RegistrationID")registrationID: Int):List<PortfolioItem>

    @GET(value = "/Portfolio")
    suspend fun createPortfolio(@Query("ActionType")actionType:String="Create",@Query("PortfolioName")portfolioName:String,@Query("RegistrationID")registrationID: Int):List<PortfolioItem>

    @GET(value = "/Portfolio")
    suspend fun deletePortfolio(@Query("ActionType")actionType:String="DeletePortfolio",@Query("PortfolioMainID")portfolioMainID:Int,@Query("RegistrationID")registrationID: Int):List<PortfolioItem>

    @GET(value = "/Portfolio")
    suspend fun getPortfolioDetail(@Query("ActionType")actionType:String="GetPortfolioFinal",@Query("PortfolioMainID")portfolioMainID: Int):PortfolioDetailItem

    @GET(value = "/Portfolio")
    suspend fun buyTrade(@Query("ActionType")actionType:String="AddTrade",@Query("PortfolioMainID")portfolioMainID: Int,@Query("PortfolioDate")portfolioDate:String,
                         @Query("PortfolioSymbol")portfolioSymbol:String,@Query("PortfolioQuantity")portfolioQuantity:String,@Query("PortfolioType")portfolioType:String="BUY",
                         @Query("PortfolioRate")portfolioRate:String,@Query("PortfolioCommission")portfolioCommission:String,@Query("PortfolioCommissionType")portfolioCommissionType:String,
                         @Query("PortfolioPosition")portfolioPosition:String,@Query("PortfolioDetailID")portfolioDetailID:String):List<PortfolioDetailItem>

    @GET(value = "/Portfolio")
    suspend fun updateTrade(@Query("ActionType")actionType:String="UpdateTrade",@Query("PortfolioMainID")portfolioMainID: Int,@Query("PortfolioDate")portfolioDate:String,
                         @Query("PortfolioSymbol")portfolioSymbol:String,@Query("PortfolioQuantity")portfolioQuantity:String,@Query("PortfolioType")portfolioType:String,
                         @Query("PortfolioRate")portfolioRate:String,@Query("PortfolioCommission")portfolioCommission:String,@Query("PortfolioCommissionType")portfolioCommissionType:String,
                         @Query("PortfolioPosition")portfolioPosition:String,@Query("PortfolioDetailID")portfolioDetailID:String):List<PortfolioDetails>



    @GET(value = "/Portfolio")
    suspend fun deleteTrade(@Query("ActionType")actionType:String="DeleteTrade",@Query("PortfolioMainID")portfolioMainID: Int,@Query("PortfolioDetailID")portfolioDetailID:Int):List<PortfolioDetails>

    @GET(value = "/Portfolio")
    suspend fun buyStock(@Query("ActionType")actionType:String="AddTrade",@Query("PortfolioMainID")portfolioMainID: Int,@Query("PortfolioDate")portfolioDate:String,
                         @Query("PortfolioSymbol")portfolioSymbol:String,@Query("PortfolioQuantity")portfolioQuantity:String,@Query("PortfolioType")portfolioType:String="BUY",
                         @Query("PortfolioRate")portfolioRate:String,@Query("PortfolioCommission")portfolioCommission:String,@Query("PortfolioCommissionType")portfolioCommissionType:String,
                         @Query("PortfolioPosition")portfolioPosition:String):List<PortfolioDetailItem>

    @GET(value = "/Portfolio")
    suspend fun sellStock(@Query("ActionType")actionType:String="AddTrade",@Query("PortfolioMainID")portfolioMainID: Int,@Query("PortfolioDate")portfolioDate:String,
                         @Query("PortfolioSymbol")portfolioSymbol:String,@Query("PortfolioQuantity")portfolioQuantity:String,@Query("PortfolioType")portfolioType:String="SELL",
                         @Query("PortfolioRate")portfolioRate:String,@Query("PortfolioCommission")portfolioCommission:String,@Query("PortfolioCommissionType")portfolioCommissionType:String,
                         @Query("PortfolioPosition")portfolioPosition:String):List<PortfolioDetailItem>


    @GET(value = "/Portfolio")
    suspend fun addDividend(@Query("ActionType")actionType: String="AddDividend",@Query("DividendSymbol")dividendSymbol:String,@Query("DividendQuantity")dividendQuantity:String,@Query("DividendPerShare")dividendPerShare:String,
                            @Query("DividendDate")dividendDate:String, @Query("PortfolioMainID")portfolioMainID:String):List<DividendItem>
    @GET(value = "/Portfolio")
    suspend fun getDividend(@Query("ActionType")actionType: String="GetDividend",@Query("PortfolioMainID")portfolioMainID:String):List<DividendItem>
    @GET(value = "/Portfolio")
    suspend fun getPortfolioItemDetail(@Query("ActionType")actionType: String="GetPortfolioItemDetails",@Query("PortfolioMainID")portfolioMainID:Int,@Query("PortfolioSymbol")portfolioSymbol:String):List<PortfolioItemDetail>

    @GET(value = "/Portfolio")
    suspend fun getPortfolioDetails(@Query("ActionType")actionType: String="GetPortfolioDetails",@Query("PortfolioMainID")portfolioMainID:Int):List<PortfolioDetails>

    @GET(value="/Notification")
    suspend fun notification():List<NotificationDto>

    @GET(value="/NotificationDetails")
    suspend fun notificationDetails(@Query("Type")type:String, @Query("id")id:Int):JsonElement

    @GET(value = "/GlobalMarket")
    suspend fun globalMarket():List<GlobalMarketItem>

    @GET(value = "/SnapTechnical")
    suspend fun snapTechnical(@Query("symbol")symbol: String):JsonElement

    /////////////////////////////////AOF///////////////////////////////////////
/*    @POST(value="api/register")
    suspend fun registerAof(@Body regiserUser: RegisterUser): Response<ResponseRegisterUser>*/
    @Multipart
    @POST(value="api/register")
    suspend fun registerAof(
        @Part("applicationId") applicationId: RequestBody?,
        @Part("email") email: RequestBody?,
        @Part("ibanNo") ibanNo: RequestBody?,
        @Part("id") id: RequestBody?,
        @Part("identificationType") identificationType: RequestBody?,
        @Part("isApp") isApp: RequestBody?,
        @Part("issueDate") issueDate: RequestBody?,
        @Part("lifecycleStatus") lifecycleStatus: RequestBody?,
        @Part("mobileNo") mobileNo: RequestBody?,
        @Part("name") name: RequestBody?,
        @Part("reference") reference: RequestBody?,
        @Part("relationship") relationship: RequestBody?,
        @Part("relativeName") relativeName: RequestBody?,
        @Part("relativeUIN") relativeUIN: RequestBody?,
        @Part("residentialStatus") residentialStatus: RequestBody?,
        @Part("uin") uin: RequestBody?,

        // Files
        @Part("nicFront") nicFront: RequestBody?,
        @Part("nicBack") nicBack: RequestBody?,
        @Part("proofofIBAN") proofofIBAN: RequestBody?,
        @Part("proofofRelationships") proofofRelationships: RequestBody?
    ): Response<ResponseRegisterUser>

    @POST(value = "api/login")
    suspend fun loginAof(@Body loginUser: LoginUser): Response<ApiResponse<Data>>

    @GET(value = "api/protected")
    suspend fun protected(): ProtectedResponse

    @POST(value = "api/basic-data")
    suspend fun basicData(@Body basicDetailDto: BasicDetailDto):Response<ApiResponse<Nothing>>

    @POST(value = "api/contact-details")
    suspend fun createContactDetails(@Body contactDetailDto: ContactDetailDto):Response<ApiResponse<Nothing>>

    @GET(value = "api/admin/country")
    suspend fun country():Response<ApiResponse<List<CountryDto>>>

    @GET(value = "api/admin/city")
    suspend fun city():Response<ApiResponse<List<CityDto>>>

    @POST(value = "api/attorney-details")
    suspend fun attorneyDetails(@Body attorneyDetailDto: AttorneyDetailDto):Response<ApiResponse<Nothing>>

    @Multipart
    @POST(value = "api/nominee-details")
    suspend fun nomineeDetails(
        @Part("addressNmn") addressNmn: RequestBody?,
        @Part("cnicExpiryDateNmn") cnicExpiryDateNmn: RequestBody?,
        @Part("cnicLifeTimeNmn") cnicLifeTimeNmn: RequestBody?,
        @Part("cnicNmn") cnicNmn: RequestBody?,
        @Part("id") id: RequestBody?, // convert int to string
        @Part("identificationNmn") identificationNmn: RequestBody?,
        @Part("mobileNoNmn") mobileNoNmn: RequestBody?,
        @Part("nameNmn") nameNmn: RequestBody?,
        @Part("nicBackNmn") nicBackNmn: RequestBody?,
        @Part("nicFrontNmn") nicFrontNmn: RequestBody?,
        @Part("nomineeType") nomineeType: RequestBody?,
        @Part("relationShipNmn") relationShipNmn: RequestBody?
    ): Response<ApiResponse<Nothing>>
//    suspend fun nomineeDetails(@Body nomineeDetailDto: NomineeDetailDto):Response<ApiResponse<Nothing>>

    @POST(value = "api/other-details")
    suspend fun otherDetails(@Body otherDetailDto: OtherDetailDto):Response<ApiResponse<Nothing>>
    @Multipart
    @POST(value =  "api/documents")
    /*suspend fun documents(@Body documentDto: DocumentDto):Response<ApiResponse<Nothing>>*/
    suspend fun documents(@Part("zakatStatus") zakatStatus: RequestBody,
                          @Part("accountType") accountType: RequestBody,
                          @Part("termsAndCondition") termsAndCondition: RequestBody,
                          @Part("identificationType") identificationType: RequestBody,

                          @Part("signatureProof") signatureProof: RequestBody,
                          @Part("empAddProof") empAddProof: RequestBody,
                          @Part("addProof") addProof: RequestBody,
                          @Part("zakaatDeclaration") zakaatDeclaration: RequestBody?):Response<ApiResponse<Nothing>>

    @GET(value = "/api/documents/application/{applicationId}")
    suspend fun getDocuments(@Path("applicationId")applicationId:String):Response<ApiResponse<DocumentDto>>

    @GET(value = "api/basic-data/application/{applicationId}")
    suspend fun getBasicData(@Path("applicationId")applicationId:String):Response<ApiResponse<BasicDetailResponse>>

    @GET(value = "api/contact-details/application/{applicationId}")
    suspend fun getContactDetails(@Path("applicationId")applicationId:String):Response<ApiResponse<ContactDetailResponse>>

    @GET(value = "api/attorney-details/application/{applicationId}")
    suspend fun getAttorneyDetails(@Path("applicationId")applicationId:String):Response<ApiResponse<AttorneyDetailResponse>>

    @GET(value = "api/nominee-details/application/{applicationId}")
    suspend fun getNomineeDetails(@Path("applicationId")applicationId:String):Response<ApiResponse<NomineeDetailResponse>>

    @GET(value = "api/other-details/application/{applicationId}")
    suspend fun getOtherDetails(@Path("applicationId")applicationId:String):Response<ApiResponse<OtherDetailResponse>>

    @POST(value = "api/verify-otp-new")
    suspend fun verifyOtp(@Body verifyOtpDto: VerifyOtpDto):Response<ApiResponse<Nothing>>


}