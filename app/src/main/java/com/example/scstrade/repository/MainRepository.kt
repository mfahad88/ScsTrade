package com.example.scstrade.repository

import RssFeed
import android.content.Context
import android.text.TextUtils
import coil.network.HttpException
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.analystopinion.AnalystOpinionItem
import com.example.scstrade.model.response.announcement.AnnouncementDataItem
import com.example.scstrade.model.response.announcement.AnnouncementTypeDataItem
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
import com.example.scstrade.model.response.news.brecoder.RssWrapper
import com.example.scstrade.model.response.notification.NotificationDto
import com.example.scstrade.model.response.portfolio.DividendItem
import com.example.scstrade.model.response.portfolio.PortfolioDetailItem
import com.example.scstrade.model.response.portfolio.PortfolioDetails
import com.example.scstrade.model.response.portfolio.PortfolioItem
import com.example.scstrade.model.response.portfolio.PortfolioItemDetail
import com.example.scstrade.model.response.researchreport.ResearchReportItem
import com.example.scstrade.model.response.researchreport.ResearchReportList
import com.example.scstrade.model.response.snapshot.CompanyDetailItem
import com.example.scstrade.model.response.snapshot.Overview
import com.example.scstrade.model.response.snapshot.chart.Charting
import com.example.scstrade.model.response.snapshot.detail.DetailItem
import com.example.scstrade.model.response.snapshot.year.YearDetailsItem
import com.example.scstrade.model.response.toppicks.TopPickItem
import com.example.scstrade.model.summary.KSEIndices
import com.example.scstrade.services.ApiService
import com.example.scstrade.services.AppDatabase
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.http.Query


class MainRepository(val apiService: ApiService,val context: Context) {

    suspend fun getIndices():Resource<List<KSEIndices>>{
        try{
            return  Resource.Success(apiService.getIndices())
        }catch (e:Exception){
            return Resource.Error(e.message?:"An error occurred")
        }
    }
  /*  suspend fun getWithChartIndices(): Resource<List<KSEIndices>> {

        try {
          *//*  val list=apiService.getIndices().map {
                if(it.iNDEXCODE.lowercase().contains("kse all")){
                    it.charts=apiService.getChart("kseall",1)
                }else if(it.iNDEXCODE.lowercase().contains("kse 30")){
                    it.charts=apiService.getChart("kse30",1)
                }else if(it.iNDEXCODE.lowercase().contains("kse 100")){
                    it.charts=apiService.getChart("kse",1)
                }else if(it.iNDEXCODE.lowercase().contains("kmi 30")){
                    it.charts=apiService.getChart("kmi30",1)
                }
                it
            }*//*

           return Resource.Success(AppDatabase.getDatabase(context).marketDao().getIndices())
//            return  Resource.Success(apiService.getIndices())
        }catch (e:Exception){

            return  Resource.Error(e.message?:"An error occurred",null)
        }
    }*/

    suspend fun fetchAllData(que: String): Resource<List<StockItem>>{
        try {

            return Resource.Success(apiService.fetchAllData(que))
//            return Resource.Success(AppDatabase.getDatabase(context).marketDao().getMarkets())
        }catch (e:Exception){
            return  Resource.Error(e.message?:"An error occurred")
        }
    }

    suspend fun fetchTopPicks(): Resource<List<TopPickItem>>{
        try {

            return Resource.Success(apiService.fetchTopPicks())
//            return Resource.Success(AppDatabase.getDatabase(context).marketDao().getMarkets())
        }catch (e:Exception){
            return  Resource.Error(e.message?:"An error occurred")
        }
    }



    suspend fun getIndexChart(symbol:String, resolution:String): Resource<List<ChartItem>> {
        try {
            return Resource.Success(apiService.getChart(symbol, resolution))
        }catch (e:Exception){
            return Resource.Error(e.message?:"An error occurred",null)
        }
    }

    suspend fun fetchLogin(email: String, password: String, fcm: String): Resource<List<LoginDataItem>> {
        try {
            return Resource.Success(apiService.fetchLogin(email,password,fcm))
        }catch (e:Exception){
            return Resource.Error(e.message?:"An error occurred",null)
        }
    }

