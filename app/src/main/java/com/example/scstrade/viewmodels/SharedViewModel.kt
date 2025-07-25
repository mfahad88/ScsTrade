package com.example.scstrade.viewmodels

import RssFeed
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.scstrade.helper.ConnectivityObserver
import com.example.scstrade.model.Resource
import com.example.scstrade.model.data.ResultData
import com.example.scstrade.model.response.balancesheet.BalanceSheetDataItem
import com.example.scstrade.model.response.fundamental.FundamentalData
import com.example.scstrade.model.response.chart.ChartItem
import com.example.scstrade.model.response.contact.ContactData
import com.example.scstrade.model.response.distribution.DistributionDataItem
import com.example.scstrade.model.response.login.LoginDataItem
import com.example.scstrade.model.response.stock.StockItem
import com.example.scstrade.model.response.technicals.TechnicalData
import com.example.scstrade.model.response.technicals.TechnicalDetailData
import com.example.scstrade.model.response.fundamental.FundamentalDetailData
import com.example.scstrade.model.response.incomestatement.IncomeStatementDataItem
import com.example.scstrade.model.response.indices.ResultIndices
import com.example.scstrade.model.response.news.NewsData
import com.example.scstrade.model.response.news.brecoder.RssWrapper
import com.example.scstrade.model.response.news.mettis.NewsItem
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
import com.example.scstrade.model.response.snapshot.ResultYearQuarter
import com.example.scstrade.model.response.snapshot.chart.Charting
import com.example.scstrade.model.response.snapshot.detail.DetailItem
import com.example.scstrade.model.response.toppicks.TopPickItem
import com.example.scstrade.model.summary.KSEIndices
import com.example.scstrade.repository.MainRepository
import com.example.scstrade.services.ApiService
import com.example.scstrade.services.MettisScraper
import com.example.scstrade.services.RetrofitInstance
import com.google.gson.JsonElement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SharedViewModel(application: Application) : AndroidViewModel(application) {
    private val repository=MainRepository(RetrofitInstance.create(ApiService::class.java),application)
    val mutableAllData=MutableLiveData<Resource<List<StockItem>>>()
    val mutableFuture=MutableLiveData<Resource<List<StockItem>>>()
    val mutableIndices=MutableLiveData<Resource<List<KSEIndices>>>()
    val mutableLogin=MutableLiveData<Resource<List<LoginDataItem>>>()
    val mutableRegister=MutableLiveData<Resource<JsonElement>>()
    val mutableChart=MutableLiveData<Resource<List<ChartItem>>>()
    val mutableOnceChart=MutableLiveData<Resource<List<ChartItem>>>()
    val mutableTechnical=MutableLiveData<Resource<List<TechnicalData>>>()
    val mutableTechnicalDetail=MutableLiveData<Resource<JsonElement>>()
    val mutableHistory=MutableLiveData<ResultData>()
    val mutableFundamental=MutableLiveData<Resource<List<FundamentalData>>>()
    val mutableFundamentalDetail=MutableLiveData<Resource<JsonElement>>()
    val mutableNews=MutableLiveData<Resource<List<NewsData>>>()
    val mutableTribune=MutableLiveData<Resource<RssFeed>>()
    val mutableBrecoder=MutableLiveData<Resource<RssWrapper>>()
    val mutableProfit=MutableLiveData<Resource<com.example.scstrade.model.response.news.profit.RssFeed>>()
    val mutableMettis=MutableLiveData<Resource<List<NewsItem>>>()
    val mutableDawn=MutableLiveData<Resource<com.example.scstrade.model.response.news.dawn.RssFeed>>()
    val mutableContactUs=MutableLiveData<Resource<List<ContactData>>>()
    val mutableOverview=MutableLiveData<Resource<Overview>>()
    val mutableDetail=MutableLiveData<Resource<List<DetailItem>>>()
    val mutableSnapShotChart=MutableLiveData<Resource<Charting>>()
    val mutableYears = MutableLiveData<ResultYearQuarter>()
//    val mutableYears=MutableLiveData<Resource<List<YearDetailsItem>>>()
//    val mutableQuarters=MutableLiveData<Resource<List<YearDetailsItem>>>()
    val mutableIncomeStatement=MutableLiveData<Resource<List<IncomeStatementDataItem>>>()
    val mutableBalanceSheet=MutableLiveData<Resource<List<BalanceSheetDataItem>>>()
    val mutableDistribution=MutableLiveData<Resource<List<DistributionDataItem>>>()
    val mutableUpdateProfile=MutableLiveData<Resource<List<LoginDataItem>>>()
    val mutablePortfolio=MutableLiveData<Resource<List<PortfolioItem>>>()
    val mutablePortfolioFinalDetail=MutableLiveData<Resource<PortfolioDetailItem?>?>()
    val mutablePortfolioFinalDetailOnce=MutableLiveData<Resource<PortfolioDetailItem>>()
    val mutableDividend=MutableLiveData<Resource<List<DividendItem>>>()
    val mutablePortfolioItemDetail=MutableLiveData<Resource<List<PortfolioItemDetail>>>()
    val mutablePortfolioDetails = MutableLiveData<Resource<List<PortfolioDetails>>>()
    val mutableNotificationList= MutableLiveData<Resource<List<NotificationDto>>>()
    val mutableNotificationDetailList= MutableLiveData<Resource<JsonElement>>()
    var isFetchAllData=true
    var isFetchIndices=true
    var isFetchPortfolioFinal=false
    var isLineChart:Boolean=false
    val isConnected = ConnectivityObserver(application)
    var isHome=false
    var portfolioJob: Job? = null
    val mutableTopPicks = MutableLiveData<Resource<List<TopPickItem>>>()
    val mutableResultIndices= MutableLiveData<Resource<ResultIndices>>()
    val mutableCompanyDetail = MutableLiveData<Resource<List<CompanyDetailItem>>>()
    val mutableSnapTechnical = MutableLiveData<Resource<JsonElement>>()
    val mutableReportList = MutableLiveData<Resource<List<ResearchReportList>>>()
    val mutableReportDetails = MutableLiveData<Resource<List<ResearchReportItem>>>()
    fun fetchAllData(){
        viewModelScope.launch(Dispatchers.Default) {
            if(isConnected.value==true) {
                val result2 = repository.fetchTopPicks()
                val result = repository.fetchAllData("AllData")
                val result1 = repository.fetchAllData("FutureData")

                withContext(Dispatchers.Main) {
                    mutableTopPicks.postValue(result2)
                    mutableAllData.postValue(result)
                    mutableFuture.postValue(result1)


                }
            }
           /* while(isFetchAllData) {
                
                if(isConnected.value==true) {
                    val result2 = repository.fetchTopPicks()
                    val result = repository.fetchAllData("AllData")
                    val result1 = repository.fetchAllData("FutureData")

                    withContext(Dispatchers.Main) {
                        mutableTopPicks.postValue(result2)
                        mutableAllData.postValue(result)
                        mutableFuture.postValue(result1)


                    }
                    delay(5000)
                }
            }*/
        }

    }

    fun fetchChart(symbol:String,resolution:String){
        viewModelScope.launch (Dispatchers.IO){
            val result = repository.getIndexChart(symbol, resolution)
            withContext(Dispatchers.Main) {
                mutableChart.value = result
            }
        }
    }

    fun fetchOnceChart(symbol:String){
        viewModelScope.launch (Dispatchers.IO){
            if(isConnected.value==true) {
                val result = repository.getIndexChart(symbol, "1")
                withContext(Dispatchers.Main) {
                    mutableOnceChart.value = result
                }
            }
        }
    }

    fun fetchIndices(){
//        mutableResultIndices.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            if (isConnected.value == true) {

                if(isLineChart){
                    val resultDeffered = async{repository.getIndices()}
                    val result_KSEALLDeffered= async{ repository.getIndexChart("KSE ALL", "1") }
                    val result_KSE100Deffered = async { repository.getIndexChart("KSE", "1") }
                    val result_KSE30Deffered = async { repository.getIndexChart("KSE 30", "1") }
                    val result_KMI30Deffered = async { repository.getIndexChart("KMI 30", "1") }
                    withContext(Dispatchers.Main){
                        val result=resultDeffered.await()
                        val result_KSEALL= result_KSEALLDeffered.await()
                        val result_KSE100 = result_KSE100Deffered.await()
                        val result_KSE30 = result_KSE30Deffered.await()
                        val result_KMI30 = result_KMI30Deffered.await()


                        mutableResultIndices.value=Resource.Success(ResultIndices(result.data,result_KSEALL.data,result_KSE100.data,result_KSE30.data,result_KMI30.data))
                    }
                }else {
                    val result = repository.getIndices()
                    withContext(Dispatchers.Main) {
                        mutableIndices.value = result
                    }
                }

            }
           /* while(isFetchIndices) {
//            mutableAllData.value = Resource.Loading()
                if (isConnected.value == true) {

                    if(isLineChart){
                        val resultDeffered = async{repository.getIndices()}
                        val result_KSEALLDeffered= async{ repository.getIndexChart("KSE ALL", "1") }
                        val result_KSE100Deffered = async { repository.getIndexChart("KSE", "1") }
                        val result_KSE30Deffered = async { repository.getIndexChart("KSE 30", "1") }
                        val result_KMI30Deffered = async { repository.getIndexChart("KMI 30", "1") }
                        withContext(Dispatchers.Main){
                            val result=resultDeffered.await()
                            val result_KSEALL= result_KSEALLDeffered.await()
                            val result_KSE100 = result_KSE100Deffered.await()
                            val result_KSE30 = result_KSE30Deffered.await()
                            val result_KMI30 = result_KMI30Deffered.await()


                            mutableResultIndices.value=Resource.Success(ResultIndices(result.data,result_KSEALL.data,result_KSE100.data,result_KSE30.data,result_KMI30.data))
                        }
                    }else {
                        val result = repository.getIndices()
                        withContext(Dispatchers.Main) {
                            mutableIndices.value = result
                        }
                    }

                    delay(5000)
                }
            }*/
        }
    }

    fun notification(){
        mutableNotificationList.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            if (isConnected.value == true) {
                val result = repository.notification()
                withContext(Dispatchers.Main) {
                    mutableNotificationList.value = result
                }

            }
        }
    }

    fun notificationDetals(type:String, id:Int){
        mutableNotificationDetailList.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            if (isConnected.value == true) {
                val result = repository.notificationDetails(type, id)
                withContext(Dispatchers.Main) {
                    mutableNotificationDetailList.value = result
                }

            }
        }
    }

    fun fetchLogin(email: String, password: String, fcm: String){
        mutableLogin.value = Resource.Loading()
        viewModelScope.launch (Dispatchers.IO){
            if(isConnected.value==true) {
                val result =repository.fetchLogin(email, password,fcm)
                withContext(Dispatchers.Main){
                    mutableLogin.postValue(result)
                }
            }
        }
    }

    fun registerUser(fullName:String,email: String,mobile:String,password: String,fireBaseID:String){
        viewModelScope.launch {
            if(isConnected.value==true) {
                mutableRegister.value = Resource.Loading()

                mutableRegister.value = repository.registerUser(fullName, email, mobile, password,fireBaseID)
            }
        }
    }

    fun getTechnicals(){
        mutableTechnical.value = Resource.Loading()
        viewModelScope.launch (Dispatchers.IO){
            if(isConnected.value==true){
                val result=repository.getTechnicals()
                withContext(Dispatchers.Main){
                    mutableTechnical.value = result
                }
            }
        }
    }

    fun getTechnicalDetail(que:String){
        mutableTechnicalDetail.value = Resource.Loading()
        if(isConnected.value==true) {
            viewModelScope.launch(Dispatchers.IO) {
                val result = repository.getTechnicalDetails(que)
                withContext(Dispatchers.Main) {
                    mutableTechnicalDetail.value = result
                }
            }
        }
    }

    fun getFundamental(){
        mutableFundamental.value = Resource.Loading()
        if(isConnected.value==true) {
            viewModelScope.launch(Dispatchers.IO) {
                val result = repository.getFundamental()
                withContext(Dispatchers.Main) {
                    mutableFundamental.value = result
                }
            }
        }
    }

    fun getFundamentalDetail(que:String){
        mutableFundamentalDetail.value = Resource.Loading()
        if(isConnected.value==true) {
            viewModelScope.launch(Dispatchers.IO) {
                val result = repository.getFundamentalDetails(que)
                withContext(Dispatchers.Main) {
                    mutableFundamentalDetail.value = result
                }
            }
        }
    }

    fun news(){
        mutableNews.value = Resource.Loading()
        if(isConnected.value==true){
            viewModelScope.launch (Dispatchers.IO){
                var result:Resource<List<NewsData>> =  repository.news()

                withContext(Dispatchers.Main){
                    mutableNews.value = result
                }
            }
        }
    }

    fun researchReportList(){
        mutableReportList.value = Resource.Loading()
        if(isConnected.value == true){
            viewModelScope.launch (Dispatchers.IO){
                val result = repository.researchReportList()
                withContext(Dispatchers.Main){
                    mutableReportList.value = result
                }
            }
        }
    }

    fun researchReport(type: String){
        mutableReportDetails.value = Resource.Loading()
        if(isConnected.value == true){
            viewModelScope.launch (Dispatchers.IO){
                val result = repository.researchReport(type)
                withContext(Dispatchers.Main){
                    mutableReportDetails.value = result
                }
            }
        }
    }

    fun tribuneNews(){
        mutableTribune.value  = Resource.Loading()
        if(isConnected.value==true){
            viewModelScope.launch (Dispatchers.IO){
                val result = repository.newsTribune()
                withContext(Dispatchers.Main){
                    mutableTribune.value = result
                }
            }
        }
    }

    fun brecoderNews(){
        mutableBrecoder.value  = Resource.Loading()
        if(isConnected.value==true){
            viewModelScope.launch (Dispatchers.IO){
                val result = repository.newsBusiness()
                withContext(Dispatchers.Main){
                    mutableBrecoder.value = result
                }
            }
        }
    }

    fun profitNews(){
        mutableProfit.value  = Resource.Loading()
        if(isConnected.value==true){
            viewModelScope.launch (Dispatchers.IO){
                val result = repository.newsProfit()
                withContext(Dispatchers.Main){
                    mutableProfit.value = result
                }
            }
        }
    }


    fun mettisNews(){
        mutableMettis.value = Resource.Loading()
        if (isConnected.value == true) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val scraper = MettisScraper()
                    val articles = scraper.fetchArticles()

                    withContext(Dispatchers.Main) {
                        mutableMettis.value = Resource.Success(articles)
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        mutableMettis.value = Resource.Error(e.message ?: "No record found")
                    }
                }
            }
        }
    }

    fun dawnNews(){
        mutableDawn.value  = Resource.Loading()
        if(isConnected.value==true){
            viewModelScope.launch (Dispatchers.IO){
                val result = repository.newsDawn()
                withContext(Dispatchers.Main){
                    mutableDawn.value = result
                }
            }
        }
    }

    fun contactUs(){
        mutableContactUs.value = Resource.Loading()
        if(isConnected.value==true){
            viewModelScope.launch (Dispatchers.IO){
                val result = repository.contactUs()
                withContext(Dispatchers.Main){
                    mutableContactUs.value = result
                }
            }
        }
    }

    fun snapshotOverview(symbol: String){
        mutableOverview.value = Resource.Loading()
        if(isConnected.value==true){
            viewModelScope.launch (Dispatchers.IO){
                val result = repository.snapshotOverview(symbol)
                withContext(Dispatchers.Main){
                    mutableOverview.value = result
                }
            }
        }
    }

    fun snapshotDetail(symbol: String){
        mutableDetail.value = Resource.Loading()
        if(isConnected.value==true){
            viewModelScope.launch (Dispatchers.IO){
                val result = repository.snapshotDetail(symbol)
                withContext(Dispatchers.Main){
                    mutableDetail.value = result
                }
            }
        }
    }

    fun snapshotChart(symbol: String){
        mutableSnapShotChart.value = Resource.Loading()
        if(isConnected.value==true){
            viewModelScope.launch (Dispatchers.IO){
                val result = repository.snapshotChart(symbol)
                withContext(Dispatchers.Main){
                    mutableSnapShotChart.value = result
                }
            }
        }
    }

    fun snapTechnical(symbol: String){
        mutableSnapTechnical.value = Resource.Loading()
        if(isConnected.value == true){
            viewModelScope.launch (Dispatchers.IO){
                val result = repository.snapTechnical(symbol)
                withContext(Dispatchers.Main){
                    mutableSnapTechnical.value = result
                }
            }
        }
    }

    fun getCompanyDetail(symbol: String){
        mutableCompanyDetail.value = Resource.Loading()
        if(isConnected.value == true){
            viewModelScope.launch (Dispatchers.IO){
                val result = repository.getCompanyDetail(symbol)
                withContext(Dispatchers.Main){
                    mutableCompanyDetail.value = result
                }
            }
        }
    }

    fun yearsDetails(symbol: String){
        if(isConnected.value==true){
            mutableYears.value = ResultYearQuarter(Resource.Loading(),Resource.Loading())
            viewModelScope.launch (Dispatchers.IO){
                val yearsDeferred= async{repository.yearsDetails(symbol)}
                val quartersDeferred = async { repository.quartersDetails(symbol) }

                val years = yearsDeferred.await()
                val quarters = quartersDeferred.await()
                withContext(Dispatchers.Main){
                    mutableYears.value = ResultYearQuarter(years,quarters)
                }

                /*val result = repository.yearsDetails(symbol)
                withContext(Dispatchers.Main){
                    mutableYears.value = result
                }*/
            }
        }
    }

