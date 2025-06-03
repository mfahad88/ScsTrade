package com.example.scstrade.views.portfolio.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentSummaryBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.portfolio.activities.StockDetailActivity
import com.example.scstrade.views.portfolio.adapter.HistoryAdapter
import kotlin.math.roundToInt


/**
 * A simple [Fragment] subclass.
 * Use the [SummaryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SummaryFragment : Fragment() {
    lateinit var binding : FragmentSummaryBinding
    private lateinit var sharedViewModel: SharedViewModel
    lateinit var stockDetailActivity: StockDetailActivity


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSummaryBinding.inflate(inflater)
        sharedViewModel = (requireActivity().application as MyApp).viewModel
        stockDetailActivity= requireActivity() as StockDetailActivity

        sharedViewModel.getDividend(stockDetailActivity.portfolioMainID.toString())
        sharedViewModel.getPortfolioItemDetail(stockDetailActivity.portfolioMainID,stockDetailActivity.symbol)
        sharedViewModel.getPortfolioFinalDetailOnce(stockDetailActivity.portfolioMainID)
        sharedViewModel.mutablePortfolioItemDetail.observe(viewLifecycleOwner, Observer { res->
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
                        holdingPL.text = "${Utils.commaSeparated(holdingsPL?.roundToInt()?:0)} (${Utils.roundTwoDecimal((holdingsPL?.div(purchaseCost?:0.0))?.times(100))}%)"
                    }

                }
            }
        })

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
                        val dividendShare=sharedViewModel.mutableDividend.value?.data?.map { it.dividendPerShare }?.sumOf { it }
                        val stockItem = sharedViewModel.mutableAllData.value?.data?.filter { it.sYM.equals(stockDetailActivity.symbol,true) }?.first()
                        val historPL=(soldValue.plus(dividendShare?:0.0)).minus(totalPurchase)

                        val res=sharedViewModel.mutablePortfolioItemDetail.value!!
                        val shares=res.data?.map { it.quantity.toDouble() }?.sumOf { it }
                        val currentPrice = sharedViewModel.mutableAllData.value?.data?.filter { it.sYM.equals(stockDetailActivity.symbol,true) }?.map { it.cL }?.first()
                        val currentMarketValue = currentPrice?.times(shares?:0.0)
                        val purchaseCost= res.data?.map { (it.rate.toDouble().times(it.quantity.toDouble())) }?.sumOf { it }
                        val holdingsPL=currentMarketValue?.minus(purchaseCost?:0.0)
                        binding.apply {
                            historyCost.text = Utils.commaSeparated(totalPurchase.roundToInt())
                            historyValue.text = Utils.commaSeparated((soldValue.plus(dividendShare?:0.0)).roundToInt())
                            ffl.text = stockItem?.sYM
                            faujiFoods.text = stockItem?.nM
                            historyPL.text = "${Utils.commaSeparated(historPL.roundToInt())} (${Utils.roundTwoDecimal((historPL.div(totalPurchase))?.times(100))}%)"

                            totalCost.text = Utils.commaSeparated(totalPurchase.plus(purchaseCost?:0.0).roundToInt())
                            totalValue.text = Utils.commaSeparated(currentMarketValue?.plus(soldValue.plus(dividendShare?:0.0))?.roundToInt()?:0)
                            totalPL.text= "${Utils.commaSeparated(holdingsPL?.plus(historPL)?.roundToInt()?:0)} (${Utils.roundTwoDecimal(((holdingsPL?.plus(historPL))?.div(totalPurchase.plus(purchaseCost?:0.0)))?.times(100))}%)"

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


        return binding.root
    }


}