    suspend fun registerUser(
        fullName: String,
        email: String,
        mobile: String,
        password: String,
        fireBaseID: String
    ): Resource<JsonElement> {
        try{
            return Resource.Success(apiService.registration(email,fullName,mobile,password,fireBaseID))
        }catch (e:Exception){
            return Resource.Error(e.message?:"An error occurred",null)
        }
    }

    suspend fun getTechnicals(): Resource<List<TechnicalData>> {
        try {
            return Resource.Success(apiService.getTechnicals())
        }catch (e:Exception){
            return Resource.Error(e.message?:"An error occurred",null)
        }
    }

    suspend fun getTechnicalDetails(que:String): Resource<JsonElement> {
        try {
            return Resource.Success(apiService.getTechnicalDetails(que))
        }catch (e:Exception){
            return Resource.Error(e.message?:"An error occurred",null)
        }
    }

    suspend fun getFundamental(): Resource<List<FundamentalData>> {
        try{
            return Resource.Success(apiService.getFundamental())
        }catch (e:Exception){
            return Resource.Error(e.message?:"An error occurred",null)
        }
    }

    suspend fun getFundamentalDetails(que:String): Resource<JsonElement> {
        try {
            return Resource.Success(apiService.getFundamentalDetails(que))
        }catch (e:Exception){
            return Resource.Error(e.message?:"An error occurred",null)
        }
    }

    suspend fun news(): Resource<List<NewsData>> {
        try {
            return Resource.Success(apiService.news())
        }catch (e:Exception){
            return Resource.Error(e.message?:"An error occurred",null)
        }
    }

    suspend fun researchReportList(): Resource<List<ResearchReportList>>{
        try {
           return Resource.Success(apiService.researchReportList())
        }catch (e:Exception){
            return Resource.Error(e.message?:"An error occurred",null)
        }
    }

    suspend fun researchReport(type: String):Resource<List<ResearchReportItem>> {
        try {
            return Resource.Success(apiService.researchReport(type))
        } catch (e: Exception) {
            return Resource.Error(e.message ?: "An error occurred", null)

        }
    }

    suspend fun analystOpinion():Resource<List<AnalystOpinionItem>>{
        try {
            return Resource.Success(apiService.analystOpinion())
        }catch (e: Exception) {
            return Resource.Error(e.message ?: "An error occurred", null)

        }
    }


    suspend fun newsTribune() : Resource<RssFeed> {
        var resource:Resource<RssFeed> = Resource.Loading()
        fetchRss("https://tribune.com.pk/feed/business"){
            if(it!=null){
                val xmlMapper=XmlMapper().apply {
                    registerModule(KotlinModule.Builder().build())
                }
                val rssFeed=xmlMapper.readValue(it,RssFeed::class.java)

                resource = Resource.Success(rssFeed)
            }else{
                resource = Resource.Error("An error occurred")
            }
        }
        return resource
    }

    suspend fun newsBusiness() : Resource<RssWrapper> {
        var resource:Resource<RssWrapper> = Resource.Loading()
        fetchRss("https://www.brecorder.com/feeds/latest-news"){
            if(it!=null){

                val xmlMapper=XmlMapper().apply {
                    registerModule(KotlinModule.Builder().build())
                }
                val rssFeed=xmlMapper.readValue(it,RssWrapper::class.java)

                resource = Resource.Success(rssFeed)
            }else{
                resource = Resource.Error("An error occurred")
            }
        }
        return resource
    }

    suspend fun newsProfit() : Resource<com.example.scstrade.model.response.news.profit.RssFeed> {
        var resource:Resource<com.example.scstrade.model.response.news.profit.RssFeed> = Resource.Loading()
        fetchRss("https://profit.pakistantoday.com.pk/feed/"){
            if(it!=null){
                val xmlMapper=XmlMapper().apply {
                    registerModule(KotlinModule.Builder().build())
                }
                val rssFeed=xmlMapper.readValue(it,com.example.scstrade.model.response.news.profit.RssFeed::class.java)

                resource = Resource.Success(rssFeed)
            }else{
                resource = Resource.Error("An error occurred")
            }
        }
        return resource
    }

