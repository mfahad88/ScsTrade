package com.example.scstrade.views.portfolio.fragments
import androidx.compose.ui.res.dimensionResource

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentSummaryBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.portfolio.CloseTrade
import com.example.scstrade.model.response.portfolio.FifoPortfolio
import com.example.scstrade.model.response.stock.StockItem
import com.example.scstrade.viewmodels.PortFolioViewModel
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.portfolio.activities.StockDetailActivity
import com.example.scstrade.views.portfolio.adapter.HistoryAdapter
import java.text.DecimalFormat
import kotlin.math.roundToInt


/**
 * A simple [Fragment] subclass.
 * Use the [SummaryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SummaryFragment : Fragment() {
    lateinit var binding : FragmentSummaryBinding
    private lateinit var sharedViewModel: SharedViewModel
    lateinit var portFolioViewModel: PortFolioViewModel
    lateinit var stockDetailActivity: StockDetailActivity


    override fun onDestroyView() {
   /*     sharedViewModel.mutablePortfolioFinalDetailOnce.value = null
        sharedViewModel.mutablePortfolioItemDetail.value = null
        sharedViewModel.mutableAllData.value = null*/
        super.onDestroyView()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSummaryBinding.inflate(inflater)
        sharedViewModel = (requireActivity().application as MyApp).viewModel
        stockDetailActivity= requireActivity() as StockDetailActivity
        portFolioViewModel = stockDetailActivity.portFolioViewModel

