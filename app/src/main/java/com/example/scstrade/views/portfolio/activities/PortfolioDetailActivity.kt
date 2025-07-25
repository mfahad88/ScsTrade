package com.example.scstrade.views.portfolio.activities
import androidx.compose.ui.res.dimensionResource

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scstrade.databinding.ActivityMyPortfolioBinding
import com.example.scstrade.databinding.ActivityPortfolioDetailBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.data.SymbolProfit
import com.example.scstrade.model.response.login.LoginDataItem
import com.example.scstrade.model.response.portfolio.CloseTrade
import com.example.scstrade.model.response.portfolio.PortfolioHeader
import com.example.scstrade.model.response.portfolio.TradeSummary
import com.example.scstrade.viewmodels.PortFolioViewModel
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.BaseActivity
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.portfolio.adapter.ShareInHandAdapter
import com.example.scstrade.views.portfolio.adapter.TradeSummaryAdapter
import com.example.scstrade.views.snapshot.SnapshotActivity
import com.example.scstrade.views.widgets.HorizontalDivider
import com.google.gson.reflect.TypeToken
import kotlin.math.roundToInt

class PortfolioDetailActivity : BaseActivity() {
    private lateinit var binding:ActivityMyPortfolioBinding
    private lateinit var sharedViewModel: SharedViewModel
    lateinit var portfolioViewModel: PortFolioViewModel
    lateinit var login: LoginDataItem
    var portfolioMainID:Int?=null

    //    var portfolioMainID:Int?=-1

    /*val Int.dp: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()*/
    override fun onResume() {
        super.onResume()
        portfolioViewModel.getPortfolioFinalDetailOnce(portfolioMainID)
    }
    fun generateSummaryForTrades(trades: List<CloseTrade>): List<TradeSummary> {
        return trades
            .groupBy { it.symbol }
            .map { (symbol, groupedTrades) ->
                val totalCost = groupedTrades.sumOf { it.purAmount.toDouble() }
                val totalValue = groupedTrades.sumOf { it.salAmount.toDouble() }
                val pnl = totalValue - totalCost
                val pnlPercent = (pnl / totalCost) * 100

                val status = if (pnl < 0) "Loss" else "Gain"
                val formattedPnl = String.format("%,.2f", kotlin.math.abs(pnl))
                val formattedPercent = String.format("%.2f", kotlin.math.abs(pnlPercent))

                TradeSummary(
                    symbol = symbol,
                    status = status,
                    amount = formattedPnl,
                    percent = formattedPercent
                )
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMyPortfolioBinding.inflate(LayoutInflater.from(this))
        // binding.toolbar.toggleToolbar(false)
        binding.toolbar.binding.market.text = intent.getStringExtra(AppConstants.PORTFOLIO_NAME)
        Utils.setEdgeToEdgeWithWhiteIcons(this)
        setContentView(binding.root)

        fetchUser(this)
        sharedViewModel = (this.application as MyApp).viewModel
        portfolioViewModel= ViewModelProvider.AndroidViewModelFactory.getInstance(this.application as MyApp).create(PortFolioViewModel::class.java)

        ViewCompat.setOnApplyWindowInsetsListener(binding.floatingActionButton ) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updateLayoutParams<FrameLayout.LayoutParams> {
                bottomMargin = systemBars.bottom+32
                rightMargin = systemBars.right+32
            }
            insets
        }
        ViewCompat.setOnApplyWindowInsetsListener(binding.mainLayout) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(
                top = 0,
                bottom = systemBars.bottom,
                left = systemBars.left,
                right = systemBars.right
            )
            insets
        }

/*        ViewCompat.setOnApplyWindowInsetsListener(binding.floatingMenu ) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updateLayoutParams<FrameLayout.LayoutParams> {
                bottomMargin = systemBars.bottom+62
                rightMargin = systemBars.right+32
            }
            insets
        }*/
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.floatingActionButton.setOnClickListener {
            if(binding.floatingMenu.visibility== View.GONE){
                binding.floatingMenu.visibility=View.VISIBLE
            }else{
                binding.floatingMenu.visibility=View.GONE
            }
        }
        binding.newBuyTrade.setOnClickListener {
            val intent = Intent(this, BuySellActivity::class.java)
            intent.putExtra(AppConstants.IS_BUY,true)
            intent.putExtra(AppConstants.MODE,0)
//            intent.putExtra(AppConstants.SYMBOL,symbol)
            intent.putExtra(AppConstants.PORTFOLIO_MAIN_ID,portfolioMainID)
            startActivity(intent)
            binding.floatingMenu.visibility = View.GONE
        }

