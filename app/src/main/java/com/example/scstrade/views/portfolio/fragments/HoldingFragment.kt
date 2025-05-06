package com.example.scstrade.views.portfolio.fragments

import android.content.Intent
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
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.portfolio.PortfolioItemDetail
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.portfolio.activities.BuySellActivity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = (requireActivity().application as MyApp).viewModel
        stockDetailActivity=(requireActivity() as StockDetailActivity)
        sharedViewModel.getPortfolioItemDetail(stockDetailActivity.portfolioMainID,stockDetailActivity.symbol)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHoldingBinding.inflate(inflater)

        binding.buyTransac.text = getString(R.string.buy_transac,stockDetailActivity.symbol)
        sharedViewModel.mutablePortfolioItemDetail.observe(viewLifecycleOwner, Observer {result->

            when(result){
                is Resource.Error -> Utils.showError(binding.root,result.message?:"An error occurred...")
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    binding.recyclerView.apply {
                        val shares=result.data?.map { it.quantity.toDouble() }?.sumOf { it }
                        val purchaseCost= result.data?.map { (it.rate.toDouble().times(it.quantity.toDouble())) }?.sumOf { it }
                        val avgBuyPrice = purchaseCost?.div(shares?:0.0)
                        val currentPrice = sharedViewModel.mutableAllData.value?.data?.filter { it.sYM.equals(stockDetailActivity.symbol,true) }?.map { it.cL }?.first()
                        val currentMarketValue = currentPrice?.times(shares?:0.0)
                        val daysPL = sharedViewModel.mutableAllData.value?.data?.filter { it.sYM.equals(stockDetailActivity.symbol,true) }?.map { it.cH }?.first()?.times(shares?:0.0)
                        val daysPercentPL = ((currentMarketValue?.minus(purchaseCost?:0.0))?.div(purchaseCost?:0.0))?.times(100)
                         val totalPL= currentMarketValue?.minus(purchaseCost?:0.0)
                        val totalPercentPL= (totalPL?.div(purchaseCost?:0.0))?.times(100)
                        binding.apply {
                            currentPriValue.text = Utils.roundTwoDecimal(currentPrice)
                            shareOwnedValue.text = "${shares}"
                            purchaseCoValue.text = Utils.roundTwoDecimal(purchaseCost)
                            avgBuyPriValue.text = Utils.roundTwoDecimal(avgBuyPrice)
                            currentMarValue.text = Utils.roundTwoDecimal(currentMarketValue)
                            daysPLHoValue.text = "${Utils.roundTwoDecimal(daysPL)} (${Utils.roundTwoDecimal(daysPercentPL)}%)"
                            totalPLHValue.text = "${Utils.roundTwoDecimal(totalPL)} (${Utils.roundTwoDecimal(totalPercentPL)}%)"
                            mainContainer.visibility = View.VISIBLE
                            loader.visibility = View.GONE
                        }
                        val list= mutableListOf<PortfolioItemDetail>()

                        result.data?.forEach {
                            list.add(PortfolioItemDetail(it.date,it.quantity,it.rate,(currentPrice!!.minus(it.rate.toDouble())).times(it.quantity.toDouble()),
                                ((currentPrice.times(it.quantity.toInt()).minus(it.rate.toDouble().times(it.quantity.toInt()))).div(it.rate.toDouble().times(it.quantity.toInt()))).times(100)
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
                    }
                }
            }

        })
        return binding.root
    }


}