//        sharedViewModel.getDividend(stockDetailActivity.portfolioMainID.toString())
//        sharedViewModel.getPortfolioItemDetail(stockDetailActivity.portfolioMainID,stockDetailActivity.symbol)
//        sharedViewModel.getPortfolioFinalDetailOnce(stockDetailActivity.portfolioMainID)
        val stockData= sharedViewModel.mutableAllData.value?.data?.filter { it.sYM.equals(stockDetailActivity.symbol,true) }?.first()
        Glide.with(this).load(stockData?.companyLogo).into(binding.imageView20)

        portFolioViewModel.mutablePortfolioFinalDetailOnce.observe(viewLifecycleOwner, Observer { result->
            when(result){
                is Resource.Error -> {
                    Utils.showError(requireView(),result.message)
                    binding.loader.visibility = View.GONE
                }
                is Resource.Loading -> {}
                is Resource.Success -> {
                    binding.apply {
                        loader.visibility  = View.GONE
                        mainContainer.visibility = View.VISIBLE
                        if(!result.data?.fifoPortfolio.isNullOrEmpty() && !result.data?.closeTrades.isNullOrEmpty()) {
                            bindSummaryDataForSymbol(
                                binding,
                                stockDetailActivity.symbol,
                                stockData,
                                result.data?.fifoPortfolio ?: emptyList(),
                                result.data?.closeTrades ?: emptyList()
                            )
                        }
                    }
                }
            }
        })



        /*sharedViewModel.mutablePortfolioItemDetail.observe(viewLifecycleOwner, Observer { res->
            when (res){
                is Resource.Error -> Utils.showError(binding.root,res.message?:"An error occurred...")
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    val shares=res.data?.map { it.quantity.toDouble() }?.sumOf { it }
                    val currentPrice = sharedViewModel.mutableAllData.value?.data?.filter { it.sYM.equals(stockDetailActivity.symbol,true) }?.map { it.cL }?.first()
                    val currentMarketValue = currentPrice?.times(shares?:0.0)
                    val purchaseCost= res.data?.map { (it.rate.toDouble().times(it.quantity.toDouble())) }?.sumOf { it }
                    val holdingsPL=currentMarketValue?.minus(purchaseCost?:0.0)

                    binding.apply {
                        holdingCost.text = "${Utils.commaSeparated(purchaseCost?.roundToInt()?:0)}"
                        holdingValue.text = "${Utils.commaSeparated(currentMarketValue?.roundToInt()?:0)}"
                        if(holdingsPL!=null) {
                            holdingPL.text =
                                "${Utils.commaSeparated(holdingsPL?.roundToInt() ?: 0)} (${
                                    Utils.roundTwoDecimal(
                                        (holdingsPL?.div(purchaseCost ?: 0.0))?.times(
                                            100
                                        )
                                    )
                                }%)"
                        }else{
                            holdingPL.text = "0 (0.0%)"
                        }
                    }
                    sharedViewModel.mutablePortfolioFinalDetailOnce.observe(viewLifecycleOwner, Observer { result->
                        when(result){
                            is Resource.Error -> Utils.showError(binding.root,result.message?:"An error occurred...")
                            is Resource.Loading -> {

                            }
                            is Resource.Success -> {
                                try {
                                    val netPL=  result.data?.closeTrades?.filter { it.symbol.equals(stockDetailActivity.symbol,true) }!!.toList().sumOf { (it.salAmount.toDouble() - it.purAmount.toDouble()) }
                                    val percentPL = (netPL.div(result.data?.closeTrades?.filter { it.symbol.equals(stockDetailActivity.symbol,true) }!!.toList().sumOf { it.purAmount.toDouble() })).times(100)
                                    val sumSellPrice = result.data?.closeTrades?.filter { it.symbol.equals(stockDetailActivity.symbol,true) }!!.toList().sumOf { (it.salAmount.toDouble()) }
                                    val sumDividend = sharedViewModel.mutableDividend.value?.data?.filter { it.dividendSymbol.contains(stockDetailActivity.symbol,true) }?.sumOf { (it.dividendPerShare) }
                                    val totalPurchase = result.data?.closeTrades?.filter { it.symbol.equals(stockDetailActivity.symbol,true) }!!.toList().sumOf { it.purAmount.toDouble()}
//                        val historicalGain = sumSellPrice.plus(sumDividend!!).minus(totalPurchase)
                                    val soldValue = result.data?.closeTrades?.filter { it.symbol.equals(stockDetailActivity.symbol,true) }!!.toList().sumOf { it.salAmount.toDouble()}
//                        val dividendShare=sharedViewModel.mutableDividend.value?.data?.map { it.dividendPerShare }?.sumOf { it }
                                    val stockItem = sharedViewModel.mutableAllData.value?.data?.filter { it.sYM.equals(stockDetailActivity.symbol,true) }?.first()
//                        val historPL=(soldValue.plus(dividendShare?:0.0)).minus(totalPurchase)
                                    val historPL=(soldValue).minus(totalPurchase)
                                    val res=sharedViewModel.mutablePortfolioItemDetail.value!!
                                    val shares=res.data?.map { it.quantity.toDouble() }?.sumOf { it }
                                    val currentPrice = sharedViewModel.mutableAllData.value?.data?.filter { it.sYM.equals(stockDetailActivity.symbol,true) }?.map { it.cL }?.first()
                                    val currentMarketValue = currentPrice?.times(shares?:0.0)
                                    val purchaseCost= res.data?.map { (it.rate.toDouble().times(it.quantity.toDouble())) }?.sumOf { it }
                                    val holdingsPL=currentMarketValue?.minus(purchaseCost?:0.0)
                                    binding.apply {
                                        historyCost.text = Utils.commaSeparated(totalPurchase.roundToInt())
//                            historyValue.text = Utils.commaSeparated((soldValue.plus(dividendShare?:0.0)).roundToInt())
                                        historyValue.text = Utils.commaSeparated((soldValue).roundToInt())
                                        ffl.text = stockItem?.sYM
                                        faujiFoods.text = stockItem?.nM
                                        if(totalPurchase!=0.0) {
//                                Log.e("PL","${historPL.roundToInt()} \n ${soldValue} \n ${totalPurchase}")
                                            historyPL.text = "${Utils.commaSeparated(historPL.roundToInt())} (${
                                                Utils.roundTwoDecimal((historPL.div(totalPurchase))?.times(100))
                                            }%)"
                                        }else{
                                            historyPL.text = "0 (0.0%)"
                                        }

                                        totalCost.text = Utils.commaSeparated(totalPurchase.plus(purchaseCost?:0.0).roundToInt())
//                            totalValue.text = Utils.commaSeparated(currentMarketValue?.plus(soldValue.plus(dividendShare?:0.0))?.roundToInt()?:0)
                                        totalValue.text = Utils.commaSeparated(currentMarketValue?.plus(soldValue)?.roundToInt()?:0)
                                        if(totalPurchase!=0.0) {
                                            Log.e("PL","${holdingsPL} \n ${historPL}")
                                            totalPL.text = "${
                                                Utils.commaSeparated(
                                                    holdingsPL?.plus(historPL)?.roundToInt() ?: 0
                                                )
                                            } (${
                                                Utils.roundTwoDecimal(
                                                    ((holdingsPL?.plus(historPL))?.div(
                                                        totalPurchase.plus(purchaseCost ?: 0.0)
                                                    ))?.times(100)
                                                )
                                            }%)"
                                        }else{
                                            totalPL.text = "0 (0.0%)"
                                        }

                                        totalPL.setTextColor(if(totalPL.text.contains("-")) ContextCompat.getColor(requireContext(),R.color.md_theme_errorContainer) else ContextCompat.getColor(requireContext(),R.color.md_theme_primary))
                                        historyPL.setTextColor(if(historyPL.text.contains("-")) ContextCompat.getColor(requireContext(),R.color.md_theme_errorContainer) else ContextCompat.getColor(requireContext(),R.color.md_theme_primary))
                                        holdingPL.setTextColor(if(holdingPL.text.contains("-")) ContextCompat.getColor(requireContext(),R.color.md_theme_errorContainer) else ContextCompat.getColor(requireContext(),R.color.md_theme_primary))

                                        mainContainer.visibility = View.VISIBLE
                                        loader.visibility = View.GONE
                                    }

                                }catch (e:Exception){
                                    e.printStackTrace()
                                }
                            }
                        }
                    })
                }
            }
        })*/




        return binding.root
    }

    fun bindSummaryDataForSymbol(
        binding: FragmentSummaryBinding,
        symbol: String,
        stockItem: StockItem?,
        fifoList: List<FifoPortfolio>,
        closeTradeList: List<CloseTrade>
    ) {

        // ----- HOLDING -----
        val holdingItems = fifoList.filter { it.symbol.equals(symbol, ignoreCase = true) }

        val holdingCost = holdingItems.sumOf { (it.quantity.toDouble() ?: 0.0) * (stockItem?.cL ?: 0.0) }
        val holdingValue = holdingItems.sumOf { (it.quantity.toDouble() ?: 0.0) * (it.price.toDouble() ?: 0.0) }
        val holdingPL = holdingValue - holdingCost

        binding.holdingCost.text = formatAmount(holdingCost)
        binding.holdingValue.text = formatAmount(holdingValue)
        binding.holdingPL.text = formatAmount(holdingPL, colorize = true)
        binding.holdingPL.setTextColor(
            ContextCompat.getColor(binding.root.context,
                if (holdingPL >= 0) R.color.md_theme_primary else R.color.md_theme_error
            )
        )

        // ----- HISTORY -----
        val closedItems = closeTradeList.filter { it.symbol.equals(symbol, ignoreCase = true) }

        val historyCost = closedItems.sumOf { it.purAmount.toDouble() ?: 0.0 }
        val historyValue = closedItems.sumOf { it.salAmount.toDouble() ?: 0.0 }
        val historyPL = historyValue - historyCost

        val totalCost=holdingCost.plus(historyCost)
        val totalValue = holdingValue.plus(holdingCost)
        val totalPL = holdingPL.plus(historyPL)

        binding.historyCost.text = formatAmount(historyCost)
        binding.historyValue.text = formatAmount(historyValue)
        binding.historyPL.text = formatAmount(historyPL, colorize = true)

        binding.totalCost.text = formatAmount(totalCost)
        binding.totalValue.text = formatAmount(totalValue)
        binding.totalPL.text = formatAmount(totalPL, colorize = true)
       /* binding.historyPL.setTextColor(
            ContextCompat.getColor(binding.root.context,
                if (historyPL >= 0) R.color.md_theme_primary else R.color.md_theme_error
            )
        )*/
    }
    private fun formatAmount(amount: Double, colorize: Boolean = false): SpannableString {
        val formatted = DecimalFormat("#,##0").format(amount)
        return SpannableString(formatted).apply {
            if (colorize) {
                val color = if (amount >= 0) ContextCompat.getColor(requireContext(),R.color.md_theme_primary) else ContextCompat.getColor(requireContext(),R.color.md_theme_error)
                setSpan(ForegroundColorSpan(color), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
    }

}