    suspend fun newsMettis() : Resource<com.example.scstrade.model.response.news.mettis.RssFeed> {
        var resource:Resource<com.example.scstrade.model.response.news.mettis.RssFeed> = Resource.Loading()
        fetchRss("https://mettisglobal.news/feed/"){
            if(it!=null){
                val xmlMapper=XmlMapper().apply {
                    registerModule(KotlinModule.Builder().build())
                }
                val rssFeed=xmlMapper.readValue(it,com.example.scstrade.model.response.news.mettis.RssFeed::class.java)

                resource = Resource.Success(rssFeed)
            }else{
                resource = Resource.Error("An error occurred")
            }
        }
        return resource
    }

    suspend fun newsDawn() : Resource<com.example.scstrade.model.response.news.dawn.RssFeed> {
        var resource:Resource<com.example.scstrade.model.response.news.dawn.RssFeed> = Resource.Loading()
        fetchRss("https://www.dawn.com/feeds/business"){
            if(it!=null){
                val xmlMapper=XmlMapper().apply {
                    registerModule(KotlinModule.Builder().build())
                }
                val rssFeed=xmlMapper.readValue(it,com.example.scstrade.model.response.news.dawn.RssFeed::class.java)

                resource = Resource.Success(rssFeed)
            }else{
                resource = Resource.Error("An error occurred")
            }
        }
        return resource
    }

    fun fetchRss(url:String,callback:(String?) -> Unit){
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        client.newCall(request).execute().use { response->
            if(response.isSuccessful){
                callback(response.body?.string())
            }else{
                callback(null)
            }
        }
    }

    suspend fun contactUs(): Resource<List<ContactData>> {
        try {
            return Resource.Success(apiService.contact())
        }catch (e:Exception){
            return Resource.Error(e.message?:"An error occurred",null)
        }
    }

    suspend fun snapshotOverview(symbol:String):Resource<Overview>{
        try {
            return Resource.Success(apiService.snapshotOver(symbol))
        }catch (e:Exception){
            return Resource.Error(e.message?:"An error occurred",null)
        }
    }

    suspend fun snapshotDetail(symbol: String):Resource<List<DetailItem>>{
        try {
            val details=apiService.snapshotDetail(symbol)
            if(details.size>0) {
                return Resource.Success(details)
            }else{
                return Resource.Error("An error occurred",null)

            }
        }catch (e:Exception){
            return Resource.Error(e.message?:"An error occurred",null)
        }
    }

    suspend fun snapshotChart(symbol: String):Resource<Charting>{
        try {
            return Resource.Success(apiService.snapshotChart(symbol))
        }catch (e:Exception){
            return Resource.Error(e.message?:"An error occurred",null)
        }
    }

    suspend fun getCompanyDetail(symbol: String):Resource<List<CompanyDetailItem>>{
        try {
            return Resource.Success(apiService.getCompanyDetail(symbol))
        }catch (e:Exception){
            return Resource.Error(e.message?:"An error occurred",null)
        }
    }

    suspend fun yearsDetails(symbol: String): Resource<List<YearDetailsItem>> {
        try {
            return Resource.Success(apiService.yearsDetails(symbol))
        }catch (e:Exception){
            return Resource.Error(e.message?:"An error occurred",null)
        }
    }

    suspend fun quartersDetails(symbol: String): Resource<List<YearDetailsItem>> {
        try {
            return Resource.Success(apiService.quartersDetails(symbol))
        }catch (e:Exception){
            return Resource.Error(e.message?:"An error occurred",null)
        }
    }
    suspend fun incomeStatement(symbol:String,year:String,quarter:String): Resource<List<IncomeStatementDataItem>> {
        try {
            if(quarter.contains("1")) {
                return Resource.Success(apiService.incomeStatement1(symbol, year, ""))
            }else if(quarter.contains("2")) {
                return Resource.Success(apiService.incomeStatement2(symbol, year, ""))
            }
            else if(quarter.contains("3")) {
                return Resource.Success(apiService.incomeStatement3(symbol, year, ""))
            }
            else {
                return Resource.Success(apiService.incomeStatement4(symbol, year, ""))
            }
        }catch (e:Exception){
            return Resource.Error(e.message?:"An error occurred",null)
        }
    }

