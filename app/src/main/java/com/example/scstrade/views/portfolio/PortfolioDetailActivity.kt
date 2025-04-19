package com.example.scstrade.views.portfolio

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.example.scstrade.R
import com.example.scstrade.databinding.ActivityPortfolioBinding
import com.example.scstrade.databinding.ActivityPortfolioDetailBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.login.LoginDataItem
import com.example.scstrade.model.response.portfolio.PortfolioDetailItem
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.MyApp
import com.google.gson.reflect.TypeToken

class PortfolioDetailActivity : AppCompatActivity() {
    private lateinit var binding:ActivityPortfolioDetailBinding
    private lateinit var sharedViewModel: SharedViewModel
    lateinit var login: LoginDataItem
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
        sharedViewModel.getPortfolioDetail(login.registrationID)

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
                val intent = Intent(this.root.context,BuySellActivity::class.java)
                intent.putExtra(AppConstants.IS_BUY,true)
                startActivity(intent)
            }

            sellTrade.setOnClickListener {
                val intent = Intent(this.root.context,BuySellActivity::class.java)
                intent.putExtra(AppConstants.IS_Sell,true)
                startActivity(intent)
            }

            addDividend.setOnClickListener {
                val intent = Intent(this.root.context,BuySellActivity::class.java)
                intent.putExtra(AppConstants.IS_Dividend,true)
                startActivity(intent)
            }
        }
        sharedViewModel.mutablePortfolioDetail.observe(this, Observer { result->
            when(result){
                is Resource.Error -> Utils.showError(binding.root,result.message?:"An error occurred")
                is Resource.Loading -> {}
                is Resource.Success -> {
                    binding.recyclerView.apply {
                        val list= result.data?.groupBy { it.portfolioSymbol }?.mapValues { (key,items)->
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
                            val currentPrice = sharedViewModel.mutableAllData.value?.data?.filter { it.sYM.equals(key,true) }?.map { it.cL }?.first()
                            val currentValue = totalShares.toDouble() * currentPrice!!
                            val totalPL= "${currentValue - totalCost} (${((currentValue - totalCost)/totalCost)*100}%)"
                            val totalBuy = items.sumOf {
                                it.portfolioQuantity * it.portfolioRate
                            }
                            val totalNow = items.sumOf {
                                it.portfolioQuantity * currentPrice
                            }


                            val perSharePL= "${totalNow - totalBuy} (${((totalNow - totalBuy)/totalBuy)*100}%)"

                            mapOf(
                                "TotalBuyAmount" to totalAmount,
                                "TotalCost" to totalCost,
                                "TotalShare" to totalShares,
                                "AverageBuy" to avgBuy,
                                "CurrentPrice" to currentPrice,
                                "CurrentValue" to currentValue,
                                "TotalP/L" to totalPL,
                                "TotalBuy" to totalBuy,
                                "TotalNow" to totalNow,
                                "PerShareP/L" to perSharePL
                            )
                        }

                        Log.e("List--->",list.toString())
                        /*Log.e("List--->",result.data?.groupBy { it.portfolioSymbol }?.mapValues { (_, details) ->
                            val totalBuyQty = details.filter { it.portfolioType.equals("BUY",true) }.sumOf { it.portfolioQuantity }
                            val totalSellQty = details.filter { it.portfolioType.equals("SELL",true) }.sumOf { it.portfolioQuantity }

                            val totalBuyAmount = details.filter { it.portfolioType == "BUY" }
                                .sumOf { it.portfolioQuantity * it.portfolioRate }

                            val totalSellAmount = details.filter { it.portfolioType == "SELL" }
                                .sumOf { it.portfolioQuantity * it.portfolioRate }

                            val totalCommission = details.sumOf {
                                val commissionRate = it.portfolioCommission / 100
                                it.portfolioQuantity * it.portfolioRate * commissionRate
                            }

                            mapOf(
                                "TotalBuyQuantity" to totalBuyQty,
                                "TotalSellQuantity" to totalSellQty,
                                "TotalBuyAmount" to totalBuyAmount,
                                "TotalSellAmount" to totalSellAmount,
                                "TotalCommission" to totalCommission
                            )
                        }?.toMap().toString())*/

//                        result.data?.groupBy { it.portfolioSymbol }.mapValues {  }.mapValues { it. }
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