package com.example.scstrade.viewmodels

import RssFeed
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.scstrade.helper.ConnectivityObserver
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.announcement.AnnouncementDataItem
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
import com.example.scstrade.model.response.news.NewsData
import com.example.scstrade.model.response.news.brecoder.RssWrapper
import com.example.scstrade.model.response.notification.NotificationDto
import com.example.scstrade.model.response.portfolio.DividendItem
import com.example.scstrade.model.response.portfolio.PortfolioDetailItem
import com.example.scstrade.model.response.portfolio.PortfolioDetails
import com.example.scstrade.model.response.portfolio.PortfolioItem
import com.example.scstrade.model.response.portfolio.PortfolioItemDetail
import com.example.scstrade.model.response.snapshot.Overview
import com.example.scstrade.model.response.snapshot.chart.Charting
import com.example.scstrade.model.response.snapshot.detail.DetailItem
import com.example.scstrade.model.response.snapshot.year.YearDetailsItem
import com.example.scstrade.model.summary.KSEIndices
import com.example.scstrade.repository.MainRepository
import com.example.scstrade.services.ApiService
import com.example.scstrade.services.RetrofitInstance
import com.google.gson.JsonElement
import kotlinx.coroutines.Dispatchers
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
    val mutableTechnicalDetail=MutableLiveData<Resource<List<TechnicalDetailData>>>()

    val mutableFundamental=MutableLiveData<Resource<List<FundamentalData>>>()
    val mutableFundamentalDetail=MutableLiveData<Resource<List<FundamentalDetailData>>>()
    val mutableNews=MutableLiveData<Resource<List<NewsData>>>()
    val mutableTribune=MutableLiveData<Resource<RssFeed>>()
    val mutableBrecoder=MutableLiveData<Resource<RssWrapper>>()
    val mutableProfit=MutableLiveData<Resource<com.example.scstrade.model.response.news.profit.RssFeed>>()
    val mutableMettis=MutableLiveData<Resource<com.example.scstrade.model.response.news.mettis.RssFeed>>()
    val mutableDawn=MutableLiveData<Resource<com.example.scstrade.model.response.news.dawn.RssFeed>>()
    val mutableContactUs=MutableLiveData<Resource<List<ContactData>>>()
    val mutableOverview=MutableLiveData<Resource<Overview>>()
    val mutableDetail=MutableLiveData<Resource<List<DetailItem>>>()
    val mutableSnapShotChart=MutableLiveData<Resource<Charting>>()
    val mutableYears=MutableLiveData<Resource<List<YearDetailsItem>>>()
    val mutableIncomeStatement=MutableLiveData<Resource<List<IncomeStatementDataItem>>>()
    val mutableBalanceSheet=MutableLiveData<Resource<List<BalanceSheetDataItem>>>()
    val mutableDistribution=MutableLiveData<Resource<List<DistributionDataItem>>>()
    val mutableUpdateProfile=MutableLiveData<Resource<List<LoginDataItem>>>()
    val mutablePortfolio=MutableLiveData<Resource<List<PortfolioItem>>>()
    val mutablePortfolioFinalDetail=MutableLiveData<Resource<PortfolioDetailItem>>()
    val mutablePortfolioFinalDetailOnce=MutableLiveData<Resource<PortfolioDetailItem>>()
    val mutableDividend=MutableLiveData<Resource<List<DividendItem>>>()
    val mutablePortfolioItemDetail=MutableLiveData<Resource<List<PortfolioItemDetail>>>()
    val mutablePortfolioDetails = MutableLiveData<Resource<List<PortfolioDetails>>>()
    val mutableNotificationList= MutableLiveData<Resource<List<NotificationDto>>>()
    val mutableNotificationDetailList= MutableLiveData<Resource<JsonElement>>()
    var isFetchAllData=true
    var isFetchIndices=true
    var isFetchPortfolioFinal=false
    val isConnected = ConnectivityObserver(application)
    fun fetchAllData(){
        viewModelScope.launch(Dispatchers.IO) {
            while(isFetchAllData) {
                if(isConnected.value==true) {
                    val result = repository.fetchAllData("AllData")
                    val result1 = repository.fetchAllData("FutureData")
                    withContext(Dispatchers.Main) {
                        mutableAllData.value = result
                        mutableFuture.value = result1
                    }
                    delay(5000)
                }
            }
        }

    }

    fun fetchChart(symbol:String){
        viewModelScope.launch (Dispatchers.IO){
            if(isConnected.value==true) {
                val result = repository.getIndexChart(symbol, 1)
                while (true) {
                    withContext(Dispatchers.Main) {
                        mutableChart.value = result
                    }
                    delay(5000)
                }
            }
        }
    }

    fun fetchOnceChart(symbol:String){
        viewModelScope.launch (Dispatchers.IO){
            if(isConnected.value==true) {
                val result = repository.getIndexChart(symbol, 1)
                withContext(Dispatchers.Main) {
                    mutableOnceChart.value = result
                }
            }
        }
    }

    fun fetchIndices(){

        viewModelScope.launch(Dispatchers.IO) {
            while(isFetchIndices) {
//            mutableAllData.value = Resource.Loading()
                if (isConnected.value == true) {
                    val result = repository.getIndices()
                    withContext(Dispatchers.Main) {
                        mutableIndices.value = result
                    }

                    delay(5000)
                }
            }
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
        viewModelScope.launch {
            if(isConnected.value==true) {
                mutableLogin.value = Resource.Loading()
                mutableLogin.value = repository.fetchLogin(email, password,fcm)
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
                var result:Resource<List<NewsData>> = Resource.Loading()
                result=repository.news()
                withContext(Dispatchers.Main){
                    mutableNews.value = result
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
        mutableMettis.value  = Resource.Loading()
        if(isConnected.value==true){
            viewModelScope.launch (Dispatchers.IO){
                val result = repository.newsMettis()
                withContext(Dispatchers.Main){
                    mutableMettis.value = result
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

    fun yearsDetails(symbol: String){
        mutableYears.value = Resource.Loading()
        if(isConnected.value==true){
            viewModelScope.launch (Dispatchers.IO){
                val result = repository.yearsDetails(symbol)
                withContext(Dispatchers.Main){
                    mutableYears.value = result
                }
            }
        }
    }

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

    fun getPortfolio(registrationId:Int?){
        mutablePortfolio.value = Resource.Loading()
        if(isConnected.value == true){
            viewModelScope.launch (Dispatchers.IO){
                if(registrationId!=null) {
                    val result = repository.getPortfolio(registrationId)
                    withContext(Dispatchers.Main){
                        mutablePortfolio.value = result
                    }
                }
            }
        }
    }

    fun cretePortfolio(name:String?,registrationId: Int?){
        mutablePortfolio.value = Resource.Loading()
        if(isConnected.value == true){
            viewModelScope.launch (Dispatchers.IO){
                val result = repository.createPortfolio(name?:"",registrationId?:-1)
                withContext(Dispatchers.Main){
                    mutablePortfolio.value = result
                }
            }
        }
    }

    fun getPortfolioFinalDetail(portfolioMainID: Int?){
        mutablePortfolioFinalDetail.value = Resource.Loading()
        if(isConnected.value == true){
            viewModelScope.launch (Dispatchers.IO){


                while (isFetchPortfolioFinal) {
                    val result = repository.getPortfolioDetail(portfolioMainID ?: -1)

                    withContext(Dispatchers.Main) {
                        mutablePortfolioFinalDetail.value = result
                    }
                    delay(5000)
                }
            }
        }
    }

    fun getPortfolioFinalDetailOnce(portfolioMainID: Int?){
        mutablePortfolioFinalDetailOnce.value = Resource.Loading()
        if(isConnected.value == true){
            viewModelScope.launch (Dispatchers.IO){
                val result = repository.getPortfolioDetail(portfolioMainID ?: -1)
                withContext(Dispatchers.Main) {
                    mutablePortfolioFinalDetailOnce.value = result
                }
            }
        }
    }
    fun buyTrade(portfolioMainID: Int,portfolioDate:String,portfolioSymbol:String,portfolioQuantity:String,portfolioRate:String,portfolioCommission:String,portfolioCommissionType:String,portfolioPosition:String,portfolioDetailID:String){
        if(isConnected.value == true){
            viewModelScope.launch (Dispatchers.IO){
                val result = repository.buyTrade(portfolioMainID = portfolioMainID, portfolioDate = portfolioDate, portfolioSymbol = portfolioSymbol, portfolioQuantity = portfolioQuantity,
                    portfolioRate = portfolioRate, portfolioCommission = portfolioCommission, portfolioCommissionType = portfolioCommissionType, portfolioPosition = portfolioPosition, portfolioDetailID = portfolioDetailID)
                withContext(Dispatchers.Main){
                    val result = repository.getPortfolioDetail(portfolioMainID ?: -1)
//                    mutablePortfolioDetail.value = result
                }
            }
        }
    }

    fun updateTrade(portfolioMainID: Int,portfolioDate:String,portfolioSymbol:String,portfolioQuantity:String,portfolioType:String,portfolioRate:String,portfolioCommission:String,portfolioCommissionType:String,portfolioPosition:String,portfolioDetailID:String){
        if(isConnected.value == true){
            viewModelScope.launch (Dispatchers.IO){
                val result = repository.updateTrade(portfolioMainID = portfolioMainID, portfolioDate = portfolioDate, portfolioType = portfolioType, portfolioSymbol = portfolioSymbol, portfolioQuantity = portfolioQuantity,
                    portfolioRate = portfolioRate, portfolioCommission = portfolioCommission, portfolioCommissionType = portfolioCommissionType, portfolioPosition = portfolioPosition, portfolioDetailID = portfolioDetailID)
                withContext(Dispatchers.Main){
                    mutablePortfolioDetails.value = result
                }
            }
        }
    }

    fun deleteTrade(portfolioMainID: Int,portfolioDetailID:Int){
        if(isConnected.value == true){
            viewModelScope.launch (Dispatchers.IO){
                val result = repository.deleteTrade(portfolioMainID = portfolioMainID, portfolioDetailID = portfolioDetailID)
                withContext(Dispatchers.Main){
                    mutablePortfolioDetails.value = result
                }
            }
        }
    }

    fun buyStock(portfolioMainID: Int,portfolioDate:String,portfolioSymbol:String,portfolioQuantity:String,portfolioRate:String,portfolioCommission:String,portfolioCommissionType:String,portfolioPosition:String){
        if(isConnected.value == true){
            viewModelScope.launch (Dispatchers.IO){
                val result = repository.buyStock(portfolioMainID = portfolioMainID, portfolioDate = portfolioDate, portfolioSymbol = portfolioSymbol, portfolioQuantity = portfolioQuantity,
                    portfolioRate = portfolioRate, portfolioCommission = portfolioCommission, portfolioCommissionType = portfolioCommissionType, portfolioPosition = portfolioPosition)
//                 repository.getPortfolioDetail(portfolioMainID ?: -1)
                withContext(Dispatchers.Main){

//                    mutablePortfolioDetail.value = result
                }
            }
        }
    }

    fun sellStock(portfolioMainID: Int,portfolioDate:String,portfolioSymbol:String,portfolioQuantity:String,portfolioRate:String,portfolioCommission:String,portfolioCommissionType:String,portfolioPosition:String){
        if(isConnected.value == true){
            viewModelScope.launch (Dispatchers.IO){
                val result = repository.sellStock(portfolioMainID = portfolioMainID, portfolioDate = portfolioDate, portfolioSymbol = portfolioSymbol, portfolioQuantity = portfolioQuantity,
                    portfolioRate = portfolioRate, portfolioCommission = portfolioCommission, portfolioCommissionType = portfolioCommissionType, portfolioPosition = portfolioPosition)
                repository.getPortfolioDetail(portfolioMainID ?: -1)
                withContext(Dispatchers.Main){

//                    mutablePortfolioDetail.value = result
                }
            }
        }
    }

    fun addDividend(dividendSymbol:String,dividendQuantity:String,dividendPerShare:String, dividendDate:String, portfolioMainID:String){
        if(isConnected.value == true){
            viewModelScope.launch (Dispatchers.IO){
                val result = repository.addDividend(dividendSymbol, dividendQuantity, dividendPerShare, dividendDate, portfolioMainID)
                withContext(Dispatchers.Main){

                    mutableDividend.value=result
                }
            }
        }
    }

    fun getPortfolioItemDetail(portfolioMainID:Int,portfolioSymbol:String){
        mutablePortfolioItemDetail.value = Resource.Loading()
        if(isConnected.value == true){
            viewModelScope.launch (Dispatchers.IO){
                val result = repository.getPortfolioItemDetail(portfolioMainID, portfolioSymbol)
                withContext(Dispatchers.Main){

                    mutablePortfolioItemDetail.value=result
                }
            }
        }
    }

    fun getPortfolioDetails(portfolioMainID:Int){
        mutablePortfolioDetails.value = Resource.Loading()
        if(isConnected.value==true){
            viewModelScope.launch (Dispatchers.IO){
                val  result = repository.getPortfolioDetails(portfolioMainID)
                withContext(Dispatchers.Main){
                    mutablePortfolioDetails.value = result
                }
            }
        }
    }

    fun getDividend(portfolioMainID:String){
        mutableDividend.value=Resource.Loading()
        if(isConnected.value == true){
            viewModelScope.launch (Dispatchers.IO){
                val result = repository.getDividend( portfolioMainID)
                withContext(Dispatchers.Main){
                    mutableDividend.value=result
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
    }

    fun deletePortfolio(portfolioMainID: Int, registrationID: Int) {
        mutablePortfolio.value = Resource.Loading()
        if(isConnected.value == true){
            viewModelScope.launch (Dispatchers.IO){
                val result = repository.deletePortfolio(portfolioMainID,registrationID)
                withContext(Dispatchers.Main){
                    mutablePortfolio.value = result
                }
            }
        }

    }


}