        binding.sellTrade.setOnClickListener {
            val intent = Intent(this, BuySellActivity::class.java)
            intent.putExtra(AppConstants.IS_Sell, true)
//            intent.putExtra(AppConstants.SYMBOL,symbol)
            intent.putExtra(AppConstants.PORTFOLIO_MAIN_ID, portfolioMainID)
            startActivity(intent)
            binding.floatingMenu.visibility = View.GONE
        }

        binding.addDividend.setOnClickListener {
            val intent = Intent(this, BuySellActivity::class.java)
            intent.putExtra(AppConstants.IS_Dividend, true)
//            intent.putExtra(AppConstants.SYMBOL,symbol)
            intent.putExtra(AppConstants.PORTFOLIO_MAIN_ID, portfolioMainID)
            startActivity(intent)
            binding.floatingMenu.visibility = View.GONE
        }
        portfolioMainID=intent.getIntExtra(AppConstants.PORTFOLIO_MAIN_ID,-1)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@PortfolioDetailActivity,LinearLayoutManager.VERTICAL,false)
            adapter = ShareInHandAdapter(sharedViewModel, onItemClick = {
                val intent = Intent(this.context, StockDetailActivity::class.java)
                intent.putExtra(AppConstants.PORTFOLIO_NAME,"${it.symbol} in ${binding.toolbar.binding.market.text.toString()}")
                intent.putExtra(AppConstants.PORTFOLIO_MAIN_ID, portfolioMainID)
                intent.putExtra(AppConstants.SYMBOL, it.symbol)
                startActivity(intent)
            }, onItemClickSnapshot = {
                val intent= Intent(this.context, SnapshotActivity::class.java)
                intent.putExtra(AppConstants.SYMBOL, it.symbol)
                startActivity(intent)
            }, onItemClickSell = {

                val intent = Intent(this.context, BuySellActivity::class.java)
                intent.putExtra(AppConstants.IS_Sell, true)
                intent.putExtra(AppConstants.AVG_PRICE,it.price)
                intent.putExtra(AppConstants.QTY,it.quantity)
                intent.putExtra(AppConstants.SYMBOL,it.symbol)
                intent.putExtra(AppConstants.PORTFOLIO_MAIN_ID, portfolioMainID)
                startActivity(intent)

            })
            addItemDecoration(HorizontalDivider(30.dp))
        }
        binding.recyclerViewHistory.apply {
            layoutManager = LinearLayoutManager(this@PortfolioDetailActivity,LinearLayoutManager.VERTICAL,false)
            addItemDecoration(HorizontalDivider(30.dp))
        }

        portfolioViewModel.mutablePortfolioFinalDetailOnce.observe(this, Observer { result->
            when(result){
                is Resource.Error -> {
                    binding.loader.visibility = View.GONE
                    Utils.showError(binding.root,result.message)
                }
                is Resource.Loading -> {
                    binding.loader.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.apply {
                        loader.visibility = View.GONE

                        val stockItem = sharedViewModel.mutableAllData.value?.data?: emptyList()
                        val data = result.data
                        if(data!=null){
                            val currentMarketValue=data.fifoPortfolio.sumOf {res-> res.quantity.toDouble().times(stockItem?.firstOrNull { it.sYM.equals(res.symbol,true) }?.cL?:0.0) }
                            val portfolioCost = data.fifoPortfolio.sumOf { res-> res.quantity.toDouble().times(res.price.toDouble()) }
                            val dayPL = data.fifoPortfolio.sumOf {res-> res.quantity.toDouble().times(stockItem?.firstOrNull { it.sYM.equals(res.symbol,true) }?.cH?:0.0) }
                            val dayPLPercent =data.fifoPortfolio.sumOf { res->
                                stockItem?.firstOrNull { it.sYM.equals(res.symbol,true) }?.cHP?:0.0
                            }
                            val totalPL = data.fifoPortfolio.sumOf { res->res.quantity.toDouble().times(stockItem?.firstOrNull { it.sYM.equals(res.symbol,true) }?.cL?:0.0) }.minus(portfolioCost)
                            val totalPlPercent = totalPL.div(portfolioCost).times(100)

                            binding.currentMarket.setValue("%,d".format(currentMarketValue.roundToInt()))
                            binding.portfolioCost.setValue("%,d".format(portfolioCost.roundToInt()))
                            binding.daySPLHolding.setValue("%,d".format(dayPL.roundToInt())+" (${Utils.roundTwoDecimal(dayPLPercent)}%)")
                            binding.totalPLHolding.setValue("%,d".format(totalPL.roundToInt())+" (${Utils.roundTwoDecimal(totalPlPercent)}%)")

                            (recyclerView.adapter as ShareInHandAdapter).submitList(data?.fifoPortfolio)
                            if(!data.fifoPortfolio.isNullOrEmpty()){
                                group.visibility = View.VISIBLE
                                groupNoRecord.visibility = View.GONE
                            }else{
                                groupNoRecord.visibility = View.VISIBLE
                                group.visibility = View.GONE
                            }

                            if(!data.closeTrades.isNullOrEmpty()){
                                binding.recyclerViewHistory.adapter = TradeSummaryAdapter(generateSummaryForTrades(data.closeTrades)){
                                    val intent = Intent(this@PortfolioDetailActivity, StockDetailActivity::class.java)
                                    intent.putExtra(AppConstants.PORTFOLIO_NAME,"${it.symbol} in ${binding.toolbar.binding.market.text.toString()}")
                                    intent.putExtra(AppConstants.PORTFOLIO_MAIN_ID, portfolioMainID)
                                    intent.putExtra(AppConstants.IS_HISTORY,true)
                                    intent.putExtra(AppConstants.SYMBOL, it.symbol)
                                    startActivity(intent)
                                }

                                groupHistory.visibility = View.VISIBLE
                            }
                        }else{
                            groupNoRecord.visibility = View.VISIBLE
                        }

                    }
                }
            }
        })

        /*   binding.apply {
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
               recyclerHistory.adapter=HistoryHoldingAdapter(){

               }
               recyclerHistory.addItemDecoration(HorizontalDivider(20.dp))
   //            recyclerView.addItemDecoration(VerticalSpaceItemDecoration(10, color = Color.parseColor("#ffffff")))
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
               recyclerView.apply {


                   adapter = ShareInHandAdapter(
                       sharedViewModel,
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
                       }, onItemEditClick = {res->
                           val intent = Intent(this.context, EditBuySellActivity::class.java)
                           intent.putExtra(AppConstants.PORTFOLIO_MAIN_ID, portfolioMainID)
                           intent.putExtra(AppConstants.SYMBOL, res.symbol)
                           startActivity(intent)
                       })

                   layoutManager = LinearLayoutManager(binding.root.context,LinearLayoutManager.VERTICAL,false)

               }

               recyclerHistory.apply {
                   layoutManager = LinearLayoutManager(binding.root.context,LinearLayoutManager.VERTICAL,false)

               }


           }


           sharedViewModel.mutablePortfolioFinalDetail.observe(this, Observer { result->
               when(result){
                   is Resource.Error -> {
                       Utils.showError(binding.root,result.message?:"An error occurred")
                       binding.loader.visibility = View.GONE
                   }
                   is Resource.Loading -> {
                       binding.loader.visibility = View.VISIBLE
                   }
                   is Resource.Success -> {
   //                    sharedViewModel.mutablePortfolioFinalDetail.value=null
                       binding.loader.visibility = View.GONE
                       var currentMarketValue=0.0
                       var daysPL=0.0
                       var totalCost=0.0
                       var history = 0.0
                       var holdingPL = 0.0
                       val list= mutableListOf<SymbolProfit>()
                       if(result.data?.closeTrades?.isNotEmpty()?:false) {
                           result.data?.closeTrades?.forEach {
                               history += it.salAmount.toDouble()
                           }

                           val profitSummary  =  result.data?.closeTrades?.groupBy { it.symbol }?.map  { (symbol, trades)  ->


                               val totalPurchaseAmount = trades.sumOf { it.purAmount.toDouble() }
                               val totalPurchaseQty = trades.sumOf { it.purQuantity.toInt() }
                               val avgBuyPrice = totalPurchaseAmount.div(totalPurchaseQty)

                               val totalSaleAmount = trades.sumOf { it.salAmount.toDouble() }
                               val totalSaleQty = trades.sumOf { it.salQuantity.toInt() }
                               val avgSellPrice = totalSaleAmount.div(totalSaleQty)

                               val totalProfit=avgSellPrice.minus(avgBuyPrice).times(totalSaleQty)
                               val totalProfitPercent=avgSellPrice.minus(avgBuyPrice).div(avgBuyPrice).times(100)
                               SymbolProfit(symbol, totalProfit, totalProfitPercent)
                           }
                           if (profitSummary != null) {
                               for (entry in profitSummary ){
                                   list.add(entry)
                               }
                           }





                       }
                       if(result.data?.fifoPortfolio?.isNotEmpty()?:false){
                           result.data?.fifoPortfolio?.forEach {res->
                               val shares=res.quantity.toInt()
                               val currentPrice = sharedViewModel.mutableAllData.value?.data?.filter { it.sYM.equals(res.symbol,true)  }?.map { it.cL }?.first()
                               val ch = sharedViewModel.mutableAllData.value?.data?.filter { it.sYM.equals(res.symbol,true)  }?.map { it.cH }?.first()
                               val cl = sharedViewModel.mutableAllData.value?.data?.filter { it.sYM.equals(res.symbol,true)  }?.map { it.cL }?.first()

                               val marketValue = cl?.times(res.quantity.toInt())
                               totalCost = totalCost.plus(res.price.toDouble().times(res.quantity.toInt()))
                               daysPL+=ch?.times(shares)?:0.0
                               currentMarketValue+=currentPrice?.times(shares)?:0.0
                               holdingPL+=marketValue?.minus(totalCost)?:0.0
                           }
                           binding.historyCost.text = Utils.commaSeparated(history.roundToInt())
                           binding.holdingCost.text = Utils.commaSeparated(totalCost.roundToInt())
                           binding.holdingPL.text =Utils.commaSeparated(holdingPL.roundToInt())
                           binding.totalCost.text = Utils.commaSeparated(history.plus(totalCost).roundToInt())
                           binding.cardMar.setText(Utils.commaSeparated(currentMarketValue.roundToInt()))
                           binding.daysPLHoValue.text = "${Utils.commaSeparated(daysPL.roundToInt())} (${Utils.roundTwoDecimal((daysPL.div(currentMarketValue)).times(100))}%)"
                           binding.totalPLHValue.text = "${Utils.commaSeparated(currentMarketValue.minus(totalCost).roundToInt())} (${Utils.roundTwoDecimal(((currentMarketValue.minus(totalCost)).div(totalCost)).times(100))}%)"

                           (binding.recyclerView.adapter as ShareInHandAdapter).submitList(result.data?.fifoPortfolio?: emptyList())

                           binding.groupEmptyHolding.visibility = View.GONE
                           if(!result.data?.fifoPortfolio.isNullOrEmpty()){
                               binding.groupHolding.visibility = View.VISIBLE
                           }else{
                               binding.groupHolding.visibility = View.GONE
                           }


                           if(!list.isNullOrEmpty()){
                               (binding.recyclerHistory.adapter as HistoryHoldingAdapter).submitList(list)
                               binding.groupHistory.visibility = View.VISIBLE
                           }else{
                               binding.groupHistory.visibility = View.GONE
                           }


                           binding.apply {
                               cardCompan.setText("${binding.recyclerView.adapter?.itemCount}")
                           }
                       }else{
                           binding.groupEmptyHolding.visibility = View.VISIBLE
                       }

                       binding.apply {
                           loader.visibility = View.GONE
                           main.visibility = View.VISIBLE
                       }

                   }

                   null -> {}
               }
           })*/

    }



    private fun fetchUser(context: Context) {
        val listType = object : TypeToken<List<LoginDataItem>>() {}
        val user= Utils.getSharedPreference(context, emptyList<LoginDataItem>(),
            AppConstants.USER,listType)
        login=user.first()
        Log.e("User: ",user.toString())
    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        if (overrideConfiguration != null) {
            // Override any incoming configuration changes
            overrideConfiguration.densityDpi = resources.displayMetrics.densityDpi
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }


    /* override fun onStop() {
         sharedViewModel.stopPortfolioFinal()
         Toast.makeText(this,"OnStopped",Toast.LENGTH_SHORT).show()

         super.onStop()
     }
 */
    override fun onDestroy() {

        super.onDestroy()
        sharedViewModel.portfolioJob?.cancel()
        sharedViewModel.mutablePortfolioFinalDetail.value=null
        sharedViewModel.stopPortfolioFinal()
    }
    override fun getResources(): Resources {

        val res = super.getResources()
        val config = Configuration(res.configuration)

        val metrics = res.displayMetrics

        // Calculate screen width and height in inches
        val widthInches = metrics.widthPixels / metrics.xdpi
        val heightInches = metrics.heightPixels / metrics.ydpi
        val diagonalInches = Math.sqrt((widthInches * widthInches + heightInches * heightInches).toDouble())

        // Set fontScale based on diagonal screen size
        if(diagonalInches>3.9 && diagonalInches<4.9){
            config.fontScale = 0.85f  // Small phones
        }else if (diagonalInches>4.9 && diagonalInches<5.4){
            config.fontScale = 0.95f
        }else if (diagonalInches>5.5 && diagonalInches<6.9){
            config.fontScale = 1.0f
        }else{
            config.fontScale = 1.2f
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            config.fontWeightAdjustment = 0

        }
        res.updateConfiguration(config, metrics)
        return res
    }

}