/*    fun quartersDetails(symbol: String){
        mutableQuarters.value = Resource.Loading()
        if(isConnected.value==true){
            viewModelScope.launch (Dispatchers.IO){
                val result = repository.quartersDetails(symbol)
                withContext(Dispatchers.Main){
                    mutableQuarters.value = result
                }
            }
        }
    }*/

    fun incomeStatement(symbol:String,year:String,quarter:String){
        mutableIncomeStatement.value = Resource.Loading()
        if(isConnected.value==true){
            viewModelScope.launch (Dispatchers.IO){
                val result = repository.incomeStatement(symbol,year, quarter)
                withContext(Dispatchers.Main){
                    mutableIncomeStatement.value = result


                }
            }
        }
    }

    fun balanceSheet(symbol:String,year:String,quarter:String){
        mutableBalanceSheet.value = Resource.Loading()
        if(isConnected.value==true){
            viewModelScope.launch (Dispatchers.IO){
                val result = repository.balanceSheet(symbol,year, quarter)
                withContext(Dispatchers.Main){
                    mutableBalanceSheet.value = result
                }
            }
        }
    }

    fun distribution(symbol:String,year:String,quarter:String){
        mutableDistribution.value = Resource.Loading()
        if(isConnected.value==true){
            viewModelScope.launch (Dispatchers.IO){
                val result = repository.distribution(symbol,year, quarter)
                withContext(Dispatchers.Main){
                    mutableDistribution.value = result
                }
            }
        }
    }

    fun updateProfile(email: String,name:String,phone:String,password: String,id:Int){
        mutableUpdateProfile.value = Resource.Loading()
        if(isConnected.value == true){
            viewModelScope.launch (Dispatchers.IO){
                val result =repository.updateProfile(email, name, phone, password, id)
                withContext(Dispatchers.Main){
                    mutableUpdateProfile.value = result
                }
            }
        }
    }



    override fun onCleared() {
        super.onCleared()
        Log.d("SharedViewModel", "ViewModel is cleared")
    }
    fun stopAll(){
        isFetchIndices=false
        isFetchAllData=false
    }
    fun startPortfolioFinal(){
        isFetchPortfolioFinal = true
    }
    fun stopPortfolioFinal(){
        isFetchPortfolioFinal = false
        mutablePortfolioFinalDetail.value=null
    }




}