package com.example.scstrade.views.portfolio.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scstrade.databinding.ActivityPortfolioDetailBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.data.ShareInHand
import com.example.scstrade.model.response.login.LoginDataItem
import com.example.scstrade.model.response.portfolio.PortfolioDetailItem
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.portfolio.adapter.ShareInHandAdapter
import com.google.gson.reflect.TypeToken

class PortfolioDetailActivity : AppCompatActivity() {
    private lateinit var binding:ActivityPortfolioDetailBinding
    private lateinit var sharedViewModel: SharedViewModel
    lateinit var login: LoginDataItem
    var portfolioMainID:Int?=-1
    var portfolioDetailItem: PortfolioDetailItem?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPortfolioDetailBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        fetchUser(this)
        sharedViewModel = (this.application as MyApp).viewModel
        ViewCompat.setOnApplyWindowInsetsListener(binding.toolbar.binding.customToolbar) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        portfolioMainID=intent.getIntExtra(AppConstants.PORTFOLIO_MAIN_ID,-1)
        sharedViewModel.getPortfolioDetail(portfolioMainID)

        binding.apply {
            viewDetail.setOnClickListener {
                if(summaryCard.visibility == View.VISIBLE){
                    summaryCard.visibility = View.GONE
                }else{
                    summaryCard.visibility = View.VISIBLE
                }
            }
            floatingActionButton.setOnClickListener {
                if(floatingMenu.visibility == View.VISIBLE){
                    floatingMenu.visibility = View.GONE
                }else{
                    floatingMenu.visibility = View.VISIBLE
                }
            }

            newBuyTrade.setOnClickListener {
                val intent = Intent(this.root.context, BuySellActivity::class.java)
                intent.putExtra(AppConstants.IS_BUY,true)
                intent.putExtra(AppConstants.PORTFOLIO_MAIN_ID,portfolioMainID)
                startActivity(intent)
            }
            sellTrade.setOnClickListener {
                if(binding.recyclerView.adapter?.itemCount?:0>0) {
                    val intent = Intent(this.root.context, BuySellActivity::class.java)
                    intent.putExtra(AppConstants.IS_Sell, true)
                    intent.putExtra(AppConstants.PORTFOLIO_MAIN_ID, portfolioMainID)
                    startActivity(intent)
                }else{
                    Utils.showError(binding.main,"You have no stocks....")
                }
            }


            addDividend.setOnClickListener {
                val intent = Intent(this.root.context, BuySellActivity::class.java)
                intent.putExtra(AppConstants.IS_Dividend,true)
                intent.putExtra(AppConstants.PORTFOLIO_MAIN_ID,portfolioMainID)
                startActivity(intent)
            }
        }
        sharedViewModel.mutablePortfolioDetail.observe(this, Observer { result->
            when(result){
                is Resource.Error -> Utils.showError(binding.root,result.message?:"An error occurred")
                is Resource.Loading -> {}
                is Resource.Success -> {
                    val list= mutableListOf<ShareInHand>()
                    binding.recyclerView.apply {
                        result.data?.groupBy { it.portfolioSymbol }?.mapValues { (key,items)->
                            portfolioDetailItem = items.first()
                            val totalAmount = items.sumOf {
                                if(it.portfolioType.equals("buy",true)){
                                    it.portfolioQuantity
                                }else{
                                    -it.portfolioQuantity
                                }
                            }
                            val totalCost = items.sumOf {
                                if(it.portfolioType.equals("buy",true)) {
                                    it.portfolioQuantity * it.portfolioRate
                                }else{
                                    -(it.portfolioQuantity * it.portfolioRate)
                                }
                            }
                            val totalShares= items.sumOf {
                                if(it.portfolioType.equals("buy",true)){
                                    it.portfolioQuantity
                                }else{
                                    -it.portfolioQuantity
                                }
                            }

                            val avgBuy= totalCost/totalShares
                            val closingPrice = sharedViewModel.mutableAllData.value?.data?.filter { it.sYM.equals(key,true) }?.map { it.oC }?.first()
                            val currentPrice = sharedViewModel.mutableAllData.value?.data?.filter { it.sYM.equals(key,true) }?.map { it.cL }?.first()
                            val currentValue = totalShares.toDouble() * currentPrice!!
                            val dayPL= "${(currentPrice.minus(closingPrice?:0.0)).times(totalShares)}"
                            val percentdayPL = ((currentPrice.minus(closingPrice!!)).div(avgBuy)).times(100).toString()
                            val totalPL= "${(currentPrice.minus(avgBuy).times(totalShares))}"
                            val percentTotalPL=((currentPrice.minus(avgBuy)).div(avgBuy)).times(100).toString()
                            val totalBuy = items.sumOf {
                                it.portfolioQuantity * it.portfolioRate
                            }
                            val totalNow = items.sumOf {
                                it.portfolioQuantity * currentPrice
                            }


                            val perSharePL= "${totalNow - totalBuy} (${((totalNow - totalBuy)/totalBuy)*100}%)"
                            list.add(ShareInHand( items.first().portfolioSymbol,
                                Utils.roundTwoDecimal(totalCost),
                                Utils.roundTwoDecimal(avgBuy),
                                Utils.roundTwoDecimal(currentValue),
                                totalShares.toString(),
                                Utils.roundTwoDecimal(dayPL.toDouble()),
                                Utils.roundTwoDecimal(percentdayPL.toDouble()),
                                Utils.roundTwoDecimal(totalPL.toDouble()),
                                Utils.roundTwoDecimal(percentTotalPL.toDouble())))

                           /* adapter=ShareInHandAdapter(  mapOf(
                                "Symbol" to items.first().portfolioSymbol,
                                "TotalCost" to totalCost,
                                "AvgBuy" to avgBuy,
                                "Share" to totalShares,
                                "MarketValue" to currentValue,
                                "DayP/L" to dayPL,
                                "TotalP/L" to totalPL
                            ).map {
                                KeyDescValue(it.key,it.value.toString(),null)
                            }){

                            }

                            layoutManager=LinearLayoutManager(this@PortfolioDetailActivity,LinearLayoutManager.VERTICAL,false)*/

                        }
                        adapter = ShareInHandAdapter(list, onItemClick = {res->

                            val intent = Intent(this.context, BuySellActivity::class.java)
                            intent.putExtra(AppConstants.IS_Sell, true)
                            intent.putExtra(AppConstants.PORTFOLIO_MAIN_ID, portfolioMainID)
                            intent.putParcelableArrayListExtra(AppConstants.STOCK_INFO,   result.data?.filter { it.portfolioSymbol.equals(res.symbol,true)}?.toList() as ArrayList)
                            startActivity(intent)

                        },onItemClickSnapshot = {
                            val intent = Intent(this.context, StockDetailActivity::class.java)

                            intent.putExtra(AppConstants.PORTFOLIO_MAIN_ID, portfolioMainID)
                            intent.putExtra(AppConstants.SYMBOL,it.symbol)
//                            intent.putParcelableArrayListExtra(AppConstants.STOCK_INFO,   result.data?.filter { it.portfolioSymbol.equals(res.symbol,true)}?.toList() as ArrayList)
                            startActivity(intent)
                        })
                        layoutManager=LinearLayoutManager(this@PortfolioDetailActivity,LinearLayoutManager.VERTICAL,false)

                    }
                }
            }
        })

    }

    private fun fetchUser(context: Context) {
        val listType = object : TypeToken<List<LoginDataItem>>() {}
        val user= Utils.getSharedPreference(context, emptyList<LoginDataItem>(),
            AppConstants.USER,listType)
        login=user.first()
        Log.e("User: ",user.toString())
    }
}