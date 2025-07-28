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
import com.example.scstrade.model.response.stock.StockItem
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
import kotlin.math.sqrt

/*
class PortfolioDetailActivity : BaseActivity() {
    private lateinit var binding:ActivityMyPortfolioBinding
    private lateinit var sharedViewModel: SharedViewModel
    lateinit var portfolioViewModel: PortFolioViewModel
    lateinit var login: LoginDataItem
    var portfolioMainID:Int?=null

    //    var portfolioMainID:Int?=-1

    */
/*val Int.dp: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()*//*

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

}*/
class PortfolioDetailActivity : BaseActivity() {

    private lateinit var binding: ActivityMyPortfolioBinding
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var portfolioViewModel: PortFolioViewModel
    private lateinit var adapter: ShareInHandAdapter
    private var stockMap: Map<String, StockItem> = emptyMap()

    private lateinit var login: LoginDataItem
    private var portfolioMainID: Int? = null

    override fun onResume() {
        super.onResume()
        portfolioViewModel.getPortfolioFinalDetailOnce(portfolioMainID)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMyPortfolioBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        Utils.setEdgeToEdgeWithWhiteIcons(this)
        fetchUser(this)

        sharedViewModel = (application as MyApp).viewModel
        portfolioViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application as MyApp)
            .create(PortFolioViewModel::class.java)

        portfolioMainID = intent.getIntExtra(AppConstants.PORTFOLIO_MAIN_ID, -1)
        binding.toolbar.binding.market.text = intent.getStringExtra(AppConstants.PORTFOLIO_NAME)

