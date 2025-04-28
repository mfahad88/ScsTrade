package com.example.scstrade.views.portfolio.fragments

import android.os.Bundle
import android.util.Log
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
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.portfolio.activities.StockDetailActivity
import com.example.scstrade.views.portfolio.adapter.HoldingAdapter


/**
 * A simple [Fragment] subclass.
 * Use the [HoldingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HoldingFragment : Fragment() {
    lateinit var binding:FragmentHoldingBinding
    lateinit var sharedViewModel: SharedViewModel
    lateinit var stockDetailActivity: StockDetailActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHoldingBinding.inflate(inflater)
        sharedViewModel = (requireActivity().application as MyApp).viewModel
        stockDetailActivity=(requireActivity() as StockDetailActivity)
        binding.buyTransac.text = getString(R.string.buy_transac,stockDetailActivity.symbol)
        sharedViewModel.mutablePortfolioDetail.observe(viewLifecycleOwner, Observer {result->

            when(result){
                is Resource.Error -> Utils.showError(binding.root,result.message?:"An error occurred...")
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    /*var totalPurchase = 0.00
                    result.data?.filter { it.portfolioSymbol.equals(stockDetailActivity.symbol,true) }
                        ?.filter { it.portfolioType.equals("buy",true) }?.map { it.portfolioRate.times(it.portfolioQuantity) }?.forEach {
                            totalPurchase+=it
                        }

                    val qty = result.data?.groupBy { it.portfolioSymbol }?.mapValues { (_,items)->
                        items.sumOf {
                            if(it.portfolioType.equals("buy",true)){
                                it.portfolioQuantity
                            }else{
                                0
                            }
                        }
                    }?.filter { it.key.equals(stockDetailActivity.symbol,true) }?.values?.first().toString().toInt()
                    val currentPrice=sharedViewModel.mutableAllData.value?.data?.filter { it.sYM.equals(stockDetailActivity.symbol,true) }?.first()?.cL
                    val previousPrice=sharedViewModel.mutableAllData.value?.data?.filter { it.sYM.equals(stockDetailActivity.symbol,true) }?.first()?.oC
                    val currentValue = currentPrice?.times(qty)
                    val avgBuy = totalPurchase.div(qty)
                    val dayPL = ((currentPrice?:0.0) - (previousPrice?:0.00)).times(qty)
                    val percentdayPL = dayPL.div(previousPrice?:0.0).times(100)
                    val totalPL = ((currentPrice?:0.0) - (avgBuy)).times(qty)
                    val percentTotalPL = totalPL.div(totalPurchase).times(100)
                    binding.currentMarValue.text = "${Utils.roundTwoDecimal(currentValue)}"
                    binding.purchaseCoValue.text = "${Utils.roundTwoDecimal(totalPurchase)}"
                    binding.avgBuyPriValue.text = "${Utils.roundTwoDecimal(avgBuy)}"
                    binding.shareOwnedValue.text = "${qty}"
                    binding.currentPriValue.text = "${Utils.roundTwoDecimal(currentPrice?:0.00)}"
                    binding.daysPLHoValue.text = "${Utils.roundTwoDecimal(dayPL)} (${Utils.roundTwoDecimal(percentdayPL)}%)"
                    binding.totalPLHValue.text = "${Utils.roundTwoDecimal(totalPL)} (${Utils.roundTwoDecimal(percentTotalPL)}%)"
                  *//* result.data?.filter {it.portfolioType.equals("buy",true) && it.portfolioType  }?.groupBy { it.portfolioSymbol }?.entries.sumOf {i  }
                    binding.purchaseCoValue.text = "${Utils.roundTwoDecimal(purchaseCost)}"*//*

                    binding.recyclerView.apply {
                        adapter=HoldingAdapter(result.data?.filter {it.portfolioSymbol.equals(stockDetailActivity.symbol,true)  }?.filter { it.portfolioType.equals("buy",true) }?: emptyList()){

                        }
                        (adapter as HoldingAdapter).setcurrentPrice(currentPrice?:0.00)
                        layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
                    }*/
                }
            }

        })
        return binding.root
    }


}