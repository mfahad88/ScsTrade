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
        sharedViewModel.isFetchPortfolioFinal=true
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
        sharedViewModel.getPortfolioFinalDetail(portfolioMainID)

        binding.apply {
            linearLayoutHistory.visibility = View.GONE
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
                floatingMenu.visibility = View.GONE
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
                floatingMenu.visibility = View.GONE
            }

            addDividend.setOnClickListener {
                if(binding.recyclerView.adapter?.itemCount?:0>0) {
                    val intent = Intent(this.root.context, BuySellActivity::class.java)
                    intent.putExtra(AppConstants.IS_Dividend, true)
                    intent.putExtra(AppConstants.PORTFOLIO_MAIN_ID, portfolioMainID)
                    startActivity(intent)
                }else{
                    Utils.showError(binding.main,"You have no stocks....")
                }
                floatingMenu.visibility = View.GONE
            }


        }
        sharedViewModel.mutablePortfolioFinalDetail.observe(this, Observer { result->
            when(result){
                is Resource.Error -> Utils.showError(binding.root,result.message?:"An error occurred")
                is Resource.Loading -> {}
                is Resource.Success -> {
                    val list= mutableListOf<ShareInHand>()
                    binding.recyclerView.apply {


                        adapter = ShareInHandAdapter(result.data?.fifoPortfolio?: emptyList(),sharedViewModel.mutableAllData.value?.data?.filter { it.sYM in result.data!!.fifoPortfolio.map { it.symbol } }?.toList()?: emptyList(),
                            onItemClick = { res->
                                val intent = Intent(this.context, BuySellActivity::class.java)
                                intent.putExtra(AppConstants.IS_Sell, true)
                                intent.putExtra(AppConstants.PORTFOLIO_MAIN_ID, portfolioMainID)
                                intent.putExtra(AppConstants.SYMBOL, res.symbol)
                                startActivity(intent)
                        }, onItemClickSnapshot = {res->
                                val intent = Intent(this.context, StockDetailActivity::class.java)
                                intent.putExtra(AppConstants.PORTFOLIO_MAIN_ID, portfolioMainID)
                                intent.putExtra(AppConstants.SYMBOL, res.symbol)
                                startActivity(intent)
                            })

                        layoutManager = LinearLayoutManager(binding.root.context,LinearLayoutManager.VERTICAL,false)
                    }

                }

                null -> {}
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

    override fun onDestroy() {
        sharedViewModel.stopPortfolioFinal()
        super.onDestroy()
    }
}