        ViewCompat.setOnApplyWindowInsetsListener(binding.floatingActionButton) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updateLayoutParams<FrameLayout.LayoutParams> {
                bottomMargin = systemBars.bottom + 32
                rightMargin = systemBars.right + 32
            }
            insets
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.mainLayout) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(top = 0, bottom = systemBars.bottom, left = systemBars.left, right = systemBars.right)
            insets
        }

        // Adapter setup
        adapter = ShareInHandAdapter(
            onItemClick = { item ->
                val intent = Intent(this, StockDetailActivity::class.java)
                intent.putExtra(AppConstants.PORTFOLIO_NAME, "${item.symbol} in ${binding.toolbar.binding.market.text}")
                intent.putExtra(AppConstants.PORTFOLIO_MAIN_ID, portfolioMainID)
                intent.putExtra(AppConstants.SYMBOL, item.symbol)
                startActivity(intent)
            },
            onItemClickSnapshot = { item ->
                val intent = Intent(this, SnapshotActivity::class.java)
                intent.putExtra(AppConstants.SYMBOL, item.symbol)
                startActivity(intent)
            },
            onItemClickSell = { item ->
                val intent = Intent(this, BuySellActivity::class.java)
                intent.putExtra(AppConstants.IS_Sell, true)
                intent.putExtra(AppConstants.AVG_PRICE, item.price)
                intent.putExtra(AppConstants.QTY, item.quantity)
                intent.putExtra(AppConstants.SYMBOL, item.symbol)
                intent.putExtra(AppConstants.PORTFOLIO_MAIN_ID, portfolioMainID)
                startActivity(intent)
            }
        )

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@PortfolioDetailActivity)
            adapter = this@PortfolioDetailActivity.adapter
            addItemDecoration(HorizontalDivider(30.dp))
        }

        // Observe AllData and update stockMap
        sharedViewModel.mutableAllData.observe(this) { result ->
            stockMap = result.data?.associateBy { it.sYM.lowercase() } ?: emptyMap()
            adapter.updateStockMap(stockMap)
        }

        // Observe portfolio data
        portfolioViewModel.mutablePortfolioFinalDetailOnce.observe(this) { result ->
            when (result) {
                is Resource.Loading -> binding.loader.visibility = View.VISIBLE
                is Resource.Error -> {
                    binding.loader.visibility = View.GONE
                    Utils.showError(binding.root, result.message)
                }
                is Resource.Success -> {
                    binding.loader.visibility = View.GONE
                    val data = result.data
                    if (data != null) {
                        val fifo = data.fifoPortfolio
                        adapter.submitList(fifo)

                        val currentMarketValue = fifo.sumOf {
                            it.quantity.toDouble() * (stockMap[it.symbol.lowercase()]?.cL ?: 0.0)
                        }
                        val portfolioCost = fifo.sumOf { it.quantity.toDouble() * it.price.toDouble() }
                        val dayPL = fifo.sumOf {
                            it.quantity.toDouble() * (stockMap[it.symbol.lowercase()]?.cH ?: 0.0)
                        }
                        val totalPL = currentMarketValue - portfolioCost
                        val totalPLPercent = (totalPL / portfolioCost) * 100

                        binding.currentMarket.setValue("%,d".format(currentMarketValue.roundToInt()))
                        binding.portfolioCost.setValue("%,d".format(portfolioCost.roundToInt()))
                        binding.daySPLHolding.setValue("%,d".format(dayPL.roundToInt()))
                        binding.totalPLHolding.setValue("%,d".format(totalPL.roundToInt()) + " (${Utils.roundTwoDecimal(totalPLPercent)}%)")

                        if (fifo.isNotEmpty()) {
                            binding.group.visibility = View.VISIBLE
                            binding.groupNoRecord.visibility = View.GONE
                        } else {
                            binding.groupNoRecord.visibility = View.VISIBLE
                            binding.group.visibility = View.GONE
                        }

                        if (!data.closeTrades.isNullOrEmpty()) {
                            binding.recyclerViewHistory.layoutManager = LinearLayoutManager(this)
                            binding.recyclerViewHistory.adapter = TradeSummaryAdapter(generateSummaryForTrades(data.closeTrades)) {
                                val intent = Intent(this, StockDetailActivity::class.java)
                                intent.putExtra(AppConstants.SYMBOL, it.symbol)
                                intent.putExtra(AppConstants.PORTFOLIO_MAIN_ID, portfolioMainID)
                                intent.putExtra(AppConstants.IS_HISTORY, true)
                                startActivity(intent)
                            }
                            binding.groupHistory.visibility = View.VISIBLE
                        }
                    } else {
                        binding.groupNoRecord.visibility = View.VISIBLE
                    }
                }
            }
        }

        // FAB menu logic
        binding.floatingActionButton.setOnClickListener {
            binding.floatingMenu.visibility = if (binding.floatingMenu.visibility == View.GONE) View.VISIBLE else View.GONE
        }

        binding.newBuyTrade.setOnClickListener {
            val intent = Intent(this, BuySellActivity::class.java)
            intent.putExtra(AppConstants.IS_BUY, true)
            intent.putExtra(AppConstants.MODE, 0)
            intent.putExtra(AppConstants.PORTFOLIO_MAIN_ID, portfolioMainID)
            startActivity(intent)
            binding.floatingMenu.visibility = View.GONE
        }

        binding.sellTrade.setOnClickListener {
            val intent = Intent(this, BuySellActivity::class.java)
            intent.putExtra(AppConstants.IS_Sell, true)
            intent.putExtra(AppConstants.PORTFOLIO_MAIN_ID, portfolioMainID)
            startActivity(intent)
            binding.floatingMenu.visibility = View.GONE
        }

        binding.addDividend.setOnClickListener {
            val intent = Intent(this, BuySellActivity::class.java)
            intent.putExtra(AppConstants.IS_Dividend, true)
            intent.putExtra(AppConstants.PORTFOLIO_MAIN_ID, portfolioMainID)
            startActivity(intent)
            binding.floatingMenu.visibility = View.GONE
        }
    }

    private fun generateSummaryForTrades(trades: List<CloseTrade>): List<TradeSummary> {
        return trades.groupBy { it.symbol }.map { (symbol, groupedTrades) ->
            val totalCost = groupedTrades.sumOf { it.purAmount.toDouble() }
            val totalValue = groupedTrades.sumOf { it.salAmount.toDouble() }
            val pnl = totalValue - totalCost
            val pnlPercent = (pnl / totalCost) * 100
            val status = if (pnl < 0) "Loss" else "Gain"

            TradeSummary(
                symbol = symbol,
                status = status,
                amount = String.format("%,.2f", kotlin.math.abs(pnl)),
                percent = String.format("%.2f", kotlin.math.abs(pnlPercent))
            )
        }
    }

    private fun fetchUser(context: Context) {
        val listType = object : TypeToken<List<LoginDataItem>>() {}
        val user = Utils.getSharedPreference(context, emptyList(), AppConstants.USER, listType)
        login = user.first()
        Log.e("User: ", user.toString())
    }

    override fun getResources(): Resources {
        val res = super.getResources()
        val config = Configuration(res.configuration)
        val metrics = res.displayMetrics

        val widthInches = metrics.widthPixels / metrics.xdpi
        val heightInches = metrics.heightPixels / metrics.ydpi
        val diagonalInches = sqrt(widthInches.toDouble() * widthInches + heightInches * heightInches)

        config.fontScale = when (diagonalInches) {
            in 3.9..4.9 -> 0.85f
            in 4.9..5.4 -> 0.95f
            in 5.5..6.9 -> 1.0f
            else -> 1.2f
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            config.fontWeightAdjustment = 0
        }

        res.updateConfiguration(config, metrics)
        return res
    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        overrideConfiguration?.densityDpi = resources.displayMetrics.densityDpi
        super.applyOverrideConfiguration(overrideConfiguration)
    }

    override fun onDestroy() {
        super.onDestroy()
        sharedViewModel.portfolioJob?.cancel()
        sharedViewModel.mutablePortfolioFinalDetail.value = null
        sharedViewModel.stopPortfolioFinal()
    }
}