    suspend fun balanceSheet(symbol:String,year:String,quarter:String): Resource<List<BalanceSheetDataItem>> {
        try {
            if(quarter.contains("1")) {
                return Resource.Success(apiService.balanceSheet1(symbol, year, ""))
            }else if(quarter.contains("2")) {
                return Resource.Success(apiService.balanceSheet2(symbol, year, ""))
            }
            else if(quarter.contains("3")) {
                return Resource.Success(apiService.balanceSheet3(symbol, year, ""))
            }
            else {
                return Resource.Success(apiService.balanceSheet4(symbol, year, ""))
            }
        }catch (e:Exception){
            return Resource.Error(e.message?:"An error occurred",null)
        }
    }

    suspend fun distribution(symbol:String,year:String,quarter:String): Resource<List<DistributionDataItem>> {
        try {
            if(quarter.contains("1")) {
                return Resource.Success(apiService.distribution1(symbol, year, ""))
            }else if(quarter.contains("2")) {
                return Resource.Success(apiService.distribution2(symbol, year, ""))
            }
            else if(quarter.contains("3")) {
                return Resource.Success(apiService.distribution3(symbol, year, ""))
            }
            else {
                return Resource.Success(apiService.distribution4(symbol, year, ""))
            }
        }catch (e:Exception){
            return Resource.Error(e.message?:"An error occurred",null)
        }
    }

    suspend fun announcementType(): Resource<List<AnnouncementTypeDataItem>> {
        return try {
            Resource.Success(apiService.announcementType("List")/*+AnnouncementTypeDataItem(type = "Insider")*/)
        }catch (e:Exception){
            Resource.Error(e.message?:"An error occurred",null)
        }
    }

    suspend fun announcements(symbol: String?,date:String?, type: String): Resource<List<AnnouncementDataItem>> {
       return try{
           Resource.Success(apiService.announcements(type,date, symbol))
        }catch (e:Exception){
           Resource.Error(e.message?:"An error occurred",null)
        }

    }

    suspend fun insider(symbol:String): Resource<List<InsiderDataItem>> {
        try{
            if(symbol.equals("")){
                return Resource.Success(apiService.insider("Insider"))
            }else {
                return Resource.Success(apiService.insider("Insider", symbol))
            }
        }catch (e:Exception){
            return Resource.Error(e.message?:"An error occurred",null)
        }

    }

    suspend fun updateProfile(email: String,name:String,phone:String,password: String,id:Int): Resource<List<LoginDataItem>> {

        try{
            val response=apiService.updateProfile(email,name,phone,password,id)

            if(response.isSuccessful){
                val gson = Gson()
                if(response.body()?.isJsonPrimitive == true){
                    return Resource.Error(response.body()?.asString?:"An error occurred",null)
                }else{
                    val type = object : TypeToken<List<LoginDataItem>>() {}.type
                    val profile:List<LoginDataItem> = gson.fromJson(response.body(),type)
                    return Resource.Success(profile)
                }
            }else{
                return Resource.Error(response.errorBody()?.string()?:"An error occurred",null)
            }
        }catch ( e:Exception){
            return Resource.Error(e.message?:"An error occurred",null)
        }
    }



    suspend fun notification():Resource<List<NotificationDto>>{
        try{
            return  Resource.Success(apiService.notification())
        }catch (e:Exception){
            return  Resource.Error(e.message?:"An error occurred",null)
        }
    }

    suspend fun notificationDetails(type:String, id:Int): Resource<JsonElement> {
        try{

            return  Resource.Success(apiService.notificationDetails(type, id))
        }catch (e:Exception){
            return Resource.Error("Unable to Fetch Data..."?: "An error occurred", null)
        }
    }

    suspend fun globalMarket():Resource<List<GlobalMarketItem>>{
        try {
            return Resource.Success(apiService.globalMarket())
        }catch (e:Exception){
            return  Resource.Error(e.message?:"An error occurred",null)
        }
    }


    suspend fun snapTechnical(symbol:String): Resource<JsonElement> {
        try{
            return  Resource.Success(apiService.snapTechnical(symbol))
        }catch (e:Exception){
            return  Resource.Error(e.message?:"An error occurred",null)
        }
    }
}