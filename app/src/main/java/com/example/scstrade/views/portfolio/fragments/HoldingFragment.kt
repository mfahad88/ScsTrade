package com.example.scstrade.views.portfolio.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentHoldingBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.portfolio.CloseTrade
import com.example.scstrade.model.response.portfolio.FifoPortfolio
import com.example.scstrade.viewmodels.PortFolioViewModel
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.portfolio.activities.StockDetailActivity
import com.example.scstrade.views.portfolio.adapter.HoldingAdapter
import com.example.scstrade.views.widgets.SideBarDivider


/**
 * A simple [Fragment] subclass.
 * Use the [HoldingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HoldingFragment : Fragment() {
    lateinit var binding:FragmentHoldingBinding
    lateinit var sharedViewModel: SharedViewModel
    lateinit var portFolioViewModel: PortFolioViewModel
    lateinit var stockDetailActivity: StockDetailActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = (requireActivity().application as MyApp).viewModel
        stockDetailActivity=(requireActivity() as StockDetailActivity)
        portFolioViewModel = stockDetailActivity.portFolioViewModel
//        portFolioViewModel.getPortfolioItemDetail(stockDetailActivity.portfolioMainID,stockDetailActivity.symbol)
//        sharedViewModel.getPortfolioItemDetail(stockDetailActivity.portfolioMainID,stockDetailActivity.symbol)
//        sharedViewModel.getPortfolioDetails(stockDetailActivity.portfolioMainID)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHoldingBinding.inflate(inflater)

        binding.buyTransac.text = getString(R.string.buy_transac,stockDetailActivity.symbol)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            val divider = SideBarDivider(
                dividerColor = Color.parseColor("#B3C6C6CD"),
                marginEnd = 10
            )
            addItemDecoration(divider)

        }


        portFolioViewModel.mutablePortfolioItemDetail.observe(viewLifecycleOwner,Observer{result->
            when(result){
                is Resource.Error -> Utils.showError(requireView(),result.message)
                is Resource.Loading -> {}
                is Resource.Success-> {
                    binding.apply {
                        loader.visibility = View.GONE
                        mainContainer.visibility = View.VISIBLE

                        val stockItem = sharedViewModel.mutableAllData.value?.data
                        val data = result.data
                        if(!data.isNullOrEmpty()){
                            val currentMarketValue= data.sumOf { res-> res.quantity.toDouble().times(stockItem?.filter { it.sYM.equals((requireActivity() as StockDetailActivity).symbol) }?.map { it.cL }?.first()?:0.0) }
                            val portfolioCost = data.sumOf { res-> res.quantity.toDouble().times(res.rate.toDouble()) }
                            val dayPL = data.sumOf { res-> res.quantity.toDouble().times(stockItem?.filter { it.sYM.equals((requireActivity() as StockDetailActivity).symbol) }?.map { it.cH }?.first()?:0.0) }
                            val totalPL = data.sumOf { res->res.quantity.toDouble().times(stockItem?.filter { it.sYM.equals((requireActivity() as StockDetailActivity).symbol) }?.map { it.cL }?.first()?:0.0) }.minus(portfolioCost)
                            val totalPlPercent = totalPL.div(portfolioCost).times(100)
                            val totalShaes=data.sumOf { res->res.quantity.toDouble()}
                            val avgBuyPrice = data.map { it.rate }.first()
                            val currentPrice = stockItem?.filter { it.sYM.equals(stockDetailActivity.symbol) }?.map { it.cL }?.first()
                            binding.currentMarket.setValue(Utils.roundTwoDecimal(currentMarketValue))
                            binding.purchaseCoValue.setValue(Utils.roundTwoDecimal(portfolioCost))
                            binding.daySPLHolding.setValue(Utils.roundTwoDecimal(dayPL))
                            binding.totalPLHolding.setValue(Utils.roundTwoDecimal(totalPL))
                            shareOwnedValue.text = totalShaes.toString()
                            avgBuyPriValue.text = String.format("%,.2f",avgBuyPrice.toDouble())
                            currentPriValue.text = currentPrice.toString()

                            binding.recyclerView.adapter=HoldingAdapter(data,stockItem?.filter { it.sYM.equals(stockDetailActivity.symbol) }?.first())
                        }else{
                            binding.currentMarket.setValue(Utils.roundTwoDecimal(0.0))
                            binding.purchaseCoValue.setValue(Utils.roundTwoDecimal(0.0))
                            binding.daySPLHolding.setValue(Utils.roundTwoDecimal(0.0))
                            binding.totalPLHolding.setValue(Utils.roundTwoDecimal(0.0))
                            shareOwnedValue.text = 0.0.toString()
                            avgBuyPriValue.text = 0.0.toString()
                            currentPriValue.text = 0.0.toString()
                        }


                    }
                }
            }

        })
        /*portFolioViewModel.mutablePortfolioFinalDetailOnce.observe(viewLifecycleOwner, Observer {result->

            when(result){
                is Resource.Error -> Utils.showError(binding.root,result.message?:"An error occurred...")
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    binding.apply {
                        loader.visibility = View.GONE
                        mainContainer.visibility = View.VISIBLE
                        val stockItem = sharedViewModel.mutableAllData.value?.data
                        val data = result.data
                        val currentMarketValue=data!!.fifoPortfolio.filter { it.symbol.equals(stockDetailActivity.symbol) }.sumOf {res-> res.quantity.toDouble().times(stockItem?.filter { it.sYM.equals(res.symbol) }?.map { it.cL }?.first()?:0.0) }
                        val portfolioCost = data!!.fifoPortfolio.filter { it.symbol.equals(stockDetailActivity.symbol) }.sumOf { res-> res.quantity.toDouble().times(res.price.toDouble()) }
                        val dayPL = data!!.fifoPortfolio.filter { it.symbol.equals(stockDetailActivity.symbol) }.sumOf {res-> res.quantity.toDouble().times(stockItem?.filter { it.sYM.equals(res.symbol) }?.map { it.cH }?.first()?:0.0) }
                        val totalPL = data!!.fifoPortfolio.filter { it.symbol.equals(stockDetailActivity.symbol) }.sumOf { res->res.quantity.toDouble().times(stockItem?.filter { it.sYM.equals(res.symbol) }?.map { it.cL }?.first()?:0.0) }.minus(portfolioCost)
                        val totalPlPercent = totalPL.div(portfolioCost).times(100)
                        val totalShaes=data!!.fifoPortfolio.filter { it.symbol.equals(stockDetailActivity.symbol) }.sumOf { res->res.quantity.toDouble()}
                        val avgBuyPrice = data!!.fifoPortfolio.filter { it.symbol.equals(stockDetailActivity.symbol) }.map { it.price }.first()
                        val currentPrice = stockItem?.filter { it.sYM.equals(stockDetailActivity.symbol) }?.map { it.cL }?.first()
                        binding.currentMarket.setValue(Utils.roundTwoDecimal(currentMarketValue))
                        binding.purchaseCoValue.setValue(Utils.roundTwoDecimal(portfolioCost))
                        binding.daySPLHolding.setValue(Utils.roundTwoDecimal(dayPL))
                        binding.totalPLHolding.setValue(Utils.roundTwoDecimal(totalPL))
                        shareOwnedValue.text = totalShaes.toString()
                        avgBuyPriValue.text = String.format("%,.2f",avgBuyPrice.toDouble())
                        currentPriValue.text = currentPrice.toString()

                        binding.recyclerView.adapter=HoldingAdapter(data.closeTrades,stockItem?.filter { it.sYM.equals(stockDetailActivity.symbol) }?.first())


                    }
                    *//*binding.recyclerView.apply {

                        val shares=result.data?.filter { it.portfolioSymbol.equals(stockDetailActivity.symbol) }?.filter { it.portfolioType.equals("buy",true) }?.map { it.portfolioQuantity.toDouble() }?.sumOf { it }
                        val purchaseCost= result.data?.filter { it.portfolioSymbol.equals(stockDetailActivity.symbol) }?.filter { it.portfolioType.equals("buy",true) }?.map { (it.portfolioRate.times(it.portfolioQuantity.toDouble())) }?.sumOf { it }
                        val avgBuyPrice = purchaseCost?.div(shares?:0.0)
                        val currentPrice = sharedViewModel.mutableAllData.value?.data?.filter { it.sYM.equals(stockDetailActivity.symbol,true) }?.map { it.cL }?.first()
                        val currentMarketValue = currentPrice?.times(shares?:0.0)

                        val daysPercentPL = sharedViewModel.mutableAllData.value?.data?.filter { it.sYM.equals(stockDetailActivity.symbol,true) }?.map { it.cHP }?.first()
                        val totalPL= currentMarketValue?.minus(purchaseCost?:0.0)
                        val totalPercentPL= (totalPL?.div(purchaseCost?:0.0))?.times(100)
                        val fifo=sharedViewModel.mutablePortfolioFinalDetail.value?.data?.fifoPortfolio?.filter { it.symbol.equals(stockDetailActivity.symbol) }?.first()
                        val daysPL = sharedViewModel.mutableAllData.value?.data?.filter { it.sYM.equals(stockDetailActivity.symbol,true) }?.map { it.cH }?.first()?.times(fifo?.quantity?.toDouble()?:0.0)
                        binding.apply {
                            currentPriValue.text = Utils.roundTwoDecimal(currentPrice)
                            shareOwnedValue.text = "${shares}"
                            purchaseCoValue.setValue(Utils.roundTwoDecimal(purchaseCost))
                            avgBuyPriValue.text = Utils.roundTwoDecimal(avgBuyPrice)
                            currentMarket.setValue(Utils.roundTwoDecimal(currentMarketValue))
                            daySPLHolding.setValue("${Utils.roundTwoDecimal(daysPL)} (${Utils.roundTwoDecimal(daysPercentPL)}%)")
                            totalPLHolding.setValue("${Utils.roundTwoDecimal(totalPL)} (${Utils.roundTwoDecimal(totalPercentPL)}%)")
                            mainContainer.visibility = View.VISIBLE
                            loader.visibility = View.GONE
                        }
                        val list= mutableListOf<PortfolioItemDetail>()

                        result.data?.filter { it.portfolioSymbol.equals(stockDetailActivity.symbol) }?.filter { it.portfolioType.equals("buy",true) }?.forEach {
                            list.add(PortfolioItemDetail(it.portfolioDate,it.portfolioQuantity.toString(),it.portfolioRate.toString(),(currentPrice!!.minus(it.portfolioRate)).times(it.portfolioQuantity.toDouble()),
                                ((currentPrice.times(it.portfolioQuantity.toInt()).minus(it.portfolioRate.toDouble().times(it.portfolioQuantity.toInt()))).div(it.portfolioRate.toDouble().times(it.portfolioQuantity.toInt()))).times(100)
                                ,null))
                        }

                        adapter = HoldingAdapter(list, onItemClick = {

                        }, onItemEditClick = {
                            val intent = Intent(requireContext(), BuySellActivity::class.java)
                            intent.putExtra(AppConstants.IS_BUY,true)
                            intent.putExtra(AppConstants.PORTFOLIO_MAIN_ID,stockDetailActivity.portfolioMainID)
                            intent.putExtra(AppConstants.SYMBOL,stockDetailActivity.symbol)
                            intent.putExtra(AppConstants.MODE,1)
                            intent.putExtra(AppConstants.PORTFOLIO_ITEM,it)
                            startActivity(intent)
                            requireActivity().finish()
                        }, onItemDeleteClick = {

                        })
                        layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
                    }*//*
                }
            }

        })*/

        return binding.root
    }



    fun calculateHistoryAndDividend(
        symbol: String,
        closeTrades: List<CloseTrade>,
        fifoList: List<FifoPortfolio>,
    ): Map<String, Double> {
        var totalPur = 0.0
        var totalSal = 0.0
        var profitBooked = 0.0
        var lossBooked = 0.0

        closeTrades.filter { it.symbol == symbol }.forEach { trade ->
            val pur = trade.purAmount.toDoubleOrNull() ?: 0.0
            val sal = trade.salAmount.toDoubleOrNull() ?: 0.0
            val pnl = sal - pur

            totalPur += pur
            totalSal += sal

            if (pnl > 0) profitBooked += pnl
            else lossBooked += pnl
        }

        val historicalGain = profitBooked + lossBooked // net P&L

        // Dividend Calculation

        return mapOf(
            "PurchaseCost" to totalPur,
            "SoldValue" to totalSal,
            "P&L" to totalSal - totalPur,
            "ProfitBooked" to profitBooked,
            "LossBooked" to lossBooked,
            "HistoricalGain" to historicalGain,
        )
    }
}